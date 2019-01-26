package com.example.asus.shoppingapp.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.AppUtilities;
import com.example.asus.shoppingapp.Utility.Constant;
import com.example.asus.shoppingapp.Utility.NetworkUtility;
import com.example.asus.shoppingapp.Utility.SharePreferenceUtil;
import com.example.asus.shoppingapp.WebServices.ServiceWrapper;
import com.example.asus.shoppingapp.beanResponse.getCartDetails;
import com.example.asus.shoppingapp.beanResponse.getOrderSummary;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by asus on 17-12-2018.
 */

public class Order_Summary extends AppCompatActivity {

    private String TAG = "order summary";
    private TextView place_order, subtotalvalue,shippingvalue,ordertotalvalue;
    private RecyclerView item_recyclerview;
    private Order_Summary_Adapter order_summary_adapter;
    private ArrayList<Cartitem_Model> cartitemModels = new ArrayList<>();
    private float totalamount = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setHomeButtonEnabled(true);

        place_order = (TextView)findViewById(R.id.place_order);
        subtotalvalue = (TextView)findViewById(R.id.subtotalvalue);
        shippingvalue = (TextView)findViewById(R.id.shippingval);
        ordertotalvalue = (TextView)findViewById(R.id.order_total_val);
        item_recyclerview = (RecyclerView)findViewById(R.id.item_recyclerview);

        LinearLayoutManager mlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        item_recyclerview.setLayoutManager(mlayoutManager);
        item_recyclerview.setItemAnimator(new DefaultItemAnimator());

        order_summary_adapter = new Order_Summary_Adapter(this,cartitemModels);
        item_recyclerview.setAdapter(order_summary_adapter);


        getusercartdetails();

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "  total amount is "+String.valueOf(totalamount));
                if (totalamount>0){
                    Intent intent = new Intent(Order_Summary.this,OrderAddress.class);
                    intent.putExtra("amount",String.valueOf(totalamount));
                    startActivity(intent);
                }

            }
        });

    }

    public void getusercartdetails(){

        if (!NetworkUtility.isnetworkconected(Order_Summary.this)) {
            AppUtilities.displaymessage(Order_Summary.this, getString(R.string.no_network_connection));
        } else {
            // Log.e(TAG,SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<getOrderSummary> call = service.getOrderSummaryCall("1234", SharePreferenceUtil.getInstance().getString(Constant.USER_DATA),SharePreferenceUtil.getInstance().getString(Constant.QUOTE_ID));
            call.enqueue(new Callback<getOrderSummary>() {
                @Override
                public void onResponse(Call<getOrderSummary> call, Response<getOrderSummary> response) {
                    Log.e(TAG, "response is " + response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            subtotalvalue.setText("$"+ response.body().getInformation().getSubtotal());
                            shippingvalue.setText(response.body().getInformation().getShippingfee());
                            ordertotalvalue.setText("$" + response.body().getInformation().getOrdertotal());

                           // totalamount = Float.valueOf(response.body().getInformation().getOrdertotal());
                            try {

                                totalamount = Float.valueOf( response.body().getInformation().getOrdertotal());

                            }catch (Exception e){
                                Log.e(TAG, "amount error "+ e.toString() );
                            }
                            cartitemModels.clear();
                            for (int i=0;i<response.body().getInformation().getProdDetails().size();i++){
                                cartitemModels.add(new Cartitem_Model(response.body().getInformation().getProdDetails().get(i).getId(),
                                        response.body().getInformation().getProdDetails().get(i).getName(),
                                        "",
                                        "",response.body().getInformation().getProdDetails().get(i).getPrice(),
                                        response.body().getInformation().getProdDetails().get(i).getQty()));


                            }
                            order_summary_adapter.notifyDataSetChanged();

                        }else{
                            AppUtilities.displaymessage(Order_Summary.this,response.body().getMsg());
                        }
                    }else{
                        AppUtilities.displaymessage(Order_Summary.this,getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<getOrderSummary> call, Throwable t) {
                    Log.e(TAG, "fail to get order summary items " + t.toString());
                    AppUtilities.displaymessage(Order_Summary.this,getString(R.string.fail_to_get_order));
                }
            });

        }
    }

}
