package rd.spec.dao;

public interface Transaction {
	public void begin() throws TransactionException;
	public void commit() throws TransactionException;
	public boolean isActive() throws TransactionException;
	public void rollback() throws TransactionException;
	public <T> T getResource(Class<T> klass);


	public static class TransactionException extends RuntimeException{

		public TransactionException(Exception e) {
			super(e);
		}
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

	}
}
