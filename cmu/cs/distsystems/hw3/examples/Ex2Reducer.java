package cmu.cs.distsystems.hw3.examples;

import cmu.cs.distsystems.hw3.io.Context;
import cmu.cs.distsystems.hw3.mapred.Reducer;

public class Ex2Reducer extends Reducer {
    @Override
    public void reduce(String key, Iterable<String> values, Context context) {
        int inDegree = 0;
        for(String value:values){
            inDegree += Integer.parseInt(value);
        }
        context.write(key,Integer.toString(inDegree));
    }
}
