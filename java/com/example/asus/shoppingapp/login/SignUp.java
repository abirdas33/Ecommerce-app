package com.example.asus.shoppingapp.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.asus.shoppingapp.beanResponse.NewUserRegistration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    private String TAG = "SignupActivity";
    EditText fullname,user_email,phone_no,username,password,retype_pass;
    TextView create_ac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.e(TAG,"alert sign_up activity");

        fullname = (EditText)findViewById(R.id.fullname);
        user_email= (EditText)findViewById(R.id.user_email);
        phone_no = (EditText)findViewById(R.id.phone_no);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.pass);
        retype_pass = (EditText)findViewById(R.id.retype_pass);
        create_ac = (TextView) findViewById(R.id.create_ac);

        create_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataValidation.isnotvalidfullname(fullname.getText().toString())){

                    AppUtilities.displaymessage(SignUp.this,getString(R.string.full_name)+ " " + getString(R.string.is_invalid));

                }else if(TextUtils.isEmpty(user_email.getText().toString())){
                    AppUtilities.displaymessage(SignUp.this,getString(R.string.email)+ " " + getString(R.string.is_invalid));

                }else if(dataValidation.isnotvalidphone(phone_no.getText().toString())){
                    AppUtilities.displaymessage(SignUp.this,getString(R.string.phone_no)+ " " + getString(R.string.is_invalid));

                }else if(dataValidation.isnotvalidaddress(username.getText().toString())){
                    AppUtilities.displaymessage(SignUp.this,getString(R.string.username)+ " " + getString(R.string.is_invalid));

                }else if(dataValidation.isnotvalidPassword(password.getText().toString())){
                    AppUtilities.displaymessage(SignUp.this,getString(R.string.password)+ " " + getString(R.string.is_invalid));

                }else if(dataValidation.isnotvalidPassword(retype_pass.getText().toString())){
                    AppUtilities.displaymessage(SignUp.this,getString(R.string.retype_pass)+ " " + getString(R.string.is_invalid));

                }else if(!password.getText().toString().equals(retype_pass.getText().toString())){
                    AppUtilities.displaymessage(SignUp.this,getString(R.string.pass_not_match));

                }else {
                    //neywork connection and progress dailog
                    //call retrofit method
                    sendnewRegistrationReq();
                }
            }
        });
    }

    public void sendnewRegistrationReq(){
        if (!NetworkUtility.isnetworkconected(SignUp.this)){
            AppUtilities.displaymessage(SignUp.this,getString(R.string.no_network_connection));
        }else {
            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<NewUserRegistration> call = serviceWrapper.newUserRegistrationCall(fullname.getText().toString(),user_email.getText().toString(),phone_no.getText().toString(),username.getText().toString(),password.getText().toString());
            call.enqueue(new Callback<NewUserRegistration>() {
                @Override
                public void onResponse(Call<NewUserRegistration> call, Response<NewUserRegistration> response) {
                    Log.d(TAG,"response :" + response.toString());
                    if (response.body()!=null && response.isSuccessful()){
                        if (response.body().getStatus() == 1){
                            //store user data to share preference
                            SharePreferenceUtil.getInstance().saveString(Constant.USER_DATA,response.body().getInformation().getUserId());
                            SharePreferenceUtil.getInstance().saveString(Constant.USER_name,response.body().getInformation().getFullname());
                            SharePreferenceUtil.getInstance().saveString(Constant.USER_email,response.body().getInformation().getEmail());
                            SharePreferenceUtil.getInstance().saveString(Constant.USER_phone,response.body().getInformation().getPhone());

                            Intent intent = new Intent(SignUp.this,home.class);
                            startActivity(intent);

                        }else {
                            AppUtilities.displaymessage(SignUp.this,response.body().getMsg());
                        }
                    }else {
                        AppUtilities.displaymessage(SignUp.this,getString(R.string.failed_request));
                    }
                }

                @Override
                public void onFailure(Call<NewUserRegistration> call, Throwable t) {
                    Log.d(TAG,"failure :" + t.toString());
                    AppUtilities.displaymessage(SignUp.this,getString(R.string.failed_request));
                }
            });
        }
    }
}
