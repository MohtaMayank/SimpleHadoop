package cmu.cs.distsystems.hw3;

public class LocalConfig implements Configuration {
    @Override
    public String getJobTrackerHost() {
        return "localhost";
    }

    @Override
    public int getJobTrackerPort() {
        return 1234;
    }

    @Override
    public int getMapperNum() {
        return 10;
    }

    @Override
    public int getReducerNum() {
        return 2;
    }
}
