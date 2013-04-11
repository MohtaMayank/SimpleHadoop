package cmu.cs.distsystems.hw3.mapred;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Record<K extends Comparable, V> implements Comparable{

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
        Record<String,String> r1 = new Record<String,String>("421","321");
        Record<String,String> r2 = new Record<String,String>("125","321");

        List<Record<String,String>> records = new ArrayList<Record<String, String>>();
        records.add(r1);
        records.add(r2);

        Collections.sort(records);

        for(Record r:records){
            System.out.println(r.getKey());
        }

    }

    @Override
    public int compareTo(Object o) {
        Record r = (Record) o;
        return key.compareTo(r.getKey());
    }

}
