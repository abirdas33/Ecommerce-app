package com.example.asus.shoppingapp.cart;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;

import com.example.asus.shoppingapp.Home.Bestselling_Adapter;
import com.example.asus.shoppingapp.ProductPreview.productDetails;
import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.AppUtilities;
import com.example.asus.shoppingapp.Utility.Constant;
import com.example.asus.shoppingapp.Utility.NetworkUtility;
import com.example.asus.shoppingapp.Utility.SharePreferenceUtil;
import com.example.asus.shoppingapp.WebServices.ServiceWrapper;
import com.example.asus.shoppingapp.Wishlist.WishlistDetails;
import com.example.asus.shoppingapp.beanResponse.getCartDetails;
import com.google.gson.Gson;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by asus on 01-09-2018.
 */

public class CartDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    public static Menu mainmenu;
    private DrawerLayout drawer;
    private String TAG = "cartdetails";
    private RecyclerView  recycler_cartitem;
    private TextView cartcount,continuetn;
    public TextView cart_totalamount;
    private Cart_Adapter cartAdapter;
    private ArrayList<Cartitem_Model> cartitemModels = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartdetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recycler_cartitem = (RecyclerView)findViewById(R.id.recycler_cartitem);
        cartcount = (TextView)findViewById(R.id.cart_count);
        cart_totalamount = (TextView)findViewById(R.id.cart_totalamount);
        continuetn = (TextView)findViewById(R.id.continuetn);


        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recycler_cartitem.setLayoutManager(mlayoutManager);
        recycler_cartitem.setItemAnimator(new DefaultItemAnimator());
        cartAdapter = new Cart_Adapter(this,cartitemModels);
        recycler_cartitem.setAdapter(cartAdapter);



        getusercartdetails();
        continuetn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = cart_totalamount.getText().toString().replace("$ ","");
                if(!temp.equalsIgnoreCase("") && Float.valueOf(temp)>0){
                    //go to order summary page
                    Intent intent = new Intent(CartDetails.this,Order_Summary.class);
                    startActivity(intent);
                }else{
                    AppUtilities.displaymessage(CartDetails.this,getString(R.string.no_prod_into_cart));
                }
            }
        });

    }

    public void getusercartdetails(){

        if (!NetworkUtility.isnetworkconected(CartDetails.this)) {
            AppUtilities.displaymessage(CartDetails.this, getString(R.string.no_network_connection));
        } else {
            // Log.e(TAG,SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<getCartDetails> call = service.getCartDetailsCall("1234", SharePreferenceUtil.getInstance().getString(Constant.QUOTE_ID),SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            call.enqueue(new Callback<getCartDetails>() {
                @Override
                public void onResponse(Call<getCartDetails> call, Response<getCartDetails> response) {

                    Log.e(TAG, "response is " + response.body() +  "---- " + new Gson().toJson(response.body()));
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            cart_totalamount.setText( "$ " + response.body().getInformation().getTotalprice());
                            cartcount.setText(getString(R.string.you_have)+" " + String.valueOf(response.body().getInformation().getProdDetails().size() + " " + getString(R.string.product_in_cart)));

                            SharePreferenceUtil.getInstance().saveInt(Constant.CART_ITEM_COUNT,response.body().getInformation().getProdDetails().size());
                            AppUtilities.UpdateCartCount(CartDetails.this,mainmenu);

                            cartitemModels.clear();
                            for (int i=0;i<response.body().getInformation().getProdDetails().size();i++){
                                cartitemModels.add(new Cartitem_Model(response.body().getInformation().getProdDetails().get(i).getId(),
                                        response.body().getInformation().getProdDetails().get(i).getName(),
                                        response.body().getInformation().getProdDetails().get(i).getImgUrl(),
                                        response.body().getInformation().getProdDetails().get(i).getMrp(),
                                        response.body().getInformation().getProdDetails().get(i).getPrice(),
                                        response.body().getInformation().getProdDetails().get(i).getQty()));


                            }
                            cartAdapter.notifyDataSetChanged();


                        }else{
                            AppUtilities.displaymessage(CartDetails.this,response.body().getMsg());
                        }
                    }else{
                        AppUtilities.displaymessage(CartDetails.this,getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<getCartDetails> call, Throwable t) {
                    Log.e(TAG, "fail to get user cart list " + t.toString());
                    AppUtilities.displaymessage(CartDetails.this,getString(R.string.fail_to_get_cart));
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.myaccount_toolbar_menu,menu);
        mainmenu = menu;
        if (mainmenu!=null){
            AppUtilities.UpdateCartCount(CartDetails.this,mainmenu);
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
            AppUtilities.UpdateCartCount(CartDetails.this,mainmenu);
        }
    }
}
