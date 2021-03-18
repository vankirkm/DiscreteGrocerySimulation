package main;

public class Customer{
    private int customerNumber;
    private int numItems;
    private double timePerItem;
    private double arrivalTime;
    private double shoppingTime;

    public Customer(double arrivalTime, int numItems, double timePerItem, int customerNumber){
        this.arrivalTime = arrivalTime;
        this.numItems = numItems;
        this.timePerItem = timePerItem;
        this.customerNumber = customerNumber;
        this.shoppingTime = (numItems * timePerItem) + arrivalTime;
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

    public double getWaitTime(){
        return 0;
    }

    public String toString(){
        return arrivalTime + " " + numItems + " " + timePerItem;
    }
}
