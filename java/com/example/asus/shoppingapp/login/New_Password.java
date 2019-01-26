package com.example.asus.shoppingapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus.shoppingapp.Home.home;
import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.AppUtilities;
import com.example.asus.shoppingapp.Utility.NetworkUtility;
import com.example.asus.shoppingapp.Utility.dataValidation;
import com.example.asus.shoppingapp.WebServices.ServiceWrapper;
import com.example.asus.shoppingapp.beanResponse.ForgotPassword;
import com.example.asus.shoppingapp.beanResponse.NewPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by asus on 12-08-2018.
 */

public class New_Password extends AppCompatActivity {
    private String TAG = "New Password",userid;
    private EditText password,retype_password;
    private TextView submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        Log.e(TAG, "alert new password");
        password = (EditText)findViewById(R.id.pass);
        retype_password = (EditText)findViewById(R.id.retype_pass);
        submit = (TextView) findViewById(R.id.submit);

        Intent intent = getIntent();

        if (intent!=null && !intent.getExtras().getString("userid").equals(null)){
            userid=intent.getExtras().getString("userid");
        }else {
            userid = "";
        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataValidation.isnotvalidPassword(password.getText().toString())){
                    AppUtilities.displaymessage(New_Password.this,getString(R.string.password)+ " " + getString(R.string.is_invalid));

                }else if(dataValidation.isnotvalidPassword(retype_password.getText().toString())){
                    AppUtilities.displaymessage(New_Password.this,getString(R.string.retype_pass)+ " " + getString(R.string.is_invalid));

                }else if(!password.getText().toString().equals(retype_password.getText().toString())){
                    AppUtilities.displaymessage(New_Password.this,getString(R.string.pass_not_match));

                }else {
                    sendnewpasswordreq();
                }
            }
        });
    }

    public void sendnewpasswordreq(){
        if (!NetworkUtility.isnetworkconected(New_Password.this)) {
            AppUtilities.displaymessage(New_Password.this, getString(R.string.no_network_connection));
        } else {
            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<NewPassword> call = serviceWrapper.UserForgotPasswordCall(userid,password.getText().toString());
            call.enqueue(new Callback<NewPassword>() {
                @Override
                public void onResponse(Call<NewPassword> call, Response<NewPassword> response) {
                    Log.d(TAG,"response :" + response.toString());
                    if (response.body()!=null && response.isSuccessful()){
                        if (response.body().getStatus() == 1){
                            //store user data to share preference
                            Intent intent = new Intent(New_Password.this,home.class);
                            startActivity(intent);

                        }else {
                            AppUtilities.displaymessage(New_Password.this,response.body().getMsg());
                        }
                    }else {
                        AppUtilities.displaymessage(New_Password.this,getString(R.string.failed_request));
                    }
                }

                @Override
                public void onFailure(Call<NewPassword> call, Throwable t) {
                    Log.d(TAG,"failure :" + t.toString());
                    AppUtilities.displaymessage(New_Password.this,getString(R.string.failed_request));
                }
            });
        }
    }
}
