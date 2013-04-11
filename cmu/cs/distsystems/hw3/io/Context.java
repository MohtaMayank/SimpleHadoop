package cmu.cs.distsystems.hw3.io;

import cmu.cs.distsystems.hw3.mapred.Record;

import java.io.IOException;
import java.util.*;

public class Context {

    private int taskId;
    private String outputDir;
    public int reducerNum;
    private Map<Integer, List<Record<String,String>>> buffer;
    private Map<Integer, TextRecordWriter> partitions;


    private String getPartitionFilename(String dir, int taskId, int paritionId){
        return dir + "part." + paritionId + "." + taskId + ".tmp.txt";
    }

    public Context(int taskId, String outputDir, int reducerNum){
        this.taskId = taskId;

        if(outputDir.charAt(outputDir.length()-1) != '/'){
            this.outputDir = outputDir + "/";
        } else {
            this.outputDir = outputDir;
        }

        this.reducerNum = reducerNum;
        this.buffer = new HashMap<Integer, List<Record<String, String>>>();
        this.partitions = new HashMap<Integer, TextRecordWriter>();

        for(int i = 0; i < this.reducerNum; i++){
            this.buffer.put(i, new ArrayList<Record<String, String>>());
            this.partitions.put(i,new TextRecordWriter(
                    getPartitionFilename(this.outputDir,this.taskId,i),"\t"));
        }
    }

    public void write(Record<String,String> record){
        String key = record.getKey();
        int hash = Math.abs(key.hashCode()) % this.reducerNum;

        List<Record<String, String>> records = this.buffer.get(hash);
        records.add(record);
    }

    public void write(String key, String value){
        this.write(new Record<String, String>(key, value));
    }

    public void flush(boolean sort) throws IOException {
        for(int partitionId:buffer.keySet()){
            List<Record<String,String>>  records = buffer.get(partitionId);
            TextRecordWriter writer = partitions.get(partitionId);
            if(sort){
                Collections.sort(records);
            }

            for(Record<String, String> record:records)
            {
                writer.writeRecord(record);
            }
            writer.close();
        }
    }

    public static void main(String[] args){
        Context context = new Context(0,"output",2);
        Record<String,String> record = new Record<String, String>("1","2");
        context.write("dfadfasdf","dsfasdfasdfa");
    }

}
