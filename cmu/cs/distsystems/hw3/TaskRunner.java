package cmu.cs.distsystems.hw3;

/**
 * Will be run as a separate JVM. In this JVM, the map or the reduce task will
 * actually run. Will communicate with the respective TaskManager thread to get
 * new tasks.
 * Do we need heartbeat for monitoring if the process is alive??
 * @author mayank
 *
 */

public class TaskRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello World from Task Runner");
		System.exit(0);
	}
	
	
	/*	if(task instanceof MapTask) {
	try {
		MapTask mapTask = (MapTask)task;
		
		String jar = mapTask.getParentJob().getJar();
		String mapClassName = mapTask.getParentJob().getMapClass();
		
		String inputFile = mapTask.getMySplit().getFilePartition().getFileName();
		String start = mapTask.getMySplit().getFilePartition().getStart() + "";
		String end = mapTask.getMySplit().getFilePartition().getEnd() + "";
		String tmpDir = null;
		//String tmpDir = mapTask.getParentJob().getTmpDir();
	
		String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + 
        		File.separator + "java";

        ProcessBuilder builder = new ProcessBuilder(
                javaBin, "-cp", jar, TASK_RUNNER_CLASS, 
                mapClassName, inputFile, start, end, tmpDir);

        Process process = builder.start();
        
        
        
        process.waitFor();
        System.out.println("Exit Value: " + process.exitValue());
	} catch (Exception e) {
		//TODO: Handle this properly
		e.printStackTrace();
	}
	}*/

}
