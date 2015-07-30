package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.FeedbackDto;
import rd.dto.InvoiceDto;
import rd.dto.ProductDto;
import rd.spec.service.FeedbackService;
import rd.spec.service.InvoiceService;
import rd.spec.service.ProductService;

@Named
@ConversationScoped
public class ProductDetailController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject FeedbackService fbService;
	@Inject ProductService prodService;
	@Inject InvoiceService invoiceService;

	public void conversationBegin() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

	public void conversationEnd() {
		if(!conversation.isTransient()){
			conversation.end();
		}
	}

	public void reload() {
		conversationBegin();
	}

	public List<FeedbackDto> getFeedbacks() throws IOException {
		if (feedbacks == null || feedbacks.size() == 0) {
			feedbacks = fbService.getFeedbackByProduct(seq);
		}
		return feedbacks;
	}

	public void setFeedbacks(List<FeedbackDto> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public List<InvoiceDto> getPurchases() throws IOException {
		if (purchases == null || purchases.size() == 0) {
			purchases = invoiceService.findInvoicesByProduct(seq);
		}
		return purchases;
	}

	public void setPurchases(List<InvoiceDto> purchases) {
		this.purchases = purchases;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	private List<InvoiceDto> purchases;
	private List<FeedbackDto> feedbacks;
	private int seq;

	@Inject SessionController sessionController;
	public double deduceAmount(InvoiceDto invoice) {
		if (sessionController.getCurrency() == 0)
			return invoice.getAmount();
		return invoice.getAmount()*sessionController.getRates().get(sessionController.getCurrency());
	}

	public Map<Integer, String> getFbDetail() {
		if (fbDetail == null) {
			fbDetail = new HashMap<Integer, String>();
			fbDetail.put(1, "Not acceptable");
			fbDetail.put(2, "Poor");
			fbDetail.put(3, "OK");
			fbDetail.put(4, "Good");
			fbDetail.put(5, "Very good");
		}
		return fbDetail;
	}

	public void setFbDetail(Map<Integer, String> fbDetail) {
		this.fbDetail = fbDetail;
	}

	private Map<Integer, String> fbDetail;

	public String getDetail(int score) {
		if (score == 1) {
			return "Not acceptable";
		} else if (score == 2) {
			return "Poor";
		} else if (score == 3) {
			return "OK";
		} else if (score == 4) {
			return "Good";
		} else if (score == 5) {
			return "Very good";
		} else {
			return "";
		}

	}
}
