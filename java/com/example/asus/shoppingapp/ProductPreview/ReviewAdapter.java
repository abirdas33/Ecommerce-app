package com.example.asus.shoppingapp.ProductPreview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatRatingBar;
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
import com.example.asus.shoppingapp.Home.BestSelling_Model;
import com.example.asus.shoppingapp.Home.Bestselling_Adapter;
import com.example.asus.shoppingapp.R;

import java.util.List;

/**
 * Created by asus on 30-08-2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mcontext;
    private List<ReviewModel> mReviewModelList;
    private String TAG = "Review adapter";

    public ReviewAdapter(Context mcontext, List<ReviewModel> mReviewModelList) {
        this.mcontext = mcontext;
        this.mReviewModelList = mReviewModelList;
    }

    private class ReviewHolder extends RecyclerView.ViewHolder {
        TextView review_title,review_details,username,postdate;
        AppCompatRatingBar ratingBar;

        public ReviewHolder(View itemView) {
            super(itemView);
            review_title = (TextView)itemView.findViewById(R.id.title);
            review_details = (TextView)itemView.findViewById(R.id.desc);
            username = (TextView)itemView.findViewById(R.id.username);
            ratingBar = (AppCompatRatingBar) itemView.findViewById(R.id.review_rating);
            postdate = (TextView) itemView.findViewById(R.id.date);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_product_review_item,parent,false);

        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ReviewModel model = mReviewModelList.get(position);
        ((ReviewHolder)holder).review_title.setText(model.getTitle());
        ((ReviewHolder)holder).review_details.setText(model.getDesc());
        ((ReviewHolder)holder).username.setText(model.getUsername());
        ((ReviewHolder)holder).postdate.setText(model.getDate());
        ((ReviewHolder)holder).ratingBar.setRating(Float.valueOf(model.getRating()));

    }

    @Override
    public int getItemCount() {
        return mReviewModelList.size();
    }
}
