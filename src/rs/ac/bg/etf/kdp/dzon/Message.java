package rs.ac.bg.etf.kdp.dzon;

import java.io.Serializable;

public interface Message<T> extends Serializable {

	public long getId();
	public void setId(long id);

	public T getBody();
	public void setBody(T body);

}
