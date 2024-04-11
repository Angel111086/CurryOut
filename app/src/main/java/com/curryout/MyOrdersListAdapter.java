package com.curryout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyOrdersListAdapter extends RecyclerView.Adapter<MyOrdersListAdapter.ViewHolder> {
    private MyOrdersParentDM[] listData;

    public MyOrdersListAdapter(MyOrdersParentDM[] listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.my_order_listlayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyOrdersParentDM myOrdersListData = listData[position];
        holder.txtRestName.setText(listData[position].getRestName());
        holder.txtFoodName.setText(listData[position].getFoodName());
        holder.imageView.setImageResource(listData[position].getRestImg());
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return listData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Context context;
        public ImageView imageView, fav_icon,fav_filled_icon;
        public TextView txtRestName, txtFoodName;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.img_Restaurant);
            this.txtRestName = (TextView) itemView.findViewById(R.id.txt_RestaurantsName);
            this.txtFoodName = (TextView) itemView.findViewById(R.id.txt_FoodName);
            fav_icon=(ImageView)itemView.findViewById(R.id.fav_icon);
            fav_filled_icon=(ImageView)itemView.findViewById(R.id.fav_filled_icon);

            fav_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fav_icon.setVisibility(View.GONE);
                    fav_filled_icon.setVisibility(View.VISIBLE);
                }
            });

            fav_filled_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fav_filled_icon.setVisibility(View.GONE);
                    fav_icon.setVisibility(View.VISIBLE);
                }
            });


        }
    }
}
