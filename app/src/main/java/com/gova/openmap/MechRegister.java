package com.gova.openmap;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.gova.openmap.models.Mechanic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class MechRegister extends AppCompatActivity {


    private static final String TAG = "Mech registration";
    ACProgressFlower dialog;
    Intent intent;
    TextView mechTypeLabel;
    ImageView mImageView;
    Button chooseImage, submit;
    ChipGroup chipGroup;
    ArrayList<String> list;





    //private DatabaseReference mDatabaseRef;

    //FirebaseFirestore mDatabaseRef;


    DatabaseReference myRef ;

    private StorageReference mStorageRef;
    FirebaseFirestore db;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;


    String id;
    Uri mImageUri;
    String primaryMobileNum;
    int isEditing = 0 ;


    private static final int PICK_IMAGE_REQUEST = 1;
    Mechanic mechanic;

    TextInputEditText address, name, primaryPhone, secondaryPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mech_register);








        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");


       db  = FirebaseFirestore.getInstance();
      //  DocumentReference noteRef = db.document("Notebook/My First Note");




        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("please wait...")
                .fadeColor(Color.DKGRAY).build();


        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        primaryMobileNum = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        address = findViewById(R.id.editTextAddress);
        mechTypeLabel = findViewById(R.id.labelMechanicType);
        mImageView = findViewById(R.id.mechanicImageView);
        submit = findViewById(R.id.buttonContinue);
        chooseImage = findViewById(R.id.chooseImage);
        name = findViewById(R.id.editTextName);
        primaryPhone = findViewById(R.id.editTextPhone);
        secondaryPhone = findViewById(R.id.editTextPhoneSecondary);
        chipGroup = (ChipGroup) findViewById(R.id.gender);



        Intent i = getIntent();
        mechanic = (Mechanic) i.getSerializableExtra("MECHANIC");
        if (mechanic != null){

            isEditing = 1 ;

            id = i.getStringExtra("ID");
            mImageUri = Uri.parse(mechanic.getImageUri());

            chooseImage.setVisibility(View.GONE);


            Picasso.get().load(mechanic.getImageUri()).into(mImageView);
            name.setText(mechanic.getName());
            primaryPhone.setText(mechanic.getPrimaryPhone());
            secondaryPhone.setText(mechanic.getSecondaryPhone());
            address.setText(mechanic.getAddress());


            submit.setText("Modify");



        }



        if (primaryMobileNum != null) {
            primaryPhone.setText(primaryMobileNum);
            primaryPhone.setEnabled(false);
        }

        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getCurrentAddress(v);
                }

            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentAddress(v);
            }
        });





        list = new ArrayList<>();
        for (int j = 0; j < chipGroup.getChildCount(); j++) {
            Chip chip = (Chip) chipGroup.getChildAt(j);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        list.add(buttonView.getText().toString());
                    } else {
                        list.remove(buttonView.getText().toString());
                    }

                }
            });
        }
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing == 1) {
                    modifyValidation();
                }else {
                    validate();
                }

            }
        });



       /* tv = findViewById(R.id.regtvinfo);

        intent = getIntent();
        String uname = (String) intent.getStringExtra("FUSER");
        if (uname != null)
        {
            tv.setText("New user :"+uname);
        }
*/
    }

    private void EditandSave() {



            dialog.show();

           /* Mechanic mechanic = new Mechanic(
                    name.getText().toString().trim(),
                    primaryMobileNum,
                    secondaryPhone.getText().toString().trim(),
                    address.getText().toString(),
                    list
            );*/


        DocumentReference documentReference = db.collection("mechanics").document(id);
        documentReference.update("name", name.getText().toString().trim());
        documentReference.update("secondaryPhone", secondaryPhone.getText().toString().trim());
        documentReference.update("address", address.getText().toString().trim());
        documentReference.update("mechanicTypes", list)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        Toast.makeText(MechRegister.this,"Document Updated",Toast.LENGTH_LONG).show();
                        startMechProfile();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(MechRegister.this,e.getMessage(),Toast.LENGTH_LONG).show();
                       // Log.d("Androidview", e.getMessage());
                    }
                });
    }


    private void validate() {
        if (mImageUri == null) {
            chooseImage.requestFocus();
            Toast.makeText(this, "please choose Image...", Toast.LENGTH_SHORT).show();


        } else if (name.getText().toString().trim().isEmpty() || name.getText().toString().trim() == null) {
            name.setError("please Enter correct Name...");
            name.requestFocus();
        } else if (address.getText().toString().trim().isEmpty() || address.getText().toString().trim() == null) {
            address.setError("please Enter correct Address...");
            address.requestFocus();
        } else if (primaryPhone.getText().toString().trim().isEmpty()) {
            primaryPhone.setError("please Enter correct phone Number...");
            primaryPhone.requestFocus();
        } else if (secondaryPhone.getText().toString().trim().isEmpty() || secondaryPhone.getText().toString().trim().length() != 10) {
            secondaryPhone.setError("please Enter correct phone Number...");
            secondaryPhone.requestFocus();
        } else if (list.isEmpty()) {
            mechTypeLabel.requestFocus();
            Toast.makeText(this, "select Mechanic Type...", Toast.LENGTH_SHORT).show();
        } else {
            uploadFile();
        }


    }


    public void getCurrentAddress(View view) {
        intent = new Intent(getApplicationContext(), Address.class);
        startActivityForResult(intent, 2);
    }

    void modifyValidation(){

        if (name.getText().toString().trim().isEmpty() || name.getText().toString().trim() == null) {
            name.setError("please Enter correct Name...");
            name.requestFocus();
        } else if (address.getText().toString().trim().isEmpty() || address.getText().toString().trim() == null) {
            address.setError("please Enter correct Address...");
            address.requestFocus();
        } else if (primaryPhone.getText().toString().trim().isEmpty()) {
            primaryPhone.setError("please Enter correct phone Number...");
            primaryPhone.requestFocus();
        } else if (secondaryPhone.getText().toString().trim().isEmpty() || secondaryPhone.getText().toString().trim().length() != 10) {
            secondaryPhone.setError("please Enter correct phone Number...");
            secondaryPhone.requestFocus();
        } else if (list.isEmpty()) {
            mechTypeLabel.requestFocus();
            Toast.makeText(this, "select Mechanic Type...", Toast.LENGTH_SHORT).show();
        } else {
            EditandSave();
        }


    }



    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (resultCode == 2) {
                String message = data.getStringExtra("MESSAGE");
                address.setText(message);


            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mImageView);


        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            dialog.show();
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    //Toast.makeText(MechRegister.this, "Upload successful URL : " + imageUrl, Toast.LENGTH_LONG).show();
                                    updateDatabase(imageUrl);
                                }
                            });

                        }

                        private void updateDatabase(String imageUrl) {
                            Mechanic upload = new Mechanic(
                                    name.getText().toString().trim(),
                                    imageUrl,
                                    primaryMobileNum,
                                    secondaryPhone.getText().toString().trim(),
                                    address.getText().toString(),
                                    list
                            );



                            db.collection("mechanics").document(id)
                                    .set(upload)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (dialog.isShowing())
                                            {
                                                dialog.dismiss();
                                                Toast.makeText(MechRegister.this, "success in firestore", Toast.LENGTH_SHORT).show();
                                                startMechProfile();
                                            }

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MechRegister.this, "failure in firestore", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    });




/*
                            mDatabaseRef.child(id)
                                    .setValue(upload)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(MechRegister.this, "Success...", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MechRegister.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
*/
                     }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MechRegister.this, "Image Upload Failure : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    })
                  /*  .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toast.makeText(MechRegister.this, "completed...", Toast.LENGTH_SHORT).show();
                        }
                    })*/;
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void startMechProfile() {
        startActivity(new Intent(getApplicationContext(), MechProfile.class));
        finish();
    }


}
