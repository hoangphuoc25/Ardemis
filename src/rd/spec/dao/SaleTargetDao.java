package rd.spec.dao;

import java.io.IOException;

import rd.dto.SaleTargetDto;

public interface SaleTargetDao {
	public SaleTargetDto getSaleTarget(Transaction transaction, String saleId) throws IOException;
	public void addSaleTarget(Transaction transaction, SaleTargetDto std) throws IOException;
	public void updateSaleTarget(Transaction transaction, SaleTargetDto std) throws IOException;
}
