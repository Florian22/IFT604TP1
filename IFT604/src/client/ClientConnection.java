package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import commun.Message;

public class ClientConnection {
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Integer requestNumber = 0;
	
	public ClientConnection(String server, Integer port) throws IOException{
		socket = new Socket(server, port);
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}

	protected void finalize() throws IOException {
		socket.close();
	}
	
	private synchronized Integer getNextRequestNumber(){
		Integer nextNumber = requestNumber;
		requestNumber++;
		return nextNumber;
	}
	
	public synchronized Message executeRequest(Message m) throws IOException, ClassNotFoundException{
		m.setNumero(getNextRequestNumber());
		oos.writeObject(m);
		oos.flush();
		
		Message reponse = (Message)ois.readObject();
		return reponse;
	}
}
