package com.example.asus.shoppingapp.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.asus.shoppingapp.beanResponse.userSignin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity {
    private String TAG = "SigninActivity";
    private TextView skip, forget_password, login;
    LinearLayout signup;
    private EditText phone_no, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Log.e(TAG, "alert sign_in activity");
        skip = (TextView) findViewById(R.id.btn_skp);
        forget_password = (TextView) findViewById(R.id.forget_password);
        login = (TextView) findViewById(R.id.login);
        signup = (LinearLayout) findViewById(R.id.layout_sign_up);
        phone_no = (EditText) findViewById(R.id.phone_no);
        password = (EditText) findViewById(R.id.pass);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, home.class);
                startActivity(intent);
                finish();
            }
        });

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, ForgetPassword.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataValidation.isnotvalidphone(phone_no.getText().toString())) {
                    AppUtilities.displaymessage(SignIn.this, getString(R.string.phone_no) + " " + getString(R.string.is_invalid));

                } else if (dataValidation.isnotvalidPassword(password.getText().toString())) {
                    AppUtilities.displaymessage(SignIn.this, getString(R.string.password) + " " + getString(R.string.is_invalid));
                } else {
                    senduserlogindata();
                }
            }
        });
    }

    public void senduserlogindata() {
        if (!NetworkUtility.isnetworkconected(SignIn.this)) {
            AppUtilities.displaymessage(SignIn.this, getString(R.string.no_network_connection));
        } else {
            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<userSignin> call = serviceWrapper.UserSignInCall(phone_no.getText().toString(),password.getText().toString());
            call.enqueue(new Callback<userSignin>() {
                @Override
                public void onResponse(Call<userSignin> call, Response<userSignin> response) {
                    Log.d(TAG,"response :" + response.toString());
                    if (response.body()!=null && response.isSuccessful()){
                        if (response.body().getStatus() == 1){
                            //store user data to share preference
                            SharePreferenceUtil.getInstance().saveString(Constant.USER_DATA,response.body().getInformation().getUserId());
                            SharePreferenceUtil.getInstance().saveString(Constant.USER_name,response.body().getInformation().getFullname());
                            SharePreferenceUtil.getInstance().saveString(Constant.USER_email,response.body().getInformation().getEmail());
                            SharePreferenceUtil.getInstance().saveString(Constant.USER_phone,response.body().getInformation().getPhone());

                            Intent intent = new Intent(SignIn.this,home.class);
                            startActivity(intent);

                        }else {
                            AppUtilities.displaymessage(SignIn.this,response.body().getMsg());
                        }
                    }else {
                        AppUtilities.displaymessage(SignIn.this,getString(R.string.failed_request));
                    }
                }

                @Override
                public void onFailure(Call<userSignin> call, Throwable t) {
                    Log.d(TAG,"failure :" + t.toString());
                    AppUtilities.displaymessage(SignIn.this,getString(R.string.failed_request));
                }
            });
        }

    }
}
