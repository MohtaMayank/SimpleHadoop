package cmu.cs.distsystems.hw3.examples;

import cmu.cs.distsystems.hw3.framework.Job;
import cmu.cs.distsystems.hw3.framework.JobClient;

public class ExampleProg2 {
    public static void main(String[] args){
        Job job = new Job();

        job.setMapClass("cmu.cs.distsystems.hw3.examples.Ex2Map");
        job.setReduceClass("cmu.cs.distsystems.hw3.examples.Ex2Reducer");

        job.setConfigFile(args[0]);

        job.setJar(args[1]);
        job.setInputDir(args[2]);
        job.setOutputDir(args[3]);

        job.setNumReducers(2);

        JobClient.submitJobAndWaitForCompletion(job);

    }
}
