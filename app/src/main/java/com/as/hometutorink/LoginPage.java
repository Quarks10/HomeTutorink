package com.as.hometutorink;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.oob.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    private EditText email, password;
    private TextView currentMode;
    private Button signUp, login;
    private static final String TAG = "Login";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        signUp = findViewById(R.id.signupbtn);
        login = findViewById(R.id.loginbtn);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.inputEmailLogin);
        password = findViewById(R.id.pwInputLogin);

        init();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String curr_mode = getIntent().getStringExtra("mode");

                Log.d(TAG, "sign Up:" + curr_mode);

                if (curr_mode.equals("Parent")) {
                    Intent toSignUp = new Intent(LoginPage.this, SignUpParent.class);
                    toSignUp.putExtra("mode_sg", curr_mode);
                    startActivity(toSignUp);
                } else if (curr_mode.equals("Tutor")) {
                    Intent toSignUp = new Intent(LoginPage.this, SignUpParent.class);
                    toSignUp.putExtra("mode_sg", curr_mode);
                    startActivity(toSignUp);
                }


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginAccount();

            }
        });

    }

    public void init() {
        currentMode = findViewById(R.id.mode_text);
        String curr_mode = getIntent().getStringExtra("mode");
        currentMode.setText(curr_mode);
    }

    public void loginAccount() {
        String emailtext = email.getText().toString();
        String pwtext = password.getText().toString();


        mAuth.signInWithEmailAndPassword(emailtext, pwtext)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                           // FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginPage.this, "Authentication failed, Try again",
                                    Toast.LENGTH_SHORT).show();
                            //  updateUI(null);
                        }
                    }
                });



    }

    public void updateUI()
    {
        String curr_mode = getIntent().getStringExtra("mode");

        Log.d(TAG, "login:" + curr_mode);

        if (curr_mode.equals("Parent")) {
            Log.d(TAG, "login: in" + curr_mode);
            Intent toSignIn = new Intent(LoginPage.this, HomePage.class);
            startActivity(toSignIn);

        } else if (curr_mode.equals("Tutor")) {
            Log.d(TAG, "login: in" + curr_mode);
            Intent toSignIn = new Intent(LoginPage.this, HomePageTutor.class);
            startActivity(toSignIn);
        }
    }

}
