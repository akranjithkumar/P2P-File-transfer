package com.example.p2pfiletransfer;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private static final int PICK_FILES_REQUEST_CODE = 1;
    LinearLayout btn_choose;
    int PICK_FILE_REQUEST_CODE = 1;
    RecyclerView recyclerView,recycle_completed;
    ArrayList array_name,arr_size,arr_status;
    TextView txt_check;
    ImageView img_hotspot;
    LinearLayout btn_recieve,btn_message;
    list_adapter adapter;
    ArrayList arr_file_name;
    String url;
    Data data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txt_check = findViewById(R.id.txt_head);

        btn_recieve = findViewById(R.id.main_recieve_btn);
        btn_message = findViewById(R.id.main_message_btn);

        btn_choose = findViewById(R.id.btn_buy);
        recyclerView = findViewById(R.id.message_recyclerview);
        img_hotspot = findViewById(R.id.img_hotspot);
        recycle_completed = findViewById(R.id.message_recyclerview_completed);

        data = new Data(getApplicationContext());
        array_name = new ArrayList();
        arr_size = new ArrayList();
        arr_status = new ArrayList();
        arr_file_name = new ArrayList();


        if(data.getAll().isEmpty()){
            ScanOptions options = new ScanOptions();
            options.setPrompt("Volume up");
            options.setBarcodeImageEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(Capture.class);
            barLauncer.launch(options);

        }




        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Message.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("url",url);
                startActivity(intent);
                finish();
            }
        });

        btn_recieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Recieve.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("url",url);
                startActivity(intent);
                finish();
            }
        });


        img_hotspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*"); // You can specify a specific type like "image/*" or "text/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_FILES_REQUEST_CODE);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILES_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    // Multiple files selected
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri uri = clipData.getItemAt(i).getUri();
                        try {
                            displayFileInfo(uri);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {

                    // Single file selected
                    Uri uri = data.getData();
                    try {
                        displayFileInfo(uri);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private void displayFileInfo(Uri uri) throws FileNotFoundException {


        getFileFromUri(getApplicationContext(),uri,data.getString("url"));




    }


    private static void getFileFromUri(Context context, Uri uri,String data) {
        try {
            String result = "temp_file";
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }

            File tempFile = new File(context.getCacheDir(), result);
            try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
                 OutputStream outputStream = new FileOutputStream(tempFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            if (tempFile == null) {
                Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                return;
            }

            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", tempFile.getName(),
                            RequestBody.create(tempFile, MediaType.parse("application/octet-stream")))
                    .build();

            Request request = new Request.Builder()
                    .url(data+"/upload")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                }
            });


            Toast.makeText(context, requestBody.toString(), Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }
}


    ActivityResultLauncher<ScanOptions> barLauncer = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            url = result.getContents().toString();
            data.putString("url",url);

            //Toast.makeText(this, result.getContents().toString(), Toast.LENGTH_SHORT).show();
        }


    });

}