package com.example.asus.shoppingapp.ProductPreview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.shoppingapp.Home.NewProduct_Adapter;
import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by asus on 29-08-2018.
 */

public class Review extends Fragment {
    private Context context;
    private RecyclerView recyclerView_userreview;
    private String reviewstring = "";
    private String TAG = "review";
    private ArrayList<ReviewModel> modelArrayList = new ArrayList<>();
    private ReviewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_product_review,container,false);
        context = view.getContext();
        recyclerView_userreview = (RecyclerView) view.findViewById(R.id.recycler_useroverview);
        LinearLayoutManager mlayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_userreview.setLayoutManager(mlayoutManager1);
        recyclerView_userreview.setItemAnimator(new DefaultItemAnimator());

        reviewstring = (((productDetails)getActivity()).prod_review);
        try {
            JSONArray jsonArray = new JSONArray(reviewstring);
            Log.e(TAG, "array size :" + jsonArray.length());
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                Log.e(TAG,"json value " + object.get("title"));
                modelArrayList.add(new ReviewModel(String.valueOf(object.get("title")),
                        String.valueOf(object.get("desc")),
                        String.valueOf(object.get("username")),
                        String.valueOf(object.get("date")),
                        String.valueOf(object.get("rating"))));

            }



        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG,"json error is :" + e.toString());
        }

        adapter = new ReviewAdapter(context, modelArrayList);
        recyclerView_userreview.setAdapter(adapter);
        return view;
    }
}
