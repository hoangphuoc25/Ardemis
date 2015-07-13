package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.FeedbackDto;
import rd.dto.InvoiceDto;
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

}
