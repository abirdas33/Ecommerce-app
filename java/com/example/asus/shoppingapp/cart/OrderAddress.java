package com.example.asus.shoppingapp.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.example.asus.shoppingapp.beanResponse.GetAddress;
import com.example.asus.shoppingapp.beanResponse.Placeorder;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by asus on 17-12-2018.
 */

public class OrderAddress extends AppCompatActivity {

    private FloatingActionButton fab;
    private String TAG = "order address";
    private RecyclerView order_recyclerview;
    private TextView continuebtn;
    private String totalamount = "0";
    public String address_id = "0";
    private OrderAddress_Adapter address_adapter;
    private ArrayList<OrderAddress_Model> orderAddress_models = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        totalamount = intent.getExtras().getString("amount");

        fab = (FloatingActionButton)findViewById(R.id.fab);
        order_recyclerview = (RecyclerView)findViewById(R.id.order_recyclerview);
        continuebtn = (TextView)findViewById(R.id.continuetn);

        LinearLayoutManager mlayoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL, false);
        order_recyclerview.setLayoutManager(mlayoutManager);
        order_recyclerview.setItemAnimator(new DefaultItemAnimator());

        address_adapter = new OrderAddress_Adapter(OrderAddress.this,orderAddress_models);
        order_recyclerview.setAdapter(address_adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderAddress.this,OrderAddress_AddNew.class);
                startActivity(intent);
            }
        });

        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!address_id.equalsIgnoreCase("0")){
                    Intent intent = new Intent(OrderAddress.this,PlaceOrder.class);
                    intent.putExtra("amount",String.valueOf(totalamount));
                    intent.putExtra("addressid",String.valueOf(address_id));
                    startActivity(intent);
                }else {
                    AppUtilities.displaymessage(OrderAddress.this,getResources().getString(R.string.select_address));

                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserAddress();
    }

    public void getUserAddress() {
        if (!NetworkUtility.isnetworkconected(OrderAddress.this)) {
            AppUtilities.displaymessage(OrderAddress.this, getString(R.string.no_network_connection));
        } else {
            // Log.e(TAG,SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<GetAddress> call = service.getAddressCall("1234", SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            call.enqueue(new Callback<GetAddress>() {
                @Override
                public void onResponse(Call<GetAddress> call, Response<GetAddress> response) {
                    Log.e(TAG, "response is " + response.body() + "---- " + new Gson().toJson(response.body()));
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            if (response.body().getInformation().getAddressDetails().size() > 0) {
                                orderAddress_models.clear();
                                for (int i = 0; i < response.body().getInformation().getAddressDetails().size(); i++) {
                                    orderAddress_models.add(new OrderAddress_Model(
                                            response.body().getInformation().getAddressDetails().get(i).getAddressId(),
                                            response.body().getInformation().getAddressDetails().get(i).getFullname(),
                                            response.body().getInformation().getAddressDetails().get(i).getAddress1() + "\n" +
                                                    response.body().getInformation().getAddressDetails().get(i).getAddress2() + "\n" +
                                                    response.body().getInformation().getAddressDetails().get(i).getCity() + " " +
                                                    response.body().getInformation().getAddressDetails().get(i).getState() + "\n" +
                                                    response.body().getInformation().getAddressDetails().get(i).getPincode(),
                                            response.body().getInformation().getAddressDetails().get(i).getPhone()));


                                }
                                address_adapter.notifyDataSetChanged();
                            }


                        } else {
                            AppUtilities.displaymessage(OrderAddress.this, response.body().getMsg());
                        }
                    } else {
                        AppUtilities.displaymessage(OrderAddress.this, getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<GetAddress> call, Throwable t) {
                    Log.e(TAG, "fail to get order summary items " + t.toString());
                    AppUtilities.displaymessage(OrderAddress.this, getString(R.string.fail_to_get_address));
                }
            });
        }
    }

    /*

    1) cart detail -- produ details price
    2) summery
    3) discount coupan-   table - sno. coupan code  valid datte, 10%, Rs 100
    3) select address - -if not --create
    4) integration intamojo...
    5) start payment process--
    6) if successfull payment then store orderid and paymetnid along with user id, product detaiils , shipping address id,
       6.1) from cartdetails delete user cart and qoute id
    7) successfull

    */




}
