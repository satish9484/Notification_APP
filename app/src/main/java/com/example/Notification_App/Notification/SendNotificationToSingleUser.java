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

import static com.example.Notification_App.Notification.Constants.URL_SEND_SINGLE_PUSH;

public class SendNotificationToSingleUser extends AppCompatActivity implements View.OnClickListener {

    private EditText entertitle, entermessage, getimageurl, enteremail;
    private Button send;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notificatoin_to_single_student);

        send = findViewById(R.id.send_message);
        entertitle = findViewById(R.id.editTextTitle);
        entermessage = findViewById(R.id.editTextMessage);
        enteremail = findViewById(R.id.editTextEmail);
        getimageurl = findViewById(R.id.editTextImage);

        progressDialog = new ProgressDialog(this);

        send.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == send) {
            sendNotification();
        }

    }

    private void sendNotification() {

        final String email = enteremail.getText().toString().trim();
        final String title = entertitle.getText().toString().trim();
        final String message = entermessage.getText().toString().trim();
        final String Image_URLI = getimageurl.getText().toString().trim();


        progressDialog.setMessage("Sending Notification...");
        progressDialog.show();


        if (title.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Title", Toast.LENGTH_LONG).show();
            return;
        } else if (message.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Message", Toast.LENGTH_LONG).show();
            return;
        } else if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_LONG).show();
            return;
        } else {
            if (Image_URLI.isEmpty()) {
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEND_SINGLE_PUSH,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                if (response.equalsIgnoreCase("")) {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                } else {
                                     Toast.makeText(getApplicationContext(), "  Sended Successfully ", Toast.LENGTH_LONG).show();
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
                        params.put("email", email);
                        params.put("title", title);
                        params.put("message", message);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

            } else {
                progressDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEND_SINGLE_PUSH,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                if (response.equalsIgnoreCase("")) {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                } else {
                                     Toast.makeText(getApplicationContext(), "  Sended Successfully ", Toast.LENGTH_LONG).show();
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
                        params.put("email", email);
                        params.put("title", title);
                        params.put("message", message);
                        params.put("image", Image_URLI);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }

        }


    }
}