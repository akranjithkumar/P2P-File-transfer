package com.example.p2pfiletransfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Message extends AppCompatActivity {

    LinearLayout btn_recieve,btn_send;
    RecyclerView recyclerView;

    ArrayList arr_send,arr_recieve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_message);

        btn_recieve = findViewById(R.id.message_recieve_btn);
        btn_send = findViewById(R.id.message_send_btn);
        recyclerView = findViewById(R.id.message_recyclerview);

        arr_send = new ArrayList();
        arr_recieve = new ArrayList();

        arr_send.add("");
        arr_recieve.add("hi");
        arr_send.add("How are you");
        arr_recieve.add("");
        arr_recieve.add("Iam fine");
        arr_send.add("");
        arr_recieve.add("What are you doing");
        arr_send.add("");
        arr_recieve.add("My name is ranjith kumar ");
        arr_send.add("");

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new Msg_adapter(getApplicationContext(),arr_send,arr_recieve));


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
            }
        });

        btn_recieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Recieve.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

    }
}