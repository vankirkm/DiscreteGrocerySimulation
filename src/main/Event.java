package main;

public class Event implements Comparable<Event>{
    private Customer customer;
    private double time;

    public Event(Customer customer, double time){
        this.customer = customer;
        this.time = time;
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

    public String toString(){
        return customer + " " + time;
    }
}
