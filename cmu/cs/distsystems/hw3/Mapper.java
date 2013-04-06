package cmu.cs.distsystems.hw3;

public abstract class Mapper {

	MapTask mapTask;
	
	public Mapper(MapTask task) {
		this.mapTask = mapTask;
		
		TextRecordReader reader = new TextRecordReader(
				mapTask.getMySplit().getFilePartition(), null);
		
		TextRecordWriter writer = new TextRecordWriter(mapTask.getParentJob().getTmpMapOpDir(), "\t");
		
	}
	
	public void run() throws Exception {
		
	}
}
