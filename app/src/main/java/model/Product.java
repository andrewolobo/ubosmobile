package model;

/**
 * Created by Dell on 8/2/2017.
 */

public class Product {

    public int id;
    public String name;
    public double price;

    public void Product(int startId, String startName, double startPrice){
        startId = id;
        startName = name;
        startPrice = price;
    }

    public int getId(){
        return id;
    }

    public double getPrice(){
        return price;
    }
    public String getName(){
        return name;
    }


}
