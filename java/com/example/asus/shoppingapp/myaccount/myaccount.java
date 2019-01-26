package com.example.asus.shoppingapp.myaccount;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.AppUtilities;
import com.example.asus.shoppingapp.Utility.Constant;
import com.example.asus.shoppingapp.Utility.SharePreferenceUtil;
import com.example.asus.shoppingapp.Wishlist.WishlistDetails;
import com.example.asus.shoppingapp.cart.CartDetails;
import com.example.asus.shoppingapp.cart.OrderAddress;
import com.example.asus.shoppingapp.cart.OrderAddress_AddNew;

import org.w3c.dom.Text;

/**
 * Created by asus on 29-12-2018.
 */

public class myaccount extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationView navigationView;
    public static Menu mainmenu;
    private DrawerLayout drawer;
    private String TAG = "myaccount";
    private TextView myac_username,myac_email,myac_phone,myac_address,myac_order;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        myac_username = (TextView)findViewById(R.id.myac_username);
        myac_email = (TextView)findViewById(R.id.myac_email);
        myac_phone = (TextView)findViewById(R.id.myac_phone);
        myac_address = (TextView)findViewById(R.id.myac_address);
        myac_order = (TextView)findViewById(R.id.myacc_orderhisory);


        myac_username.setText(SharePreferenceUtil.getInstance().getString(Constant.USER_name));
        myac_email.setText(SharePreferenceUtil.getInstance().getString(Constant.USER_email));
        myac_phone.setText(SharePreferenceUtil.getInstance().getString(Constant.USER_phone));

        myac_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myaccount.this,OrderAddress_AddNew.class);
                startActivity(intent);
            }
        });

        myac_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myaccount.this,OrderHistory.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.myaccount_toolbar_menu,menu);
        mainmenu = menu;
        if (mainmenu!=null){
            AppUtilities.UpdateCartCount(myaccount.this,mainmenu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.e(TAG,"option click "+ item.getTitle());
        if (id==R.id.search){

            SearchManager searchManager = (SearchManager)getSystemService(SEARCH_SERVICE);
            SearchView searchView = (SearchView)mainmenu.findItem(R.id.search).getActionView();
            final EditText searchtext = (EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchtext.setHint(R.string.search_name);

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchtext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH){
                        Log.e(TAG,"search is: "+ searchtext.getText().toString());
                        if(null!=searchtext.getText().toString().trim() && !searchtext.getText().toString().trim().equals("")){

                        }
                    }

                    return false;
                }
            });

        }else if (id == R.id.cart){
            Intent intent = new Intent(this, CartDetails.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.nav_home){

        }else if (id == R.id.nav_my_ac){

        }else if (id == R.id.nav_pro){

        }else if (id == R.id.nav_wishlist){
            Intent intent = new Intent(this, WishlistDetails.class);
            startActivity(intent);

        }else if(id == R.id.nav_logout){

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mainmenu!=null){
            AppUtilities.UpdateCartCount(myaccount.this,mainmenu);
        }
    }
}
