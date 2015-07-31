package rd.impl.controler;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ConversationScoped
public class SearchController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;

	public void reload() {
		conversationBegin();
	}

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

}
