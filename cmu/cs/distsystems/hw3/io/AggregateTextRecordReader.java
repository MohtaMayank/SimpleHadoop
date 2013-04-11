package cmu.cs.distsystems.hw3.io;

import cmu.cs.distsystems.hw3.framework.ReduceUnit;
import cmu.cs.distsystems.hw3.mapred.Record;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

class RecordReaderWrapper implements Comparable<RecordReaderWrapper>{
    Record<String,String> record;
    BufferedReader reader;

    public RecordReaderWrapper(Record<String, String> record, BufferedReader reader){
        this.record = record;
        this.reader = reader;
    }

    @Override
    public int compareTo(RecordReaderWrapper recordReaderWrapper) {
        return this.record.compareTo(recordReaderWrapper.record);
    }
}

public class AggregateTextRecordReader implements Iterator<ReduceUnit> {

    private List<String> files;
    private boolean isInit = false;
    private List<BufferedReader> readers;
    private String fileDelimter = "\t";
    private PriorityQueue<RecordReaderWrapper> priorityQueue;


    public AggregateTextRecordReader(List<String> files){
        this.files = files;
        this.priorityQueue = new PriorityQueue<RecordReaderWrapper>();
    }

    private RecordReaderWrapper moveNext(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if(line == null) return null;
        else{
            String[] splits = line.split(fileDelimter);
            Record<String,String> record = new Record<String, String>(splits[0],splits[1]);
            return new RecordReaderWrapper(record,reader);
        }
    }

    private void init(List<String> files) throws IOException {
        readers = new ArrayList<BufferedReader>();
        for(String file:files){
            try {
                readers.add(new BufferedReader(new FileReader(new File(file))));
            } catch (FileNotFoundException e) {
                System.out.println("Temp file doesn't exist!");
            }
        }

        for(BufferedReader br:readers){
            RecordReaderWrapper rrw = moveNext(br);
            priorityQueue.add(rrw);
        }
    }


    @Override
    public boolean hasNext() {
        if(!isInit){
            try {
                init(this.files);
                isInit = true;
            } catch (IOException e) {
                System.out.println("IO error! Illigal files!");
                return false;
            }
        }

        return !priorityQueue.isEmpty();
    }

    @Override
    public ReduceUnit next() {

        List<String> values = new ArrayList<String>();

        RecordReaderWrapper rrw = priorityQueue.poll();
        String key = rrw.record.getKey();
        values.add(rrw.record.getValue());

        //System.out.println("next Key:" + key);

        while(true){

            try {
                RecordReaderWrapper rrw2 = moveNext(rrw.reader);
                if(rrw2 != null){
                    priorityQueue.add(rrw2);
                }
            } catch (IOException e) {
                System.out.println("Error occur when adding item!");
            }

            RecordReaderWrapper peek = priorityQueue.peek();
            if(peek == null) break;

            String nextKey = peek.record.getKey();
            if(!nextKey.equals(key)) break;
            else{
                rrw = priorityQueue.poll();
                values.add(rrw.record.getValue());
            }
        }

        return new ReduceUnit(key,values);
    }

    @Override
    public void remove() {}


    public static void main(String[] args){
        List<String> files = new ArrayList<String>();
        files.add("/Users/mimighostipad/Desktop/HW3/part1");
        files.add("/Users/mimighostipad/Desktop/HW3/part2");
        files.add("/Users/mimighostipad/Desktop/HW3/part3");
        AggregateTextRecordReader atrr = new AggregateTextRecordReader(files);


        File f = new File("/Users/mimighostipad/Desktop/HW3/part2");
        System.out.println("FileName:" + f.getName());

        int numPair = 0;

        while(atrr.hasNext()){
            ReduceUnit ru = atrr.next();
            System.out.println("Key:" + ru.getKey());
            for(String value:ru.getValues()){
                System.out.println("Value:" + value);
                numPair++;
            }
            System.out.println("#####");
        }

        System.out.println("pairNum:" + numPair);
    }

}
