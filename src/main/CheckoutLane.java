package main;

import java.util.LinkedList;
import java.util.Queue;

abstract class CheckoutLane implements Comparable<CheckoutLane>{

    private Queue<Customer> customers;

    public CheckoutLane(){
        this.customers = new LinkedList<>();
    }

    public Queue<Customer> getCustomers() {
        return customers;
    }

    abstract double getCheckoutTime();

    public int compareTo(CheckoutLane o) {
        if(this.customers.size() < o.customers.size()){
            return -1;
        }
        if(this.customers.size() > o.customers.size()){
            return 1;
        }
        if(this.customers.size() == o.customers.size()){
            if(this instanceof ExpressLane){
                return -1;
            }
            if(o instanceof ExpressLane){
                return 1;
            }
        }
        return 0;
    }
}
