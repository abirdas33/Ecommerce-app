package com.example.asus.shoppingapp.cart;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.shoppingapp.Home.home;
import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.AppUtilities;
import com.example.asus.shoppingapp.Utility.Constant;
import com.example.asus.shoppingapp.Utility.NetworkUtility;
import com.example.asus.shoppingapp.Utility.SharePreferenceUtil;
import com.example.asus.shoppingapp.WebServices.ServiceWrapper;
import com.example.asus.shoppingapp.beanResponse.Placeorder;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by asus on 28-12-2018.
 */

public class PlaceOrder extends AppCompatActivity {
    private String TAG = "place order";
    private String totalamount = "10", addressid = "0",deliverymode = "instant_pay";
    private RadioButton check_cod,check_paymentgatway;
    private TextView orderidtxt,pay,continue_shopping;
    private RelativeLayout layout1,layout2;
    private Boolean gotohomeflag = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        final Intent intent = getIntent();
        //totalamount = intent.getExtras().getString("amount");
        addressid = intent.getExtras().getString("addressid");
        check_cod = (RadioButton)findViewById(R.id.checkbox_cod);
        check_paymentgatway = (RadioButton)findViewById(R.id.checkbox_paymentgateway);
        orderidtxt = (TextView)findViewById(R.id.orderidtext);
        continue_shopping = (TextView)findViewById(R.id.continue_Shop);
        pay = (TextView)findViewById(R.id.pay);
        layout1 = (RelativeLayout)findViewById(R.id.relative_layout1);
        layout2 = (RelativeLayout)findViewById(R.id.relative_msglayout);
        //totalamount = 12;

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_paymentgatway.isChecked()){
                    deliverymode = "instant_pay";
                }else{
                    deliverymode = "cash_on_delivery";
                }

                if (!addressid.equalsIgnoreCase("0")){
                   // String order_id = "a089f02724ed4a8db6c069f6d30b3245";
                  //  String payment_id = "MOJO7918005A76494611";
                   // CallPlaceOrderAPI(order_id,payment_id,totalamount,addressid);
                     callInstamojoPay(  SharePreferenceUtil.getInstance().getString(Constant.USER_email),  SharePreferenceUtil.getInstance().getString(Constant.USER_phone),
                             totalamount, " buy from app ", SharePreferenceUtil.getInstance().getString(Constant.USER_name));

                }else {
                    AppUtilities.displaymessage(PlaceOrder.this,getResources().getString(R.string.select_address));

                }
            }
        });

        continue_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PlaceOrder.this,home.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                finish();
            }
        });



    }

    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
            final Activity activity = this;
            InstamojoPay instamojoPay = new InstamojoPay();
            IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
            registerReceiver(instamojoPay, filter);
            JSONObject pay = new JSONObject();
            try {
                pay.put("email", email);
                pay.put("phone", phone);
                pay.put("purpose", purpose);
                pay.put("amount", amount);
                pay.put("name", buyername);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            initListener();
            instamojoPay.start(activity, pay, listener);
        }

        InstapayListener listener;


        private void initListener() {
            listener = new InstapayListener() {
                @Override
                public void onSuccess(String response) {
                    Log.e(TAG, " payment done succesfully "+ response);
                    // status=success:orderId=a089f02724ed4a8db6c069f6d30b3245:txnId=None:paymentId=MOJO7918005A76494611:token=qyFwLidQ0aBNNWlsmwHx1gHFhlt6A1
                    // status=success:orderId=fed40b6b79754a65af7f16cf5ef7ecb3:txnId=None:paymentId=MOJO8223005N32548486:token=qnKill4PsGm6zfkehGlm6QS6vfJZdx
                    //String status = response.substring(0, response.indexOf(":"));
                    String resArray[] =   response.split(":");
                    Log.e(TAG,  " array index 0 "+ resArray[0] + "--orderid -"+ resArray[1] +"---paymentid--"+ resArray[3] );
                    String order_id =  resArray[1].substring(resArray[1].indexOf("=")+1);
                    String payment_id =  resArray[3].substring(resArray[3].indexOf("=")+1);

                    CallPlaceOrderAPI(order_id,payment_id,totalamount,addressid);

                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG) .show();
                }

                @Override
                public void onFailure(int code, String reason) {
                    Log.e(TAG, " payment fail "+ code+ "  reason -- " +reason);
                    Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG) .show();
                }
            };
        }

    public void CallPlaceOrderAPI(String order_id,String payment_id,String totalamount,String addressid){

        if (!NetworkUtility.isnetworkconected(PlaceOrder.this)) {
            AppUtilities.displaymessage(PlaceOrder.this, getString(R.string.no_network_connection));
        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
             Call<Placeorder> call = serviceWrapper.placeorderCall("1234", SharePreferenceUtil.getInstance().getString(Constant.USER_DATA),order_id,payment_id,addressid,totalamount,SharePreferenceUtil.getInstance().getString(Constant.QUOTE_ID), deliverymode );
             call.enqueue(new Callback<Placeorder>() {
                 @Override
                 public void onResponse(Call<Placeorder> call, Response<Placeorder> response) {
                     Log.e(TAG, "response is " + response.body() + "---- " + new Gson().toJson(response.body()));
                     if (response.body() != null && response.isSuccessful()) {
                         if (response.body().getStatus() == 1) {
                             gotohomeflag = true;
                             layout1.setVisibility(View.GONE);
                             layout2.setVisibility(View.VISIBLE);
                             orderidtxt.setText(response.body().getInformation().getOrderId());
                             SharePreferenceUtil.getInstance().saveString(Constant.QUOTE_ID,"");

                         }else{

                             AppUtilities.displaymessage(PlaceOrder.this, response.body().getMsg());
                         }
                     }else{

                         AppUtilities.displaymessage(PlaceOrder.this, getString(R.string.network_error));
                     }
                 }

                 @Override
                 public void onFailure(Call<Placeorder> call, Throwable t) {
                     Log.e(TAG, "fail to get order summary items " + t.toString());
                     AppUtilities.displaymessage(PlaceOrder.this, getString(R.string.fail_to_get_address));

                 }
             });

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (gotohomeflag){
            Intent intent1 = new Intent(PlaceOrder.this,home.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            finish();
        }
    }
}