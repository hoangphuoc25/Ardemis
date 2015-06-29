package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.TeamDto;
import rd.spec.dao.TeamDao;
import rd.spec.dao.Transaction;
import rd.spec.service.TeamService;

public class TeamServiceImpl implements TeamService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private TeamDao teamDao;

	@Inject
	public TeamServiceImpl(Transaction transaction, TeamDao teamDao) {
		this.transaction = transaction;
		this.teamDao = teamDao;
	}

	public void addTeam(TeamDto team) throws IOException {
		try {
			transaction.begin();
			teamDao.addTeam(transaction, team);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void updateTeam(TeamDto team) throws IOException {
		try {
			transaction.begin();
			teamDao.updateTeam(transaction, team);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public TeamDto getById(int seq) throws IOException {
		try {
			transaction.begin();
			TeamDto team = teamDao.getById(transaction, seq);
			transaction.commit();
			return team;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void deleteTeam(TeamDto team) throws IOException {
		try {
			transaction.begin();
			teamDao.deleteTeam(transaction, team);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<TeamDto> getAll() throws IOException {
		try{
			transaction.begin();
			List<TeamDto> all = teamDao.getAll(transaction);
			transaction.commit();
			return all;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
