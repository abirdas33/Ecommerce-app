package com.example.asus.shoppingapp.myaccount;

/**
 * Created by asus on 29-12-2018.
 */

public class OrderHistory_Model {
    private String orderid,shipping,price,date;

    public OrderHistory_Model(String orderid,String shipping,String price,String date){
        this.orderid=orderid;
        this.shipping = shipping;
        this.price = price;
        this.date = date;
    }

    public String getOrderid(){ return orderid;}
    public void setOrderid(String id){
        this.orderid = id;
    }

    public String getShipping(){ return shipping;}
    public void setShipping(String name){
        this.shipping = name;
    }

    public String getPrice(){ return price;}
    public void setPrice(String name){
        this.price = name;
    }

    public String getDate(){ return date;}
    public void setDate(String name){
        this.date = name;
    }

}
