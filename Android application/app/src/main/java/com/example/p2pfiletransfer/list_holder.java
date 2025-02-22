package com.example.p2pfiletransfer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class list_holder extends RecyclerView.ViewHolder {

    TextView txt_name,txt_size;
    ImageView img_download,img_trash;

    public list_holder(@NonNull View itemView) {
        super(itemView);

        txt_name = itemView.findViewById(R.id.list_name);
        txt_size = itemView.findViewById(R.id.list_size);
        img_download = itemView.findViewById(R.id.img_download);
        img_trash = itemView.findViewById(R.id.image_trash);


    }
}
