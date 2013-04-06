package cmu.cs.distsystems.hw3;

public class ExampleProg1 {

	public static void main(String[] args) {
		Job job = new Job();
		
		job.setMapClass("Ex1Map");
		job.setCombinerClass("Ex1Combiner");
		job.setReduceClass("Ex1Reducer");
		
		//TODO: implement this ... currently not implemented
		job.setConfigFile("");
		
		job.setJar("");
		job.setInputDir("/home/mayank/DistributedSystems/HW3/input/");
		job.setOutputDir("/home/mayank/DistributedSystems/HW3/output/");
		
		job.setNumReducers(2);
		
		JobClient.submitJobAndWaitForCompletion(job);
	}
}

class Ex1Map {
	
}

class Ex1Combiner {
	
}

class Ex1Reducer {
	
}
