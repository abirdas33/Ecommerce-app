package com.example.asus.shoppingapp.cart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.shoppingapp.Home.Bestselling_Adapter;
import com.example.asus.shoppingapp.ProductPreview.productDetails;
import com.example.asus.shoppingapp.R;
import com.example.asus.shoppingapp.Utility.AppUtilities;
import com.example.asus.shoppingapp.Utility.Constant;
import com.example.asus.shoppingapp.Utility.NetworkUtility;
import com.example.asus.shoppingapp.Utility.SharePreferenceUtil;
import com.example.asus.shoppingapp.WebServices.ServiceWrapper;
import com.example.asus.shoppingapp.beanResponse.AddtoCart;
import com.example.asus.shoppingapp.beanResponse.editCartItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.asus.shoppingapp.cart.CartDetails.mainmenu;

/**
 * Created by asus on 13-12-2018.
 */

public class Cart_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Cartitem_Model> cartitem_models;
    private Context mcontext;
    private String TAG = "cart Adapter";

    public Cart_Adapter(Context context, List<Cartitem_Model> cartitem_models){
        this.cartitem_models = cartitem_models;
        this.mcontext = context;
    }

    private class cartItemHolder extends RecyclerView.ViewHolder {
        ImageView prod_img,prod_delete,addtowishlist;
        TextView prod_name,old_price,new_price;
        RatingBar ratingBar;
        CardView cardView;
        EditText prod_qty;

        public cartItemHolder(View itemView) {
            super(itemView);
            prod_img = (ImageView)itemView.findViewById(R.id.prod_img);
            prod_name = (TextView)itemView.findViewById(R.id.prod_name);
            old_price = (TextView)itemView.findViewById(R.id.old_price);
            new_price = (TextView)itemView.findViewById(R.id.new_price);
            ratingBar = (RatingBar)itemView.findViewById(R.id.prod_rating);
            cardView = (CardView)itemView.findViewById(R.id.cardview);
            prod_delete =(ImageView)itemView.findViewById(R.id.cart_delete);
            prod_qty = (EditText)itemView.findViewById(R.id.prod_qty);
            addtowishlist = (ImageView)itemView.findViewById(R.id.add_to_wishlist);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cartdetails_item,parent,false);
        return new cartItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Cartitem_Model model = cartitem_models.get(position);
        ((cartItemHolder)holder).prod_name.setText(model.getProd_name());
        ((cartItemHolder)holder).old_price.setText(model.getOld_price());
        ((cartItemHolder)holder).new_price.setText(model.getPrice());
        ((cartItemHolder)holder).prod_qty.setText(model.getQty());

        Glide
                .with(mcontext)
                .load(model.getImg_url())
                .into(((cartItemHolder) holder).prod_img);


        ((cartItemHolder)holder).addtowishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtowishlistapi(model.getProd_id(),model.getPrice());
            }
        });

        ((cartItemHolder)holder).prod_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, productDetails.class);
                intent.putExtra("prod_id",model.getProd_id());
                mcontext.startActivity(intent);
            }
        });

        ((cartItemHolder)holder).prod_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, productDetails.class);
                intent.putExtra("prod_id",model.getProd_id());
                mcontext.startActivity(intent);
            }
        });

        ((cartItemHolder)holder).prod_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteproduct(model.getProd_id());
            }
        });

        ((cartItemHolder)holder).prod_qty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_SEND){
                    if (!((cartItemHolder)holder).prod_qty.getText().toString().trim().equals("") || !((cartItemHolder)holder).prod_qty.getText().toString().trim().equals("0")){
                        updateCartQty(model.getProd_id(),((cartItemHolder)holder).prod_qty.getText().toString().trim());
                    }
                }

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartitem_models.size();
    }

    public void addtowishlistapi(String prod_id,String prod_price) {

        if (!NetworkUtility.isnetworkconected(mcontext)) {
            AppUtilities.displaymessage(mcontext, mcontext.getString(R.string.no_network_connection));
        } else {
            // Log.e(TAG,SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddtoCart> call = service.addtocartCall("12345", prod_id, SharePreferenceUtil.getInstance().getString(Constant.USER_DATA),prod_price);
            call.enqueue(new Callback<AddtoCart>() {
                @Override
                public void onResponse(Call<AddtoCart> call, Response<AddtoCart> response) {
                    Log.e(TAG, "response is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            AppUtilities.displaymessage(mcontext,mcontext.getString(R.string.add_to_wishlist));
                        }else{
                            AppUtilities.displaymessage(mcontext,mcontext.getString(R.string.fail_to_add_to_user_wishlist));
                        }
                    }else{
                        AppUtilities.displaymessage(mcontext,mcontext.getString(R.string.network_error));
                    }
                }
                @Override
                public void onFailure(Call<AddtoCart> call, Throwable t) {
                    Log.e(TAG, "fail to add to cart " + t.toString());
                    AppUtilities.displaymessage(mcontext,mcontext.getString(R.string.fail_to_add_to_cart));

                }
            });

        }
    }

    public void deleteproduct(String prod_id){
        if (!NetworkUtility.isnetworkconected(mcontext)) {
            AppUtilities.displaymessage(mcontext, mcontext.getString(R.string.no_network_connection));
        } else {
            // Log.e(TAG,SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddtoCart> call = service.deletecartprodCall("12345", prod_id, SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            call.enqueue(new Callback<AddtoCart>() {
                @Override
                public void onResponse(Call<AddtoCart> call, Response<AddtoCart> response) {
                    Log.e(TAG, "response is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            AppUtilities.displaymessage(mcontext,response.body().getMsg());
                            ((CartDetails)mcontext).getusercartdetails();
                            //update cart count
                         //   SharePreferenceUtil.getInstance().saveInt(Constant.CART_ITEM_COUNT, SharePreferenceUtil.getInstance().getInt(Constant.CART_ITEM_COUNT)-1);
                         //   AppUtilities.UpdateCartCount(mcontext,mainmenu);

                        }else{
                            AppUtilities.displaymessage(mcontext,response.body().getMsg());
                        }
                    }else{
                        AppUtilities.displaymessage(mcontext,mcontext.getString(R.string.network_error));
                    }
                }
                @Override
                public void onFailure(Call<AddtoCart> call, Throwable t) {
                    Log.e(TAG, "fail to delete from cart " + t.toString());
                    AppUtilities.displaymessage(mcontext,mcontext.getString(R.string.fail_to_add_to_cart));

                }
            });
        }
    }

    public void updateCartQty(String prod_id,String prod_qty){
        if (!NetworkUtility.isnetworkconected(mcontext)) {
            AppUtilities.displaymessage(mcontext, mcontext.getString(R.string.no_network_connection));
        } else {
            // Log.e(TAG,SharePreferenceUtil.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<editCartItem> call = service.editcartprodqtyCall("12345", prod_id, SharePreferenceUtil.getInstance().getString(Constant.USER_DATA),prod_qty);
            call.enqueue(new Callback<editCartItem>() {
                @Override
                public void onResponse(Call<editCartItem> call, Response<editCartItem> response) {
                    Log.e(TAG, "edit response is " + response.toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            AppUtilities.displaymessage(mcontext,response.body().getInformation().getStatus());
                            if (response.body().getInformation().getStatus().equalsIgnoreCase("successful update cart")){

                                ((CartDetails)mcontext).cart_totalamount.setText(response.body().getInformation().getTotalprice());

                            }
                        }else{
                            AppUtilities.displaymessage(mcontext,response.body().getMsg());
                        }
                    }else{
                        AppUtilities.displaymessage(mcontext,mcontext.getString(R.string.network_error));
                    }

                }

                @Override
                public void onFailure(Call<editCartItem> call, Throwable t) {
                    Log.e(TAG, "edit fail " + t.toString());
                    AppUtilities.displaymessage(mcontext,mcontext.getString(R.string.fail_to_edit_cart));
                }
            });
        }
    }
}
