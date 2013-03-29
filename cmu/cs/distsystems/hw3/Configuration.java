package cmu.cs.distsystems.hw3;

public interface Configuration {
    public String getJobTrackerHost();
    public int getJobTrackerPort();
    public int getMapperNum();
    public int getReducerNum();
}
