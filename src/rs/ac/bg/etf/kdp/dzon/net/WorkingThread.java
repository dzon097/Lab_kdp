package rs.ac.bg.etf.kdp.dzon.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import rs.ac.bg.etf.kdp.dzon.Message;
import rs.ac.bg.etf.kdp.dzon.MessageBox;
import rs.ac.bg.etf.kdp.dzon.Priority;

//U ovoj klasi obradjujem ceo zahtev poslat od starane clienta 
public class WorkingThread extends Thread {

	private Socket client;
	private MessageBox<String> buffer;

	public WorkingThread(Socket client, MessageBox<String> buffer2) {
		this.client = client;
		this.buffer = buffer2;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public void run() {
		try (Socket socket = this.client;
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());) {

			String operation = (String) in.readObject();
			Message<String> msg = null;

			switch (operation) {
			
			case "send":
				msg = (Message<String>) in.readObject();
				Priority priority = (Priority) in.readObject();
				long ttl = (long) in.readObject();
				System.out.println("SEND - MSG: " + msg.getBody() + "; FROM: " + socket.getRemoteSocketAddress());
				buffer.put(msg, priority, ttl, null);
				out.writeObject("OK");	//Potvrda prijema poruke
				break;
				
			case "receive":
				System.out.println("**Primio get**");
				msg = buffer.get(0, null, null);
				System.out.println("RECEIVE - MSG: " + msg.getBody() + "; FROM: " + socket.getRemoteSocketAddress());
				out.writeObject(msg);
				in.readObject();		//Ceka potvrdu ?!
				break;
				
			default:
				System.out.println("*** Unknown operation ***");
				break;
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
