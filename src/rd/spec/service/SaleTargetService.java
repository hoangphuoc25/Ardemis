package rd.spec.service;

import java.io.IOException;

import rd.dto.SaleTargetDto;

public interface SaleTargetService {
	public SaleTargetDto getSaleTarget(String saleId) throws IOException;
	public void addSaleTarget(SaleTargetDto std) throws IOException;
	public void updateSaleTarget(SaleTargetDto std) throws IOException;
}
