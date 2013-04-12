package cmu.cs.distsystems.hw3.examples;

import cmu.cs.distsystems.hw3.framework.Job;
import cmu.cs.distsystems.hw3.framework.JobClient;

public class ExampleProg1 {

	public static void main(String[] args) {
		Job job = new Job();
		
		job.setMapClass("cmu.cs.distsystems.hw3.examples.Ex1Map");
		job.setCombinerClass("cmu.cs.distsystems.hw3.examples.Ex1Combiner");
		job.setReduceClass("cmu.cs.distsystems.hw3.examples.Ex1Reducer");
		
		//TODO: implement this ... currently not implemented
		job.setConfigFile(args[0]);
		
		job.setJar(args[1]);
		job.setInputDir(args[2]);
		job.setOutputDir(args[3]);
		
		/*job.setJar("/Users/mimighostipad/Desktop/HW3/DistributedSystems.jar");
		job.setInputDir("/Users/mimighostipad/Desktop/HW3/input/");
		job.setOutputDir("/Users/mimighostipad/Desktop/HW3/output/");*/
		
		job.setNumReducers(2);
		
		JobClient.submitJobAndWaitForCompletion(job);
	}
}

class Ex1Combiner {
	
}
