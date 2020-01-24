package com.gova.openmap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gova.openmap.models.Mechanic;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ImageView mechProfile;
    TextView mechName,mechAdd,mechType;
    Button priPHno, secPhNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mechProfile = findViewById(R.id.frag_mech_IV);
        mechName = findViewById(R.id.frag_mech_name_tv);
        mechAdd = findViewById(R.id.frag_add_tv);
        mechType = findViewById(R.id.frag_mech_mechType_tv);
        priPHno = findViewById(R.id.frag_mech_Pri_Btn);
        secPhNo = findViewById(R.id.frag_mech_Sec_BTN);


        Mechanic mechanic = (Mechanic) getIntent().getSerializableExtra("Mechanic");
        loadDataa2View(mechanic);


        priPHno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + priPHno.getText()));
                startActivity(intent);
            }
        });

        secPhNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + secPhNo.getText()));
                startActivity(intent);
            }
        });


    }

    private void loadDataa2View(Mechanic mechanic) {

        Picasso.get().load(mechanic.getImageUri()).fit().into(mechProfile);
        mechName.setText(mechanic.getName());
        mechAdd.setText(mechanic.getAddress());
        mechType.setText(mechanic.getMechanicTypes().toString());
        priPHno.setText(mechanic.getPrimaryPhone());
        secPhNo.setText("+91"+mechanic.getSecondaryPhone());

    }
}
