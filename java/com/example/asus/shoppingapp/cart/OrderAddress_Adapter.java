package com.example.asus.shoppingapp.cart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus.shoppingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 19-12-2018.
 */

public class OrderAddress_Adapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderAddress_Model> orderAddress_models;
    private Context mcontext;
    private String TAG = "order address Adapter";
    private ArrayList<RelativeLayout> addrlayoutlist = new ArrayList<>();
    private ArrayList<ImageView> imagelist = new ArrayList<>();

    public OrderAddress_Adapter(Context context, List<OrderAddress_Model> orderAddress_models){
        this.orderAddress_models = orderAddress_models;
        this.mcontext = context;
    }

    private class orderAddressHolder extends RecyclerView.ViewHolder {
        TextView fullname,fulladdress,phone_nunber;
        ImageView select;
        RelativeLayout address_layout;


        public orderAddressHolder(View itemView) {
            super(itemView);
            fullname = (TextView)itemView.findViewById(R.id.fullname);
            phone_nunber = (TextView)itemView.findViewById(R.id.phone_no);
            fulladdress = (TextView) itemView.findViewById(R.id.fulladdress);
            select = (ImageView)itemView.findViewById(R.id.imageselect);
            address_layout = (RelativeLayout)itemView.findViewById(R.id.address_layout);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_order_address_item,parent,false);
        Log.e(TAG," view created ");
        return new orderAddressHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final OrderAddress_Model model = orderAddress_models.get(position);
        ((orderAddressHolder)holder).fullname.setText(model.getfullname());
        ((orderAddressHolder)holder).phone_nunber.setText(model.getaddress_phone());
        ((orderAddressHolder)holder).fulladdress.setText(model.getfulladdress());
       // ((orderAddressHolder)holder).address_layout.setTag(model.getaddress_id());

        addrlayoutlist.add(((orderAddressHolder)holder).address_layout);
        imagelist.add(((orderAddressHolder)holder).select);

        ((orderAddressHolder)holder).address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"user select the address " + model.getaddress_id());
                ((OrderAddress)mcontext).address_id = model.getaddress_id();

                for (int i=0;i<addrlayoutlist.size();i++){
                    imagelist.get(i).setVisibility(View.GONE);
                    addrlayoutlist.get(i).setBackgroundResource(R.drawable.rounded_corner_black);

                }

                ((orderAddressHolder)holder).select.setVisibility(View.VISIBLE);
                ((orderAddressHolder)holder).address_layout.setBackgroundResource(R.drawable.border_green_rounded);

            }
        });

    }

    @Override
    public int getItemCount() {
        return orderAddress_models.size();
    }
}

