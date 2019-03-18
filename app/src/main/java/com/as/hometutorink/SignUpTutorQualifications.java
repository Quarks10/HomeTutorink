package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpTutorQualifications extends AppCompatActivity {

    CheckBox chk1_qualifications, chk2_qualifications, chk3_qualifications, chk4_qualifications,chk5_qualifications, chk6_qualifications, chk7_qualifications;
    CheckBox chk1_subject, chk2_subject, chk3_subject, chk4_subject,chk5_subject, chk6_subject, chk7_subject, chk8_subject, chk9_subject, chk10_subject;
    EditText job_title, job_desc;
    Button signUpbtn;
    FirebaseAuth mAuth;
    ArrayList<String> list_qualifications, list_subjects;
    String job_title_text, job_description_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_tutor_qualifications);
        signUpbtn = findViewById(R.id.SignUpbtnQualification);
        job_title = findViewById(R.id.job_title_text);
        job_desc = findViewById(R.id.job_description_text);
        mAuth = FirebaseAuth.getInstance();
        init_chkbox_qualification();
        init_chkbox_subjects();




        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            list_qualifications = checkedQualifications();
            list_subjects = checkedSubjects();
            job_title_text = job_title.getText().toString();
            job_description_text = job_desc.getText().toString();
            FirebaseUser curr_user = mAuth.getCurrentUser();

            addTutorQualifications(list_qualifications,list_subjects,job_title_text,job_description_text, curr_user);

            Intent toHomepageTutor = new Intent(SignUpTutorQualifications.this,HomePageTutor.class);
            startActivity(toHomepageTutor);

            }
        });


    }

    public void addTutorQualifications(ArrayList<String> listQualifications, ArrayList<String> listSubjects, String jobTitle, String jobDesc, FirebaseUser user)
    {
        String userID = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tutor");

        myRef.child(userID).child("job_title").setValue(jobTitle);
        myRef.child(userID).child("job_desc").setValue(jobDesc);

        for (String qualifications : listQualifications)
        {
            myRef.child(userID).child("qualifications").push().child("qualifications").setValue(qualifications);
        }

        for (String subjects : listSubjects)
        {
            myRef.child(userID).child("subjects").setValue(subjects);
        }



    }



    public void init_chkbox_qualification()
    {
        chk1_qualifications = findViewById(R.id.spmcb);
        chk2_qualifications = findViewById(R.id.stpmcb);
        chk3_qualifications = findViewById(R.id.foundycb);
        chk4_qualifications = findViewById(R.id.matrixcb);
        chk5_qualifications = findViewById(R.id.degreecb);
        chk6_qualifications = findViewById(R.id.mastercb);
        chk7_qualifications = findViewById(R.id.phdcb);
    }

    public void init_chkbox_subjects()
    {
        chk1_subject = findViewById(R.id.addm3cbtutor);
        chk2_subject = findViewById(R.id.physicscbtutor);
        chk3_subject = findViewById(R.id.mathscbtutor);
        chk4_subject = findViewById(R.id.sciencecbtutor);
        chk5_subject = findViewById(R.id.biocbtutor);
        chk6_subject = findViewById(R.id.chmcbtutor);
        chk7_subject = findViewById(R.id.bmcbtutor);
        chk8_subject = findViewById(R.id.engcbtutor);
        chk9_subject = findViewById(R.id.historycbtutor);
        chk10_subject = findViewById(R.id.acccbtutor);
    }

    public ArrayList<String> checkedQualifications()
    {
        ArrayList<String> checked = new ArrayList<String>();

        if(chk1_qualifications.isChecked()){
            checked.add(chk1_qualifications.getText().toString());
        }
        if(chk2_qualifications.isChecked()){
            checked.add(chk2_qualifications.getText().toString());
        }
        if(chk3_qualifications.isChecked()){
            checked.add(chk3_qualifications.getText().toString());
        }
        if(chk4_qualifications.isChecked()){
            checked.add(chk4_qualifications.getText().toString());
        }
        if(chk5_qualifications.isChecked()){
            checked.add(chk5_qualifications.getText().toString());
        }
        if(chk6_qualifications.isChecked()){
            checked.add(chk6_qualifications.getText().toString());
        }
        if(chk7_qualifications.isChecked()){
            checked.add(chk7_qualifications.getText().toString());
        }

        return checked;
    }

    public ArrayList<String> checkedSubjects()
    {
        ArrayList<String> checked = new ArrayList<String>();

        if(chk1_subject.isChecked()){
            checked.add(chk1_subject.getText().toString());
        }
        if(chk2_subject.isChecked()){
            checked.add(chk2_subject.getText().toString());
        }
        if(chk3_subject.isChecked()){
            checked.add(chk3_subject.getText().toString());
        }
        if(chk4_subject.isChecked()){
            checked.add(chk4_subject.getText().toString());
        }
        if(chk5_subject.isChecked()){
            checked.add(chk5_subject.getText().toString());
        }
        if(chk6_subject.isChecked()){
            checked.add(chk6_subject.getText().toString());
        }
        if(chk7_subject.isChecked()){
            checked.add(chk7_subject.getText().toString());
        }
        if(chk8_subject.isChecked()){
            checked.add(chk8_subject.getText().toString());
        }
        if(chk9_subject.isChecked()){
            checked.add(chk9_subject.getText().toString());
        }
        if(chk10_subject.isChecked()){
            checked.add(chk10_subject.getText().toString());
        }

        return checked;
    }




}
