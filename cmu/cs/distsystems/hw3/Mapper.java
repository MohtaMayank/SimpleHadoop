package cmu.cs.distsystems.hw3;

public abstract class Mapper{

	MapTask mapTask;
    TextRecordReader reader;
    Context context;

    public void init(MapTask mapTask){
        this.mapTask = mapTask;
        this.reader = new TextRecordReader(
                mapTask.getMySplit().getFilePartition(), null);

        int taskId = mapTask.getTaskId();
        String outputDir = mapTask.getParentJob().getTmpMapOpDir();
        int reduceNum = mapTask.getParentJob().getNumReducers();
        this.context = new Context(taskId, outputDir, reduceNum);

    }

    abstract public void map(String key, String value, Context context);
}
