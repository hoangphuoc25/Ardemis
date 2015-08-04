package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.CompanyDto;
import rd.spec.dao.CompanyDao;
import rd.spec.dao.Transaction;
import rd.spec.service.CompanyService;

public class CompanyServiceImpl implements CompanyService, Serializable {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private CompanyDao companyDao;

	@Inject
	public CompanyServiceImpl(Transaction transaction, CompanyDao companyDao) {
		this.transaction = transaction;
		this.setCompanyDao(companyDao);
	}

	public void updateCompany(CompanyDto company) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			companyDao.updateCompany(transaction, company);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public void deleteCompany(int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			companyDao.deleteCompany(transaction, seq);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public void insertCompany(CompanyDto company) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			companyDao.insertCompany(transaction, company);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public CompanyDao getCompanyDao() {
		return companyDao;
	}

	public void setCompanyDao(CompanyDao companyDao) {
		this.companyDao = companyDao;
	}

	public CompanyDto getById(int seq) throws IOException {
		try{
			transaction.begin();
			CompanyDto company = companyDao.getById(transaction, seq);
			transaction.commit();
			return company;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<CompanyDto> getByIndustry(String industry) throws IOException {
		try{
			transaction.begin();
			List<CompanyDto> list = companyDao.getByIndustry(transaction, industry);
			transaction.commit();
			return list;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<CompanyDto> searchCompanyByName(String name) throws IOException {
		try{
			transaction.begin();
			List<CompanyDto> result = companyDao.searchCompanyByName(transaction, name);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<CompanyDto> getAll() throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<CompanyDto> result = companyDao.getAll(transaction);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public int getSeq() throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			int seq = companyDao.getSeq(transaction);
			transaction.commit();
			return seq;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<CompanyDto> getCompanyByContactStatus(String status) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<CompanyDto> result = companyDao.getCompanyByContactStatus(transaction, status);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<CompanyDto> searchByIndustry(String industry) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<CompanyDto> result = companyDao.searchByIndustry(transaction, industry);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<CompanyDto> searchByLocation(String location) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<CompanyDto> result = companyDao.searchByLocation(transaction, location);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public CompanyDto searchCompanyByNameExact(String name) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			CompanyDto result = companyDao.searchCompanyByNameExact(transaction, name);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<CompanyDto> getCompanyByContactStatusAndUser(String status, String user) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<CompanyDto> result = companyDao.getCompanyByContactStatusAndUser(transaction, status, user);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
