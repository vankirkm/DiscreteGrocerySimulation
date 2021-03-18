package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Simulation {


    private static int numExpressLanes = 6;
    private static int numRegularLanes = 6;
    private static PriorityQueue<CheckoutLane> expressLaneQueue = initExpressLanes();
    private static PriorityQueue<CheckoutLane> regularLaneQueue = initRegularLanes();
    private static ArrayList<Customer> customerList = new ArrayList<>();

    public static void main(String[] args){
        String fileName = "arrival medium.txt";
        PriorityQueue<Event> eventQueue = createEventQueue(fileName);
        double simTime = 0;

        while(!eventQueue.isEmpty()){
            Event event = eventQueue.poll();
            if(event instanceof ArrivalEvent){
                simTime = event.getTime();
                System.out.println(simTime + " Arrival: Customer "  + event.getCustomer().getCustomerNumber());
                Event endShopping = new EndShoppingEvent(event.getCustomer(), event.getCustomer().getShoppingTime());
                eventQueue.offer(endShopping);
            }

            if(event instanceof EndShoppingEvent){
                simTime = event.getTime();
                System.out.println(simTime + " End Shopping: Customer "  + event.getCustomer().getCustomerNumber());
                CheckoutLane expressLane = expressLaneQueue.poll();
                CheckoutLane regularLane = regularLaneQueue.poll();

                if(chooseLane(event.getCustomer(), expressLane, regularLane)){
                    if(expressLane.getCustomers().size() == 1){
                        Event endCheckout = new EndCheckoutEvent(event.getCustomer(), expressLane.getCheckoutTime(), expressLane);
                        eventQueue.offer(endCheckout);
                    }
                }
                else{
                    if(regularLane.getCustomers().size() == 1){
                        Event endCheckout = new EndCheckoutEvent(event.getCustomer(), regularLane.getCheckoutTime(), regularLane);
                        eventQueue.offer(endCheckout);
                    }
                }
                expressLaneQueue.offer(expressLane);
                regularLaneQueue.offer(regularLane);
            }

            if(event instanceof EndCheckoutEvent){
                simTime = event.getTime();
                System.out.println(simTime + " End Checkout: Customer " + event.getCustomer().getCustomerNumber());
                double previousCustCheckoutTime = event.getLane().getCustomers().poll().getCheckoutTime();
                if(event.getLane() instanceof ExpressLane){
                    CheckoutLane expressLane = event.getLane();
                    if(expressLane.getCustomers().size() > 0){
                        Event endCheckout = new EndCheckoutEvent(expressLane.getCustomers().peek(), expressLane.getCheckoutTime(), expressLane);
                        eventQueue.offer(endCheckout);
                    }
                }
                else{
                    CheckoutLane regularLane = event.getLane();
                    if(regularLane.getCustomers().size() > 0){
                        Event endCheckout = new EndCheckoutEvent(regularLane.getCustomers().peek(), regularLane.getCheckoutTime(), regularLane);
                        eventQueue.offer(endCheckout);
                    }
                }
            }
        }
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

    public static PriorityQueue<CheckoutLane> initExpressLanes(){
        PriorityQueue<CheckoutLane> laneQueue = new PriorityQueue<>();
        for(int i = 0; i < numExpressLanes; i++){
            laneQueue.offer(new ExpressLane());
        }
        return laneQueue;
    }

    public static PriorityQueue<CheckoutLane> initRegularLanes(){
        PriorityQueue<CheckoutLane> laneQueue = new PriorityQueue<>();
        for(int i = 0; i < numRegularLanes; i++){
            laneQueue.offer(new RegularLane());
        }
        return laneQueue;
    }

    //Choose checkout lane for customer. if customer chooses express lane, return true.
    //If customer chooses regular lane, return false
    public static boolean chooseLane(Customer customer, CheckoutLane expressLane, CheckoutLane regularLane){
        if(customer.getNumItems() <= 12){
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
            regularLane.getCustomers().offer(customer);
            return false;
        }
    }
}
