package com.curryout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.curryout.model.RestaurantDataModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantListFragment extends Fragment {

    boolean check_ScrollingUp = false;
    ArrayList<RestaurantDataModel> alist;
    RecyclerView recyclerView;
    RestaurantListAdapter recyAdapter;
    RestaurantDataModel restaurantDataModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.restaurant_list_fragment, container, false);

        init(view);
        listener(view);

        final RelativeLayout rl_toolbar = (RelativeLayout) view.findViewById(R.id.rl_toolbar);
        alist = new ArrayList<>();

        callGetAllRestaurants();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyAdapter = new RestaurantListAdapter(alist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        recyclerView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (recyclerView != null) {
                    if (recyclerView.getChildAt(0).getBottom() <= (recyclerView.getHeight() + recyclerView.getScrollY())) {
                        rl_toolbar.setVisibility(View.VISIBLE);
                    } else {
                        rl_toolbar.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        return view;
    }

    private void init(View view) {

    }

    private void listener(View view) {

    }

    public List<ImageSliderObject> getTestData() {
        List<ImageSliderObject> mTestData = new ArrayList<ImageSliderObject>();
        mTestData.add(new ImageSliderObject(R.drawable.fiftypercent_img));
        mTestData.add(new ImageSliderObject(R.drawable.fiftypercent_img));
        mTestData.add(new ImageSliderObject(R.drawable.fiftypercent_img));
        return mTestData;
    }

    @Override
    public void onResume() {
        super.onResume();
//        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
//        handler.removeCallbacks(runnable);
    }

    public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

        private ArrayList<RestaurantDataModel> listdata;

        public RestaurantListAdapter(ArrayList<RestaurantDataModel> listdata) {
            this.listdata = listdata;
        }

        @NonNull
        @Override
        public RestaurantListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.resturant_listitem_layout, parent, false);
            RestaurantListAdapter.ViewHolder viewHolder = new RestaurantListAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RestaurantListAdapter.ViewHolder holder, int position) {
            final RestaurantDataModel restauListData = alist.get(position);
            holder.txtRestauName.setText(restauListData.getRestauName());
            holder.txtFoodName.setText(restauListData.getFoodName());
            holder.txtAddress.setText(restauListData.getAddress());
            holder.img_Restaurant.setImageResource(restauListData.getImgRestaurant());
            Log.e("RES",restauListData.getRestrauntID());
            final String rid = restauListData.getRestrauntID();
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new SingleItemRestuFragment(rid);
                    Bundle bun = new Bundle();
                    bun.putString("RID","1");
                    myFragment.setArguments(bun);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
                }
            });

        }

        @Override
        public int getItemCount() {
            return alist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView img_Restaurant, fav_icon, fav_filled_icon;
            public TextView txtRestauName, txtFoodName, txtAddress;
            public LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                this.txtRestauName = (TextView) itemView.findViewById(R.id.txt_RestaurantsName);
                this.txtFoodName = (TextView) itemView.findViewById(R.id.txt_FoodName);
                this.txtAddress = (TextView) itemView.findViewById(R.id.txt_Address);
                this.img_Restaurant = (ImageView) itemView.findViewById(R.id.img_Restaurant);
                fav_icon = (ImageView) itemView.findViewById(R.id.fav_icon);
                fav_filled_icon = (ImageView) itemView.findViewById(R.id.fav_filled_icon);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);

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

    public void callGetAllRestaurants() {
        if (!CommonUtilities.isOnline(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Please Wait");
        pd.setCancelable(false);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "restraunt/getAllRestraunts";
        //String testurl = "http://www.mocky.io/v2/5cc4019f3400006500765495";
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                //map.put("cuisineID", );
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(getActivity()));
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);
    }

    public void parseResponse(String response) {
        Log.e("CurryOut Cuisi_ID Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            if (data == true) {
                JSONArray jarr = job.getJSONArray("data");

                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject jobj = jarr.getJSONObject(i);

                    String restrauntID = jobj.getString("restrauntID");
                    String user_id = jobj.getString("user_id");
                    String name = jobj.getString("name");
                    String city = jobj.getString("city");
                    String owner_name = jobj.getString("owner_name");
                    String phone = jobj.getString("phone");
                    String email = jobj.getString("email");
                    String address = jobj.getString("address");
                    String image = jobj.getString("image");
                    String food_type = jobj.getString("food_type");
                    String provide_delivery = jobj.getString("provide_delivery");
                    String cuisine_name = jobj.getString("cuisine_name");
                    String url="";
                    JSONArray jFaceShape = jobj.optJSONArray("cuisine_name");
                    for(int jj=0; jj<jFaceShape.length(); jj++)
                    {
                        url += (String) jFaceShape.get(jj) +" | ";
                        Log.d("CN", "url => " + url);
                    }

                    Log.e("RD", restrauntID);
                    Log.e("RD1", name);
                    Log.e("RD2", food_type);
                    Log.e("RD3", url);
                    restaurantDataModel = new RestaurantDataModel(restrauntID,name, url, address,R.drawable.fastfood_img);
                    alist.add(restaurantDataModel);
                }
            }

            recyclerView.setAdapter(recyAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

