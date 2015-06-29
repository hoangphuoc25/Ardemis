package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import rd.dto.UserDto;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;
import rd.spec.service.UserService;

public class UserServiceImpl implements UserService, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6618357296233537217L;
	private Transaction transaction;
	private UserDao userDao;

	@Inject
	public UserServiceImpl(Transaction transaction, UserDao s_userDao){
		this.transaction =  transaction;
		this.userDao = s_userDao;
	}

	@Override
	public UserDto findUserById(String id) throws IOException {
		try{
			transaction.begin();
			UserDto userDto = userDao.findUser(transaction, id);
			transaction.commit();
			return userDto;
		}catch (IOException e){
			transaction.rollback();
			throw e;
		}
	}

	@Override
	@RolesAllowed("admin")
	public void addUser(UserDto userDto) throws IOException {
		try{
			transaction.begin();
			userDao.addUser(transaction, userDto);
			transaction.commit();
		}catch(IOException e){
			transaction.rollback();
			throw e;
		}
	}

	@Override
	@RolesAllowed("admin")
	public void updateUser(UserDto userDto) throws IOException {
		try{
			transaction.begin();
			userDao.updateUser(transaction, userDto);
			transaction.commit();
		}catch(IOException e){
			transaction.rollback();
			throw e;
		}
	}

	@Override
	@RolesAllowed("admin")
	public void deleteUser(String id) throws IOException {
		try{
			transaction.begin();
			userDao.deleteUser(transaction, id);
			transaction.commit();
		}catch(IOException e){
			transaction.rollback();
			throw e;
		}
	}

	public List<UserDto> getUserByTeam(int teamSeq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<UserDto> result = userDao.getUserByTeam(transaction, teamSeq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<UserDto> searchByName(String name) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<UserDto> result = userDao.searchByName(transaction, name);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public void updatePassword(String user, String password) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			userDao.updatePassword(transaction, user, password);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<UserDto> getUserByRole(String role) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<UserDto> usrs = userDao.getUserByRole(transaction, role);
			transaction.commit();
			return usrs;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
