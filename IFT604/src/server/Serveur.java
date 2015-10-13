package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

import commun.Marshallizer;
import commun.Match;
import commun.Message;

public class Serveur {

	public static void main(String[] zero) {
		// LNH creation
		LNH lnh = new LNH();
		// Create matchs
		Match m1 = new Match("Canadiens", "Bruins", 1);
		Match m2 = new Match("Rangers", "Red wings", 2);
		lnh.startMatch(m1);
		lnh.startMatch(m2);
		
		m1.scoreEquipeA("P.K Subban");
		m1.scoreEquipeA("Paccioretty");
		m1.scoreEquipeB("Bergeron");
		m1.penaliteEquipeA("Paccioretty", 2);
		
		m2.scoreEquipeA("JoueurX");
		
		
		//
		ServerSocket listenSocket;
		try {
			listenSocket = new ServerSocket(2015);
			System.out.println("Server Online");
			while (true) {
				Socket clientSocket = listenSocket.accept();
				Connection c = new Connection(clientSocket, lnh);
				new Thread(c).start();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}

