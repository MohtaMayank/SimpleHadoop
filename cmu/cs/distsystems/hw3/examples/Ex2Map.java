package cmu.cs.distsystems.hw3.examples;

import cmu.cs.distsystems.hw3.io.Context;
import cmu.cs.distsystems.hw3.mapred.Mapper;

public class Ex2Map extends Mapper {
    @Override
    public void map(String key, String value, Context context) {
        if(value != null && value.length() > 0 && value.charAt(0) != '#'){
            String[] fields = value.split("\t");
            if(fields.length == 2){
                String inNode = fields[1];
                context.write(inNode,"1");
            }
        }
    }
}
