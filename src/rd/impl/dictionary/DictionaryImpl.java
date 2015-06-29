package rd.impl.dictionary;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import rd.spec.dictionary.Dictionary;
import rd.spec.manager.SessionManager;

@Named
@SessionScoped
public class DictionaryImpl implements Dictionary, Map<String, String>, Serializable {

	private static final long serialVersionUID = 842658217049435641L;

	private Map<String, String> innerMap;

	public DictionaryImpl(){

	}
	@Inject
	public DictionaryImpl(SessionManager sessionManager){
		innerMap = new HashMap<String, String>();
		//These codes are sample.
		innerMap.put("userIdRequired", "Id is required.");
		innerMap.put("firstNameRequired", "First name is required.");
		innerMap.put("lastNameRequired", "Last name is required.");
		innerMap.put("invalidEmail", "Invalid E-mail.");
		innerMap.put("primaryOfficeRequired", "Primary office is required.");
		innerMap.put("PasswordConfirmNotEqual", "Password is not equal Password confirm.");
	}

	@Override
	public String get(Object key) {
		return get((String)key);
	}
	@Override
	public int size() {
		return innerMap.size();
	}
	@Override
	public boolean isEmpty() {
		return innerMap.isEmpty();
	}
	@Override
	public boolean containsKey(Object key) {
		return innerMap.containsKey(key);
	}
	@Override
	public boolean containsValue(Object value) {
		return innerMap.containsValue(value);
	}
	@Override
	public String remove(Object key) {
		return innerMap.remove(key);
	}
	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		innerMap.putAll(m);
	}
	@Override
	public Set<String> keySet() {
		return innerMap.keySet();
	}
	@Override
	public Collection<String> values() {
		return innerMap.values();
	}
	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return innerMap.entrySet();
	}
	@Override
	public String get(String key) {
		//TODO You need to implement a program(only R&D4) to get the value of a dictionary based on the information session.

		String value = innerMap.get(key);
		if(value == null){
			value = key;
		}
		return value;
	}
	@Override
	public String put(String key, String value) {
		return innerMap.put(key, value);
	}
	@Override
	public void clear() {
		innerMap.clear();
	}

}
