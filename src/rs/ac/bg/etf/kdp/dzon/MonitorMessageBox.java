package rs.ac.bg.etf.kdp.dzon;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MonitorMessageBox<T> implements MessageBox<T> {

	private List<Message<T>> buffer;
	private final int capacity;

	public MonitorMessageBox(int capacity) {
		super();
		this.capacity = capacity;
		buffer = new LinkedList<>();
	}

	@Override
	public synchronized void put(Message<T> m, Priority p, long ttl, TimeUnit unit) {
		while (buffer.size() == capacity) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		notifyAll();
		buffer.add(m);
	}

	@Override
	public synchronized Message<T> get(long ttd, TimeUnit unit, Status s) {
		while (buffer.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		Message<T> msg = buffer.remove(0);
		notifyAll();
		return msg;
	}

}
