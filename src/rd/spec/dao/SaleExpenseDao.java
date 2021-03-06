package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.SaleExpenseDto;

public interface SaleExpenseDao {
	public void addSaleExpense(Transaction transaction, SaleExpenseDto se)
			throws IOException;

	public SaleExpenseDto getSaleExpenseById(Transaction transaction, int seq)
			throws IOException;

	public void updateSaleExpense(Transaction transaction, SaleExpenseDto se)
			throws IOException;

	public void deleteSaleExpense(Transaction transaction, int seq)
			throws IOException;
	public int getSeq(Transaction transaction) throws IOException;
	public List<SaleExpenseDto> getBySalepersonId(Transaction transaction, String id) throws IOException;
}
