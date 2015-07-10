package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.CompanyDto;
import rd.dto.NoteDto;
import rd.dto.SaleExpenseDto;
import rd.dto.UserDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.CompanyService;
import rd.spec.service.NoteService;
import rd.spec.service.UserService;

@Named
@ConversationScoped
public class NoteController implements Serializable {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private UserDto newUser = new UserDto();
	private NoteDto newNote = new NoteDto();
	private List<NoteDto> notes = new ArrayList<NoteDto>();

	@Inject
	NoteService noteService;
	@Inject
	SessionManager sessionManager;
	@Inject
	UserService userService;
	@Inject
	private
	Conversation conversation;
	@Inject CompanyService compService;

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

	public NoteDto getNewNote() {
		if (newNote == null) {
			newNote = new NoteDto();
		}
		return newNote;
	}

	public void setNewNote(NoteDto newNote) {
		this.newNote = newNote;
	}

	public UserDto getNewUser() {
		if (newUser == null) {
			newUser = new UserDto();
		}
		return newUser;
	}

	public void setNewUser(UserDto newUser) {
		this.newUser = newUser;
	}

	public void validateNewUser(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String newId = value.toString();
		try {
			UserDto user = userService.findUserById(newId);
			System.out.println(user.getName());
			if (user != null) {
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, null, null));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addUserAndNote() throws IOException {
		userService.addUser(newUser);
		logger.error("new user added");

		newNote.setSeq(noteService.getSeq());
		newNote.setFromUser(sessionManager.getLoginUser());
		newNote.setToUser(newUser);
		newNote.setCreatedDate(new Date());
		noteService.addNote(newNote);

		notes.add(newNote);

		newUser = new UserDto();
		newNote = new NoteDto();

		addMode = false;

		sessionManager.addGlobalMessageInfo("USER CREATED", null);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("noteDialog_w.hide();");
	}

	public List<NoteDto> getNotes() {
		if (notes == null || notes.size() == 0) {
			try {
				notes = noteService.getBySender(sessionManager.getLoginUser().getId());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return notes;
	}

	public void setNotes(List<NoteDto> notes) {
		this.notes = notes;
	}

	public void addNote() throws IOException {
		UserDto toUser = userService.findUserById(newUser.getId());
		newNote.setSeq(noteService.getSeq());
		newNote.setFromUser(sessionManager.getLoginUser());
		newNote.setToUser(toUser);
		newNote.setCreatedDate(new Date());
		noteService.addNote(newNote);
		notes.add(newNote);
		logger.error("newnote added");

		newUser = new UserDto();
		newNote = new NoteDto();
		addMode = false;
	}

	public boolean isAddMode() {
		System.out.println("isAddMode: " + addMode);
		return addMode;
	}

	public void setAddMode(boolean addMode) {
		this.addMode = addMode;
	}

	private boolean addMode = false;;

	public void startAdd() {
		reload();
		System.out.println("NoteController.startAdd()");
		addMode = true;
		newUser = new UserDto();
	}

	public UserDto getNewUser2() {
		if (newUser2 == null)
			newUser2 = new UserDto();
		return newUser2;
	}

	public void setNewUser2(UserDto newUser2) {
		this.newUser2 = newUser2;
	}

	public NoteDto getNewNote2() {
		if (newNote2 == null)
			newNote2 = new NoteDto();
		return newNote2;
	}

	public void setNewNote2(NoteDto newNote2) {
		this.newNote2 = newNote2;
	}

	private NoteDto newNote2;
	private UserDto newUser2;

	public void addUserAndNote2() throws IOException {
		System.out.println("NoteController.addUserAndNote2()");
		if (newUser2.getId().isEmpty()) {
			logger.error(newUser2.getId());
			sessionManager.addGlobalMessageFatal("User ID is required", null);
			return;
		}

		userService.addUser(newUser2);
		logger.error("new user added");

		newNote2.setSeq(noteService.getSeq());
		newNote2.setFromUser(sessionManager.getLoginUser());
		newNote2.setToUser(newUser2);
		newNote2.setCreatedDate(new Date());
		noteService.addNote(newNote2);

		notes.add(newNote2);

		newUser2 = new UserDto();
		newNote2 = new NoteDto();

		addMode = false;
	}

    public void userValidator(FacesContext context, UIComponent component, Object value) {
    	String id = value.toString();
		logger.error("ID:" + id);
		if (id == null || id.isEmpty()) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "ABCDEFGH", null));
		}
		try {
			if (userService == null) {
				logger.error("USERSERVICE IS NULL");
			}
			UserDto user = userService.findUserById(id);
			if (user != null) {
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "XYZ", null));
			}
		} catch (IOException e) {
			logger.error("ERROR");
			e.printStackTrace();
		}
    }

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public void remove(NoteDto note) {
		reload();
		for (int i = notes.size() -1; i >= 0; i--) {
			if (notes.get(i).getSeq() == note.getSeq()) {
				notes.remove(i);
				break;
			}
		}
	}

	public boolean isExtMode() {
		return extMode;
	}

	public void setExtMode(boolean extMode) {
		this.extMode = extMode;
	}

	private boolean extMode;
	private String clientName;

	public List<String> suggestClient(String clientName) throws IOException {
		List<String> result = new ArrayList<String>();
		List<UserDto> users = userService.searchByName(clientName);
		for (UserDto user: users) {
			result.add(user.getName() + "(" + user.getId() + ")");
		}
		return result;
	}

	public void startAddExisting() {
		reload();
		extMode = true;
	}

	public void addNewNote() throws IOException {
		if (clientName == null || clientName.isEmpty()) {
			sessionManager.addGlobalMessageFatal("User name is required.", null);
			return;
		}
		if (newNote.getNote().isEmpty()) {
			sessionManager.addGlobalMessageFatal("Note content required.", null);
			return;
		}
		String id = getClientName().split("[()]")[1];
		UserDto receiver = userService.findUserById(id);
		newNote.setFromUser(sessionManager.getLoginUser());
		newNote.setToUser(receiver);
		newNote.setCreatedDate(new Date());
		noteService.addNote(newNote);

		extMode = false;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void onRowEdit(RowEditEvent event) throws IOException {
		NoteDto note = (NoteDto) event.getObject();
		if (note.getNote().isEmpty()) {
			sessionManager.addGlobalMessageFatal("Note content is required", null);
			restoreObj(event);
			return;
		}

		for (int i = notes.size() - 1; i >= 0; i--) {
    		if (notes.get(i).getSeq() == note.getSeq()) {
    			notes.set(i, note);
    			sessionManager.addGlobalMessageInfo("Info updated successully", null);
    			break;
    		}
    	}
	}

	public void onRowEditCancel(RowEditEvent event) throws IOException {
		restoreObj(event);
	}

	public void restoreObj(RowEditEvent event) throws IOException {
    	NoteDto obj = (NoteDto) event.getObject();
    	for (int i = notes.size() - 1; i >= 0; i--) {
    		if (notes.get(i).getSeq() == obj.getSeq()) {
    			notes.set(i, noteService.getById(obj.getSeq()));
    			System.out.println("restored");
    			break;
    		}
    	}
    }

	public void cancelNew() {
		addMode = false;
		conversationEnd();
	}

	public void cancelExisting() {
		extMode = false;
		conversationEnd();
	}

	public void validateNoteContent(FacesContext facesContext, UIComponent component, Object value) throws IOException {
		String content = value.toString();
		if (content.isEmpty()) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Note is required.", null));
		}
	}

	public void validateClientName(FacesContext facesContext, UIComponent component, Object value) throws IOException {
		String name = value.toString();
		if (name.isEmpty()) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name is required.", null));
		}
	}
}
