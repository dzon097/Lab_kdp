package rs.ac.bg.etf.kdp.dzon.net.modification;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import rs.ac.bg.etf.kdp.dzon.Message;

public class DataMessage implements Message<Object>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	byte[] data;
	long id;

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;

	}

	@Override
	public Object getBody() {
		if (data == null || name == null)
			return name;
		else {
			try {
				File f = new File(name);
				if (!f.exists())
					f.createNewFile();
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(data);
				fos.close();
				
				StringBuilder sb = new StringBuilder();
				FileReader fr = new  FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String line;
				
				while((line=br.readLine()) != null)
					sb.append(line);
				
				br.close();
				fr.close();
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	@Override
	public void setBody(Object body) {
		// na osnovu imena fajla ucitava se njegov sadrzaj u program
		name = (String) body;
		if (name == null)
			return;
		data = null;
		try {
			File f = new File(name);
			if (f.exists()) {
				
				
				Path path = Paths.get(name);
				data = Files.readAllBytes(path);
			}
		} catch (Exception e) {
		}
	}

}
