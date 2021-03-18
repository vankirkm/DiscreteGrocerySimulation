package main;

public class ExpressLane extends CheckoutLane{

    public double getCheckoutTime(){
        double checkoutTime = 0;
        for(Customer customer : getCustomers()){
            checkoutTime += customer.getArrivalTime() + customer.getShoppingTime() + (customer.getNumItems() * 0.1) + 1;
        }
        return checkoutTime;
    }
}
