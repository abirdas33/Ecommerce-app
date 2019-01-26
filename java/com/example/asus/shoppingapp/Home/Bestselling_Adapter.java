package com.example.asus.shoppingapp.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.shoppingapp.ProductPreview.productDetails;
import com.example.asus.shoppingapp.R;

import java.util.List;

/**
 * Created by asus on 17-08-2018.
 */

public class Bestselling_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mcontext;
    private List<BestSelling_Model> mbestSellingModelList;
    private String TAG = "Bestselling adapter";
    private int screenwidth;

    public Bestselling_Adapter(Context mcontext, List<BestSelling_Model> mbestSellingModelList,int screenWidth) {
        this.mcontext = mcontext;
        this.mbestSellingModelList = mbestSellingModelList;
        this.screenwidth = screenWidth;
    }

    private class BestSellingHolder extends RecyclerView.ViewHolder {
        ImageView prod_img;
        TextView prod_name,old_price,new_price;
        RatingBar ratingBar;
        CardView cardView;

        public BestSellingHolder(View itemView) {
            super(itemView);
            prod_img = (ImageView)itemView.findViewById(R.id.prod_img);
            prod_name = (TextView)itemView.findViewById(R.id.pro_name);
            old_price = (TextView)itemView.findViewById(R.id.old_price);
            new_price = (TextView)itemView.findViewById(R.id.new_price);
            ratingBar = (RatingBar)itemView.findViewById(R.id.prod_rating);
            cardView = (CardView)itemView.findViewById(R.id.cardview);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenwidth - (screenwidth/100*65),LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(10,10,10,10);
            cardView.setLayoutParams(params);
            cardView.setPadding(5,5,5,5);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_bestselling,parent,false);

        return new BestSellingHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BestSelling_Model model = mbestSellingModelList.get(position);
        ((BestSellingHolder)holder).prod_name.setText(model.getProd_name());
        ((BestSellingHolder)holder).old_price.setText(model.getOld_price());
        ((BestSellingHolder)holder).new_price.setText(model.getPrice());
        ((BestSellingHolder)holder).ratingBar.setRating(Float.valueOf(model.getRating()));

        //imageview glider lib to get image from url
        Glide
                .with(mcontext)
                .load(model.getImg_url())
                .into(((BestSellingHolder) holder).prod_img);

        ((BestSellingHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, productDetails.class);
                intent.putExtra("prod_id",model.getProd_id());
                mcontext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mbestSellingModelList.size();
    }
}
