package rs.ac.bg.etf.kdp.dzon.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import rs.ac.bg.etf.kdp.dzon.LockMessageBox;
import rs.ac.bg.etf.kdp.dzon.MessageBox;

public class Server {

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		MessageBox<String> buffer   = new LockMessageBox<String>(5);
		try(ServerSocket server = new ServerSocket(port);){
			System.out.println("Server sarted...");
			while(true) {
				Socket client = server.accept();
				new WorkingThread(client, buffer).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
