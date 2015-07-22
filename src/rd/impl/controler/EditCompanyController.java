package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.CompanyDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.CompanyService;

@Named
@ConversationScoped
public class EditCompanyController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject CompanyService compService;
	@Inject SessionManager sessionManager;

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

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public CompanyDto getCust() throws IOException {
		if (cust == null) {
			cust = compService.getById(seq);
			oldName = cust.getName();
		}
		return cust;
	}

	public void setCust(CompanyDto cust) {
		this.cust = cust;
	}

	private int seq;
	private CompanyDto cust;
	private String oldName;

	public void validateCompName(FacesContext facesContext, UIComponent component, Object value) throws IOException {
		String name = value.toString().trim();
		List<CompanyDto> comp = compService.searchCompanyByName(name);
		System.out.println(oldName);
		System.out.println(cust.getName());
		System.out.println(comp.size());
		if ((cust.getName().compareTo(oldName) != 0) && comp != null && comp.size() > 0) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "A company with this name already exists.", null));
		}
	}

	public void edit() throws IOException {
		List<CompanyDto> comp = compService.searchCompanyByName(cust.getName());
		if ((cust.getName().compareTo(oldName) != 0) && comp != null && comp.size() > 0) {
			sessionManager.addGlobalMessageFatal("Invalid name", null);
			return;
		}
		compService.updateCompany(cust);
		conversationEnd();

		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect("companyList.jsf");
	}

	public void cancel() throws IOException {
		conversationEnd();

		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect("companyList.jsf");
	}
}
