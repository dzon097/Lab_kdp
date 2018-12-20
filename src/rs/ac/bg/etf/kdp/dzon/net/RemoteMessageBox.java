package rs.ac.bg.etf.kdp.dzon.net;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import rs.ac.bg.etf.kdp.dzon.*;

public class RemoteMessageBox<T> implements MessageBox<T> {

	String host;
	int port;

	public RemoteMessageBox(String host, String port) {
		super();
		this.host = host;
		this.port = Integer.parseInt(port);
	}

	@Override
	public void put(Message<T> m, Priority p, long ttl, TimeUnit unit) {
		try (Socket socket = new Socket(host, port);
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());) {
			out.writeObject("send");
			out.writeObject(m);
			out.writeObject(p);
			out.writeObject(ttl);
			out.flush(); // do ovog trenutka se sve sto sam radio writeObject sakupljalo u lokalnom
							// baferu i cekalo slanje

			in.readObject(); // primam potvrdnu poruko, nesto cime mi Server javlja da je primio moj zahtev;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Message<T> get(long ttd, TimeUnit unit, Status s) {
		Message<T> msg =null;
		try(Socket socket = new Socket(host, port);
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				){
			out.writeObject("receive");
			msg = (Message<T>) in.readObject();
			out.writeObject("OK");  //potvrda prijema poruke od strane servera
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

}
