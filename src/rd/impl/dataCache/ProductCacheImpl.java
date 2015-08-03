package rd.impl.dataCache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

import rd.dto.ProductDto;
import rd.spec.dataCache.ProductCache;

@Stateful
@SessionScoped
public class ProductCacheImpl implements ProductCache, Serializable {
	private static final long serialVersionUID = 1L;

	private Map<Integer, ProductDto> productMap = new HashMap<Integer, ProductDto>();

	@Override
	public ProductDto getProduct(int seq) {
		if (productMap.containsKey(seq))
			return productMap.get(seq);
		else
			return null;
	}

	@Override
	public void putProduct(ProductDto product) {
		productMap.put(product.getSeq(), product);
	}

	@Override
	public void clear() {
		productMap.clear();
	}

	@Override
	public void removeProduct(int seq) {
		productMap.remove(seq);
	}
}
