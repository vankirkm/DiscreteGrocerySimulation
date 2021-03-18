package main;

public class Event implements Comparable<Event>{
    private Customer customer;
    private double time;
    private CheckoutLane lane;


    public Event(Customer customer, double time){
        this.customer = customer;
        this.time = time;
    }

    public Event(Customer customer, double time, CheckoutLane lane){
        this.customer = customer;
        this.time = time;
        this.lane = lane;
    }

    @Override
    public int compareTo(Event o) {
        if(this.time < o.time){
            return -1;
        }
        if(this.time > o.time){
            return 1;
        }
        return 0;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTime(){
        return time;
    }

    public String toString(){
        return customer + " " + time;
    }

    public CheckoutLane getLane() {
        return lane;
    }
}
