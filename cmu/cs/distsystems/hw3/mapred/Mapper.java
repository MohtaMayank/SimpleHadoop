package cmu.cs.distsystems.hw3.mapred;

import cmu.cs.distsystems.hw3.io.Context;
import cmu.cs.distsystems.hw3.framework.MapTask;
import cmu.cs.distsystems.hw3.io.TextRecordReader;

public abstract class Mapper{

    private MapTask mapTask;
    private TextRecordReader reader;
    private Context context;

    public void init(MapTask mapTask){
        this.mapTask = mapTask;
        this.reader = new TextRecordReader(
                mapTask.getMySplit().getFilePartition(), null);

        int taskId = mapTask.getTaskId();
        String outputDir = mapTask.getParentJob().getTmpMapOpDir();
        int reduceNum = mapTask.getParentJob().getNumReducers();
        this.context = new Context(taskId, outputDir, reduceNum);

    }

    public MapTask getMapTask() {
        return mapTask;
    }

    public TextRecordReader getReader() {
        return reader;
    }

    public Context getContext() {
        return context;
    }

    abstract public void map(String key, String value, Context context);
}