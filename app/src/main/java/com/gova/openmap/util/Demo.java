package com.gova.openmap.util;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.gova.openmap.R;

import java.util.ArrayList;

public class Demo extends Activity {


    ChipGroup chipGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        String[] strings = {"one","two","three","one","two","three","one","two","three","one","two","three","one","two","three"};


        chipGroup = (ChipGroup)findViewById(R.id.gender);

        final ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < chipGroup.getChildCount(); i++){
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        list.add(buttonView.getText().toString());
                    }else{
                        list.remove(buttonView.getText().toString());
                    }
                    if(!list.isEmpty()){
                        Toast.makeText(Demo.this, list.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }
}
