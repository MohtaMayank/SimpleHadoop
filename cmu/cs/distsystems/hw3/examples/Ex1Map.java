package cmu.cs.distsystems.hw3.examples;

import cmu.cs.distsystems.hw3.io.Context;
import cmu.cs.distsystems.hw3.mapred.Mapper;

public class Ex1Map extends Mapper {

	private static final boolean DEBUG = false;

	@Override
	public void map(String key, String value, Context context) {
		String[] toks = value.split(" ");
		int count = 0;
		for(String s : toks) {
			context.write(s, "1");
			count++;
			//Artificial delay
			if(DEBUG && count % 100 == 0) {
				try {
					//50 ms delay for every 300 words
					Thread.sleep(50);
				} catch (Exception e) {
					
				}
			}
		}
	}
	
}