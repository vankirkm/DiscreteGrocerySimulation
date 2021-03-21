package main;

public class LaneConfig {
    private int numExpress;
    private int numRegular;

    public LaneConfig(int numExpress, int numRegular){
        this.numExpress = numExpress;
        this.numRegular = numRegular;
    }

    public int getNumExpress() {
        return numExpress;
    }

    public int getNumRegular() {
        return numRegular;
    }
}
