package com.gova.openmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectMechanicType extends AppCompatActivity {


    ListView listView;
    String[] mechanicTpes = {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mechanic_type);

        FirstUser.mechTypeTV.setText("Select Mechanic");


        listView = findViewById(R.id.mechTypeListView);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mechanicTpes);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, mechanicTpes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value = adapter.getItem(position);
               // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();



                Intent intent = new Intent();
                intent.putExtra("TADA", value);
                setResult(3, intent);
                finish();//finishing activity



            }
        });


    }
}
