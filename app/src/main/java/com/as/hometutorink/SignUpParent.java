package com.as.hometutorink;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpParent extends AppCompatActivity {

    private EditText email,password, fname, lname, address;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    private static final String TAG = "signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_parent);

        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);
        fname = findViewById(R.id.fNameInput);
        lname = findViewById(R.id.lNameInput);
        address = findViewById(R.id.addressInput);
        btnSignUp = findViewById(R.id.SignUpbtn);
        mAuth = FirebaseAuth.getInstance();


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email_text = email.getText().toString();
                String pass_text = password.getText().toString();
                String fname_text = fname.getText().toString();
                String lname_text = lname.getText().toString();
                String address_text = address.getText().toString();

             //   createAccount(email_text,pass_text,fname_text,lname_text,address_text);

                String curr_mode = getIntent().getStringExtra("mode_sg");


                if (curr_mode.equals("Parent"))
                {
                    Intent toChildInfo = new Intent(SignUpParent.this,ChildInfo.class);
                    startActivity(toChildInfo);
                }else if (curr_mode.equals("Tutor"))
                {
                    Intent toQualifications = new Intent(SignUpParent.this,SignUpTutorQualifications.class);
                    startActivity(toQualifications);
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            Log.d(TAG,currentUser.getEmail());
        }

    }

    private void updateUI() {

       // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent toAddChild = new Intent(SignUpParent.this,AddNewChild.class);
        startActivity(toAddChild);
        // finish();

    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }

    private void createAccount(String email, String password, final String fname, final String lname, final String address) {
        Log.d(TAG, "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addNewParent(fname,lname,address, user);
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpParent.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }
                    }
                });
    }

    private void addNewParent(String fname, String lname, String address, FirebaseUser user) {


                Map<String, String> parent_details = new HashMap<String, String>();
                parent_details.put("first_name",  fname);
                parent_details.put("last_name", lname);
                parent_details.put("address", address);


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("parent");

                myRef.child(user.getUid()).setValue(parent_details);


    }


}
