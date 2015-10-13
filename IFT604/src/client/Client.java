package client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import commun.Message;
import commun.Message.Method;

public class Client {

	public static void main(String[] zero) throws ClassNotFoundException{
		Socket socket;
		ObjectOutputStream oos;
		ObjectInputStream ois;
		try {
			// Connection
			socket = new Socket("localhost",2015);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			// Liste des matchs
			Message m = new Message(1, Method.ListeMatchs);
			oos.writeObject(m);
			oos.flush();
			
			Message reponse = (Message)ois.readObject();
			HashMap<Integer, String> matchs = (HashMap<Integer, String>) reponse.getArgument().get(0);
			for (Map.Entry<Integer, String> entry : matchs.entrySet()) {
			    System.out.println("Match no " + entry.getKey() + " : " + entry.getValue());
			}
			
			// Détails d'un match
			Message m2 = new Message(2, Method.DetailsMatch);
			m2.addArgument(1);
			oos.writeObject(m2);
			oos.flush();

			// TODO get results
			
			// TODO parie en ligne
			
			socket.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
