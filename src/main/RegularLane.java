package main;

public class RegularLane extends CheckoutLane{

    public double getCheckoutTime(){
        double checkoutTime = getCustomers().peek().getShoppingTime() + (getCustomers().peek().getNumItems() * 0.05) + 2;
        getCustomers().peek().setCheckoutTime(checkoutTime);
        return checkoutTime;
    }
}
