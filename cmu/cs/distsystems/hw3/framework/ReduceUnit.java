package cmu.cs.distsystems.hw3.framework;

import java.util.List;

/**
*   Using K-way merge to merge kv pairs with same key in a ReduceUnit
**/

public class ReduceUnit{
    private String key;
    private List<String> values;

    public ReduceUnit(String key, List<String> values){
        this.key = key;
        this.values = values;
    }

    public String getKey() {
        return key;
    }

    public List<String> getValues() {
        return values;
    }
}
