package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private CalendarView cv;
    private TextView tv;
    private Button parent_button;
    private Button tutor_button;
    private static final String TAG = "MyActivity";
    private String choose_mode = "";
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent_button = findViewById(R.id.ParentLoginbtn);
        tutor_button = findViewById(R.id.TutorLoginbtn);

        parent_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_mode = parent_button.getText().toString();
                set_mode(choose_mode);
            }
        });

        tutor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_mode = tutor_button.getText().toString();
                set_mode(choose_mode);
            }
        });
    }


    public void set_mode(String mode_selected)
    {
        Intent moveLoginPage = new Intent(MainActivity.this,LoginPage.class);
        moveLoginPage.putExtra("mode",mode_selected);
        startActivity(moveLoginPage);
    }



}
