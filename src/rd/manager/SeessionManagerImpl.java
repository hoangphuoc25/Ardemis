package rd.manager;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import rd.dto.UserDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.UserService;

@Stateful
@Named
@SessionScoped
public class SeessionManagerImpl implements SessionManager, Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -7594833796034520517L;

	@Inject
	private UserService userService;

	private UserDto user = null;

	@Override
	public UserDto getLoginUser() throws IOException {
		if(user == null){
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			user = userService.findUserById(externalContext.getUserPrincipal().getName());
		}
		return user;
	}

	@Override
	public boolean hasUserRole(String role){
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return externalContext.isUserInRole(role);
	}

	@Override
	public void logoff() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)externalContext.getSession(false);
		if(session != null){
			session.invalidate();
		}
	}

	@Override
	public void redirect(String url)  throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect(url);
	}

	@Override
	public void addGlobalMessageFatal(String summary, String detail){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, detail));
	}

	@Override
	public void addGlobalMessageWarn(String summary, String detail){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail));
	}

	@Override
	public void addGlobalMessageInfo(String summary, String detail){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
	}
}
