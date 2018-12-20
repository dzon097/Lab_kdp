package rs.ac.bg.etf.kdp.dzon.net.modification;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import rs.ac.bg.etf.kdp.dzon.Message;
import rs.ac.bg.etf.kdp.dzon.MessageBox;
import rs.ac.bg.etf.kdp.dzon.Priority;

//Modifikovan recive ili ti dohvatanje podatka od strane klijenta
public class ModificationWorkingThread extends Thread {

	private Socket client;
	private MessageBox<String> buffer;

	public ModificationWorkingThread(Socket client, MessageBox<String> buffer2) {
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
				out.writeObject("OK"); // Potvrda prijema poruke

				/* Dodatak */System.out.println(Thread.currentThread().getName() + " message");
				break;

			case "receive": // AKO SE SLANJE NE URADI USPESNO, PODATAK SE NE PROSLEDI KLIJENTU, POTREBNO JE
							// VRATITI ELEMENT NA VRH BAFFERA ODAKLE JE I UZET, TREBA ZNACI DA BI SE TO
							// IZVELO ISPOMERATI I PRESLOZITI ELEMENTE
				try {
					msg = buffer.get(0, null, null);
					System.out
							.println("RECEIVE - MSG: " + msg.getBody() + "; FROM: " + socket.getRemoteSocketAddress());
					out.writeObject(msg);
					in.readObject(); // Ceka potvrdu ?!

					/* Dodatak */System.out.println(Thread.currentThread().getName() + "End message");
					break;
				} catch (Exception e) {
					Message<String> tmp1, tmp2 = null;
					//inic
					buffer.put(msg, null, 0, null); // ubacim staru pooruku na kraj bufera
					tmp1 = buffer.get(0, null, null); // izvucem trenutno prvu poruku iz bafera
					
					buffer.put(msg, null, 0, null); // ponovo ubacim staru poruku na kraj bafera
					while (tmp1 != msg) {
						tmp2 = buffer.get(0, null, null); // uzimam sledecisa vrha sve dok ne uzmem onu staru sto sam
															// stavio 2 puta jednom za ovaj uslov i jedno zato sto treba
						buffer.put(tmp1, null, 0, null);	//ubacim na kraj taj element sto sam dohvatio sa vrha
						tmp1 = tmp2;						//prebacim se na sledeci i teram daje
					}

				}

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
