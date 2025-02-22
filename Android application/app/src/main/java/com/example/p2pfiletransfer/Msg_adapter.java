package com.example.p2pfiletransfer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Msg_adapter extends RecyclerView.Adapter<Msg_holder> {
    Context context;
    ArrayList arr_send,arr_recieve;

    public Msg_adapter(Context context, ArrayList arr_send, ArrayList arr_recieve) {
        this.context = context;
        this.arr_send = arr_send;
        this.arr_recieve = arr_recieve;
    }
    @NonNull
    @Override
    public Msg_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Msg_holder(LayoutInflater.from(context).inflate(R.layout.msg_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull Msg_holder holder, int position) {
        if(arr_send.get(position).toString().equals("")){
            holder.recieve_box.setVisibility(View.INVISIBLE);
            holder.txt_send.setText(arr_send.get(position).toString());
            holder.txt_recieve.setText(arr_recieve.get(position).toString());
        }
        else {
            holder.send_box.setVisibility(View.INVISIBLE);
            holder.txt_send.setText(arr_send.get(position).toString());
            holder.txt_recieve.setText(arr_recieve.get(position).toString());
        }
    }

    @Override
    public int getItemCount() {
        return arr_send.size();
    }
}
