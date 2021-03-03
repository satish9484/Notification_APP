package com.example.Notification_App;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Notification_App.Notification.Constants;
import com.example.Notification_App.Notification.NonNull;
import com.example.Notification_App.Notification.Notification;
import com.example.Notification_App.Notification.SendNotificationToSingleUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button Send_Notification;
    private EditText User_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Send_Notification = findViewById(R.id.Send_Notification);
        User_Name = findViewById(R.id.User_Name);
        Send_Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = User_Name.getText().toString();

                if (Email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_LONG).show();
                } else {

                    Intent intent = new Intent(MainActivity.this, SendNotificationToSingleUser.class);
                    intent.putExtra(Intent.EXTRA_TEXT,Email );

                   // startActivity(intent);
                    startActivity(new Intent(getApplicationContext(), Notification.class));
                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {

                            if (task.isSuccessful()) {
                                String DeviceToken;
                                DeviceToken = task.getResult().getToken();

                                if (DeviceToken == null) {
                                    Toast.makeText(getApplicationContext(), "Token not generated", Toast.LENGTH_LONG).show();
                                    return;
                                } else {
                                    sendToken(DeviceToken);
                                    //Toast.makeText(getApplicationContext(), "Token generated successfully", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }

            }
        });


    }

    public void sendToken(final String deviceToken) {
        final String TokenName = User_Name.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER_DEVICE,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            // Toast.makeText(HomePage.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", TokenName);
                params.put("token", deviceToken);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}