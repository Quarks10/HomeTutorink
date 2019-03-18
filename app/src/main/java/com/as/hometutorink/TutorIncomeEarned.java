package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TutorIncomeEarned extends AppCompatActivity {

    ImageButton btnHomePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_income_earned);
        btnHomePage = findViewById(R.id.homebtn);

        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomePage = new Intent(getApplicationContext(),HomePageTutor.class);
                startActivity(toHomePage);
            }
        });


    }
}
