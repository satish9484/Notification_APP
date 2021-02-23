package com.example.Notification_App.Notification;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Notification_App.R;

import java.util.HashMap;
import java.util.Map;

public class SendNotificationToAll extends AppCompatActivity implements View.OnClickListener {

    private EditText Enter_Title, Enter_Message, Get_Image_URL;
    private Button Send_Without_ID;
    ProgressDialog progressDialog;
    String Image_URL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification_to_all);

        Send_Without_ID = findViewById(R.id.send_message);
        Enter_Title = findViewById(R.id.editTextTitle);
        Enter_Message = findViewById(R.id.editTextMessage);
        Get_Image_URL = findViewById(R.id.editTextImage);

        progressDialog = new ProgressDialog(this);

        Send_Without_ID.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == Send_Without_ID) {
            SendToAll();
        }
    }

    private void SendToAll() {
        final String title = Enter_Title.getText().toString().trim();
        final String message = Enter_Message.getText().toString().trim();
        Image_URL = Get_Image_URL.getText().toString().trim();

        progressDialog.setMessage("Sending Notification...");
        progressDialog.show();


        if (title.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Enter Title", Toast.LENGTH_LONG).show();
            return;
        } else if (message.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Enter Message", Toast.LENGTH_LONG).show();
            return;
        } else {

            // Image URl Is Empty
            if (Image_URL.isEmpty()) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SEND_MULTIPLE_PUSH,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                if (response.equalsIgnoreCase("")) {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                } else {
                                     Toast.makeText(getApplicationContext(), "  Sended  Successfully   ", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.hide();
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
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

            } else {
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SEND_MULTIPLE_PUSH,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                if (response.equalsIgnoreCase("")) {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                } else {
                                     Toast.makeText(getApplicationContext(), "  Sended  Successfully   ", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.hide();
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
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }

        }


    }
}
