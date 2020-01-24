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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Address extends AppCompatActivity {


    private String TAG = "Address";
    TextView emptyElement;
    EditText addressEd;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    String data;

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    void fetch_listview(String[] strings) {
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, strings);
        listView.setAdapter(arrayAdapter);
        listView.setEmptyView(findViewById(R.id.emptyElement));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                String message = ((TextView) view).getText().toString();
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                // String message=editText1.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", message);
                setResult(2, intent);
                finish();//finishing activity


            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        addressEd = findViewById(R.id.address);
        Button searchButton = findViewById(R.id.search_button);
        listView = findViewById(R.id.listView1);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = addressEd.getText().toString();
                addressEd.requestFocus();
                if (address.isEmpty()) {
                    addressEd.setError("Enter a valid address");
                    addressEd.requestFocus();
                    return;
                }
                hideSoftKeyboard(v);
                String url = "https://nominatim.openstreetmap.org/search?q=" + address + "&countrycodes=in&format=json";
                //addressEd.setText("");
                OkHttpClient httpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "error in getting response using async okhttp call");
                        Address.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Address.this, "something went wrong! please check Internet Connectivity...", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }


                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            data = response.body().string();
                            Address.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        JSONArray jsonArray = new JSONArray(data);
                                        String[] strings = new String[jsonArray.length()];
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                                            String display_names = jsonObject.getString("display_name");
                                            strings[i] = display_names;


                                            /*String[] display_names = jsonObject.getString("display_name").split(",");

                                            strings[i] = display_names[0];

                                            stringBuilder.append(strings[i] + "\n");*/

                                        }
                                        fetch_listview(strings);
                                        //textView.setText(stringBuilder.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            });
                        }


                    }
                });


            }
        });


    }
    // https://nominatim.openstreetmap.org/search?city=Dharmavaram&countrycodes=in&format=json

}
