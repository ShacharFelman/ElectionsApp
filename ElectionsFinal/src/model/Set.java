package model;

import java.io.Serializable;
import java.util.Vector;

public class Set<T> implements Serializable {

	private Vector<T> setVector;

	public Set() {
		this.setVector = new Vector<T>();
	}

	public boolean add(T newElement) {
		if (!setVector.contains(newElement)) {
			setVector.add(newElement);
			return true;
		}
		return false;
	}

	public boolean contains(T theElement) {
		return setVector.contains(theElement);
	}

	public int size() {
		return setVector.size();
	}

	public T getElement(int index) {
		return setVector.get(index);
	}

	public Vector<T> getAll() {
		return setVector;
	}

	public T find(T theElement) {
		for (T element : setVector) {
			if (element.equals(theElement))
				return element;
		}
		return null;
	}
	
	public int indexOf(T theElement) {
		return setVector.indexOf(theElement);
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		for (T element : setVector) {
			str.append(element.toString() + "\n");
		}
		return str.toString();
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Set)) {
			return false;
		}
		Set<T> temp = (Set<T>)(obj);
		if (temp.size() != this.size()) {
			return false;
		}
		if (!temp.getAll().equals(this.setVector)) {
			return false;
		}
		return true;
	}
}
