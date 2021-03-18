package main;

public class EndCheckoutEvent extends Event{

    public EndCheckoutEvent(Customer customer, double time){
        super(customer, time);
    }

}
