package com.example.asus.shoppingapp.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus.shoppingapp.Home.home;
import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.AppUtilities;
import com.example.asus.shoppingapp.Utility.Constant;
import com.example.asus.shoppingapp.Utility.NetworkUtility;
import com.example.asus.shoppingapp.Utility.SharePreferenceUtil;
import com.example.asus.shoppingapp.Utility.dataValidation;
import com.example.asus.shoppingapp.WebServices.ServiceWrapper;
import com.example.asus.shoppingapp.beanResponse.ForgotPassword;
import com.example.asus.shoppingapp.beanResponse.userSignin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {
    private String TAG = "Forget Password Activity";
    private EditText phone;
    private TextView submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Log.e(TAG,"alert forget_password activity");
        phone=(EditText) findViewById(R.id.phone_no);
        submit = (TextView)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataValidation.isnotvalidphone(phone.getText().toString())){
                    AppUtilities.displaymessage(ForgetPassword.this,getString(R.string.phone_no)+ " " + getString(R.string.is_invalid));
                }else {
                    calForgotpasswordAPI();
                }
            }
        });

    }
    public void calForgotpasswordAPI(){
        if (!NetworkUtility.isnetworkconected(ForgetPassword.this)) {
            AppUtilities.displaymessage(ForgetPassword.this, getString(R.string.no_network_connection));
        } else {
            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<ForgotPassword> call = serviceWrapper.UserForgotPasswordCall(phone.getText().toString());
            call.enqueue(new Callback<ForgotPassword>() {
                @Override
                public void onResponse(Call<ForgotPassword> call, Response<ForgotPassword> response) {
                    Log.d(TAG,"response :" + response.toString());
                    if (response.body()!=null && response.isSuccessful()){
                        if (response.body().getStatus() == 1){
                            //store user data to share preference
                            Intent intent = new Intent(ForgetPassword.this,OTP_verify.class);
                            intent.putExtra("userid",response.body().getInformation().getUserId());
                            intent.putExtra("otp",response.body().getInformation().getOtp());
                            intent.putExtra("phoneno",phone.getText().toString());
                            startActivity(intent);

                        }else {
                            AppUtilities.displaymessage(ForgetPassword.this,response.body().getMsg());
                        }
                    }else {
                        AppUtilities.displaymessage(ForgetPassword.this,getString(R.string.failed_request));
                    }
                }

                @Override
                public void onFailure(Call<ForgotPassword> call, Throwable t) {
                    Log.d(TAG,"failure :" + t.toString());
                    AppUtilities.displaymessage(ForgetPassword.this,getString(R.string.failed_request));
                }
            });
        }
    }
}
