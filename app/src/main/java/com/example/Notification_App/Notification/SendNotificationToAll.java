package com.example.Notification_App.Notification;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.Notification_App.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.Map;

public class SendNotificationToAll extends AppCompatActivity implements View.OnClickListener {
    private EditText enter_title, enter_message;
    private ImageView imageView;
    private Button send_to_all,Brows_image;
    ProgressDialog progressDialog;
    private String Image_URL = null;
    private int count=1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification_to_all);

        send_to_all = findViewById(R.id.send_message);
        Brows_image = findViewById(R.id.Brows_image);
        enter_title = findViewById(R.id.editTextTitle);
        enter_message = findViewById(R.id.editTextMessage);
        imageView = findViewById(R.id.Image_ID);

        progressDialog = new ProgressDialog(this);

        send_to_all.setOnClickListener(this);

        // Constants.URL_SEND_MULTIPLE_PUSH

        Brows_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(SendNotificationToAll.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Please select Image"), 1);
                            }


                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                // count=1;

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            uploadToFirebase();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @Override
    public void onClick(View v) {
        if(v == send_to_all){
            SendToAll();
        }

    }

    private void uploadToFirebase() {
        if (imageUri != null) {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("Notification_Image/" + System.currentTimeMillis() + "." + getFileExtention(imageUri));
            riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();

                    final String download_url = String.valueOf(downloadUrl);

                    // HashMap<String, Object> hashMap = new HashMap<>();
                    // hashMap.put("Image_URL", download_url);

                    Image_URL = download_url;

                    Glide.with(SendNotificationToAll.this).load(Image_URL).into(imageView);
                    count = 0;

                    progressDialog.dismiss();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "upload Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }


    private void SendToAll() {

        progressDialog.setMessage("Sending Notification...");
        progressDialog.show();


        final String title = enter_title.getText().toString().trim();
        final String message = enter_message.getText().toString().trim();


        if (title.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Enter Title", Toast.LENGTH_LONG).show();
            return;
        } else if (message.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Enter Message", Toast.LENGTH_LONG).show();
            return;
        } else {


            StringRequest stringRequest;
            if(count == 1){
                stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SEND_MULTIPLE_PUSH,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                enter_title.setText("");
                                enter_message.setText("");

                                progressDialog.dismiss();
                                if (response.equalsIgnoreCase("")) {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "   Successfully Send   ", Toast.LENGTH_LONG).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("title", title);
                        params.put("message", message);
                        return params;
                    }
                };

            }
            else{
                stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SEND_MULTIPLE_PUSH,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                progressDialog.dismiss();

                                enter_title.setText("");
                                enter_message.setText("");
                                imageView.setImageResource(0);

                                if (response.equalsIgnoreCase("")) {
                                    Toast.makeText(getApplicationContext(), "   Successfully Send to All  ", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("title", title);
                        params.put("message", message);
                        params.put("image", Image_URL);
                        return params;
                    }
                };
            }
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }
    }
}


