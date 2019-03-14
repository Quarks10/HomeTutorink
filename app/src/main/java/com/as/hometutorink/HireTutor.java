package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HireTutor extends AppCompatActivity {

    Button postjob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_tutor);

        postjob = findViewById(R.id.postJobbtn);

        postjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTutorRequestOpen = new Intent(HireTutor.this,TutorRequestOpen.class);
                startActivity(toTutorRequestOpen);
            }
        });

    }
}
