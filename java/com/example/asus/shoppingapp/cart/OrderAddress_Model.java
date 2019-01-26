package com.example.asus.shoppingapp.cart;

/**
 * Created by asus on 19-12-2018.
 */

public class OrderAddress_Model {
    private String address_id,fullname,fulladdress,address_phone;

    public OrderAddress_Model(String address_id,String fullname,String fulladdress,String address_phone){
        this.address_id=address_id;
        this.fullname = fullname;
        this.fulladdress = fulladdress;
        this.address_phone = address_phone;
    }

    public String getaddress_id(){ return address_id;}
    public void setaddress_id(String id){
        this.address_id = id;
    }

    public String getfullname(){ return fullname;}
    public void setfullname(String name){
        this.fullname = name;
    }

    public String getfulladdress(){ return fulladdress;}
    public void setfulladdress(String name){
        this.fulladdress = name;
    }



    public String getaddress_phone(){ return address_phone;}
    public void setaddress_phone(String name){
        this.address_phone = name;
    }


}
