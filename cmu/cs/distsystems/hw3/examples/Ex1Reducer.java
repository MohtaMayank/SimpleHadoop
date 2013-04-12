package cmu.cs.distsystems.hw3.examples;

import cmu.cs.distsystems.hw3.io.Context;
import cmu.cs.distsystems.hw3.mapred.Reducer;

public class Ex1Reducer extends Reducer {

	private static final boolean DEBUG = false;
	private static int debugCount = 0;
	
    @Override
    public void reduce(String key, Iterable<String> values, Context context) {
        int num = 0;
        for(String value:values){
            num += Integer.parseInt(value);
        }
        if(DEBUG && debugCount == 50) {
        	try {
				//50 ms delay for every 50 reductions
				Thread.sleep(50);
			} catch (Exception e) {
				
			}
        }
        
        context.write(key,Integer.toString(num));
    }
}