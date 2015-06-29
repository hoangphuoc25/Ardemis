package rd.spec.service;

import java.io.IOException;

import rd.dto.SaleExpenseDto;

public interface SaleExpenseService {
	public void addSaleExpense(SaleExpenseDto se) throws IOException;
	public SaleExpenseDto getSaleExpenseById(int seq) throws IOException;
	public void updateSaleExpense(SaleExpenseDto se) throws IOException;
	public void deleteSaleExpense(int seq) throws IOException;
}
