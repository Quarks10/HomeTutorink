package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TutorRequestRecommend extends AppCompatActivity {

    Button view_open, view_application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_request_recommend);

        view_open = findViewById(R.id.openbtnrq);
        view_application = findViewById(R.id.viewAppbtnrq);


        view_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toviewopen = new Intent(TutorRequestRecommend.this,TutorRequestOpen.class);
                startActivity(toviewopen);
            }
        });

        view_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toviewrecommmended = new Intent(TutorRequestRecommend.this,TutorRequestViewApp.class);
                startActivity(toviewrecommmended);
            }
        });



    }
}
