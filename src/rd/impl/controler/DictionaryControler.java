package rd.impl.controler;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import rd.spec.dictionary.Dictionary;

@Named
@RequestScoped
public class DictionaryControler implements Serializable {


	private static final long serialVersionUID = -1903186271970239627L;

	@Inject
	private Dictionary dictionary;

	public DictionaryControler(){

	}

	public Map<String, String> getDictionary(){
		return (Map<String, String>)dictionary;
	}

	public void reset() {
		dictionary.clear();
	}

}
