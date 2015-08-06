package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.ExpBudgetDto;

public interface ExpBudgetService {
	public void addBudget(ExpBudgetDto budget) throws IOException;
	public void updateBudget(ExpBudgetDto budget) throws IOException;
	public void deleteBudget(ExpBudgetDto budget) throws IOException;
	public List<ExpBudgetDto> getBudgetByUser(String userId) throws IOException;
	public ExpBudgetDto getCurrentBudget(String userId) throws IOException;
}
