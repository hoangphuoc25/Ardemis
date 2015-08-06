package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.ExpBudgetDto;

public interface ExpBudgetDao {
	public void addBudget(Transaction transaction, ExpBudgetDto budget) throws IOException;
	public void updateBudget(Transaction transaction, ExpBudgetDto budget) throws IOException;
	public void deleteBudget(Transaction transaction, ExpBudgetDto budget) throws IOException;
	public List<ExpBudgetDto> getBudgetByUser(Transaction transaction, String userId) throws IOException;
	public ExpBudgetDto getCurrentBudget(Transaction transaction, String userId) throws IOException;
}
