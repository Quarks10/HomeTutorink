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

public class SignUp extends AppCompatActivity {

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
              //  String parent_id = mAuth.getUid();

                String curr_mode = getIntent().getStringExtra("mode_sg");
                createAccount(email_text,pass_text,fname_text,lname_text,address_text,curr_mode);


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
        }else
        {
            Log.d(TAG,"no user");
        }

    }

    private void updateUI(String curr_mode, FirebaseUser user) {

        if (curr_mode.equals("Parent"))
        {
            Intent toChildInfo = new Intent(SignUp.this,ChildInfo.class);
            toChildInfo.putExtra("Parent_uid", user.getUid());
            startActivity(toChildInfo);
        }else if (curr_mode.equals("Tutor"))
        {
            Intent toQualifications = new Intent(SignUp.this,SignUpTutorQualifications.class);
            startActivity(toQualifications);
        }


       // Intent toAddChild = new Intent(SignUp.this,AddNewChild.class);
      //  startActivity(toAddChild);
        // finish();

    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }

    private void createAccount(String email, String password, final String fname, final String lname, final String address, final String curr_mode) {
        Log.d(TAG, "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (curr_mode.equals("Parent")){
                                addNewParent(fname,lname,address, user);
                            }else {
                                addNewTutor(fname,lname,address, user);
                            }

                            updateUI(curr_mode,user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }
                    }
                });
    }

    private void addNewParent(String fname, String lname, String address, FirebaseUser user) {

        Map<String, String> parent_details = new HashMap<String, String>();
       //parent_details.put("parent_id",  user.getUid());
        parent_details.put("first_name",  fname);
        parent_details.put("last_name", lname);
        parent_details.put("address", address);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("parent");

        myRef.child(user.getUid()).setValue(parent_details);


    }

    private void addNewTutor(String fname, String lname, String address, FirebaseUser user) {

        Map<String, String> tutor_details = new HashMap<String, String>();
       // tutor_details.put("tutor_id",  user.getUid());
        tutor_details.put("first_name",  fname);
        tutor_details.put("last_name", lname);
        tutor_details.put("address", address);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tutor");

        myRef.child(user.getUid()).setValue(tutor_details);


    }


}
