package com.example.asus.shoppingapp.ProductPreview;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.shoppingapp.Home.BestSelling_Model;
import com.example.asus.shoppingapp.Home.Bestselling_Adapter;
import com.example.asus.shoppingapp.Home.home;
import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.AppUtilities;
import com.example.asus.shoppingapp.Utility.Constant;
import com.example.asus.shoppingapp.Utility.NetworkUtility;
import com.example.asus.shoppingapp.Utility.SharePreferenceUtil;
import com.example.asus.shoppingapp.WebServices.ServiceWrapper;
import com.example.asus.shoppingapp.Wishlist.WishlistDetails;
import com.example.asus.shoppingapp.beanResponse.AddtoCart;
import com.example.asus.shoppingapp.beanResponse.NewProdResponce;
import com.example.asus.shoppingapp.beanResponse.ProductDetailsResponse;
import com.example.asus.shoppingapp.cart.CartDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by asus on 25-08-2018.
 */

public class productDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView navigationView;
    private Menu mainmenu;
    private DrawerLayout drawer;
    private String TAG = "product details";
    private String prod_id = "";
    private TextView prod_name,prod_price,prod_oldprice,prod_rating_count,prod_stock,prod_qty;
    private AppCompatRatingBar prod_rating;
    private ImageView add_to_cart,add_to_wishlist;

    //related product
    private RecyclerView recycler_related;
    private ArrayList<BestSelling_Model> relatedModelArrayList = new ArrayList<BestSelling_Model>();
    private Bestselling_Adapter relatedAdapter;

    //overview and review tab layout
    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    public String prod_overview = "";
    public String prod_fulldetails = "";
    public String prod_review = "";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Intent intent = getIntent();
        prod_id = intent.getExtras().getString("prod_id");

        prod_name=(TextView)findViewById(R.id.prod_name);
        prod_price=(TextView)findViewById(R.id.new_price);
        prod_oldprice=(TextView)findViewById(R.id.old_price);
        prod_rating_count=(TextView)findViewById(R.id.rating_count);
        prod_stock=(TextView)findViewById(R.id.stock);
        prod_qty=(TextView)findViewById(R.id.prod_qty_value);
        prod_rating = (AppCompatRatingBar)findViewById(R.id.prod_rating);
        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        frameLayout = (FrameLayout)findViewById(R.id.fraq_container);
        add_to_cart = (ImageView)findViewById(R.id.add_to_cart);
        add_to_wishlist = (ImageView)findViewById(R.id.add_to_wishlist);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.overview)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.details)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.review)));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment FragPrev = fragmentManager.findFragmentByTag("com.example.asus.shoppingapp.ProductPreview.tabfragment");
                if (FragPrev!=null){
                    fragmentTransaction.remove(FragPrev);
                }
                if (tab.getPosition()==0){
                    Overview overview = new Overview();
                    fragmentTransaction.add(R.id.fraq_container,overview,"com.example.asus.shoppingapp.ProductPreview.tabfragment");
                    fragmentTransaction.commit();
                }else if (tab.getPosition()==1){
                    Details details = new Details();
                    fragmentTransaction.add(R.id.fraq_container,details,"com.example.asus.shoppingapp.ProductPreview.tabfragment");
                    fragmentTransaction.commit();
                }else if (tab.getPosition()==2){
                    Review review = new Review();
                    fragmentTransaction.add(R.id.fraq_container,review,"com.example.asus.shoppingapp.ProductPreview.tabfragment");
                    fragmentTransaction.commit();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        getproductdetails();

        recycler_related = (RecyclerView) findViewById(R.id.related_prod);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_related.setLayoutManager(mlayoutManager);
        recycler_related.setItemAnimator(new DefaultItemAnimator());
        relatedAdapter = new Bestselling_Adapter(this, relatedModelArrayList, getScreenWidth());
        recycler_related.setAdapter(relatedAdapter);

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store prod_id with user_id on server and get quate id as response and store it on sharepreference
               if ( !prod_price.getText().toString().equals("") && prod_price.getText().toString()!=null){
                   addtocartapi();
               }
            }
        });

        add_to_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addtowishlistapi();
            }
        });

    }

    public int getScreenWidth() {
        int width = 100;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;
    }

    public void getproductdetails() {
        if (!NetworkUtility.isnetworkconected(productDetails.this)) {
            AppUtilities.displaymessage(productDetails.this, getString(R.string.no_network_connection));
        } else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<ProductDetailsResponse> call = service.getProductDetails("1345",prod_id);
            call.enqueue(new Callback<ProductDetailsResponse>() {
                @Override
                public void onResponse(Call<ProductDetailsResponse> call, Response<ProductDetailsResponse> response) {
                    Log.e(TAG, "response is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            if (response.body().getInformation().getDetails().getName()!=null){
                                prod_name.setText(response.body().getInformation().getDetails().getName());
                                prod_oldprice.setText(response.body().getInformation().getDetails().getMrp());
                                prod_price.setText(response.body().getInformation().getDetails().getPrice());
                                prod_rating_count.setText(response.body().getInformation().getDetails().getRatingCount());
                                prod_rating.setRating(response.body().getInformation().getDetails().getRating());
                                prod_qty.setText("1");
                                if(response.body().getInformation().getDetails().getStock()>0){
                                    prod_stock.setText(getString(R.string.in_stock));
                                }else{
                                    prod_stock.setText(getString(R.string.out_of_stock));
                                }


                                if (response.body().getInformation().getRelatedprod().size()>0){
                                    relatedModelArrayList.clear();
                                    for (int i = 0; i < response.body().getInformation().getRelatedprod().size(); i++) {
                                        relatedModelArrayList.add(new BestSelling_Model(
                                                response.body().getInformation().getRelatedprod().get(i).getId(),
                                                response.body().getInformation().getRelatedprod().get(i).getName(),
                                                response.body().getInformation().getRelatedprod().get(i).getImgUrl(),
                                                response.body().getInformation().getRelatedprod().get(i).getMrp(),
                                                response.body().getInformation().getRelatedprod().get(i).getPrice(),
                                                String.valueOf(response.body().getInformation().getRelatedprod().get(i).getRating())));
                                    }
                                    relatedAdapter.notifyDataSetChanged();
                                }//(String prod_id,String prod_name,String img_url,String old_price,String price,String rating){
                                prod_overview = response.body().getInformation().getDetails().getDesc();
                                prod_fulldetails = response.body().getInformation().getDetails().getFulldetails();
                                prod_review = response.body().getInformation().getReview();

                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                Overview overview = new Overview();
                                fragmentTransaction.add(R.id.fraq_container,overview,"com.example.asus.shoppingapp.ProductPreview.tabfragment");
                                fragmentTransaction.commit();

                            }
                        }else {
                            Log.e(TAG, "failed to get new product" + response.body().getMsg());
                        }
                    }else{
                        Log.e(TAG, "failed to get new product" + response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<ProductDetailsResponse> call, Throwable t) {
                    Log.e(TAG, "fail to get product details " + t.toString());
                }
            });
        }
    }

    public void addtocartapi(){

        if (!NetworkUtility.isnetworkconected(productDetails.this)) {
            AppUtilities.displaymessage(productDetails.this, getString(R.string.no_network_connection));
        } else {

            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddtoCart> call = service.addtocartCall("12345",prod_id,SharePreferenceUtil.getInstance().getString(Constant.USER_DATA),prod_price.getText().toString());
            call.enqueue(new Callback<AddtoCart>() {
                @Override
                public void onResponse(Call<AddtoCart> call, Response<AddtoCart> response) {

                    Log.e(TAG, "response is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            AppUtilities.displaymessage(productDetails.this,getString(R.string.add_to_cart));
                            SharePreferenceUtil.getInstance().saveString(Constant.QUOTE_ID,response.body().getInformation().getQouteId());
                            SharePreferenceUtil.getInstance().saveInt(Constant.CART_ITEM_COUNT, response.body().getInformation().getCartCount());
                            AppUtilities.UpdateCartCount(productDetails.this,mainmenu);


                        }else{
                            AppUtilities.displaymessage(productDetails.this,getString(R.string.fail_to_add_to_cart));
                        }
                    }else{
                        AppUtilities.displaymessage(productDetails.this,getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<AddtoCart> call, Throwable t) {
                    Log.e(TAG, "fail to add to cart " + t.toString());
                    AppUtilities.displaymessage(productDetails.this,getString(R.string.fail_to_add_to_cart));

                }
            });
        }
    }


    public void addtowishlistapi() {

        if (!NetworkUtility.isnetworkconected(productDetails.this)) {
            AppUtilities.displaymessage(productDetails.this, getString(R.string.no_network_connection));
        } else {
            // Log.e(TAG,SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddtoCart> call = service.addtocartCall("12345", prod_id, SharePreferenceUtil.getInstance().getString(Constant.USER_DATA),prod_price.getText().toString());
            call.enqueue(new Callback<AddtoCart>() {
                @Override
                public void onResponse(Call<AddtoCart> call, Response<AddtoCart> response) {
                    Log.e(TAG, "response is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            AppUtilities.displaymessage(productDetails.this,getString(R.string.add_to_wishlist));
                        }else{
                            AppUtilities.displaymessage(productDetails.this,getString(R.string.fail_to_add_to_user_wishlist));
                        }
                    }else{
                        AppUtilities.displaymessage(productDetails.this,getString(R.string.network_error));
                    }
                }
                @Override
                public void onFailure(Call<AddtoCart> call, Throwable t) {
                    Log.e(TAG, "fail to add to wishlist " + t.toString());
                    AppUtilities.displaymessage(productDetails.this,getString(R.string.fail_to_add_to_cart));

                }
            });

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.myaccount_toolbar_menu,menu);
        mainmenu = menu;
        if (mainmenu!=null){
            AppUtilities.UpdateCartCount(productDetails.this,mainmenu);
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
            Intent intent = new Intent(productDetails.this, CartDetails.class);
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
            AppUtilities.UpdateCartCount(productDetails.this,mainmenu);
        }
    }
}
