package com.gova.openmap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.gova.openmap.models.Mechanic;
import com.gova.openmap.util.MechanicAdapter;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class FirstUser extends AppCompatActivity {


    private static final String TAG = "FirstUser";
    private static final int REQUEST_CODE_CHECK = 512;
    FirebaseFirestore db;
    boolean focus = false;
    Spinner spinner;
    TextView addressEdittext;
    Intent intent;
    String mechanicType;
    String addressString;
    //TextView result;
    Button searchButton;
    private TextView emptyView;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;

    ArrayList<Mechanic> mechanicsl = new ArrayList<>();
    MechanicAdapter mAdapter;
    RecyclerView recyclerView;
    public static TextView mechTypeTV;
    ACProgressFlower dialog;


    public void getCurrentAddress(View view) {
        intent = new Intent(getApplicationContext(), Address.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (resultCode == 2) {
                addressString = data.getStringExtra("MESSAGE");
                addressEdittext.setText(addressString);


            }
        }
        if (resultCode == 3) {
            mechanicType = data.getStringExtra("TADA");
            mechTypeTV.setText(mechanicType);
            if (mechanicType == null)
                mechTypeTV.setText("Select Mechanic");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_user);

        getSupportActionBar().setTitle("Mechanics...");

        /*toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);*/


        /*setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle("Mechanics");
*/
        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("please wait...")
                .fadeColor(Color.DKGRAY).build();
        //hideSoftKeyboard(addressEdittext);
        db = FirebaseFirestore.getInstance();
        //result = findViewById(R.id.resulttv);
        searchButton = findViewById(R.id.seButton);
        emptyView = (TextView) findViewById(R.id.empty_view);
        emptyView.setVisibility(View.GONE);
        mechTypeTV = findViewById(R.id.mechanicTVResult);
        mechTypeTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
        // this.spinner = (Spinner) findViewById(R.id.spinner);
        addressEdittext = findViewById(R.id.addSearch);
        addressEdittext.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new MechanicAdapter(mechanicsl);
        //GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                //new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        // getData();
        addressEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hideSoftKeyboard(v);
                getCurrentAddress(v);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addressString != null){
                    dialog.show();
                    getData();
                }
                else
                    Toast.makeText(FirstUser.this, "plese provide address....", Toast.LENGTH_SHORT).show();
            }
        });

    }
    // Create an array adapter and set it to the Spinner.
    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mechanicTpes);


    void getData() {
        //only for address query
        if (addressString != null && mechanicType != null) {
            // Toast.makeText(this, "both conditions executed...", Toast.LENGTH_SHORT).show();
            //String[] add = addressString.split(",",1);
            String[] split = addressString.split(",");
            String address = split[0];
            db.collection("mechanics")
                    .orderBy("address")
                    .startAt(address)
                    //.startAt(addressString)
                    // .whereEqualTo("address",addressString)
                    .whereArrayContains("mechanicTypes", mechanicType)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                mechanicsl.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Mechanic mechanic = document.toObject(Mechanic.class);
                                    mechanicsl.add(mechanic);
                                    //  result.setText(document.getId() + " => " + document.getData());
                                    //Toast.makeText(FirstUser.this, "Data \n"+document.getId() + " => " + document.getData(), Toast.LENGTH_SHORT).show();
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                if (mechanicsl.isEmpty()) {
                                    dialog.dismiss();
                                    recyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                } else {
                                    dialog.dismiss();
                                    emptyView.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    mAdapter.notifyDataSetChanged();

                                }

                            } else {
                                dialog.dismiss();
                                Toast.makeText(FirstUser.this, "on success " + task.getException(), Toast.LENGTH_SHORT).show();
                                //result.setText("Error getting documents: "+ task.getException());
                                //Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Log.i(TAG, "onFailure: " + e.getMessage());
                            Toast.makeText(FirstUser.this, "Failure " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
            ;


        } else if (addressString != null) {
            // Toast.makeText(this, "single condition executed...", Toast.LENGTH_SHORT).show();
            //String[] add = addressString.split(",",1);
            String[] split = addressString.split(",");
            String address = split[0];
            db.collection("mechanics")
                    .orderBy("address")
                    .startAt(address)
                    /*//.startAt(addressString)
                    // .whereEqualTo("address",addressString)
                    .whereArrayContains("mechanicTypes","lorry mechanic")*/
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                mechanicsl.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Mechanic mechanic = document.toObject(Mechanic.class);
                                    mechanicsl.add(mechanic);
                                    //  result.setText(document.getId() + " => " + document.getData());
                                    //Toast.makeText(FirstUser.this, "Data \n"+document.getId() + " => " + document.getData(), Toast.LENGTH_SHORT).show();
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                if (mechanicsl.isEmpty()) {
                                    recyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                } else {
                                    emptyView.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    mAdapter.notifyDataSetChanged();

                                }

                            } else {
                                dialog.dismiss();
                                Toast.makeText(FirstUser.this, "on success " + task.getException(), Toast.LENGTH_SHORT).show();
                                //result.setText("Error getting documents: "+ task.getException());
                                //Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Log.i(TAG, "onFailure: " + e.getMessage());
                            Toast.makeText(FirstUser.this, "Failure " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
            ;


        }


    }

    public void getMechanicType(View view) {
        mechanicType = null;
        Intent i = new Intent(FirstUser.this, SelectMechanicType.class);
        startActivityForResult(i, 3);

    }

   /* private void fetchRecycleData(ArrayList<Mechanic> mechanicsl) {


    }*/


}
