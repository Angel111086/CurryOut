package com.curryout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyOrdersFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_orders, container, false);

        init(view);
        listener(view);


        MyOrdersParentDM[] myOrdersParentDMS= new MyOrdersParentDM[] {

                new MyOrdersParentDM("Wabi Sabi Restaurant", "North Indian", R.drawable.wabisabi_rest_img),
                new MyOrdersParentDM("Wabi Sabi Restaurant", "North Indian", R.drawable.wabisabi_rest_img),
                new MyOrdersParentDM("Wabi Sabi Restaurant", "North Indian", R.drawable.wabisabi_rest_img),
        };

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.myOrderRecyListView);
        MyOrdersListAdapter adapter = new MyOrdersListAdapter(myOrdersParentDMS);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }


    private void init(View view) {
    }

    private void listener(View view) {

    }


}
