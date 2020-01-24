package com.gova.openmap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gova.openmap.models.Mechanic;
import com.squareup.picasso.Picasso;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class MechProfile extends AppCompatActivity {

    ACProgressFlower dialog;

    Intent intent;
    TextView name, phone, address, mechanictype;
    Button logout;
    ImageView imageView;


    private StorageReference mStorageRef;

    FirebaseFirestore db;

    FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mech_profile);

        db = FirebaseFirestore.getInstance();

        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("please wait.your profile is still loading..")
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
// ...
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        //  mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        logout = findViewById(R.id.logout);
        name = findViewById(R.id.nameLabel);
        phone = findViewById(R.id.numberLabel);
        address = findViewById(R.id.address_label);
        mechanictype = findViewById(R.id.mechanic_Type_Label);
        imageView = findViewById(R.id.imageViewMechPro);



        String uid = (String) FirebaseAuth.getInstance().getCurrentUser().getUid();
        // String mobile = (String) FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();


        db.collection("mechanics").document(uid).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MechProfile.this, "Failure in fetching data"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Mechanic mechanic = documentSnapshot.toObject(Mechanic.class);

                        Toast.makeText(MechProfile.this, "Data : "+toString(), Toast.LENGTH_SHORT).show();


                        name.setText(mechanic.getName());
                        phone.setText(mechanic.getPrimaryPhone()+"\n+91"+mechanic.getSecondaryPhone());
                        address.setText(mechanic.getAddress());


                        StringBuilder stringBuilder = new StringBuilder();
                        for (String string : mechanic.getMechanicTypes())
                        {
                            stringBuilder.append(string+"\n");
                        }
                        mechanictype.setText(stringBuilder.toString());
                        Picasso.get().load(mechanic.getImageUri()).into(imageView);



                        if (dialog.isShowing())
                        {
                            dialog.dismiss();
                        }


                    }
                });
             /*   .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Toast.makeText(MechProfile.this, "Data : "+toString(), Toast.LENGTH_SHORT).show();
                Mechanic mechanic = dataSnapshot.getValue(Mechanic.class);

                name.setText(mechanic.getName());
                phone.setText(mechanic.getPrimaryPhone()+"\n+91"+mechanic.getSecondaryPhone());
                address.setText(mechanic.getAddress());


                StringBuilder stringBuilder = new StringBuilder();
                for (String string : mechanic.getMechanicTypes())
                {
                    stringBuilder.append(string+"\n");
                }
                mechanictype.setText(stringBuilder.toString());
                Picasso.get().load(mechanic.getImageUri()).into(imageView);



                if (dialog.isShowing())
                {
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                if (dialog.isShowing())
                {
                    dialog.dismiss();
                }
                Toast.makeText(MechProfile.this, "sorry something went wrong! check internet connectivity.."+databaseError, Toast.LENGTH_SHORT).show();
            }
        });*/
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), FirstActivity.class));
            }
        });


    }


}
