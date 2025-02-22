package com.example.p2pfiletransfer;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class VolleyMultipartRequest {

    public static void uploadFile(Context context, String filePath) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "";  // Use 10.0.2.2 for localhost in Emulator

        try {

            File file = new File(filePath);
            if (!file.exists()) {
                Log.e("FileUploader", "File does not exist");
                return;
            }

            // Read file and convert to Base64
            FileInputStream fis = new FileInputStream(file);
            byte[] fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            fis.close();
            String encodedFile = Base64.encodeToString(fileBytes, Base64.DEFAULT);

            // Create JSON object
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("file_name", file.getName());
            jsonRequest.put("file_data", encodedFile);
            Toast.makeText(context, jsonRequest.toString(), Toast.LENGTH_SHORT).show();

            // Make POST request
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("FileUploader", "Response: " + response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("FileUploader", "Error: " + error.toString());
                        }
                    }
            );

            queue.add(request);

        } catch (IOException | JSONException e) {

            e.printStackTrace();
        }
    }
}
