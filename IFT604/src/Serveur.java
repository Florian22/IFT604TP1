import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

	public static void main(String[] zero) {
		
		ServerSocket socketserver  ;
		Socket socketduserveur ;

		try {
		
			socketserver = new ServerSocket(2015, 2000);
			socketduserveur = socketserver.accept(); 
			System.out.println("Un z�ro s'est connect� !");
		        socketserver.close();
		        socketduserveur.close();

		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}
