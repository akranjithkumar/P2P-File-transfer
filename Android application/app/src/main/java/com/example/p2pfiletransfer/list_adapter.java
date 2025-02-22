package com.example.p2pfiletransfer;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class list_adapter extends RecyclerView.Adapter<list_holder> {

    Context context;
    String url;
    ArrayList array_name,arr_size,arr_status;
    ArrayList arr_completed;
    Data data;

    public list_adapter(Context context, ArrayList array_name, ArrayList arr_size, ArrayList arr_status, String url) {
        this.context = context;
        this.array_name = array_name;
        this.arr_size = arr_size;
        this.arr_status = arr_status;
        this.url = url;
        data = new Data(context);
    }

    @NonNull
    @Override
    public list_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new list_holder(LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull list_holder holder, int position) {

        holder.img_download.setImageResource(R.drawable.make_download);
        holder.img_trash.setImageResource(R.drawable.trash);

        holder.img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = array_name.get(position).toString();
                String fileUrl = data.getString("url")+"/download/"+ fileName;

                DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(fileUrl));
                request1.setTitle("Downloading "+ fileName);
                request1.setDescription("Please wait...");
                request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request1.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);


                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                long downloadId = manager.enqueue(request1); // Store download ID

                BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long completedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                        if (completedDownloadId == downloadId) { // Check if it's the same file
                            Toast.makeText(context, "Download Completed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.registerReceiver(onDownloadComplete, filter, Context.RECEIVER_NOT_EXPORTED);
                } else {
                    context.registerReceiver(onDownloadComplete, filter);
                }
            }
        });

        holder.img_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = array_name.get(position).toString();
                Toast.makeText(context, fileName, Toast.LENGTH_SHORT).show();

                String del_url = data.getString("url")+ "/delete/" + fileName; // Change this URL to match your API

                // Creating a request queue
                RequestQueue queue = Volley.newRequestQueue(context);

                // Creating a DELETE request
                StringRequest del_request = new StringRequest(Request.Method.DELETE, del_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (position >= 0 && position < array_name.size()) {
                                    array_name.remove(position);
                                    arr_status.remove(position);
                                    arr_size.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, array_name.size()); // Adjust remaining items
                                }


                                Log.d("Response", "File Deleted Successfully: " + response);
                                Toast.makeText(context, "File deleted successfully!", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse != null) {
                                    String errorMessage = "Error: " + error.networkResponse.statusCode + " - " + new String(error.networkResponse.data);
                                    Log.e("Response", errorMessage);
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("Response", "Error: " + error.getMessage());
                                    Toast.makeText(context, "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json"); // Adjust if needed
                        return headers;
                    }
                };

                queue.add(del_request);

            }
        });

        holder.txt_name.setText(String.valueOf(array_name.get(position)));
        holder.txt_size.setText(String.valueOf(arr_size.get(position)));

    }

    @Override
    public int getItemCount() {
        return array_name.size();
    }
}
