package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.RoleDto;
import rd.spec.dao.RoleDao;
import rd.spec.dao.Transaction;
import rd.spec.service.RoleService;

public class RoleServiceImpl implements RoleService, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private RoleDao roleDao;

	@Inject
	public RoleServiceImpl(Transaction transaction, RoleDao roleDao) {
		this.transaction = transaction;
		this.roleDao = roleDao;
	}

	@Override
	public List<RoleDto> getAllList() throws IOException {
		try {
			transaction.begin();
			List<RoleDto> roleList = roleDao.getAllList(transaction);
			transaction.commit();
			return roleList;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
