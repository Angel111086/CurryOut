package com.curryout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curryout.R;
import com.curryout.model.ShowCartFoodItemModel;

import java.util.ArrayList;

public class ProductInf extends BaseAdapter {

    Context context;
    ArrayList<ShowCartFoodItemModel> alist;

    public ProductInf(Context context, ArrayList<ShowCartFoodItemModel> alist) {
        this.context = context;
        this.alist = alist;
    }

    @Override
    public int getCount() {
        return alist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShowCartFoodItemModel fm = alist.get(position);
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myv = inf.inflate(R.layout.product_inflate_in_cart,null);
        TextView txtListProduct = (TextView) myv.findViewById(R.id.txtListProduct);
        TextView txtListQty = (TextView) myv.findViewById(R.id.txtListQty);
        TextView txtListPrice = (TextView) myv.findViewById(R.id.txtListPrice);
        txtListProduct.setText(fm.getName());
        txtListQty.setText(fm.getQuantity());
        txtListPrice.setText(fm.getPrice());
        return myv;
    }
}
