package rd.spec;

import java.io.IOException;

import javax.sql.DataSource;

public interface ResourceHolder {

	public DataSource getResource() throws IOException;

}
