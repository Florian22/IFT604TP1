package client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import commun.Match;
import commun.Message;
import commun.Message.Method;
import commun.Penalite;

public class Client {

	public static void main(String[] zero) throws ClassNotFoundException, IOException{
		ClientConnection cc = new ClientConnection("localhost",2015);
		try {			
			// Liste des matchs
			Message m = new Message(Method.ListeMatchs);
			// Results
			Message reponse = cc.executeRequest(m);
			HashMap<Integer, String> matchs = (HashMap<Integer, String>) reponse.getArgument().get(0);
			for (Map.Entry<Integer, String> entry : matchs.entrySet()) {
			    System.out.println("Match no " + entry.getKey() + " : " + entry.getValue());
			}
			
			// Follow match 2
			MatchFollower matchFollower = new MatchFollower(2, cc);
			Thread tmf = new Thread(matchFollower);
			tmf.start();
			
					
			// TODO parie en ligne
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
