package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class HomeMessageTutor extends AppCompatActivity {

    ImageButton btnHomePage;
    CardView cardView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_message_tutor);


        mAuth = FirebaseAuth.getInstance();

        btnHomePage = findViewById(R.id.homebtn);
        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomePage = new Intent(getApplicationContext(),HomePageTutor.class);
                startActivity(toHomePage);
            }
        });

        cardView = findViewById(R.id.cvmessage);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSecondary = new Intent(HomeMessageTutor.this,MessageContentTutor.class);
                startActivity(toSecondary);
            }
        });




    }
}
