package com.example.asus.shoppingapp.ProductPreview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.shoppingapp.R;

/**
 * Created by asus on 29-08-2018.
 */

public class Details extends Fragment {
    private Context context;
    private TextView prod_details;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_product_details,container,false);
        context = view.getContext();

        prod_details = (TextView)view.findViewById(R.id.prod_details);
        prod_details.setText(((productDetails)getActivity()).prod_fulldetails);

        return view;
    }
}
