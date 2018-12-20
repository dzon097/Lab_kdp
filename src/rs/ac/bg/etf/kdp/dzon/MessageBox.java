package rs.ac.bg.etf.kdp.dzon;

import java.util.concurrent.TimeUnit;

public interface MessageBox<T> {

	public void put(Message<T> m, Priority p, long ttl, TimeUnit unit);
	public Message<T> get(long ttd, TimeUnit unit, Status s);
}
