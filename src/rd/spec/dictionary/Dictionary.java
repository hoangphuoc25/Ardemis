package rd.spec.dictionary;

public interface Dictionary {
	public String get(String key);
	public String put(String key, String value);
	public void clear();
}
