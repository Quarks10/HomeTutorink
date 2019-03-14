package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.oob.SignUp;

public class LoginPage extends AppCompatActivity {

    TextView currentMode;
    Button signUp, login;
    private static final String TAG = "Login";

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

                String curr_mode = getIntent().getStringExtra("mode");

                Log.d(TAG,"sign Up:" + curr_mode);

                if (curr_mode.equals("Parent"))
                {
                    Intent toSignUp = new Intent(LoginPage.this,SignUpParent.class);
                    toSignUp.putExtra("mode_sg",curr_mode);
                    startActivity(toSignUp);
                }else if (curr_mode.equals("Tutor"))
                {
                    Intent toSignUp = new Intent(LoginPage.this, SignUpParent.class);
                    toSignUp.putExtra("mode_sg",curr_mode);
                    startActivity(toSignUp);
                }



            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String curr_mode = getIntent().getStringExtra("mode");

                Log.d(TAG,"login:" + curr_mode);

                if (curr_mode.equals("Parent"))
                {
                    Log.d(TAG,"login: in" + curr_mode);
                    Intent toSignIn = new Intent(LoginPage.this,HomePage.class);
                    startActivity(toSignIn);

                }else if(curr_mode.equals("Tutor"))
                {
                    Log.d(TAG,"login: in" + curr_mode);
                    Intent toSignIn = new Intent(LoginPage.this,HomePageTutor.class);
                    startActivity(toSignIn);
                }

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
