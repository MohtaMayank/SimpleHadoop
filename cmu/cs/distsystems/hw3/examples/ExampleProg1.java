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
		job.setConfigFile("/home/mayank/workspace/" +
				"DistributedSystems/src/cmu/cs/distsystems/hw3/cluster_config.txt");
		
		job.setJar(args[0]);
		System.out.println(args[0]);
		job.setInputDir(args[1]);
		System.out.println(args[1]);
		job.setOutputDir(args[2]);
		System.out.println(args[2]);
		
		/*job.setJar("/Users/mimighostipad/Desktop/HW3/DistributedSystems.jar");
		job.setInputDir("/Users/mimighostipad/Desktop/HW3/input/");
		job.setOutputDir("/Users/mimighostipad/Desktop/HW3/output/");*/
		
		job.setNumReducers(2);
		
		JobClient.submitJobAndWaitForCompletion(job);
	}
}

class Ex1Combiner {
	
}
