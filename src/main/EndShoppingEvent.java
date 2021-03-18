package main;

public class EndShoppingEvent extends Event{

    public EndShoppingEvent(Customer customer, double time){
        super(customer, time);
    }
}
