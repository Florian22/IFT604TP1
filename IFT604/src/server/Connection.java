package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import commun.Marshallizer;
import commun.Match;
import commun.Message;
import commun.Message.Method;

public class Connection implements Runnable {
	LNH lnh;
	private Socket socket;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	private static int nbrclient = 0;

	public Connection(Socket s, LNH l) throws IOException {
		socket = s;
		lnh = l;
		ois = new ObjectInputStream(socket.getInputStream());
		oos = new ObjectOutputStream(socket.getOutputStream());
		nbrclient++;
		System.out.println("Un client est connecté !");
	}

	protected void finalize() throws IOException {
		nbrclient--;
		socket.close();
	}

	public void run() {
		Message m;
		try {
			while ((m = (Message) ois.readObject()) != null) {
				int noRequete = m.getNumero();
				Message reply = new Message(noRequete, Method.Reply);
				System.out.println("No de requete : " + m.getNumero());
				switch (m.getMethod()) {
				case ListeMatchs:
					System.out.println("Liste match!!");
					Map<Integer, String> matchs = lnh.getMatchList();
					reply.addArgument(matchs);
					sendMessage(reply);
					break;

				case DetailsMatch:
					System.out.println("Detail match!!");
					Integer matchNumber = (Integer)m.getArgument().get(0);
					System.out.println("match number : " + matchNumber);
					Match match = lnh.getMatchDetails(matchNumber);
					reply.addArgument(match);
					sendMessage(reply);
					break;

				default:
					break;
				}
				System.out.println("");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		System.out.println("Un client est déconnecté !");
	}
	
	private void sendMessage(Message m) throws IOException{
		oos.reset();
		oos.writeObject(m);
		oos.flush();
	}
}
