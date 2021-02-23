package com.example.Notification_App.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Notification_App.R;


public class Notification extends AppCompatActivity {


    private Button With_ID, Without_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        With_ID = findViewById(R.id.Send_to_Single);
        Without_ID = findViewById(R.id.Send_to_All);

		
        With_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SendNotificationToSingleUser.class));
            }
        });

        Without_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SendNotificationToAll.class));
            }
        });

    }




}
