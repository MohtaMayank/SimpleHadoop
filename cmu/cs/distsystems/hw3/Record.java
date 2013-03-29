package cmu.cs.distsystems.hw3;

import java.util.List;

public class Record<K, V> {

	K key;
	V value;
	
	public Record(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
