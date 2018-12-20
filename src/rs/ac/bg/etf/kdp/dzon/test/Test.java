package rs.ac.bg.etf.kdp.dzon.test;

import rs.ac.bg.etf.kdp.dzon.*;
import rs.ac.bg.etf.kdp.dzon.gui.*;
import rs.ac.bg.etf.kdp.dzon.net.RemoteMessageBox;
import rs.ac.bg.etf.kdp.dzon.net.modification.ModificationRemoteMessageBox;

public class Test {

	public static void main(String[] args) {
		//MessageBox<String> mBox = new MonitorMessageBox<String>(3); //new LockMessageBox<>(3);
		
		//MessageBox<String> mBox = new RemoteMessageBox<String>(args[0], args[1]);
		String host = "localhost";
		int port = 5678;
		MessageBox<String> mBox = new ModificationRemoteMessageBox<String>(host	, ""+port);
		new Put(mBox);
		new Get(mBox);
	}

}
