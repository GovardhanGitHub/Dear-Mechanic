package com.gova.openmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class FirstActivity extends Activity {


    RelativeLayout userrl,mechrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        userrl = findViewById(R.id.userrl);
        mechrl = findViewById(R.id.mechrl);


        userrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FirstUser.class);
                startActivity(intent);
            }
        });



        mechrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FirstMech.class);
                startActivity(intent);
            }
        });

    }








}
