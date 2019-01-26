package com.example.asus.shoppingapp.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.asus.shoppingapp.Home.home;
import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.SharePreferenceUtil;
import com.example.asus.shoppingapp.login.SignIn;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    public void init(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //if user is registered then show home else show login screen
                if(SharePreferenceUtil.getInstance().getString("register_user").equalsIgnoreCase("")){
                    Intent intent = new Intent(splash.this, SignIn.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(splash.this,home.class );
                    startActivity(intent);
                }
                finish();
            }
        },5000);
    }
}
