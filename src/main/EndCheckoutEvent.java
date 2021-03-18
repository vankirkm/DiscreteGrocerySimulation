package main;

public class EndCheckoutEvent extends Event{
    public EndCheckoutEvent(Customer customer, double time, CheckoutLane lane){
        super(customer, time, lane);
    }


}
