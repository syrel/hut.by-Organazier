package by.hut.flat.calendar.utils;

import java.util.ArrayList;

public class AssociativeList<E> {
	private ArrayList<String> strKeys;
	private ArrayList<Integer> intKeys;
	private ArrayList<E> values;
	private int size = 0;
	
	public boolean invariant(){
		return strKeys != null
				&& intKeys != null
				&& values != null
				&& strKeys.size() == size
				&& intKeys.size() == size
				&& values.size() == size;
	}
	
	public AssociativeList(){
		strKeys = new ArrayList<String>();
		intKeys = new ArrayList<Integer>();
		values = new ArrayList<E>();
		assert invariant();
	}
	public void add(E obj, String key){
		assert obj != null;
		assert key != null;
		assert key.length() > 0;
		assert (strKeys.contains(key) == false);
		strKeys.add(key);
		intKeys.add(null);
		values.add(obj);
		size++;
		assert invariant();
	}
	public void add(E obj, int key){
		assert obj != null;
		assert (strKeys.contains(key) == false);
		strKeys.add(null);
		intKeys.add(key);
		values.add(obj);
		size++;
		assert invariant();
	}
	public E get(String key){
		assert invariant();
		if (!strKeys.contains(key)) return null;
		return values.get(strKeys.indexOf(key));
	}
	public E get(int key){
		assert invariant();
		if (!intKeys.contains(key)) return null;
		return values.get(intKeys.indexOf(key));
	}
	
	public E getAt(int index){
		assert invariant();
		assert index >= 0;
		assert index < size;
		return values.get(index);
	}
	
	public int getSize(){
		assert invariant();
		return size;
	}
	
	public boolean containsKey(int key){
		assert invariant();
		return intKeys.contains(key);
	}
	
	public boolean containsKey(String key){
		assert invariant();
		assert key != null;
		assert key.length() > 0;
		return intKeys.contains(key);
	}
	
	public String toString(){
		String str = "[";
		for (int i = 0 ; i < size; i++){
			str += "'"+((strKeys.get(i) != null) ? strKeys.get(i) : intKeys.get(i))+"':'"+values.get(i)+((i != size-1) ? "', ": "'");
		}
		str += "]";
		return str;
	}
}
