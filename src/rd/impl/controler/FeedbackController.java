package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.FeedbackDto;
import rd.dto.NoteDto;
import rd.dto.ProductDto;
import rd.dto.UserDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.FeedbackService;
import rd.spec.service.NoteService;
import rd.spec.service.ProductService;

@Named
@ConversationScoped
public class FeedbackController implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 2L;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject Conversation conversation;
	@Inject SessionManager sessionManager;
	@Inject NoteService noteService;
	@Inject FeedbackService fbService;
	@Inject ProductService prodService;

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

	public int getProduct() {
		return product;
	}

	public void setProduct(int product) {
		this.product = product;
	}

	private int product;

	public void submit() {
		System.out.println("FeedbackController.submit()");
		rating = 0;
		product = -1;
		setFeedback("");
		sessionManager.addGlobalMessageInfo("Thanks for your feedback", null);
	}

	public void addFeedback() throws IOException {
		System.out.println("FeedbackController.addFeedback()");
		int seq = fbService.getSeq();
		ProductDto prod = prodService.getProductById(product);
		UserDto user = sessionManager.getLoginUser();
		FeedbackDto newFeedback = new FeedbackDto(seq, prod, rating, user, feedback);
		fbService.addFeedback(newFeedback);
		submit();
	}

	public void validateProduct(FacesContext context, UIComponent component, Object value) {
		if (value.toString().isEmpty()) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please choose a product", null));
		}
		int id = Integer.parseInt(value.toString());
		if (id == -1) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please choose a product", null));
		}
    }

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public List<NoteDto> getNotes() throws IOException {
		if (notes == null || notes.size() == 0) {
			notes = new ArrayList<NoteDto>();
			notes = noteService.getByReceiver(sessionManager.getLoginUser().getId());
		}
		return notes;
	}

	public void setNotes(List<NoteDto> notes) {
		this.notes = notes;
	}

	private int rating;
	private String feedback;
	private List<NoteDto> notes;

	public String logout() {
		conversationEnd();
		sessionManager.logoff();
		return "../portal.jsf?faces-redirect=true";
	}

	public List<SelectItem> getSelectOptions() {
		if (selectOptions == null) {
			selectOptions = new ArrayList<SelectItem>();
			selectOptions.add(new SelectItem(0, "Select"));
			selectOptions.add(new SelectItem(1, "Not acceptable"));
			selectOptions.add(new SelectItem(2, "Poor"));
			selectOptions.add(new SelectItem(3, "OK"));
			selectOptions.add(new SelectItem(4, "Good"));
			selectOptions.add(new SelectItem(5, "Very good"));
		}
		return selectOptions;
	}

	public void setSelectOptions(List<SelectItem> selectOptions) {
		this.selectOptions = selectOptions;
	}

	public FeedbackDto getFb() {
		if (fb == null) {
			fb = new FeedbackDto();
		}
		return fb;
	}

	public void setFb(FeedbackDto fb) {
		this.fb = fb;
	}

	public boolean isFeedbackMode() {
		return feedbackMode;
	}

	public void setFeedbackMode(boolean feedbackMode) {
		this.feedbackMode = feedbackMode;
	}

	private List<SelectItem> selectOptions;
	private FeedbackDto fb;
	private boolean feedbackMode;

	public void sendFeedback() throws IOException {
		System.out.println("FeedbackController.addFeedback()");
		int seq = fbService.getSeq();
		ProductDto prod = prodService.getProductById(product);
		UserDto user = sessionManager.getLoginUser();
		fb.setSeq(seq);
		fb.setProduct(prod);
		fb.setFeedback(feedback);
		fb.setCustomer(user);
		fbService.addFeedback(fb);
		submit();
		feedbackMode = false;
		fb = new FeedbackDto();
	}

	public void startFeedback() {
		reload();
		feedbackMode = true;
		System.out.println("FeedbackController.startFeedback()");
	}

	public void cancelFeedback() {
		feedbackMode = false;
		fb = new FeedbackDto();
	}
}

