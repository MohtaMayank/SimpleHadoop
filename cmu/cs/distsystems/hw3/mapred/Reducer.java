package cmu.cs.distsystems.hw3.mapred;

import cmu.cs.distsystems.hw3.io.AggregateTextRecordReader;
import cmu.cs.distsystems.hw3.io.Context;
import cmu.cs.distsystems.hw3.framework.ReduceTask;
import cmu.cs.distsystems.hw3.io.TextRecordWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

abstract public class Reducer {

    private ReduceTask reduceTask;
    private AggregateTextRecordReader reader;
    private Context context;

    public void init(ReduceTask reduceTask){
        int partitionNumber = reduceTask.getPartitionNumber();
        List<String> files = new ArrayList<String>();

        String mapTempDir = reduceTask.getParentJob().getTmpMapOpDir();
        File mapDir = new File(mapTempDir);

        for(File fileEntry:mapDir.listFiles()){
            String path = fileEntry.getAbsolutePath();
            String fileName = fileEntry.getName();
            
            //Do not include incomplete files
            if(fileName.contains(TextRecordWriter.INCOMPLETE_STRING)) {
            	continue;
            }
            
            String[] fields = fileName.split("\\.");
            if(fields.length >= 2 && fields[0].equals("part")){
                int filePartition = Integer.parseInt(fields[1]);
                if(filePartition == partitionNumber){
                    files.add(path);
                }
            }
        }

        reader = new AggregateTextRecordReader(files);
        context = new Context(reduceTask.getTaskId(), reduceTask.getParentJob().getOutputDir(),1);
    }

    public ReduceTask getReduceTask() {
        return reduceTask;
    }

    public AggregateTextRecordReader getReader() {
        return reader;
    }

    public Context getContext() {
        return context;
    }

    abstract public void reduce(String key, Iterable<String> values, Context context);
}
