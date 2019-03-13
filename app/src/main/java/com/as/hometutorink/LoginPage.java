package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity {

    TextView currentMode;
    Button signUp, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        signUp = findViewById(R.id.signupbtn);
        login = findViewById(R.id.loginbtn);
        init();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignUp = new Intent(LoginPage.this,SignUpParent.class);
                startActivity(toSignUp);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent toSignIn = new Intent(LoginPage.this,SignUpParent.class);
                //startActivity(toSignIn);
            }
        });

    }

    public void init()
    {
        currentMode = findViewById(R.id.mode_text);
        String curr_mode = getIntent().getStringExtra("mode");
        currentMode.setText(curr_mode);
    }


}
