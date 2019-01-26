package com.example.asus.shoppingapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus.shoppingapp.Home.home;
import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.AppUtilities;
import com.example.asus.shoppingapp.Utility.NetworkUtility;
import com.example.asus.shoppingapp.WebServices.ServiceWrapper;
import com.example.asus.shoppingapp.beanResponse.ForgotPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by asus on 12-08-2018.
 */

public class OTP_verify extends AppCompatActivity {

    private String TAG = "OTP verifyy";
    private EditText otp_value;
    private TextView submit,resend_otp;
    private String userid,phoneno,otpvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        Log.e(TAG, "alert otp verification");
        otp_value = (EditText)findViewById(R.id.otp_value);
        submit = (TextView)findViewById(R.id.submit);
        resend_otp = (TextView)findViewById(R.id.resend_otp);

        Intent intent = getIntent();

        if (intent!=null && !intent.getExtras().getString("userid").equals(null)){
            userid=intent.getExtras().getString("userid");
            otpvalue=intent.getExtras().getString("otp");
            phoneno=intent.getExtras().getString("phoneno");
        }else {
            userid = "";
            phoneno = "";
            otpvalue = "";
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(otp_value.getText().toString().trim())){
                    AppUtilities.displaymessage(OTP_verify.this,"otp  " + getString(R.string.is_invalid));
                }else {
                    if (otp_value.getText().toString().trim().equals(otpvalue)){
                        //show the new password screen
                        Intent intent = new Intent(OTP_verify.this,New_Password.class);
                        intent.putExtra("userid",userid);
                        startActivity(intent);
                    }else {
                        AppUtilities.displaymessage(OTP_verify.this,getString(R.string.otp_not_matched));
                    }
                }
            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calForgotpasswordAPI();
            }
        });

    }

    public void calForgotpasswordAPI(){
        if (!NetworkUtility.isnetworkconected(OTP_verify.this)) {
            AppUtilities.displaymessage(OTP_verify.this, getString(R.string.no_network_connection));
        } else {
            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<ForgotPassword> call = serviceWrapper.UserForgotPasswordCall(phoneno);
            call.enqueue(new Callback<ForgotPassword>() {
                @Override
                public void onResponse(Call<ForgotPassword> call, Response<ForgotPassword> response) {
                    Log.d(TAG,"response :" + response.toString());
                    if (response.body()!=null && response.isSuccessful()){
                        if (response.body().getStatus() == 1){


                        }else {
                            AppUtilities.displaymessage(OTP_verify.this,response.body().getMsg());
                        }
                    }else {
                        AppUtilities.displaymessage(OTP_verify.this,getString(R.string.failed_request));
                    }
                }

                @Override
                public void onFailure(Call<ForgotPassword> call, Throwable t) {
                    Log.d(TAG,"failure :" + t.toString());
                    AppUtilities.displaymessage(OTP_verify.this,getString(R.string.failed_request));
                }
            });
        }
    }
}
