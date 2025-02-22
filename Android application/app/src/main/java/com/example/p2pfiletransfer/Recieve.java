package com.example.p2pfiletransfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Recieve extends AppCompatActivity {

    LinearLayout btn_recieve,btn_send;
    ArrayList array_name,arr_size,arr_status,arr_file_name;
    RecyclerView recyclerView;
    ImageView img_hotspot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recieve);

        recyclerView = findViewById(R.id.recieve_recyclerview);
        img_hotspot = findViewById(R.id.recieve_img_hotspot);

        btn_recieve = findViewById(R.id.recieve_message_btn);
        btn_send = findViewById(R.id.recieve_send_btn);

        array_name = new ArrayList();
        arr_size = new ArrayList();
        arr_status = new ArrayList();
        arr_file_name = new ArrayList();



        String strurl = "http://172.16.14.3:5000/files";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, strurl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Loop through the array
                            for (int i = 0; i < response.length(); i++) {
                                String fileName = response.getString(i);

                                array_name.add(fileName);
                                arr_size.add(String.valueOf(10/1000));
                                arr_status.add("not");
                                Toast.makeText(Recieve.this, "kjg", Toast.LENGTH_SHORT).show();
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(new list_adapter(getApplicationContext(),array_name,arr_size,arr_status));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        Toast.makeText(this, arr_file_name.toString(), Toast.LENGTH_SHORT).show();

        Volley.newRequestQueue(this).add(request);



        img_hotspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Connect_hotspot.class);
                startActivity(intent);
                finish();
            }
        });



        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        btn_recieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Message.class);
                startActivity(intent);
                finish();
            }
        });

    }
}