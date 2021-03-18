package main;

public class RegularLane extends CheckoutLane{

    public double getCheckoutTime(){
        double checkoutTime = 0;
        for(Customer customer : getCustomers()){
            checkoutTime += customer.getArrivalTime() + customer.getShoppingTime() + (customer.getNumItems() * 0.05) + 2;
        }
        return checkoutTime;
    }
}
