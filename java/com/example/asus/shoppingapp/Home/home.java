package com.example.asus.shoppingapp.Home;

import android.app.SearchManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.AppUtilities;
import com.example.asus.shoppingapp.Utility.Constant;
import com.example.asus.shoppingapp.Utility.NetworkUtility;
import com.example.asus.shoppingapp.Utility.SharePreferenceUtil;
import com.example.asus.shoppingapp.WebServices.ServiceWrapper;
import com.example.asus.shoppingapp.Wishlist.WishlistDetails;
import com.example.asus.shoppingapp.beanResponse.NewProdResponce;
import com.example.asus.shoppingapp.beanResponse.getBannerModel;
import com.example.asus.shoppingapp.beanResponse.getCartDetails;
import com.example.asus.shoppingapp.cart.CartDetails;
import com.example.asus.shoppingapp.cart.Cartitem_Model;
import com.example.asus.shoppingapp.login.SignIn;
import com.example.asus.shoppingapp.myaccount.myaccount;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ss.com.bannerslider.Slider;
//import ss.com.bannerslider.Slider;
//import ss.com.bannerslider.banners.Banner;
//import ss.com.bannerslider.banners.RemoteBanner;
//import ss.com.bannerslider.views.BannerSlider;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Menu mainmenu;
    private NavigationView navigationView;
    private String TAG = "HomeActivity";

    private RecyclerView recycler_bestSelling;
    private ArrayList<BestSelling_Model> bestSellingModelArrayList = new ArrayList<BestSelling_Model>();
    private BestSelling_Model bestSellingmodel;
    private Bestselling_Adapter bestsellingAdapter;

    private RecyclerView recyclerView_newProduct;
    private ArrayList<NewProduct_Model> newProductModelArrayList = new ArrayList<NewProduct_Model>();
    private NewProduct_Model newProductModel;
    private NewProduct_Adapter newProductAdapter;

    private RecyclerView recyclerView_trending;
    private ArrayList<BestSelling_Model> trendingModelArraylist = new ArrayList<BestSelling_Model>();
    private Bestselling_Adapter trendingadapter;

    private RecyclerView recyclerView_conditional;
    private ArrayList<BestSelling_Model> conditionalModelArraylist = new ArrayList<BestSelling_Model>();
    private Bestselling_Adapter conditionaladapter;

    //private BannerSlider bannerSlider;
    //private List<Banner> remoteBanners=new ArrayList<>();

   // private Slider slider;
    //private ArrayList<String> bannerimage = new ArrayList<>();
   // private BannerSlider bannerSlider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        //slider = (Slider)findViewById(R.id.banner_slider1);

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //not yet completed image slider
     //   bannerimage.add("https://abirdas33.000webhostapp.com/Iphone%20x.jpg");
       // bannerimage.add("https://abirdas33.000webhostapp.com/abir.jpg");



        //for best selling
        recycler_bestSelling = (RecyclerView) findViewById(R.id.best_sell);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_bestSelling.setLayoutManager(mlayoutManager);
        recycler_bestSelling.setItemAnimator(new DefaultItemAnimator());
        bestsellingAdapter = new Bestselling_Adapter(this, bestSellingModelArrayList, getScreenWidth());
        recycler_bestSelling.setAdapter(bestsellingAdapter);

        //for trending product
        recyclerView_trending = (RecyclerView) findViewById(R.id.trending);
        LinearLayoutManager mlayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_trending.setLayoutManager(mlayoutManager2);
        recyclerView_trending.setItemAnimator(new DefaultItemAnimator());
        trendingadapter = new Bestselling_Adapter(this, trendingModelArraylist, getScreenWidth());
        recyclerView_trending.setAdapter(trendingadapter);

        //for new product
        recyclerView_newProduct = (RecyclerView) findViewById(R.id.new_pod);
        LinearLayoutManager mlayoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_newProduct.setLayoutManager(mlayoutManager1);
        recyclerView_newProduct.setItemAnimator(new DefaultItemAnimator());
        newProductAdapter = new NewProduct_Adapter(this, newProductModelArrayList, getScreenWidth());
        recyclerView_newProduct.setAdapter(newProductAdapter);

        //for conditional product
        recyclerView_conditional = (RecyclerView) findViewById(R.id.conditional);
        LinearLayoutManager mlayoutManager3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_conditional.setLayoutManager(mlayoutManager3);
        recyclerView_conditional.setItemAnimator(new DefaultItemAnimator());
        conditionaladapter = new Bestselling_Adapter(this, conditionalModelArraylist, getScreenWidth());
        recyclerView_conditional.setAdapter(conditionaladapter);

        NewProdResponse();
        BestSellingResponse();
        TrendindResponse();
        ConditionalProdResponse();
       // getbannerimg();
        //slider = findViewById(R.id.banner_slider1);
       // slider.setAdapter(new MainSliderAdapter());


    }

   /* public void getbannerimg(){
        if (!NetworkUtility.isnetworkconected(home.this)){
            AppUtilities.displaymessage(home.this,  getString(R.string.no_network_connection));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<getBannerModel> call = service.getBannerModelCall("1234");
            call.enqueue(new Callback<getBannerModel>() {
                @Override
                public void onResponse(Call<getBannerModel> call, Response<getBannerModel> response) {
                    Log.e(TAG, " banner response is "+ response.body().getInformation().toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1) {
                            if (response.body().getInformation().size() > 0) {

                                for (int i=0; i<response.body().getInformation().size(); i++) {
                                    remoteBanners.add(new RemoteBanner(response.body().getInformation().get(i).getImgurl()));

                                }


                            }else {

                                remoteBanners.add(new RemoteBanner("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg"
                                ));
                                remoteBanners.add(new RemoteBanner("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg"
                                ));
                            }

                            bannerSlider.setBanners(remoteBanners);
                        }else {
                            remoteBanners.add(new RemoteBanner("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg"
                            ));
                            remoteBanners.add(new RemoteBanner("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg"
                            ));
                            bannerSlider.setBanners(remoteBanners);
                        }
                    }
                }

                @Override
                public void onFailure(Call<getBannerModel> call, Throwable t) {
                    Log.e(TAG, "fail banner ads "+ t.toString());

                }
            });
        }

    }*/



    public void NewProdResponse() {
        if (!NetworkUtility.isnetworkconected(home.this)) {
            AppUtilities.displaymessage(home.this, getString(R.string.no_network_connection));
        } else {

            ServiceWrapper service = new ServiceWrapper(null);
            Call<NewProdResponce> call = service.getNewProductRes("1345");
            call.enqueue(new Callback<NewProdResponce>() {
                @Override
                public void onResponse(Call<NewProdResponce> call, Response<NewProdResponce> response) {
                    Log.e(TAG, "response is " + response.body().getInformation().toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().size() > 0) {
                                newProductModelArrayList.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {
                                    newProductModel = new NewProduct_Model(response.body().getInformation().get(i).getId(),
                                            response.body().getInformation().get(i).getName(),
                                            response.body().getInformation().get(i).getImgUrl());
                                    newProductModelArrayList.add(newProductModel);
                                }
                                newProductAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.e(TAG, "failed to get new product" + response.body().getMsg());
                            // AppUtilities.displaymessage(home.this,response.body().getMsg());
                        }
                    } else {
                        // AppUtilities.displaymessage(home.this,getString(R.string.failed_request));
                    }
                }

                @Override
                public void onFailure(Call<NewProdResponce> call, Throwable t) {

                    Log.e(TAG, "fail new product " + t.toString());
                }
            });
        }
    }

    public void BestSellingResponse() {
        if (!NetworkUtility.isnetworkconected(home.this)) {
            AppUtilities.displaymessage(home.this, getString(R.string.no_network_connection));
        } else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<NewProdResponce> call = service.getBestSellingRes("1345");
            call.enqueue(new Callback<NewProdResponce>() {
                @Override
                public void onResponse(Call<NewProdResponce> call, Response<NewProdResponce> response) {
                    Log.e(TAG, "response is " + response.body().getInformation().toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().size() > 0) {
                                bestSellingModelArrayList.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {
                                    bestSellingModelArrayList.add(new BestSelling_Model(response.body().getInformation().get(i).getId(),
                                            response.body().getInformation().get(i).getName(),
                                            response.body().getInformation().get(i).getImgUrl(),
                                            response.body().getInformation().get(i).getPrice(),
                                            response.body().getInformation().get(i).getMrp(),
                                            response.body().getInformation().get(i).getRating()));
                                }
                                bestsellingAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e(TAG, "failed to get new product" + response.body().getMsg());
                            // AppUtilities.displaymessage(home.this,response.body().getMsg());
                        }
                    } else {
                        Log.e(TAG, "failed to get new product" + response.body().getMsg());
                        // AppUtilities.displaymessage(home.this,response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<NewProdResponce> call, Throwable t) {
                    Log.e(TAG, "fail new product " + t.toString());
                }
            });
        }
    }

    public void TrendindResponse() {
        if (!NetworkUtility.isnetworkconected(home.this)) {
            AppUtilities.displaymessage(home.this, getString(R.string.no_network_connection));
        } else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<NewProdResponce> call = service.getTrendingProdRes("1345");
            call.enqueue(new Callback<NewProdResponce>() {
                @Override
                public void onResponse(Call<NewProdResponce> call, Response<NewProdResponce> response) {
                    Log.e(TAG, "response is " + response.body().getInformation().toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().size() > 0) {
                                trendingModelArraylist.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {
                                    trendingModelArraylist.add(new BestSelling_Model(response.body().getInformation().get(i).getId(),
                                            response.body().getInformation().get(i).getName(),
                                            response.body().getInformation().get(i).getImgUrl(),
                                            response.body().getInformation().get(i).getPrice(),
                                            response.body().getInformation().get(i).getMrp(),
                                            response.body().getInformation().get(i).getRating()));
                                }
                                trendingadapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e(TAG, "failed to get new product" + response.body().getMsg());
                            // AppUtilities.displaymessage(home.this,response.body().getMsg());
                        }
                    } else {
                        Log.e(TAG, "failed to get new product" + response.body().getMsg());
                        // AppUtilities.displaymessage(home.this,response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<NewProdResponce> call, Throwable t) {
                    Log.e(TAG, "fail new product " + t.toString());
                }
            });
        }
    }

    public void ConditionalProdResponse() {
        if (!NetworkUtility.isnetworkconected(home.this)) {
            AppUtilities.displaymessage(home.this, getString(R.string.no_network_connection));
        } else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<NewProdResponce> call = service.getConditionalProdRes("1345");
            call.enqueue(new Callback<NewProdResponce>() {
                @Override
                public void onResponse(Call<NewProdResponce> call, Response<NewProdResponce> response) {
                    Log.e(TAG, "response is " + response.body().getInformation().toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().size() > 0) {
                                conditionalModelArraylist.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {
                                    conditionalModelArraylist.add(new BestSelling_Model(response.body().getInformation().get(i).getId(),
                                            response.body().getInformation().get(i).getName(),
                                            response.body().getInformation().get(i).getImgUrl(),
                                            response.body().getInformation().get(i).getPrice(),
                                            response.body().getInformation().get(i).getMrp(),
                                            response.body().getInformation().get(i).getRating()));
                                }
                                conditionaladapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e(TAG, "failed to get new product" + response.body().getMsg());
                            // AppUtilities.displaymessage(home.this,response.body().getMsg());
                        }
                    } else {
                        Log.e(TAG, "failed to get new product" + response.body().getMsg());
                        // AppUtilities.displaymessage(home.this,response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<NewProdResponce> call, Throwable t) {
                    Log.e(TAG, "fail new product " + t.toString());
                }
            });
        }
    }



    public int getScreenWidth() {
        int width = 100;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.myaccount_toolbar_menu,menu);
        mainmenu = menu;
        if (mainmenu!=null){
            AppUtilities.UpdateCartCount(home.this,mainmenu);
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
            Intent intent = new Intent(home.this, CartDetails.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.nav_home){

        }else if (id == R.id.nav_my_ac){
            Intent intent = new Intent(this, myaccount.class);
            startActivity(intent);

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
            AppUtilities.UpdateCartCount(home.this,mainmenu);
            getusercartdetails();
        }
    }


    public void getusercartdetails(){

        if (!NetworkUtility.isnetworkconected(home.this)) {
            AppUtilities.displaymessage(home.this, getString(R.string.no_network_connection));
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


                            SharePreferenceUtil.getInstance().saveInt(Constant.CART_ITEM_COUNT,response.body().getInformation().getProdDetails().size());
                            AppUtilities.UpdateCartCount(home.this,mainmenu);

                        }
                    }
                }

                @Override
                public void onFailure(Call<getCartDetails> call, Throwable t) {
                    Log.e(TAG, "fail to get user cart list " + t.toString());
                   // AppUtilities.displaymessage(home.this,getString(R.string.fail_to_get_cart));
                }
            });
        }
    }

}

