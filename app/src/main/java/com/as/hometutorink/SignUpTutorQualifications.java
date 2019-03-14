package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpTutorQualifications extends AppCompatActivity {

    Button signUpbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_tutor_qualifications);

        signUpbtn = findViewById(R.id.SignUpbtnQualification);

        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomepageTutor = new Intent(SignUpTutorQualifications.this,HomePageTutor.class);
                startActivity(toHomepageTutor);
            }
        });


    }
}
