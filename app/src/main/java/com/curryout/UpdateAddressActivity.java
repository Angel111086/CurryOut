package com.curryout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.curryout.model.UserAddressModel;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UpdateAddressActivity extends AppCompatActivity {

    EditText edplotNumber,edStreet,edLandmark,edCity,edState,edPostal,edCountry;
    String plot,street,landmark,city,state,postal,country;
    LinearLayout ln_updateAddress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);
        init();
        getAddress();
        listener();
    }

    public void init(){
        edplotNumber = findViewById(R.id.edplotNumber);
        edStreet = findViewById(R.id.edStreet);
        edLandmark = findViewById(R.id.edLandmark);
        edCity = findViewById(R.id.edCity);
        edState = findViewById(R.id.edState);
        edPostal = findViewById(R.id.edPostal);
        edCountry = findViewById(R.id.edCountry);
        ln_updateAddress = findViewById(R.id.ln_updateAddress);
    }

    public void getAddress(){
        UserAddressModel uam = (UserAddressModel) getIntent().getSerializableExtra("UpdateModel");
        edplotNumber.setText(uam.getHouseno());
        edStreet.setText(uam.getStreet());
        edLandmark.setText(uam.getLandmark());
        edCity.setText(uam.getCity());
        edState.setText(uam.getState());
        edPostal.setText(uam.getPostalCode());
        edCountry.setText(uam.getCountry());

    }
    public void listener(){
        ln_updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plot = edplotNumber.getText().toString();
                street = edStreet.getText().toString();
                landmark = edLandmark.getText().toString();
                city = edCity.getText().toString();
                state = edState.getText().toString();
                postal = edPostal.getText().toString();
                country = edCountry.getText().toString();

                //callAddAddressapi(plot,street,landmark,city,state,postal,country);

            }
        });
    }

    public void testApi(String plot,String street,String landmark,String city,String state,String postal,String country){
        if (!CommonUtilities.isOnline(UpdateAddressActivity.this)) {
            Toast.makeText(UpdateAddressActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(UpdateAddressActivity.this);
        pd.setTitle("Please Wait");
        pd.setCancelable(false);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "";
        String testurl = "";

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateAddressActivity.this));
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
        VolleySingleton.getInstance(UpdateAddressActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse(String response) {
        Log.e("CO Product Response", "response " + response);
        try{

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
