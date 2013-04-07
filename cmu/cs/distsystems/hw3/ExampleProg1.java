package cmu.cs.distsystems.hw3;

public class ExampleProg1 {

	public static void main(String[] args) {
		Job job = new Job();
		
		job.setMapClass("cmu.cs.distsystems.hw3.Ex1Map");
		job.setCombinerClass("cmu.cs.distsystems.hw3.Ex1Combiner");
		job.setReduceClass("cmu.cs.distsystems.hw3.Ex1Reducer");
		
		//TODO: implement this ... currently not implemented
		job.setConfigFile("");
		
		job.setJar("/Users/mimighostipad/Desktop/HW3/DistributedSystems.jar");
		job.setInputDir("/Users/mimighostipad/Desktop/HW3/input/");
		job.setOutputDir("/Users/mimighostipad/Desktop/HW3/output/");
		
		job.setNumReducers(2);
		
		JobClient.submitJobAndWaitForCompletion(job);
	}
}

class Ex1Map extends Mapper {

	@Override
	public void map(String key, String value, Context context) {
		String[] toks = value.split(" ");
		for(String s : toks) {
			context.write(s, "1");
		}
	}
	
}

class Ex1Combiner {
	
}

class Ex1Reducer {
	
}
