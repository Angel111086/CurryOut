package com.curryout;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.curryout.adapter.ProductInf;
import com.curryout.appsharedpreference.AppGlobalConstants;
import com.curryout.appsharedpreference.AppSharedPreference;
import com.curryout.appsharedpreference.CommonUtilities;
import com.curryout.model.ShowCartFoodItemModel;
import com.curryout.model.UserAddressModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.json.JsonObject;


public class CartActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;

    ImageView back_icon;
    LinearLayout ln_payMethod,linearListener1,ln_placeOrder;
    ProductInf adapter;
    TextView txtAddress,txtSubTotal,txtDeliveryFee,txtPromoCode,txtGrandTotal,txtPaymentM;
    ArrayList<ShowCartFoodItemModel> alist;
    ListView mylist;
    EditText edInstructions,edInstructionsO;
    String uadd_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        init();
        listener();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    private void init() {

        back_icon=(ImageView)findViewById(R.id.back_icon);
        ln_payMethod=(LinearLayout)findViewById(R.id.ln_payMethod);
        linearListener1 = (LinearLayout) findViewById(R.id.linearListener1);
        mylist = findViewById(R.id.myList);
        alist = new ArrayList<>();
        txtAddress = findViewById(R.id.txtAddress);
        edInstructions = findViewById(R.id.edInstructionsDB);
        edInstructionsO = findViewById(R.id.edInstructionsO);
        txtDeliveryFee = findViewById(R.id.txtDeliveryFee);
        txtSubTotal = findViewById(R.id.txtSubTotal);
        txtPromoCode = findViewById(R.id.txtPromoCode);
        txtGrandTotal = findViewById(R.id.txtGrandTotal);
        txtPaymentM = findViewById(R.id.txtPaymentM);
        ln_placeOrder = findViewById(R.id.ln_placeOrder);
        callgetCartItemList();
    }

    private void listener() {

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ln_payMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, PaymentMethodActivity.class));
            }
        });

        ln_placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPlaceOrderApi();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
