package com.curryout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WelcomePhoneActivity extends AppCompatActivity {

    ImageView img_next;
    EditText et_phoneNumber;
    String mob="";
    String add="+91",added="";
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_phone);
        ctx = WelcomePhoneActivity.this;
            init();
            listener();
    }

    private void init() {
        img_next=(ImageView)findViewById(R.id.img_next);
        et_phoneNumber=(EditText)findViewById(R.id.et_phoneNumber);

    }

    private void listener() {
        et_phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mob = et_phoneNumber.getText().toString();
                added = add+mob;
            }
        });

        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mob = et_phoneNumber.getText().toString();
                added = add+mob;
                if(validatePhone(added)) {
                callAuthApi(added);
                Log.e("Mobile Value",added+"");

            }}
        });

        et_phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePhone(mob);
            }
        });
    }
    //To check the phone number
    private boolean validatePhone(String mobile) {
        if (mobile.isEmpty() || !AppSharedPreference.isValidPhone(mobile)) {
            et_phoneNumber.setError("Invalid phone number");
            requestFocus(et_phoneNumber);
            return false;
        } else {
            //et_phoneNumber.setError("Valid");
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void callAuthApi(final String mob){

        if (!CommonUtilities.isOnline(ctx)) {
            Toast.makeText(ctx, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog pd = new ProgressDialog(WelcomePhoneActivity.this);
        pd.setTitle("Please Wait");
        pd.setCancelable(false);
        pd.show();
        Log.e("Mobile in Call",mob);
        String url = AppGlobalConstants.WEBSERVICE_BASE_URL+"auth/index";
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("mobile",mob);
                map.put("user_type","customer");
                return map;
            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        sr.setShouldCache(false);
        VolleySingleton.getInstance(WelcomePhoneActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse(String response){
        Log.e("CurryOut Parse Response","response "+response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean)job.get("status");
            if(data==true){
                Toast.makeText(WelcomePhoneActivity.this,"OTP sent on mobile number through sms",Toast.LENGTH_LONG).show();
                Intent in = new Intent(WelcomePhoneActivity.this, VerificationActivity.class);
                in.putExtra("MobileData",added);
                startActivity(in);
                AppSharedPreference.saveUserPhoneToPreferences(WelcomePhoneActivity.this,added);
            }
            else{
                Toast.makeText(WelcomePhoneActivity.this,"Field cannot be empty or check mobile number.",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
