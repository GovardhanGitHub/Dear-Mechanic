package com.gova.openmap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;

import com.gova.openmap.models.Mechanic;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ImageView mechProfile;
    TextView mechName,mechAdd,mechType;
    Button priPHno, secPhNo;
    public Mechanic mechanic;
    ShareActionProvider shareActionProvider;




   /* public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.shareMechaic:
                share();
                break;


        }
        return true;
    }*/
    private void share() {



        String Body = "Mechanic Name: " + mechanic.getName() + "\n\n"
                + "Mechanic Type: " + mechanic.getMechanicTypes().toString() + "\n\n" + "Address: " + mechanic.getAddress()
                + "\n\n" + "Primary Mobile Number: " + mechanic.getPrimaryPhone()
                + "\n" + "Scondary Mobile Number: " + mechanic.getSecondaryPhone();



    /*    String fileName = mechanic.getImageUri();//Name of an image
        String externalStorageDirectory = Environment.getExternalStorageDirectory().toString();
        String myDir = externalStorageDirectory + "/saved_images/"; // the file will be in saved_images*/
       // Uri uri =Uri.parse(mechanic.getImageUri());
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mechnanic Detials");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, Body);
        //shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareActionProvider.setShareIntent(shareIntent);
       // startActivity(Intent.createChooser(shareIntent, "Share Mechanic Detials"));



/*
        Intent share = new Intent(android.content.Intent.ACTION_SEND);

        Uri screenshotUri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + mechanic.getImageUri());
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_SUBJECT, "The EssexPass");
        share.putExtra(Intent.EXTRA_TEXT, Body);
        share.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(share, "Share Deal"));*/
    }


    //Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

/*
        toolbar = findViewById(R.id.detail_toolBar);
        setSupportActionBar(toolbar);*/

        mechProfile = findViewById(R.id.frag_mech_IV);
        mechName = findViewById(R.id.frag_mech_name_tv);
        mechAdd = findViewById(R.id.frag_add_tv);
        mechType = findViewById(R.id.frag_mech_mechType_tv);
        priPHno = findViewById(R.id.frag_mech_Pri_Btn);
        secPhNo = findViewById(R.id.frag_mech_Sec_BTN);


        mechanic = (Mechanic) getIntent().getSerializableExtra("Mechanic");
        loadDataa2View(mechanic);


        priPHno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", priPHno.getText().toString(), null));
                startActivity(intent);
            }
        });

        secPhNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", secPhNo.getText().toString(), null));
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.mechanic_detials_share, menu);

        MenuItem menuItem = menu.findItem(R.id.shareMechaic);
        shareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        share();
        return super.onCreateOptionsMenu(menu);
    }
}
