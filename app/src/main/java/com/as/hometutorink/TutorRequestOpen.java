package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TutorRequestOpen extends AppCompatActivity {

    Button view_application, view_recommended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_request_open);

        view_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toviewapplication = new Intent(TutorRequestOpen.this,TutorRequestViewApp.class);
                startActivity(toviewapplication);
            }
        });

        view_recommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toviewrecommmended = new Intent(TutorRequestOpen.this,TutorRequestRecommend.class);
                startActivity(toviewrecommmended);
            }
        });
    }
}
