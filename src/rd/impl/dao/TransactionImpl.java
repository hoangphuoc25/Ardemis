package rd.impl.dao;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.spec.ResourceHolder;
import rd.spec.dao.Transaction;

@Stateful
@RequestScoped
public class TransactionImpl implements Transaction, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7828715984476316619L;

	private static final String YOUR_STM_ID = "si1506s113";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject
	private ResourceHolder resourceHolder;
	private Connection connection;

	@Override
	public void begin() throws TransactionException {
		if(connection == null){
			DataSource dataSource = null;
			try {
				// This usage is irregular. Only on the STM. Normaly use 'getConnection()'.
				// connection = resourceHolder.getResource().getConnection(YOUR_STM_ID, YOUR_STM_ID);
				dataSource = InitialContext.doLookup("jdbc/testdb");
				connection = dataSource.getConnection(YOUR_STM_ID, YOUR_STM_ID);
				connection.setAutoCommit(false);

			} catch (SQLException e) {
				throw new TransactionException(e);
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void commit() throws TransactionException {
		try {
			if(connection != null){
				connection.commit();
			}
		} catch (SQLException e) {
			throw new TransactionException(e);
		}
	}

	@Override
	public void rollback() throws TransactionException {
		try {
			if(connection != null){
				connection.rollback();
			}
		} catch (SQLException e) {
			throw new TransactionException(e);
		}
	}

	@Override
	public boolean isActive() throws TransactionException {
		try {
			if (connection != null && connection.isClosed()) {
				connection = null;
				return false;
			}
		} catch (SQLException e) {
			throw new TransactionException(e);
		}

		return true;
	}

	@PreDestroy
	public void preDestory(){
		if(connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				logger.warn(e.getMessage(), e);
			}
			connection = null;
		}
	}

	@Override
	public <T> T getResource(Class<T> klass) {
		if (klass.equals(Connection.class)) {
			@SuppressWarnings("unchecked")
			T con = (T)connection;

			return con;
		}

		throw new IllegalArgumentException();
	}

}
