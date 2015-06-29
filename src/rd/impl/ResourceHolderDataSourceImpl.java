package rd.impl;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.Resource;
import javax.sql.DataSource;

import rd.spec.ResourceHolder;

public class ResourceHolderDataSourceImpl implements ResourceHolder, Serializable {


	private static final long serialVersionUID = -6342428613816329369L;

	@Resource(name="jdbc/testdb")
	private DataSource dataSource;// = null;

	@Override
	public DataSource getResource() throws IOException {
		return dataSource;
	}


}
