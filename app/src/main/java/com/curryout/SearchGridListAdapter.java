package com.curryout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.curryout.appsharedpreference.AppGlobalConstants;
import com.curryout.appsharedpreference.AppSharedPreference;
import com.curryout.appsharedpreference.CommonUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchGridListAdapter extends RecyclerView.Adapter<SearchGridListAdapter.ViewHolder> {


    private ArrayList<SearchGridDataModel> listData;
    static  Context context;
    static String productid,pri;

    public SearchGridListAdapter(ArrayList<SearchGridDataModel> listData,Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchGridListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.grid_fooditem_layout, parent, false);
        SearchGridListAdapter.ViewHolder viewHolder = new SearchGridListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchGridListAdapter.ViewHolder holder, int position) {

        final SearchGridDataModel searchGridDataModel= listData.get(position);
        holder.txt_name.setText(searchGridDataModel.getTitle());
        holder.txt_price.setText(searchGridDataModel.getPrice());
        holder.img_title.setImageResource(searchGridDataModel.getImgTitle());
        final String pid = searchGridDataModel.getProductID();
        //productid = pid;
        final String price = searchGridDataModel.getPrice();
        pri = price;
        holder.img_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new SingleFoodDetailsFragment(pid,price);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_title, img_min, img_plus;
        public TextView txt_name, txt_price, txt_count,addToCarttxt;
        public LinearLayout linearLayout, ln_add, ln_plusMin;
        private int count;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.img_title = (ImageView) itemView.findViewById(R.id.imgTitle);
            this.txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            this.txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            ln_add=(LinearLayout)itemView.findViewById(R.id.ln_add);
            ln_plusMin=(LinearLayout)itemView.findViewById(R.id.ln_plusMin);
            img_plus=(ImageView)itemView.findViewById(R.id.img_plus);
            img_min=(ImageView)itemView.findViewById(R.id.img_min);
            txt_count=(TextView)itemView.findViewById(R.id.txt_count);
            addToCarttxt = (TextView)itemView.findViewById(R.id.addToCarttxt);
            final AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
            img_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count++;
                    txt_count.setText(String.valueOf(count));

                }
            });

            img_min.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(count>0) {
                        count--;
                        txt_count.setText(String.valueOf(count));
                    }
                }
            });


            ln_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ln_add.setVisibility(View.GONE);
                    ln_plusMin.setVisibility(View.VISIBLE);
                }
            });

            addToCarttxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(count==0){
                        Toast.makeText(activity, "Please Select Quantity", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "Please Click on Quantity", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            txt_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calladdtoCartApi(count);
                    //startActivity(new Intent(activity(), CartActivity.class));
                }
            });

//            img_moreItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(itemView.getContext(), CatalogueActivity.class);
//                    itemView.getContext().startActivity(intent);
//                }
//            });
        }
    }
    //Get Cart Item List
    public void callgetCartItemList(){
        if (!CommonUtilities.isOnline(context)) {
            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Please Wait");
        pd.setCancelable(false);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "cart/getCartItemList";
        String testurl = "";

        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponse1(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                NetworkResponse response = error.networkResponse;

                Log.e("com.curryout", "error response " + response);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.e("mls", "VolleyError TimeoutError error or NoConnectionError");
                } else if (error instanceof AuthFailureError) {                    //TODO
                    Log.e("mls", "VolleyError AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("mls", "VolleyError ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("mls", "VolleyError NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("mls", "VolleyError TParseError");
                }
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        Log.e("com.curryout", "error response " + res);

                        parseResponse1(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(context));
                return header;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        sr.setShouldCache(false);
        VolleySingleton.getInstance(context).addToRequestQueue(sr);
    }

    public void parseResponse1(String response) {
        Log.e("CO Product Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            JSONObject jdata = job.getJSONObject("data");
            JSONArray arr = jdata.optJSONArray("current_address");

            JSONArray cartarr = jdata.getJSONArray("cart");
            String name = "", price = "", url = "",cartID="",cart_quantity="",productID="";
            for (int i = 0; i < cartarr.length(); i++) {
                JSONObject obj = cartarr.getJSONObject(i);
                cartID = obj.getString("cartID");
                String user_id = obj.getString("user_id");
                cart_quantity = obj.getString("cart_quantity");
                Log.e("cart quantity",obj.getString("cart_quantity"));
                String cart_price = obj.getString("cart_price");
                String cart_added_at = obj.getString("cart_added_at");
                productID = obj.getString("productID");
                String restaurant_id = obj.getString("restaurant_id");
                name = obj.getString("name");
                String category = obj.getString("category");
                String sub_Category = obj.getString("sub_Category");
                String food_type = obj.getString("food_type");
                price = obj.getString("price");
                String description = obj.getString("description");
                String quantity = obj.getString("quantity");
                String ingrediants = obj.getString("ingrediants");
                String image = obj.getString("image");
                JSONArray jFaceShape = obj.optJSONArray("available_days");

                for (int jj = 0; jj < jFaceShape.length(); jj++) {
                    url += (String) jFaceShape.get(jj);
                    Log.d("AD", "url => " + url);
                }
                String available_start_time = obj.getString("available_start_time");
                String available_end_time = obj.getString("available_end_time");
                String createdAt = obj.getString("createdAt");
                String status1 = obj.getString("status");
                String addon_item = obj.getString("addon_item");
                String category_name = obj.getString("category_name");
                String sub_category_name = obj.getString("sub_category_name");
                Log.e("Cart_Id", cartID);
                Log.e("Revised Name", name);
                Log.e("Cart Qty", cart_quantity);

            }
            if (data == false) {
                String message = (String) job.get("message");
                Log.e("False", message);
                if (message.equalsIgnoreCase("The Product Id field is required.<br>\n")) {
                    Toast.makeText(context, "Product Id Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    //Add to Cart
    public static void calladdtoCartApi(final int q){
        if (!CommonUtilities.isOnline(context)) {
            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Please Wait");
        pd.setCancelable(false);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "cart/addtoCart";
        String testurl = "";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                NetworkResponse response = error.networkResponse;

                Log.e("com.curryout", "error response " + response);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.e("mls", "VolleyError TimeoutError error or NoConnectionError");
                } else if (error instanceof AuthFailureError) {                    //TODO
                    Log.e("mls", "VolleyError AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("mls", "VolleyError ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("mls", "VolleyError NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("mls", "VolleyError TParseError");
                }
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        Log.e("com.curryout", "error response " + res);

                        parseResponse(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("productId", productid);
                map.put("quantity", q+"");
                map.put("price", pri);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(context));
                return header;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        sr.setShouldCache(false);
        VolleySingleton.getInstance(context).addToRequestQueue(sr);
    }

    public static  void parseResponse(String response) {
        Log.e("CO SingleItem Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            String message="";
            if (data == true) {
                message = (String)job.get("message");
                if(message.equalsIgnoreCase("Product Added to Cart")){
                    Toast.makeText(context, "Product Added to Cart Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
            if(data==false){
                message = (String)job.get("message");
                Log.e("False",message);
                if(message.equalsIgnoreCase("The Quantity field is required.<br>\n")){
                    Toast.makeText(context, "Quantity Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
                if(message.equalsIgnoreCase("The Product field is required.<br>\n")){
                    Toast.makeText(context, "Product Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
                if(message.equalsIgnoreCase("The Product field is required.<br>\n")){
                    Toast.makeText(context, "Product Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
