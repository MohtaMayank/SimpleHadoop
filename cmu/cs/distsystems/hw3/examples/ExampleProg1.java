package cmu.cs.distsystems.hw3.examples;

import cmu.cs.distsystems.hw3.io.Context;
import cmu.cs.distsystems.hw3.framework.Job;
import cmu.cs.distsystems.hw3.framework.JobClient;
import cmu.cs.distsystems.hw3.mapred.Mapper;
import cmu.cs.distsystems.hw3.mapred.Reducer;

public class ExampleProg1 {

	public static void main(String[] args) {
		Job job = new Job();
		
		job.setMapClass("cmu.cs.distsystems.hw3.examples.Ex1Map");
		job.setCombinerClass("cmu.cs.distsystems.hw3.examples.Ex1Combiner");
		job.setReduceClass("cmu.cs.distsystems.hw3.examples.Ex1Reducer");
		
		//TODO: implement this ... currently not implemented
		job.setConfigFile("/home/mayank/DistributedSystems/HW3/cluster_config.txt");
		
		job.setJar(args[0]);
		job.setInputDir(args[1]);
		job.setOutputDir(args[2]);
		
		/*job.setJar("/Users/mimighostipad/Desktop/HW3/DistributedSystems.jar");
		job.setInputDir("/Users/mimighostipad/Desktop/HW3/input/");
		job.setOutputDir("/Users/mimighostipad/Desktop/HW3/output/");*/
		
		job.setNumReducers(2);
		
		JobClient.submitJobAndWaitForCompletion(job);
	}
}

class Ex1Map extends Mapper {

	private static final boolean DEBUG = true;

	@Override
	public void map(String key, String value, Context context) {
		String[] toks = value.split(" ");
		int count = 0;
		for(String s : toks) {
			context.write(s, "1");
			count++;
			//Artificial delay
			if(DEBUG && count % 100 == 0) {
				try {
					//50 ms delay for every 300 words
					Thread.sleep(50);
				} catch (Exception e) {
					
				}
			}
		}
	}
	
}

class Ex1Combiner {
	
}

class Ex1Reducer extends Reducer {

	private static final boolean DEBUG = true;
	private static int debugCount = 0;
	
    @Override
    public void reduce(String key, Iterable<String> values, Context context) {
        int num = 0;
        for(String value:values){
            num += Integer.parseInt(value);
        }
        if(DEBUG && debugCount == 50) {
        	try {
				//50 ms delay for every 50 reductions
				Thread.sleep(50);
			} catch (Exception e) {
				
			}
        }
        
        context.write(key,Integer.toString(num));
    }
}
