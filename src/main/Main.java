package main;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        ArrayList<LaneConfig> laneConfigs = Simulation.initLaneConfigurations();
        String fileName = "arrival.txt";

        //create a new simulation for each lane configuration, run it
        //save results to log file in /logs/
        for(LaneConfig config : laneConfigs){
            Simulation sim = new Simulation(config.getNumExpress(), config.getNumRegular(), fileName);
            sim.runSim();
        }
    }
}
