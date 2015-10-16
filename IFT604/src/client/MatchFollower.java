package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import commun.Match;
import commun.Message;
import commun.Penalite;
import commun.Message.Method;

public class MatchFollower implements Runnable {
	Integer matchId;
	Match match;
	ClientConnection cc;

	public MatchFollower(Integer matchId, ClientConnection cc) {
		this.matchId = matchId;
		this.cc = cc;
	}

	public void run() {
		while (true) {
			Message m = new Message(Method.DetailsMatch);
			m.addArgument(matchId);
			// Results
			Message reponse;
			try {
				reponse = cc.executeRequest(m);
				match = (Match) reponse.getArgument().get(0);

				// Debug
				Match match = (Match) reponse.getArgument().get(0);
				System.out.println("Periode : " + match.getPeriode());
				System.out.println("Chrono : " + match.getChronometre());
				System.out.println("Score : " + match.getCompteursA().size() + "-" + match.getCompteursB().size());
				for (String s : match.getCompteursA())
					System.out.println("Scoreur equipe A : " + s);
				for (String s : match.getCompteursB())
					System.out.println("Scoreur equipe B : " + s);
				for (Penalite p : match.getPenalitesA())
					System.out.println("Penalite equipe A : " + p.joueur + " | "
							+ (match.getChronometre() - p.chronometreLiberation));
				for (Penalite p : match.getPenalitesB())
					System.out.println("Penalite equipe B : " + p.joueur + " | "
							+ (match.getChronometre() - p.chronometreLiberation));
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// Sleep
			try {
				Thread.sleep(5000);//2 * 60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
