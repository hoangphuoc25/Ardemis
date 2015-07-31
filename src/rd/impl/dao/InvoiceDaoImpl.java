package rd.impl.dao;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.CompanyDto;
import rd.dto.InvoiceDto;
import rd.dto.ProductDto;
import rd.dto.UserDto;
import rd.spec.dao.CompanyDao;
import rd.spec.dao.InvoiceDao;
import rd.spec.dao.ProductDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;

public class InvoiceDaoImpl implements InvoiceDao, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ProductDao productDao;
	private CompanyDao companyDao;
	private UserDao userDao;

	@Inject
	public InvoiceDaoImpl(ProductDao productDao, CompanyDao companyDao, UserDao userDao) {
		this.productDao = productDao;
		this.companyDao = companyDao;
		this.userDao = userDao;
	}

	public List<InvoiceDto> getAll(Transaction transaction) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ALL);
			resultSet = prepareStatement.executeQuery();

			List<InvoiceDto> result = new ArrayList<InvoiceDto>();
			while (resultSet.next()) {
				result.add(makeInvoiceDto(transaction, resultSet));
			}
			return result;

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}

			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	private InvoiceDto makeInvoiceDto(Transaction transaction,
			ResultSet resultSet) throws SQLException, IOException {
		int seq = resultSet.getInt(1);
		CompanyDto company = companyDao.getById(transaction,
				resultSet.getInt(2));
		Date purchaseDate = new Date(resultSet.getDate(3).getTime());
		double amount = resultSet.getDouble(4);
		UserDto sale = userDao.findUser(transaction, resultSet.getString(5));

		return new InvoiceDto(seq, company, purchaseDate, amount, null, sale);
	}

	public InvoiceDto getById(Transaction transaction, int seq) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			InvoiceDto result = null;
			if(resultSet.next()) {
				result = makeInvoiceDto(transaction, resultSet);
			}
			return result;

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}

			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	public void deleteById(Transaction transaction, int seq) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_INVOICE);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			prepareStatement = connection.prepareStatement(DELETE_PRODUCT_PURCHASE);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}

			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	public List<ProductDto> getProductByInvoiceId(Transaction transaction,
			int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_PRODUCT_BY_INVOICE_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			List<ProductDto> result = new ArrayList<ProductDto>();
			while (resultSet.next()) {
				result.add(productDao.getProductById(transaction,
						resultSet.getInt(1)));
			}

			return result;
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	public int getSeq(Transaction transaction) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_SEQ);
			resultSet = prepareStatement.executeQuery();

			int seq = 1;
			if (resultSet.next()) {
				seq = resultSet.getInt(1);
			}
			return seq;

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}

			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	public void addInvoice(Transaction transaction, InvoiceDto invoice) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_INVOICE);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setInt(2, invoice.getCustomer().getSeq());
			prepareStatement.setDate(3, new java.sql.Date(invoice.getPurchaseDate().getTime()));
			prepareStatement.setDouble(4, invoice.getAmount());
			prepareStatement.setString(5, invoice.getSalesperson().getId());
			resultSet = prepareStatement.executeQuery();

			for (ProductDto dto: invoice.getProducts()) {
				prepareStatement = connection.prepareStatement(ADD_PRODUCT_PURCHASE);
				prepareStatement.setInt(1, invoice.getSeq());
				prepareStatement.setInt(2, dto.getSeq());
				resultSet = prepareStatement.executeQuery();
			}
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	private static String ADD_INVOICE = "insert into t_invoice (seq, customer_seq, purchase_date, amount, salesperson) values (?, ?, ?, ?, ?)";
	private static String GET_PRODUCT_BY_INVOICE_ID = "select product_seq from t_product_purchase where invoice_seq=?";
	private static String GET_BY_ID = "select seq, customer_seq, purchase_date, amount, salesperson from t_invoice where seq=?";
	private static String GET_BY_CUSTOMER = "select seq, customer_seq, purchase_date, amount, salesperson from t_invoice where customer_seq=?";
	private static String UPDATE_INVOICE = "update t_invoice set customer_seq=?, purchase_date=?, amount=?, salesperson=? where seq=?";
	private static String DELETE_INVOICE = "delete from t_invoice where seq=?";
	private static String GET_SEQ = "select max(seq)+1 from t_invoice";
	private static String GET_ALL = "select seq, customer_seq, purchase_date, amount, salesperson from t_invoice order by seq";
	private static String ADD_PRODUCT_PURCHASE = "insert into t_product_purchase (invoice_seq, product_seq) values (?, ?)";
	private static String DELETE_PRODUCT_PURCHASE = "delete from t_product_purchase where invoice_seq=?";

	public List<InvoiceDto> getByCustomer(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_CUSTOMER);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			List<InvoiceDto> result = new ArrayList<InvoiceDto>();
			while (resultSet.next()) {
				result.add(makeInvoiceDto(transaction, resultSet));
			}
			return result;
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	public void updateInvoice(Transaction transaction, InvoiceDto invoice) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_INVOICE);
			prepareStatement.setInt(1, invoice.getCustomer().getSeq());
			prepareStatement.setDate(2, new java.sql.Date(invoice.getPurchaseDate().getTime()));
			prepareStatement.setDouble(3, invoice.getAmount());
			prepareStatement.setString(4, invoice.getSalesperson().getId());
			prepareStatement.setInt(5, invoice.getSeq());
			resultSet = prepareStatement.executeQuery();

			prepareStatement = connection.prepareStatement(DELETE_PRODUCT_PURCHASE);
			prepareStatement.setInt(1, invoice.getSeq());
			resultSet = prepareStatement.executeQuery();

			for (ProductDto dto: invoice.getProducts()) {
				prepareStatement = connection.prepareStatement(ADD_PRODUCT_PURCHASE);
				prepareStatement.setInt(1, invoice.getSeq());
				prepareStatement.setInt(2, dto.getSeq());
				resultSet = prepareStatement.executeQuery();
			}

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}
	public List<InvoiceDto> findInvoicesByProduct(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(FIND_INVOICES_BY_PRODUCT);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			List<Integer> temp = new ArrayList<Integer>();
			while (resultSet.next()) {
				temp.add(resultSet.getInt(1));
			}

			List<InvoiceDto> result = new ArrayList<InvoiceDto>();
			for (Integer sequence: temp) {
				result.add(getById(transaction, sequence));
			}

			return result;
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}
	private static String FIND_INVOICES_BY_PRODUCT = "select distinct ti.seq from t_invoice ti join t_product_purchase tpp on ti.seq=tpp.invoice_seq where tpp.product_seq=?";
	public List<InvoiceDto> searchInvoiceBeforeDate(Transaction transaction, Date date) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(SEARCH_INVOICE_BEFORE_DATE);
			prepareStatement.setDate(1, new java.sql.Date(date.getTime()));
			resultSet = prepareStatement.executeQuery();

			List<InvoiceDto> result = new ArrayList<InvoiceDto>();
			while (resultSet.next()) {
				result.add(makeInvoiceDto(transaction, resultSet));
			}
			return result;
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}
	private static String SEARCH_INVOICE_BEFORE_DATE = "select seq, customer_seq, purchase_date, amount from t_invoice where purchase_date <= ? ";
	public List<InvoiceDto> searchInvoiceAfterDate(Transaction transaction, Date date) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(SEARCH_INVOICE_AFTER_DATE);
			prepareStatement.setDate(1, new java.sql.Date(date.getTime()));
			resultSet = prepareStatement.executeQuery();

			List<InvoiceDto> result = new ArrayList<InvoiceDto>();
			while (resultSet.next()) {
				result.add(makeInvoiceDto(transaction, resultSet));
			}
			return result;
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}
	private static String SEARCH_INVOICE_AFTER_DATE = "select seq, customer_seq, purchase_date, amount from t_invoice where purchase_date >= ?";
	public List<InvoiceDto> searchInvoiceBeforeAfter(Transaction transaction, Date after, Date before) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(SEARCH_INVOICE_BEFORE_AFTER);
			prepareStatement.setDate(1, new java.sql.Date(after.getTime()));
			prepareStatement.setDate(2, new java.sql.Date(before.getTime()));
			resultSet = prepareStatement.executeQuery();

			List<InvoiceDto> result = new ArrayList<InvoiceDto>();
			while (resultSet.next()) {
				result.add(makeInvoiceDto(transaction, resultSet));
			}
			return result;
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}
	private static String SEARCH_INVOICE_BEFORE_AFTER = "select seq, customer_seq, purchase_date, amount from t_invoice where purchase_date >= ? and purchase_date <= ?";
	public List<CompanyDto> findCompanyByProduct(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(FIND_COMPANY_BY_PRODUCT);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			List<CompanyDto> result = new ArrayList<CompanyDto>();
			while (resultSet.next()) {
				result.add(companyDao.getById(transaction, resultSet.getInt(1)));
			}
			return result;

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}
	private static String FIND_COMPANY_BY_PRODUCT = "select distinct i.customer_seq from t_invoice i join t_product_purchase pp on i.seq = pp.invoice_seq where pp.product_seq=?";
}
