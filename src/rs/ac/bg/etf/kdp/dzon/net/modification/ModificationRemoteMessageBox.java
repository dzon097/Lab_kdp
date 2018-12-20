package rs.ac.bg.etf.kdp.dzon.net.modification;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import rs.ac.bg.etf.kdp.dzon.*;

//primer testa sa laba 3!!

public class ModificationRemoteMessageBox<T> implements MessageBox<T> {

	String host;
	int port;

	private static final int TRY = 3;

	public ModificationRemoteMessageBox(String host, String port) {
		super();
		this.host = host;
		this.port = Integer.parseInt(port);
	}

	@Override
	public void put(Message<T> m, Priority p, long ttl, TimeUnit unit) {

		boolean work = true;
		int count = 0;

		while (work) {
			try (Socket socket = new Socket(host, port);
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());) {

				out.writeObject("send"); // proslediti sve argumente poziva serveru, a ne samo ime metode i poruku !!
				out.writeObject(m);
				out.writeObject(p);
				out.writeObject(ttl); // ovo posto znam tip mog slati kao out.writeLong(ttl); ali tako se onda mora i
										// primiti na serverskoj strani !!
				out.flush();

				in.readObject(); // primam potvrdnu poruko, nesto cime mi Server javlja da je primio moj zahtev;
									// obezbedjuje inhronisovanost razmene poruka izmedju klijenta i servera tako
									// sto cekam potvrdu od Servera da je primijo i obradio zahtev
				// kod sinhrone komunikacije proces se blokira na slanju poruke, posto ovde nije
				// to slucaj, ovo postizemo cekanjem te potvrde

				work = false; // uspesno obavljen posao !!

			} catch (IOException e) {
				if (++count == TRY) {
					work = false;
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				if (++count == TRY) {
					work = false;
					e.printStackTrace();
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Message<T> get(long ttd, TimeUnit unit, Status s) {

		boolean work = true;
		int count = 0;

		Message<T> msg = null;

		while (work) {

			try (Socket socket = new Socket(host, port);
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());) {
				out.writeObject("receive");
				msg = (Message<T>) in.readObject();
				
				out.writeObject("OK"); // potvrda prijema poruke od strane servera

				work = false;

			} catch (IOException e) {
				if (++count == TRY) {
					work = false;
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				if (++count == TRY) {
					work = false;
					e.printStackTrace();
				}
			}
		}
		return msg;
	}

}
