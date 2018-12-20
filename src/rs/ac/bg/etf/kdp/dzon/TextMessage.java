package rs.ac.bg.etf.kdp.dzon;

public class TextMessage implements Message<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String body;

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String getBody() {
		return body;
	}

	@Override
	public void setBody(String body) {
		this.body = body;
	}

}
