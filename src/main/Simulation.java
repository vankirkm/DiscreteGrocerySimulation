package main;

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Simulation {
    private int numExpressLanes;
    private int numRegularLanes;
    private String fileName;
    private PriorityQueue<CheckoutLane> expressLaneQueue;
    private PriorityQueue<CheckoutLane> regularLaneQueue;
    private static ArrayList<Customer> customerList = new ArrayList<>();
    private static FileWriter fw;

    public Simulation(int numExpressLanes, int numRegularLanes, String fileName){
        this.fileName = fileName;
        this.numExpressLanes = numExpressLanes;
        this.numRegularLanes = numRegularLanes;
        this.expressLaneQueue = initExpressLanes();
        this.regularLaneQueue = initRegularLanes();
    }

    //while the eventqueue is not empty, continue to process events
    public void runSim(){
        String logPath = "logs/";
        String logFileName = numExpressLanes + "express" + numRegularLanes + "regular.log";
        File logFile = new File(logPath + logFileName);
        try{
            fw = new FileWriter(logFile);
        }catch(IOException e){
            System.out.println("File does not exist");
        }
        PriorityQueue<Event> eventQueue = createEventQueue(fileName);
        double simTime = 0;
        double waitTime = 0;
        double previousCustCheckoutTime = 0;
        while(!eventQueue.isEmpty()){
            Event event = eventQueue.poll();
            if(event instanceof ArrivalEvent){
                simTime = event.getTime();
                System.out.println(simTime + " Arrival: Customer "  + event.getCustomer().getCustomerNumber());
                try{
                    fw.write(simTime + " Arrival: Customer "  + event.getCustomer().getCustomerNumber() + "\n");
                }catch(IOException e){
                    System.out.println("Error writing to log file");
                }
                Event endShopping = new EndShoppingEvent(event.getCustomer(), event.getCustomer().getShoppingTime());
                eventQueue.offer(endShopping);
            }

            if(event instanceof EndShoppingEvent){
                simTime = event.getTime();
                try{
                    fw.write(simTime + " End Shopping: Customer "  + event.getCustomer().getCustomerNumber() + "\n");
                }catch(IOException e){
                    System.out.println("Error writing to log file");
                }
                System.out.println(simTime + " End Shopping: Customer "  + event.getCustomer().getCustomerNumber());
                CheckoutLane expressLane = expressLaneQueue.poll();
                CheckoutLane regularLane = regularLaneQueue.poll();

                if(chooseLane(event.getCustomer(), expressLane, regularLane)){
                    if(expressLane.getCustomers().size() == 1){
                        Event endCheckout = new EndCheckoutEvent(event.getCustomer(), expressLane.getCheckoutTime(), expressLane);
                        eventQueue.offer(endCheckout);
                        expressLane.getCustomers().peek().setWaitTime(0,0);
                    }
                }
                else{
                    if(regularLane.getCustomers().size() == 1){
                        Event endCheckout = new EndCheckoutEvent(event.getCustomer(), regularLane.getCheckoutTime(), regularLane);
                        eventQueue.offer(endCheckout);
                        regularLane.getCustomers().peek().setWaitTime(0,0);
                    }
                }
                if(expressLane != null){
                    expressLaneQueue.offer(expressLane);
                }
                regularLaneQueue.offer(regularLane);
            }

            if(event instanceof EndCheckoutEvent){
                simTime = event.getTime();
                try{
                    fw.write(simTime + " End Checkout: Customer " + event.getCustomer().getCustomerNumber() + "\n");
                }catch(IOException e){
                    System.out.println("Error writing to log file");
                }
                System.out.println(simTime + " End Checkout: Customer " + event.getCustomer().getCustomerNumber());
                previousCustCheckoutTime = event.getLane().getCustomers().poll().getCheckoutTime();
                if(event.getLane() instanceof ExpressLane){
                    CheckoutLane expressLane = event.getLane();
                    if(expressLane.getCustomers().size() > 0){
                        Event endCheckout = new EndCheckoutEvent(expressLane.getCustomers().peek(), expressLane.getCheckoutTime(), expressLane);
                        eventQueue.offer(endCheckout);
                        endCheckout.getCustomer().setWaitTime(previousCustCheckoutTime, endCheckout.getCustomer().getCheckoutTime());
                    }
                }
                else{
                    CheckoutLane regularLane = event.getLane();
                    if(regularLane.getCustomers().size() > 0){
                        Event endCheckout = new EndCheckoutEvent(regularLane.getCustomers().peek(), regularLane.getCheckoutTime(), regularLane);
                        eventQueue.offer(endCheckout);
                        endCheckout.getCustomer().setWaitTime(previousCustCheckoutTime, endCheckout.getCustomer().getCheckoutTime());
                    }
                }
            }
        }

        //after the eventqueue ahs been emptied, calculate the average wait time
        //for all customers in the customer list
        for(Customer customer : customerList){
            waitTime += customer.getWaitTime();
        }
        try{
            fw.write("Average wait time: " + waitTime / customerList.size() + "\n");
        }catch(IOException e){
            System.out.println("Error writing to log file");
        }
        try{
            fw.close();
        }catch(IOException e){
            System.out.println("Error closing filewriter");
        }
        System.out.println("Average wait time: " + waitTime / customerList.size());
    }

    //Create initial eventQueue of customer arrivals and add customers to customerList
    public static PriorityQueue<Event> createEventQueue(String filename){
        PriorityQueue<Event> eventQueue = new PriorityQueue<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader("data/" + filename));
            String line;
            boolean keepGoing = true;
            int customerCount = 0;
            while(keepGoing){
                try{
                    line = br.readLine();
                    String[] fields = line.split("\\s+");
                    Customer newCust = new Customer(Double.parseDouble(fields[0]), Integer.parseInt(fields[1]), Double.parseDouble(fields[2]), customerCount);
                    customerList.add(newCust);
                    eventQueue.offer(new ArrivalEvent(newCust, newCust.getArrivalTime()));
                }catch(IOException e){
                    keepGoing = false;
                }catch(NullPointerException e1){
                    keepGoing = false;
                }
                customerCount++;
            }
        }catch(FileNotFoundException e){
            System.out.println("File does not exist. Please check path and try again.");
        }
        return eventQueue;
    }

    //initialize priorityqueue of express lanes
    public PriorityQueue<CheckoutLane> initExpressLanes(){
        PriorityQueue<CheckoutLane> laneQueue = new PriorityQueue<>();
        for(int i = 0; i < numExpressLanes; i++){
            laneQueue.offer(new ExpressLane());
        }
        return laneQueue;
    }

    //initialize priorityqueue of regular lanes
    public PriorityQueue<CheckoutLane> initRegularLanes(){
        PriorityQueue<CheckoutLane> laneQueue = new PriorityQueue<>();
        for(int i = 0; i < numRegularLanes; i++){
            laneQueue.offer(new RegularLane());
        }
        return laneQueue;
    }

    //Choose checkout lane for customer. if customer chooses express lane, return true.
    //If customer chooses regular lane, return false
    public static boolean chooseLane(Customer customer, CheckoutLane expressLane, CheckoutLane regularLane){
        if(customer.getNumItems() <= 12 && expressLane != null){
            if(expressLane.getCustomers().size() <= regularLane.getCustomers().size()){
                expressLane.getCustomers().offer(customer);
                return true;
            }
            else{
                regularLane.getCustomers().offer(customer);
                return false;
            }
        }
        else{
            if(regularLane != null){
                regularLane.getCustomers().offer(customer);
            }

            return false;
        }
    }

    //create arraylist of all possible lane configurations that
    //add up to 12 given that express, regular, and closed are all viable options
    public static ArrayList<LaneConfig> initLaneConfigurations(){
        ArrayList<LaneConfig> laneConfig = new ArrayList<>();
        for(int i = 12; i >= 0; i--){
            for(int j = 0; j < i; j++){
                int express = j;
                int regular = i - j;
                LaneConfig config = new LaneConfig(express, regular);
                laneConfig.add(config);
            }
        }
        return laneConfig;
    }

}
