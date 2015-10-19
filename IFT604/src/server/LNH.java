package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import commun.Match;

public class LNH {
    private Map<Integer, Match> matchs;
    private Map<Integer, Thread> threadGestionnairesDePari;
    private Map<Integer, MatchBetManager> gestionnairesDePari;
    Thread chronoUpdater;
    Thread matchUpdater;
    
    public LNH() {
    	matchs = new HashMap<Integer, Match>();
    	threadGestionnairesDePari = new HashMap<Integer, Thread>();
    	gestionnairesDePari = new HashMap<Integer, MatchBetManager>();
    	chronoUpdater = new Thread(new ChronoUpdater(this));
    	matchUpdater = new Thread(new MatchUpdater(this));
    	chronoUpdater.start();
    	matchUpdater.start();
	}

	public void startMatch(Match m){
		if(matchs.values().size() < 10){
			MatchBetManager mbm = new MatchBetManager(m);
			Thread tmbm = new Thread(mbm);
			tmbm.start();
			
			matchs.put(m.getId(), m);
			gestionnairesDePari.put(m.getId(), mbm);
			threadGestionnairesDePari.put(m.getId(), tmbm);
		}
    }

	public Map<Integer, String> getMatchList() {
		Map<Integer, String> listeDesMatchs = new HashMap<Integer, String>();
		for (Match m : matchs.values())
			listeDesMatchs.put(m.getId(), m.toString());
		return listeDesMatchs;
	}

	public Match getMatchDetails(Integer matchId) {
		return matchs.get(matchId);
	}

	public MatchBetManager getGestionnaireDePari(Integer matchId) {
		return gestionnairesDePari.get(matchId);
	}
	
	public void endMatch(Integer matchId){
		// Obtenir les objets
		Match m = matchs.get(matchId);
		MatchBetManager mbm = gestionnairesDePari.get(matchId);
		Thread tmbm = threadGestionnairesDePari.get(matchId);
		// Retirer les objets des collections
		matchs.remove(matchId);
		gestionnairesDePari.remove(matchId);
		threadGestionnairesDePari.remove(matchId);
		// Arreter le thread des paris
		tmbm.stop();
		// Calculer et envoyer les messages de paris
		mbm.finalizeGame();
		
	}
	
	public class ChronoUpdater implements Runnable {
		LNH lnh;
		public ChronoUpdater(LNH lnh) {
			this.lnh = lnh;
		}
		public void run() {
			while(true){
				for(Match m : lnh.matchs.values()){
					m.setChronometre(m.getChronometre() - 11);//0.5);
					// Gestion des penalités
					if(m.getPenalitesA().size() != 0)
						for (int i = m.getPenalitesA().size()-1; i >= 0; i--)
							if(m.getPenalitesA().get(i).chronometreLiberation >= m.getChronometre())
								m.getPenalitesA().remove(i);
					if(m.getPenalitesB().size() != 0)
						for (int i = m.getPenalitesB().size()-1; i >= 0; i--)
							if(m.getPenalitesB().get(i).chronometreLiberation >= m.getChronometre())
								m.getPenalitesB().remove(i);
					// Gestion des périodes
					if(m.getChronometre() <= 0){
						m.setChronometre(20.0);
						m.setPeriode(m.getPeriode()+1);
						if(m.getPeriode() == 4)
							lnh.endMatch(m.getId());
					}
					
				}
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public class MatchUpdater implements Runnable {
		LNH lnh;
		public MatchUpdater(LNH lnh) {
			this.lnh = lnh;
		}
		public void run() {
			while(true){
				for(Match m : lnh.matchs.values()){
					// Generation aleatoire d'evenemnts
					Integer event = randomRange(0, 40);
					if(event == 0) // Equipe A score
						m.scoreEquipeA("Dat name");
					else if(event == 1) // Equipe B score
						m.scoreEquipeB("Dat name");
					else if(event == 2 || event == 3)// 2 minutes equipe A
						m.penaliteEquipeA("Dat name", 2);
					else if(event == 4 || event == 5)// 2 minutes equipe B
						m.penaliteEquipeB("Dat name", 2);
					else if(event == 6 || event == 7)// 5 minutes equipe A
						m.penaliteEquipeA("Dat name", 5);
					else if(event == 8 || event == 9)// 5 minutes equipe B
						m.penaliteEquipeB("Dat name", 5);
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		private Integer randomRange(Integer min, Integer max){
			return min + (int)(Math.random() * ((max - min) + 1));
		}
	}
}


