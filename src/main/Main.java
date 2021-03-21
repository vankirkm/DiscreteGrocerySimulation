package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        ArrayList<LaneConfig> laneConfigs = Simulation.initLaneConfigurations();
        String fileName = "arrival medium.txt";
        for(LaneConfig config : laneConfigs){
            Simulation sim = new Simulation(config.getNumExpress(), config.getNumRegular(), fileName);
            sim.runSim();
        }
    }
}
