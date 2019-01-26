package com.example.asus.shoppingapp.cart;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.AppUtilities;
import com.example.asus.shoppingapp.Utility.Constant;
import com.example.asus.shoppingapp.Utility.NetworkUtility;
import com.example.asus.shoppingapp.Utility.SharePreferenceUtil;
import com.example.asus.shoppingapp.Utility.dataValidation;
import com.example.asus.shoppingapp.WebServices.ServiceWrapper;
import com.example.asus.shoppingapp.beanResponse.AddNewAddress;
import com.example.asus.shoppingapp.login.SignIn;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by asus on 18-12-2018.
 */

public class OrderAddress_AddNew extends AppCompatActivity {
    private FloatingActionButton fab;
    public AlertDialog alert;
    private String TAG = "order address";
    private RecyclerView recyclerView;
    private CheckBox checkbill,Shipbill;
    private EditText fullname,address1,address2,state,city,pincode,phone;
    private TextView savecontinue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_editaddress);

        //getSupportActionBar().setHomeButtonEnabled(true);
        checkbill = (CheckBox)findViewById(R.id.checkbox_billing);
        Shipbill = (CheckBox)findViewById(R.id.checkbox_Shipping);
        fullname = (EditText)findViewById(R.id.fullname);
        address1 = (EditText)findViewById(R.id.address1);
        address2 = (EditText)findViewById(R.id.address2);
        state = (EditText)findViewById(R.id.state);
        city = (EditText)findViewById(R.id.city);
        pincode = (EditText)findViewById(R.id.pincode);
        phone = (EditText)findViewById(R.id.phone);
        savecontinue = (TextView)findViewById(R.id.savecontinue);

        savecontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataValidation.isnotvalidfullname(fullname.getText().toString())) {
                    AppUtilities.displaymessage(OrderAddress_AddNew.this, getString(R.string.full_name) + " " + getString(R.string.is_invalid));
                }else if(TextUtils.isEmpty((address1.getText().toString().trim()))){
                    AppUtilities.displaymessage(OrderAddress_AddNew.this, getString(R.string.address_line1) + " " + getString(R.string.is_invalid));
                }else if(TextUtils.isEmpty((city.getText().toString().trim()))){
                    AppUtilities.displaymessage(OrderAddress_AddNew.this, getString(R.string.city) + " " + getString(R.string.is_invalid));
                }else if(TextUtils.isEmpty((state.getText().toString().trim()))){
                    AppUtilities.displaymessage(OrderAddress_AddNew.this, getString(R.string.state) + " " + getString(R.string.is_invalid));
                }else if(TextUtils.isEmpty((pincode.getText().toString().trim()))){
                    AppUtilities.displaymessage(OrderAddress_AddNew.this, getString(R.string.pincode) + " " + getString(R.string.is_invalid));
                }else if(dataValidation.isnotvalidphone(phone.getText().toString().trim())){
                    AppUtilities.displaymessage(OrderAddress_AddNew.this, getString(R.string.phone_no) + " " + getString(R.string.is_invalid));
                }else{
                    if(TextUtils.isEmpty(address2.getText().toString().trim())){
                        address2.setText("");
                    }
                    addnewaddressAPI();
                }
            }
        });

    }

    public void addnewaddressAPI(){
        if (!NetworkUtility.isnetworkconected(OrderAddress_AddNew.this)) {
            AppUtilities.displaymessage(OrderAddress_AddNew.this, getString(R.string.no_network_connection));
        } else {
            // Log.e(TAG,SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddNewAddress> call = service.addNewAddressCall("1234", SharePreferenceUtil.getInstance().getString(Constant.USER_DATA),
                    fullname.getText().toString(),address1.getText().toString(),address2.getText().toString(),city.getText().toString(),
                    state.getText().toString(),pincode.getText().toString(),phone.getText().toString());
            call.enqueue(new Callback<AddNewAddress>() {
                @Override
                public void onResponse(Call<AddNewAddress> call, Response<AddNewAddress> response) {
                    Log.e(TAG, "response is " + response.body() +  "---- " + new Gson().toJson(response.body()));
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderAddress_AddNew.this);
                            LayoutInflater inflater = LayoutInflater.from(OrderAddress_AddNew.this);
                            View v = inflater.inflate(R.layout.display_message_popup,null);
                            TextView textView = (TextView)v.findViewById(R.id.text_msg);
                            TextView btn_ok = (TextView)v.findViewById(R.id.btn_ok);

                            textView.setText(response.body().getMsg());
                            builder.setView(v);
                            alert = builder.create();
                            alert.setCancelable(false);
                            alert.getWindow().setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));

                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alert.dismiss();
                                    finish();
                                }
                            });

                            alert.show();

                        }else{
                            AppUtilities.displaymessage(OrderAddress_AddNew.this,response.body().getMsg());
                        }
                    }else{
                        AppUtilities.displaymessage(OrderAddress_AddNew.this,getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<AddNewAddress> call, Throwable t) {
                    Log.e(TAG, "fail to add new address " + t.toString());
                    AppUtilities.displaymessage(OrderAddress_AddNew.this,getString(R.string.fail_toaddaddress));
                }
            });
        }

    }
}
