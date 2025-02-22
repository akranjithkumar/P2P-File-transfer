package com.example.p2pfiletransfer;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class Msg_holder extends RecyclerView.ViewHolder {

    TextView txt_send,txt_recieve;
    ConstraintLayout send_box,recieve_box;

    public Msg_holder(@NonNull View itemView) {
        super(itemView);

        txt_send = itemView.findViewById(R.id.msg_layout_txt_send);
        txt_recieve = itemView.findViewById(R.id.msg_layout_txt_recieve);

        send_box = itemView.findViewById(R.id.send_box);
        recieve_box = itemView.findViewById(R.id.recieve_box);



    }
}
