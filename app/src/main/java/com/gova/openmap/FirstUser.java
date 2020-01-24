package com.gova.openmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.gova.openmap.models.Mechanic;
import com.gova.openmap.util.MechanicAdapter;

import java.util.ArrayList;

public class FirstUser extends AppCompatActivity {


    private static final String TAG = "FirstUser";
    FirebaseFirestore db;
    boolean focus = false;
    Spinner spinner;
    EditText addressEdittext;
    Intent intent;
    String mechanicType;
    String addressString ;
    TextView result;
    Button searchButton;
    private TextView emptyView;

    ArrayList<Mechanic>  mechanicsl = new ArrayList<>();
    MechanicAdapter mAdapter;
    RecyclerView  recyclerView;

    String[] mechanicTpes = {"Select Mechanic",
            "bike mechanic",
            "lorry mechanic",
            "car mechanic",
            "JCB mechanic",
            "plumber",
            "electrician",
            "truck mechanic",
            "van mechanic",
            "bus mechanic",
            "cycle mechanic"};


    public void getCurrentAddress(View view) {
        intent = new Intent(getApplicationContext(), Address.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (resultCode == 2) {
                addressString  = data.getStringExtra("MESSAGE");
                addressEdittext.setText(addressString);


            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_user);

        //hideSoftKeyboard(addressEdittext);

        db = FirebaseFirestore.getInstance();
        //result = findViewById(R.id.resulttv);
        searchButton = findViewById(R.id.seButton);
        emptyView = (TextView) findViewById(R.id.empty_view);

        this.spinner = (Spinner) findViewById(R.id.spinner);
        addressEdittext = findViewById(R.id.addSearch);
        addressEdittext.setFocusableInTouchMode(false);
        addressEdittext.setFocusable(false);
        addressEdittext.setFocusableInTouchMode(true);
        addressEdittext.setFocusable(true);



        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new MechanicAdapter(mechanicsl);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



       // getData();



        addressEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                hideSoftKeyboard(v);

                getCurrentAddress(v);
            }
        });

        addressEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    if (focus)
                    {

                        hideSoftKeyboard(v);
                        getCurrentAddress(v);
                    }
                    focus = true;
                }

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressString != null)
                getData();
                else
                    addressEdittext.setError("plese provide address....");
            }
        });


        // Create an array adapter and set it to the Spinner.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mechanicTpes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(adapter);
        // Set the message to default.
        this.spinner.setSelection(0);
        // Set itm selected listener.
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    onNothingSelected(parent);
                } else {
                     mechanicType = mechanicTpes[position];
                    Toast.makeText(FirstUser.this, "Mechanic Tpye : " + mechanicType, Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                mechanicType = null;
                Toast.makeText(FirstUser.this, "NOTHING Is SELECTED IN SPINNER", Toast.LENGTH_SHORT).show();


            }
        });




    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



   void getData()
    {
        //only for address query
        if (addressString != null  && mechanicType != null)
        {
            Toast.makeText(this, "both conditions executed...", Toast.LENGTH_SHORT).show();

            //String[] add = addressString.split(",",1);
            String[] split = addressString.split(",");
            String address = split[0];


                    db.collection("mechanics")
                   .orderBy("address")
                    .startAt(address)
                    //.startAt(addressString)
                   // .whereEqualTo("address",addressString)
                    .whereArrayContains("mechanicTypes",mechanicType)
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
                                    recyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                }
                                else {
                                    emptyView.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    mAdapter.notifyDataSetChanged();

                                }

                            } else {
                                Toast.makeText(FirstUser.this, "on success "+task.getException(), Toast.LENGTH_SHORT).show();
                                //result.setText("Error getting documents: "+ task.getException());

                                //Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    })

            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: "+e.getMessage());
                    Toast.makeText(FirstUser.this, "Failure "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            ;





        }


       else if (addressString != null )


        {
            Toast.makeText(this, "single condition executed...", Toast.LENGTH_SHORT).show();


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
                                }
                                else {
                                    emptyView.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    mAdapter.notifyDataSetChanged();

                                }

                            } else {
                                Toast.makeText(FirstUser.this, "on success "+task.getException(), Toast.LENGTH_SHORT).show();
                                //result.setText("Error getting documents: "+ task.getException());

                                //Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "onFailure: "+e.getMessage());
                            Toast.makeText(FirstUser.this, "Failure "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
            ;





        }




    }

   /* private void fetchRecycleData(ArrayList<Mechanic> mechanicsl) {


    }*/


}
