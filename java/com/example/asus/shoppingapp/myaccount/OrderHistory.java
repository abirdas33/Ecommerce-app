package com.example.asus.shoppingapp.myaccount;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.AppUtilities;
import com.example.asus.shoppingapp.Utility.Constant;
import com.example.asus.shoppingapp.Utility.NetworkUtility;
import com.example.asus.shoppingapp.Utility.SharePreferenceUtil;
import com.example.asus.shoppingapp.WebServices.ServiceWrapper;
import com.example.asus.shoppingapp.beanResponse.OrderHistoryapi;
import com.example.asus.shoppingapp.cart.CartDetails;
import com.example.asus.shoppingapp.cart.Cart_Adapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by asus on 29-12-2018.
 */

public class OrderHistory extends AppCompatActivity {

    private String TAG = "orderhistory";
    private RecyclerView recyclerView_order;
    private ArrayList<OrderHistory_Model> models = new ArrayList<>();
    private OrderHistory_Adapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);

        recyclerView_order = (RecyclerView)findViewById(R.id.recycler_orderhistory);

        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_order.setLayoutManager(mlayoutManager);
        recyclerView_order.setItemAnimator(new DefaultItemAnimator());
        adapter = new OrderHistory_Adapter(this,models);
        recyclerView_order.setAdapter(adapter);

        getuserorderhistory();
    }

    public void getuserorderhistory() {
        if (!NetworkUtility.isnetworkconected(OrderHistory.this)) {
            AppUtilities.displaymessage(OrderHistory.this, getString(R.string.no_network_connection));
        } else {
            // Log.e(TAG,SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<OrderHistoryapi> call = service.getOrderHistoryCall("1234", SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            call.enqueue(new Callback<OrderHistoryapi>() {
                @Override
                public void onResponse(Call<OrderHistoryapi> call, Response<OrderHistoryapi> response) {
                    Log.e(TAG, "response is " + response.body() + "---- " + new Gson().toJson(response.body()));
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            models.clear();
                            if (response.body().getInformation().size()>0){

                                for (int i=0;i<response.body().getInformation().size();i++){

                                    models.add(new OrderHistory_Model(
                                            response.body().getInformation().get(i).getOrderId(),
                                            response.body().getInformation().get(i).getShippingaddress(),
                                            response.body().getInformation().get(i).getPrice(),
                                            response.body().getInformation().get(i).getDate()));
                                }
                                adapter.notifyDataSetChanged();
                            }

                        } else {
                            AppUtilities.displaymessage(OrderHistory.this, response.body().getMsg());
                        }
                    } else {
                        AppUtilities.displaymessage(OrderHistory.this, getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<OrderHistoryapi> call, Throwable t) {
                    Log.e(TAG, "fail to get user cart list " + t.toString());
                    AppUtilities.displaymessage(OrderHistory.this,getString(R.string.fail_to_get_history));
                }
            });

        }
    }

}
