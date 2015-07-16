package rd.utils;

public class Pair<E, T> {
	private E first;
	private T second;

	public E getFirst() {
		return first;
	}

	public void setFirst(E first) {
		this.first = first;
	}

	public T getSecond() {
		return second;
	}

	public void setSecond(T second) {
		this.second = second;
	}

	public Pair(E first, T second) {
		this.first = first;
		this.second = second;
	}
}
