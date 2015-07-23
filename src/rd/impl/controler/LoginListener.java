package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import rd.spec.manager.SessionManager;
import rd.spec.service.UserService;

public class LoginListener implements HttpSessionListener, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@Inject UserService userService;
	@Inject SessionManager sessionManager;

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		System.out.println("LoginListener.sessionCreated()");
		try {
			userService.updateLoginTime(sessionManager.getLoginUser().getId(), new Date());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("LoginListener.sessionDestroyed()");
		System.out.println("sessionDestroyed - deduct one session from counter");
	}
}
