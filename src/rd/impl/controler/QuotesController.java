package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.ProductDto;
import rd.spec.service.ProductService;

@Named
@ConversationScoped
public class QuotesController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject ProductService prodService;

	public void conversationBegin() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

	public void conversationEnd() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
	}

	public void reload() {
		conversationBegin();
	}

	public List<ProductDto> getProdList() {
		if (prodList == null) {
			prodList = new ArrayList<ProductDto>();
		}
		return prodList;
	}

	public void setProdList(List<ProductDto> prodList) {
		this.prodList = prodList;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getProdSearch() {
		return prodSearch;
	}

	public void setProdSearch(String prodSearch) {
		this.prodSearch = prodSearch;
	}

	private List<ProductDto> prodList;
	private double total;
	private String prodSearch;

	public void calculateTotal() {
		total = 0;
		for (ProductDto dto: prodList) {
			if (dto.getDuration() == 0) {
				total += dto.getPermanentPrice() * dto.getQuantity();
			} else {
				total += dto.getPrice() * dto.getQuantity() * dto.getDuration();
			}
		}
	}

	public void addProdToList() throws NumberFormatException, IOException {
		prodList.add(prodService.getProductById(Integer.parseInt(prodSearch.split("[()]")[1])));
		calculateTotal();
		prodSearch = "";
	}

	public void removeSelected() {
		for (int i = prodList.size() - 1; i >= 0; i--) {
			if (prodList.get(i).isSelected()) {
				prodList.remove(i);
			}
		}
		calculateTotal();
	}
}
