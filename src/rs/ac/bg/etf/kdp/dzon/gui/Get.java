package rs.ac.bg.etf.kdp.dzon.gui;

import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

import rs.ac.bg.etf.kdp.dzon.*;

/**
 * Get klasa.<br>
 * Koristi Java Thread klasu za izvrsavanje zahteva.
 */
@SuppressWarnings("serial")
public class Get extends JFrame {

	JButton jb;
	JTextArea jta;
	MessageBox<String> messageBox;

	public Get(MessageBox<String> mBox) {
		super("Get");
		jb = new JButton("Get");
		jta = new JTextArea();
		messageBox = mBox;

		jta.setEditable(false);

		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jb.setEnabled(false);
				
				
				SwingWorker<String, Void> sw = new SwingWorker<String, Void>() {

					@Override
					protected String doInBackground() throws Exception {
						Message<String> msg = messageBox.get(0, null, null);
						String boody = msg.getBody();
						return boody;
					}
					@Override
					protected void done() {
						try {
							jta.setText(get());
						}catch(Exception e) {
							e.printStackTrace();
						}
						jb.setEnabled(true);
					}
					
					
				};
				
				sw.execute();

			/*	Thread t = new Thread() {

					@Override
					public void run() {
						Message<String> msg = messageBox.get(0, null, null);
						String body = msg.getBody();
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								jta.setText(body);
								jb.setEnabled(true);
							}
						});
					}
				};
				t.start();
				*/
			}
		});

		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(2, 1));
		this.setBounds(600, 200, 300, 300);
		this.add(jb);
		this.add(jta);
	}

}
