package com.example.asus.shoppingapp.cart;

/**
 * Created by asus on 13-12-2018.
 */

public class Cartitem_Model {

    private String prod_id,prod_name,img_url,old_price,price,qty;

    public Cartitem_Model(String prod_id,String prod_name,String img_url,String old_price,String price,String qty){
        this.prod_id=prod_id;
        this.prod_name = prod_name;
        this.img_url = img_url;
        this.old_price = old_price;
        this.price = price;
        this.qty = qty;
    }

    public String getProd_id(){ return prod_id;}
    public void setProd_id(String id){
        this.prod_id = id;
    }

    public String getProd_name(){ return prod_name;}
    public void setProd_name(String name){
        this.prod_name = name;
    }

    public String getImg_url(){ return img_url;}
    public void setImg_url(String name){
        this.img_url = name;
    }

    public String getOld_price(){ return old_price;}
    public void setOld_price(String name){
        this.old_price = name;
    }

    public String getPrice(){ return price;}
    public void setPrice(String name){
        this.price = name;
    }

    public String getQty(){ return qty;}
    public void setQty(String name){
        this.qty = name;
    }
}
