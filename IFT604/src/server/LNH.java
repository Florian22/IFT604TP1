package server;

import commun.Match;
import commun.Penalite;

import java.util.HashMap;
import java.util.Map;

public class LNH {
    private Map<Integer, Match> matchs;
    Thread chronoUpdater;
    Thread matchUpdater;
    
    public LNH() {
    	matchs = new HashMap<Integer, Match>();
    	chronoUpdater = new Thread(new ChronoUpdater(this));
    	matchUpdater = new Thread(new ChronoUpdater(this));
    	chronoUpdater.start();
    	matchUpdater.start();
	}

	public void startMatch(Match m){
		if(matchs.values().size() < 10)
			matchs.put(m.getId(), m);
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
	
	public void endMatch(Integer matchId){
		
	}
	
	public class ChronoUpdater implements Runnable {
		LNH lnh;
		public ChronoUpdater(LNH lnh) {
			this.lnh = lnh;
		}
		public void run() {
			while(true){
				for(Match m : lnh.matchs.values()){
					m.setChronometre(m.getChronometre() - 0.5);
					// Gestion des penalités
					for (int i = m.getPenalitesA().size()-1; i >= 0; i--)
						if(m.getPenalitesA().get(i).chronometreLiberation >= m.getChronometre())
							m.getPenalitesA().remove(i);
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
					Integer event = randomRange(0, 80);
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


