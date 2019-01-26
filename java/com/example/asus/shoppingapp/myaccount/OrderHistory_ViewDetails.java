package com.example.asus.shoppingapp.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.AppUtilities;
import com.example.asus.shoppingapp.Utility.Constant;
import com.example.asus.shoppingapp.Utility.NetworkUtility;
import com.example.asus.shoppingapp.Utility.SharePreferenceUtil;
import com.example.asus.shoppingapp.WebServices.ServiceWrapper;
import com.example.asus.shoppingapp.beanResponse.getorderproddetails;
import com.example.asus.shoppingapp.cart.Cartitem_Model;
import com.example.asus.shoppingapp.cart.Order_Summary_Adapter;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by asus on 29-12-2018.
 */

public class OrderHistory_ViewDetails extends AppCompatActivity {

    private String TAG = "order history view details",orderid = "";
    private RecyclerView item_recyclerview;
    private Order_Summary_Adapter order_summary_adapter;
    private ArrayList<Cartitem_Model> cartitemModels = new ArrayList<>();
    private float totalamount = 0;
    private String shippingaddress = "";
    private TextView subtotalvalue,shippingval,order_total_val,order_ship_address,order_bill_address;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory_viewdetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
      //  getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        orderid = intent.getExtras().getString("order_id");
        shippingaddress = intent.getExtras().getString("address");

        item_recyclerview = (RecyclerView)findViewById(R.id.item_recyclerview);
        subtotalvalue = (TextView)findViewById(R.id.subtotalvalue);
        shippingval = (TextView)findViewById(R.id.shippingval);
        order_total_val = (TextView)findViewById(R.id.order_total_val);
        order_ship_address = (TextView)findViewById(R.id.order_ship_address);
        order_bill_address = (TextView)findViewById(R.id.order_bill_address);


        LinearLayoutManager mlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        item_recyclerview.setLayoutManager(mlayoutManager);
        item_recyclerview.setItemAnimator(new DefaultItemAnimator());

        order_summary_adapter = new Order_Summary_Adapter(this,cartitemModels);
        item_recyclerview.setAdapter(order_summary_adapter);

        getorderdetails();
    }

    public void getorderdetails(){
        if (!NetworkUtility.isnetworkconected(OrderHistory_ViewDetails.this)) {
            AppUtilities.displaymessage(OrderHistory_ViewDetails.this, getString(R.string.no_network_connection));
        } else {
            // Log.e(TAG,SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<getorderproddetails> call = service.getorderproddetailsCall("1234", SharePreferenceUtil.getInstance().getString(Constant.USER_DATA),orderid);
            call.enqueue(new Callback<getorderproddetails>() {
                @Override
                public void onResponse(Call<getorderproddetails> call, Response<getorderproddetails> response) {
                    Log.e(TAG, "response is " + response.body() + "---- " + new Gson().toJson(response.body()));
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            if (response.body().getInformation().size()>0) {

                                subtotalvalue.setText(response.body().getSubtotal());
                                shippingval.setText(response.body().getShippingfee());
                                order_total_val.setText(response.body().getGrandtotal());
                                order_ship_address.setText(shippingaddress);
                                order_bill_address.setText(shippingaddress);

                                cartitemModels.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {
                                    cartitemModels.add(new Cartitem_Model(response.body().getInformation().get(i).getProdId(),
                                            response.body().getInformation().get(i).getProdName(),
                                            "",
                                            "", response.body().getInformation().get(i).getPrice(),
                                            response.body().getInformation().get(i).getQty()));


                                }
                                order_summary_adapter.notifyDataSetChanged();
                            }

                        } else {
                            AppUtilities.displaymessage(OrderHistory_ViewDetails.this, response.body().getMsg());
                        }
                    } else {
                        AppUtilities.displaymessage(OrderHistory_ViewDetails.this, getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<getorderproddetails> call, Throwable t) {
                    Log.e(TAG, "fail to get user cart list " + t.toString());
                    AppUtilities.displaymessage(OrderHistory_ViewDetails.this,getString(R.string.fail_to_get_history));
                }
            });
        }

    }
}
