package com.example.asus.shoppingapp.myaccount;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus.shoppingapp.ProductPreview.productDetails;
import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.cart.OrderAddress;
import com.example.asus.shoppingapp.cart.OrderAddress_Adapter;
import com.example.asus.shoppingapp.cart.OrderAddress_Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 29-12-2018.
 */

public class OrderHistory_Adapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderHistory_Model> orderHistory_models;
    private Context mcontext;
    private String TAG = "order history Adapter";
    private ArrayList<RelativeLayout> addrlayoutlist = new ArrayList<>();
    private ArrayList<ImageView> imagelist = new ArrayList<>();

    public OrderHistory_Adapter(Context context, List<OrderHistory_Model> orderHistory_models){
        this.orderHistory_models = orderHistory_models;
        this.mcontext = context;
    }

    private class orderhistoryHolder extends RecyclerView.ViewHolder {
        TextView order_id,order_shipping,order_price,order_date,order_viewdetails;
        


        public orderhistoryHolder(View itemView) {
            super(itemView);
            order_id = (TextView)itemView.findViewById(R.id.order_id);
            order_price = (TextView)itemView.findViewById(R.id.order_price);
            order_shipping = (TextView) itemView.findViewById(R.id.order_shipping);
            order_date = (TextView)itemView.findViewById(R.id.order_date);
            order_viewdetails = (TextView)itemView.findViewById(R.id.order_viewdetails);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_orderhistory_item,parent,false);
        Log.e(TAG," view created ");
        return new orderhistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final OrderHistory_Model model = orderHistory_models.get(position);
        ((orderhistoryHolder)holder).order_id.setText("Order id: " + model.getOrderid());
        ((orderhistoryHolder)holder).order_price.setText("$" + model.getPrice());
        ((orderhistoryHolder)holder).order_shipping.setText(model.getShipping());
        ((orderhistoryHolder)holder).order_date.setText(model.getDate());

        //addrlayoutlist.add(((OrderAddress_Adapter.orderAddressHolder)holder).address_layout);
       // imagelist.add(((OrderAddress_Adapter.orderAddressHolder)holder).select);

        ((orderhistoryHolder)holder).order_viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"user select the order id " + model.getOrderid());
                Intent intent = new Intent(mcontext, OrderHistory_ViewDetails.class);
                intent.putExtra("order_id",model.getOrderid());
                intent.putExtra("address",model.getShipping());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderHistory_models.size();
    }
}



