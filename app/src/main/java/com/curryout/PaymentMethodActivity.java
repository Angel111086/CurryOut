package com.curryout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodActivity extends AppCompatActivity {

    String[] ItemName;
    List<Items> rowItem;
    ListView list;

    ImageView back_icon;
    LinearLayout ln_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);


        init();
        listener();

        rowItem = new ArrayList<Items>();
        ItemName = getResources().getStringArray(R.array.name);

        for(int i = 0 ; i < ItemName.length ; i++)
        {
            Items itm = new Items(ItemName[i]);
            rowItem.add(itm);
        }

        list = (ListView) findViewById(R.id.lvCheckBox);
        CustomListActivity adapter = new CustomListActivity(this, rowItem);
        list.setAdapter(adapter);
    }

    private void init() {

        back_icon=(ImageView)findViewById(R.id.back_icon);
        ln_save=(LinearLayout)findViewById(R.id.ln_save);
    }

    private void listener() {

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ln_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private class Items {

        private String items;
        private boolean selected;

        public Items(String items) {

            this.items = items;

        }

        public String getItems() {

            return items;
        }

        public void setItemName(String name) {

            this.items = name;
        }
        public boolean getSelected() {
            return selected;
        }

        public boolean setSelected(Boolean selected) {
            return this.selected = selected;
        }
    }

    private class CustomListActivity  extends BaseAdapter {

        Context context;
        List<Items> rowItem;
        View listView;
        boolean checkState[];

        ViewHolder holder;

        public CustomListActivity(Context context, List<Items> rowItem) {

            this.context = context;
            this.rowItem = rowItem;
            checkState = new boolean[rowItem.size()];

        }

        @Override
        public int getCount() {

            return rowItem.size();
        }

        @Override
        public Object getItem(int position) {

            return rowItem.get(position);

        }

        @Override
        public long getItemId(int position) {

            return rowItem.indexOf(getItem(position));

        }

        public class ViewHolder {
            TextView tvItemName;
            CheckBox check;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            holder = new ViewHolder();
            final Items itm = rowItem.get(position);
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (view == null) {

                listView = new View(context);
                listView = layoutInflater.inflate(R.layout.payment_method_listitem,
                        parent, false);

                holder.tvItemName = (TextView) listView
                        .findViewById(R.id.txt_payName);
                holder.check = (CheckBox) listView.findViewById(R.id.checkbox);
                listView.setTag(holder);

            } else {
                listView = (View) view;
                holder = (ViewHolder) listView.getTag();
            }

            holder.tvItemName.setText(itm.getItems());

            holder.check.setChecked(checkState[position]);

            holder.check.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    for(int i=0;i<checkState.length;i++)
                    {
                        if(i==position)
                        {
                            checkState[i]=true;
                        }
                        else
                        {
                            checkState[i]=false;
                        }
                    }
                    notifyDataSetChanged();

                }
            });
            return listView;
        }
    }
}
