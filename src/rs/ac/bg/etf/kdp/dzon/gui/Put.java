package rs.ac.bg.etf.kdp.dzon.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import rs.ac.bg.etf.kdp.dzon.Message;
import rs.ac.bg.etf.kdp.dzon.MessageBox;
import rs.ac.bg.etf.kdp.dzon.TextMessage;

/**
 * Put klasa. <br>
 * Koristi Java Thread klasu za izvrsavanje zahteva.
 */
@SuppressWarnings("serial")
public class Put extends JFrame {

	JButton jb;
	JTextArea jta;
	MessageBox<String> messageBox;

	public Put(MessageBox<String> mBox) {
		super("Put");
		jb = new JButton("Put");
		jta = new JTextArea();
		messageBox = mBox;

		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				jb.setEnabled(false);
				jta.setEditable(false);
				String text = jta.getText();

				Thread t = new Thread() {

					@Override
					public void run() {
						Message<String> msg = new TextMessage();
						msg.setBody(text);
						messageBox.put(msg, null, 0, null);

						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								jta.setEditable(true);
								jb.setEnabled(true);
								jta.setText("");
							}
						});
					}
				};
				t.start();
			}
		});

		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(2, 1));
		this.setBounds(200, 200, 300, 300);
		this.add(jta);
		this.add(jb);
	}

}
