package main;

public class Customer{
    private int customerNumber;
    private int numItems;
    private double timePerItem;
    private double arrivalTime;
    private double shoppingTime;
    private double checkoutTime;

    public Customer(double arrivalTime, int numItems, double timePerItem, int customerNumber){
        this.arrivalTime = arrivalTime;
        this.numItems = numItems;
        this.timePerItem = timePerItem;
        this.customerNumber = customerNumber;
        this.shoppingTime = (numItems * timePerItem) + arrivalTime;
        this.checkoutTime = 0;
    }

    public int getNumItems(){
        return numItems;
    }

    public double getShoppingTime() {
        return shoppingTime;
    }

    public double getArrivalTime(){
        return arrivalTime;
    }

    public double getCheckoutTime(){
        return checkoutTime;
    }

    public int getCustomerNumber(){
        return customerNumber;
    }

    public void setCheckoutTime(double checkoutTime){
        this.checkoutTime = checkoutTime;
    }

    public String toString(){
        return "numItems";
    }
}
