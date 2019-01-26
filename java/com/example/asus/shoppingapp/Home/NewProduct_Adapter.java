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
 * Created by asus on 19-08-2018.
 */

public class NewProduct_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mcontext;
    private List<NewProduct_Model> mNewProductModelList;
    private String TAG = "NewProduct adapter";
    private int screenwidth;

    public NewProduct_Adapter(Context mcontext, List<NewProduct_Model> List,int screenWidth) {
        this.mcontext = mcontext;
        this.mNewProductModelList = List;
        this.screenwidth = screenWidth;
    }

    private class NewProductHolder extends RecyclerView.ViewHolder {
        ImageView prod_img;
        TextView prod_name;
        CardView cardView;

        public NewProductHolder(View itemView) {
            super(itemView);
            prod_img = (ImageView)itemView.findViewById(R.id.prod_img);
            prod_name = (TextView)itemView.findViewById(R.id.prod_name);
            cardView = (CardView)itemView.findViewById(R.id.cardview);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenwidth - (screenwidth/100*65),LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(10,10,10,10);
            cardView.setLayoutParams(params);
            cardView.setPadding(5,5,5,5);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_new_product,parent,false);

        return new NewProductHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NewProduct_Model model = mNewProductModelList.get(position);
        ((NewProductHolder)holder).prod_name.setText(model.getProd_name());

        //imageview glider lib to get image from url
        Glide
                .with(mcontext)
                .load(model.getImg_url())
                .into(((NewProductHolder) holder).prod_img);

        ((NewProductHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
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
        return mNewProductModelList.size();
    }
}
