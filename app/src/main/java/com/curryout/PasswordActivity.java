package com.curryout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PasswordActivity extends AppCompatActivity {

    ImageView img_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        init();
        listener();
    }

    private void init() {
        img_next=(ImageView)findViewById(R.id.img_next);
    }

    private void listener() {

        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });

    }
}
