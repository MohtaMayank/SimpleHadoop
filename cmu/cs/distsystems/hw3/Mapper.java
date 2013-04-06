package cmu.cs.distsystems.hw3;

public abstract class Mapper{

	MapTask mapTask;
    TextRecordReader reader;
    TextRecordWriter writer;

    public void init(MapTask mapTask){
        this.mapTask = mapTask;
        this.reader = new TextRecordReader(
                mapTask.getMySplit().getFilePartition(), null);

        this.writer = new TextRecordWriter(mapTask.getParentJob().getTmpMapOpDir(), "\t");

    }

    abstract public void map(String key, String value, TextRecordWriter writer);
}
