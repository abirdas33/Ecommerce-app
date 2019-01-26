package com.example.asus.shoppingapp.cart;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.asus.shoppingapp.R;

import java.util.List;

/**
 * Created by asus on 17-12-2018.
 */

public class Order_Summary_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Cartitem_Model> cartitem_models;
    private Context mcontext;
    private String TAG = "order summary Adapter";

    public Order_Summary_Adapter(Context context, List<Cartitem_Model> cartitem_models){
        this.cartitem_models = cartitem_models;
        this.mcontext = context;
    }

    private class orderSummaryitemHolder extends RecyclerView.ViewHolder {
        TextView prod_name,prod_qty,prod_price;


        public orderSummaryitemHolder(View itemView) {
            super(itemView);
            prod_name = (TextView)itemView.findViewById(R.id.prod_name);
            prod_price = (TextView)itemView.findViewById(R.id.prod_price);
            prod_qty = (TextView) itemView.findViewById(R.id.prod_qty);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_order_summary_item,parent,false);
        Log.e(TAG," view created ");
        return new orderSummaryitemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Cartitem_Model model = cartitem_models.get(position);
        ((orderSummaryitemHolder)holder).prod_name.setText(model.getProd_name());
        ((orderSummaryitemHolder)holder).prod_price.setText(model.getPrice());
        ((orderSummaryitemHolder)holder).prod_qty.setText(model.getQty());

    }

    @Override
    public int getItemCount() {
        return cartitem_models.size();
    }
}
