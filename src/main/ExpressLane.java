package main;

public class ExpressLane extends CheckoutLane{

    public double getCheckoutTime(){
        double checkoutTime = getCustomers().peek().getShoppingTime() + (getCustomers().peek().getNumItems() * 0.1) + 1;
        getCustomers().peek().setCheckoutTime(checkoutTime);
        return checkoutTime;
    }
}
