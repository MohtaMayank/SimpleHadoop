package cmu.cs.distsystems.hw3.framework;

import cmu.cs.distsystems.hw3.mapred.Record;
import cmu.cs.distsystems.hw3.io.TextRecordReader;
import cmu.cs.distsystems.hw3.mapred.Mapper;

import java.io.*;

class MapWorker implements Runnable {
	private Task task;
	private FileWriter logsWriter;

    public MapWorker(Task task) {
    	this.task = task;
    	try {
    	this.logsWriter = new FileWriter(
    			new File(task.getParentJob().getTmpMapOpDir() + "logs.txt"));
        this.logsWriter.write("I am all set!" + "\n");
        logsWriter.flush();

        } catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @Override
    public void run() {

        this.task.setState(TaskState.RUNNING);
        MapTask mapTask = (MapTask) this.task;
        String jar = mapTask.getParentJob().getJar();

        String mapClassName = mapTask.getParentJob().getMapClass();

        Class<?> mapClazz;
        try {
        	logsWriter.write("HERE!\n");
            logsWriter.flush();

            mapClazz = TaskRunner.loadClass(jar, mapClassName);

            logsWriter.write("Load!!!!\n" + mapClazz.getName()+"\n");
            logsWriter.flush();

            Mapper mapper = (Mapper) mapClazz.newInstance();

            mapper.init(mapTask);

            logsWriter.write("INIT!\n");
            logsWriter.flush();

            TextRecordReader reader = mapper.getReader();
            Record<String,String> record = reader.readNextRecord();

            logsWriter.write("INIT!\n" + mapper.getContext().reducerNum + "\n");
            logsWriter.flush();


            while(record != null){
                mapper.map(record.getKey(), record.getValue(), mapper.getContext());
                record = reader.readNextRecord();
            }

            mapper.getContext().flush(true);

            this.task.setPercentComplete(100);
            this.task.setState(TaskState.SUCCESS);

        } catch (Exception e) {
        	try {
                this.task.setState(TaskState.FAILED);
        		logsWriter.write("ERROR!! \n");
                e.printStackTrace(new PrintStream(
                        new FileOutputStream(new File(task.getParentJob().getTmpMapOpDir() + "err.txt"))));
                logsWriter.flush();
                //logsWriter.write(e.getCause().getMessage() + "\n");
        	} catch (IOException ioe) {

        	}
        	System.exit(2);
        }

    }

}
