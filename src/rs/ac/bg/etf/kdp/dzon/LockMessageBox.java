package rs.ac.bg.etf.kdp.dzon;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockMessageBox<T> implements MessageBox<T> {

	private Lock lock;
	private Condition notFull, notEmpty;

	private List<Message<T>> buffer;
	private int capacity;

	public LockMessageBox(int capacity) {
		this.capacity = capacity;
		buffer = new LinkedList<>();
		lock = new ReentrantLock(true);
		notFull = lock.newCondition();
		notEmpty = lock.newCondition();
	}

	@Override
	public void put(Message<T> m, Priority p, long ttl, TimeUnit unit) {
		lock.lock();
		try {
			while (buffer.size() == capacity) {
				notFull.awaitUninterruptibly();
			}
			notEmpty.signal();
			buffer.add(m);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Message<T> get(long ttd, TimeUnit unit, Status s) {
		lock.lock();
		try {
			while (buffer.isEmpty()) {
				notEmpty.awaitUninterruptibly();
			}
			Message<T> msg = buffer.remove(0);
			notFull.signal();
			return msg;
		} finally {
			lock.unlock();
		}
	}

}
