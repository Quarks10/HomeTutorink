package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TutorRequestViewApp extends AppCompatActivity {

    Button view_open, view_recommended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_request_view_app);

        view_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toviewopen = new Intent(TutorRequestViewApp.this,TutorRequestOpen.class);
                startActivity(toviewopen);
            }
        });

        view_recommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toviewrecommmended = new Intent(TutorRequestViewApp.this,TutorRequestRecommend.class);
                startActivity(toviewrecommmended);
            }
        });


    }
}
