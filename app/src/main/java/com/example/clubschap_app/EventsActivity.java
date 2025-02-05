package com.example.clubschap_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    private static final String API_KEY = "LA-c837da59b95749d8ad8ca553d47395ef841bd398a0be4a66aeccbcaebcd0d821";
    private static final String API_URL = "https://api.llama.com/v1/chat/completions";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events);
//        getSystemService()
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        String reg_num = intent.getStringExtra("regnum");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db_users = database.getReference("users");
        DatabaseReference db_events = database.getReference("events");

        List<String> typeArray = new ArrayList<>();
        List<String> eventArray = new ArrayList<>();

        db_events.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot eventsnap : snapshot.getChildren())
                {
                    String event_type = eventsnap.child("type").getValue().toString();
                    eventArray.add(eventsnap.getKey().toString());
                    typeArray.add(event_type);
                }
                String types_event = TextUtils.join(", ", typeArray);
//                Toast.makeText(EventsActivity.this, types_event, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}