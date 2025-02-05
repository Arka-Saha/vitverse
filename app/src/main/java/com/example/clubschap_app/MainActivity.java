package com.example.clubschap_app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView username_tv, upcoming_name_tv, upcoming_loc_tv, upcoming_time_tv, upcoming_desc_tv;
    ShapeableImageView user_pfp;
    CardView explorecard, aicard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        username_tv=findViewById(R.id.user_name);
        user_pfp = findViewById(R.id.user_pfp);
        upcoming_name_tv = findViewById(R.id.upcoming_event_name);
        upcoming_time_tv = findViewById(R.id.upcoming_event_time);
        upcoming_loc_tv = findViewById(R.id.upcoming_event_venue);
        upcoming_desc_tv = findViewById(R.id.upcoming_event_points);
        explorecard = findViewById(R.id.explore_card);
        aicard = findViewById(R.id.ai_card);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db_users = database.getReference("users");
        DatabaseReference db_events = database.getReference("events");

//        DatabaseReference my =         myRef.child("24bce0938").child("name")
//        myRef.child("24bce0938").child("name").setValue("Arka");
//        myRef.child("24bce0938").child("club").setValue("mewo club");
//        myRef.child("24bce0938").child("num").setValue("12");

//        myRef.child("arka").setValue("hehe");

        Intent intent = getIntent();
        String reg_num = intent.getStringExtra("regnum");

//        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();


        db_users.child(reg_num.toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String uripfp = snapshot.child("pfp").getValue().toString();
                String upcoming_event_club = snapshot.child("upcoming").getValue().toString();

                Glide.with(getApplicationContext()).load(uripfp).into(user_pfp);


                db_events.child(upcoming_event_club.toLowerCase()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot){
                        String upcoming_name = snapshot.child("event_name").getValue().toString();
                        String upcoming_loc = snapshot.child("event_loc").getValue().toString();
                        String upcoming_time = snapshot.child("event_time").getValue().toString();
                        String upcoming_desc = snapshot.child("event_desc").getValue().toString();

                        upcoming_name_tv.setText(upcoming_name);
                        upcoming_loc_tv.setText(upcoming_loc);
                        upcoming_time_tv.setText(upcoming_time);
                        upcoming_desc_tv.setText(upcoming_desc.replace("$", "\n"));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                username_tv.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();

            }
        });






        explorecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EventsActivity.class);
                i.putExtra("regnum", reg_num);
                startActivity(i);
            }
        });

        aicard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AiActivity.class));
            }
        });


    }
}