package com.curryout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class WelcomeEmailActivity extends AppCompatActivity {


    ImageView imgEmailUnCheck, imgEmailCheck, imgPhoneUnCheck, imgPhoneCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_email);

        init();
        listener();

    }

    private void init() {

        imgEmailCheck=(ImageView)findViewById(R.id.imgEmailCheck);
        imgEmailUnCheck=(ImageView)findViewById(R.id.imgEmailUnCheck);
        imgPhoneCheck=(ImageView)findViewById(R.id.imgPhoneCheck);
        imgPhoneUnCheck=(ImageView)findViewById(R.id.imgPhoneUnCheck);
    }

    private void listener() {


        imgPhoneUnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                imgPhoneCheck.setVisibility(View.VISIBLE);
//                imgPhoneUnCheck.setVisibility(View.GONE);
//                imgEmailCheck.setVisibility(View.GONE);
//                imgEmailUnCheck.setVisibility(View.VISIBLE);

                startActivity(new Intent(WelcomeEmailActivity.this, WelcomePhoneActivity.class));
            }
        });


    }
}