//Showing Current Location Marker on Map
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    String subLocality = listAddresses.get(0).getSubLocality();
                    markerOptions.title("" + latLng + "," + subLocality + "," + state
                            + "," + country);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void callgetCartItemList(){
        if (!CommonUtilities.isOnline(CartActivity.this)) {
            Toast.makeText(CartActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(CartActivity.this);
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
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(CartActivity.this));
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
        VolleySingleton.getInstance(CartActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse1(String response) {
        Log.e("CO Product Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            JSONObject jdata = job.getJSONObject("data");
            //get Address Data
            JSONObject arr = jdata.optJSONObject("current_address");
            for (int i = 0; i < arr.length(); i++) {
                uadd_id = arr.getString("user_addressID");
                String user_id = arr.getString("user_id");
                final String houseno = arr.getString("houseno");
                final String street = arr.getString("street");
                final String landmark = arr.getString("landmark");
                final String city = arr.getString("city");
                final String state = arr.getString("state");
                final String postalCode = arr.getString("postalCode");
                final String country = arr.getString("country");
                String current_address = arr.getString("current_address");

                txtAddress.setText(houseno+" "+street+" "+landmark+" "+city+" "+state+" "+postalCode+" "+country);

                txtAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserAddressModel uam = new UserAddressModel(houseno,street,landmark,city,state,postalCode,country);
                        Intent in = new Intent(CartActivity.this,UpdateAddressActivity.class);
                        in.putExtra("UpdateModel",uam);
                        startActivity(in);
                    }
                });
            }
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


                ShowCartFoodItemModel fm = new ShowCartFoodItemModel(productID,cartID,name, cart_quantity, price);
                alist.add(fm);

            }
            adapter = new ProductInf(CartActivity.this, alist);
            mylist.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            if(cartID!=null){
                    mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ShowCartFoodItemModel sc = alist.get(position);
                            //Intent in = new Intent(CartActivity.this,MainActivity.class);
                            Log.e("FoodM",sc.getName());
                            Intent in = new Intent();
                            in.putExtra("CartDetail",sc);
                            setResult(RESULT_OK,in);
                            finish();

                        }
                    });
                }
                else{

                }



            if (data == false) {
                String message = (String) job.get("message");
                Log.e("False", message);
                if (message.equalsIgnoreCase("The Product Id field is required.<br>\n")) {
                    Toast.makeText(CartActivity.this, "Product Id Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){

            e.printStackTrace();
        }


    }

    public void testData(){
//        boolean data = (boolean) job.get("status");
//        JSONArray jarr = job.getJSONArray("data");
//        int length = jarr.length();
//        String name="",price="",url="",quantity="";
//        if (data == true) {
//        for (int i = 0; i < length; i++){
//        JSONObject obj=jarr.getJSONObject(i);
//final String cartID=obj.getString("cartID");
//        String user_id=obj.getString("user_id");
//        String cart_quantity=obj.getString("cart_quantity");
//        String cart_price=obj.getString("cart_price");
//        String cart_added_at=obj.getString("cart_added_at");
//        String productID=obj.getString("productID");
//        String restaurant_id=obj.getString("restaurant_id");
//        name=obj.getString("name");
//        String category=obj.getString("category");
//        String sub_Category=obj.getString("sub_Category");
//        String food_type=obj.getString("food_type");
//        price=obj.getString("price");
//        String description=obj.getString("description");
//        quantity=obj.getString("quantity");
//        String ingrediants=obj.getString("ingrediants");
//        String image=obj.getString("image");
//        JSONArray jFaceShape=obj.optJSONArray("available_days");
//        url="";
//        for(int jj=0;jj<jFaceShape.length();jj++){
//        url+=(String)jFaceShape.get(jj);
//        Log.d("AD","url => "+url);
//        }
//        String available_start_time=obj.getString("available_start_time");
//        String available_end_time=obj.getString("available_end_time");
//        String createdAt=obj.getString("createdAt");
//        String status1=obj.getString("status");
//        String addon_item=obj.getString("addon_item");
//        String category_name=obj.getString("category_name");
//        String sub_category_name=obj.getString("sub_category_name");
//
//        Log.e("Cart_Id",cartID);
//        Log.e("Revised Name",name);
//        }
//        }
//        }
    }

    public ShowCartFoodItemModel getListData(){
        ShowCartFoodItemModel scfm=new ShowCartFoodItemModel();
        for(int i=0;i<alist.size();i++){
             scfm = alist.get(i);
        }
        return scfm;
    }
    public void callPlaceOrderApi(){
        if (!CommonUtilities.isOnline(CartActivity.this)) {
            Toast.makeText(CartActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(CartActivity.this);
        pd.setTitle("Please Wait");
        pd.setCancelable(false);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "order/placeOrder";
        String testurl = "";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponse2(response);
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

                        parseResponse2(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("user_addressId",uadd_id);
                map.put("instruction_to_delivery_boy",edInstructions.getText().toString());
                map.put("instruction_for_order",edInstructionsO.getText().toString());
                map.put("sub_total",txtSubTotal.getText().toString());
                map.put("delivery_fee",txtDeliveryFee.getText().toString());
                map.put("promo_code",txtPromoCode.getText().toString());
                map.put("discount","30");
                map.put("total",txtGrandTotal.getText().toString());
                map.put("payment_method",txtPaymentM.getText().toString());
                ShowCartFoodItemModel scfm = getListData();
                map.put("product_id[0]",scfm.getPid());
                map.put("quantity[0]",scfm.getQuantity());
                map.put("price[0]",scfm.getPrice());

                map.put("product_id[1]",scfm.getPid());
                map.put("quantity[1]",scfm.getQuantity());
                map.put("price[1]",scfm.getPrice());


                Log.e("PlaceOrderData",uadd_id+":"+
                        edInstructions.getText().toString()+":"+
                        edInstructionsO.getText().toString()+":"+
                        txtSubTotal.getText().toString()+":"+
                        txtDeliveryFee.getText().toString()+":"+
                        txtPromoCode.getText().toString()+":"+
                        txtGrandTotal.getText().toString()+":"+
                        txtPaymentM.getText().toString()+":"+
                        scfm.getPid()+":"+scfm.getPrice()+scfm.getQuantity()
                        );
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(CartActivity.this));
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
        VolleySingleton.getInstance(CartActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse2(String response){
        Log.e("CO PlaceOrder Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            String message = job.getString("message");
            if (data == true) {
                if (message.equalsIgnoreCase("Order Placed Successfully.")) {
                    Toast.makeText(CartActivity.this, "Order Placed Successfully.", Toast.LENGTH_SHORT).show();

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
