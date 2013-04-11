package cmu.cs.distsystems.hw3.framework;

import cmu.cs.distsystems.hw3.io.AggregateTextRecordReader;
import cmu.cs.distsystems.hw3.mapred.Reducer;

import java.io.*;

class ReduceWorker implements Runnable {

    private Task task;
    private FileWriter logsWriter;

    public ReduceWorker(Task task) {
        this.task = task;
        try {
            this.logsWriter = new FileWriter(
                    new File(task.getParentJob().getOutputDir() + "logs.txt"));
            this.logsWriter.write("I am all set!" + "\n");
            logsWriter.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        this.task.setState(TaskState.RUNNING);
        ReduceTask reduceTask = (ReduceTask)task;
        String jar = reduceTask.getParentJob().getJar();
        String reduceClassName = reduceTask.getParentJob().getReduceClass();

        Class<?> reduceClazz;


        try {
            logsWriter.write("HERE!\n");
            logsWriter.flush();

            reduceClazz = TaskRunner.loadClass(jar, reduceClassName);

            logsWriter.write("Load!!!!\n" + reduceClazz.getName()+"\n");
            logsWriter.flush();

            Reducer reducer = (Reducer) reduceClazz.newInstance();

            reducer.init(reduceTask);

            logsWriter.write("INIT!\n");
            logsWriter.flush();

            AggregateTextRecordReader reader = reducer.getReader();

            while(reader.hasNext()){
                ReduceUnit ru = reader.next();
                //logsWriter.write(ru.key + " " + ru.values.size() + "\n");
                reducer.reduce(ru.getKey(), ru.getValues(), reducer.getContext());
            }

            logsWriter.write("Flush!!\n");
            reducer.getContext().flush(false);

            this.task.setPercentComplete(100);
            this.task.setState(TaskState.SUCCESS);

        } catch (Exception e) {
            try {
                this.task.setState(TaskState.FAILED);
                logsWriter.write("ERROR!! Task Id " + this.task.getTaskId() + " failed\n");
                e.printStackTrace(new PrintStream(
                        new FileOutputStream(new File(task.getParentJob().getOutputDir() + "err.txt"))));
                logsWriter.flush();
                //logsWriter.write(e.getCause().getMessage() + "\n");
            } catch (IOException ioe) {

            }
            System.exit(2);
        }


    }
}
