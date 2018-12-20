package rs.ac.bg.etf.kdp.dzon.net.modification;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rs.ac.bg.etf.kdp.dzon.LockMessageBox;
import rs.ac.bg.etf.kdp.dzon.MessageBox;
import rs.ac.bg.etf.kdp.dzon.net.WorkingThread;

public class ModificationServer {

	public static void main(String[] args) {
		//int port = Integer.parseInt(args[0]);
		
		int port = 5678;
		MessageBox<String> buffer   = new LockMessageBox<String>(5);
		
		ExecutorService executor = Executors.newFixedThreadPool(4);
		
		try(ServerSocket server = new ServerSocket(port);){
			System.out.println("Server sarted...");
			
			while(true) {
				Socket client = server.accept();
				Runnable worker = new ModificationWorkingThread(client, buffer);
				executor.execute(worker);
				
				//new WorkingThread(client, buffer).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			executor.shutdown();
			while(!executor.isTerminated());
			
			System.out.println("Finished all threads");
		}

	}

}
