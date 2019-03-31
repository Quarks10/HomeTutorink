package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TutorJobAppReject extends AppCompatActivity {

    ImageButton btnHomePage;
    Button job_pending;
    FirebaseAuth mAuth;
    private static final String TAG = "JobReject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_job_app_reject);

        btnHomePage = findViewById(R.id.homebtn);

        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomePage = new Intent(getApplicationContext(),HomePageTutor.class);
                startActivity(toHomePage);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        job_pending = findViewById(R.id.ongoingbtn);

        job_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toJobPending = new Intent(TutorJobAppReject.this,TutorJobAppPending.class);
                startActivity(toJobPending);
            }
        });

        init();

    }

    public void init() {

        getAppliedPost(new CallbackGetAppliedPost() {
            @Override
            public void onCallBack(final ArrayList<String> PostingKeys) {

                getParentsData(new FirebaseCallbackAcceptJob() {
                    @Override
                    public void onCallBack(ArrayList<Parent> parent) {
                        for (String post_key:PostingKeys) {
                            generateAllApplyList(post_key, parent);
                        }
                    }
                });
            }
        });
    }


    private void getAppliedPost(final CallbackGetAppliedPost callbackGetAppliedPost){

        final String userID = mAuth.getUid();
        final ArrayList<String> postingKey = new ArrayList<>();
        DatabaseReference refjobapply = FirebaseDatabase.getInstance().getReference("jobapply");
        refjobapply.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    for (DataSnapshot datasnap: ds.getChildren()) {

                        if(datasnap.getKey().equals(userID) && datasnap.child("status").getValue().toString().equals("Rejected")){
                            postingKey.add(ds.getKey());
                        }
                    }
                }

                callbackGetAppliedPost.onCallBack(postingKey);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getParentsData(final FirebaseCallbackAcceptJob firebaseCallbackAcceptJob) {


        final ArrayList<Parent> parent = new ArrayList<Parent>();
        DatabaseReference refparents = FirebaseDatabase.getInstance().getReference("parent");
        refparents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    Parent tempParent = new Parent(ds.child("first_name").getValue().toString(),ds.child("last_name").getValue().toString(),
                            ds.child("address").getValue().toString(),ds.getKey());

                    parent.add(tempParent);

                }

                firebaseCallbackAcceptJob.onCallBack(parent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void generateAllApplyList(final String post_key, final ArrayList<Parent> parent) {

        DatabaseReference refposting = FirebaseDatabase.getInstance().getReference("jobposting");
        refposting.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    for (DataSnapshot datasnap: ds.getChildren()) {

                       // if(datasnap.child("status").getValue().toString().equals("Pending") && datasnap.getKey().equals(post_key)){
                        if(datasnap.getKey().equals(post_key)){

                            Parent currparent = new Parent();

                            for (Parent p:parent) {
                                if(ds.getKey().equals(p.parentID)){
                                    currparent = p;
                                }
                            }

                            String parentname = currparent.getFirst_name() + " " + currparent.getLast_name();
                            String childname = datasnap.child("child_name").getValue().toString();
                            String childlevel = datasnap.child("level").getValue().toString();
                            String childedulevel = datasnap.child("edu_level").getValue().toString();
                            String location = datasnap.child("location").getValue().toString();
                            String address = currparent.getAddress();
                            String subject = datasnap.child("subject").getValue().toString();
                            String date = datasnap.child("date").getValue().toString();

                            generateApplyList(parentname,childname,childlevel,childedulevel,location,address,subject,date);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void generateApplyList(String parentname, String childname, String childlevel, String childedulevel, String location, String address, String subject, String date) {
        LinearLayout mainLayout = findViewById(R.id.jobAppRejectLayout);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.tutor_job_app_reject, mainLayout, false);

        TextView parentName = myLayout.findViewById(R.id.parentNametxt);
        parentName.setText(parentname);

        TextView childName = myLayout.findViewById(R.id.childNametxt);
        childName.setText("Child Name: " + childname);

        TextView childLevel = myLayout.findViewById(R.id.childEdulvltxt);
        if(childedulevel.equals("Primary School")){
            childLevel.setText("Standard: " + childlevel );
        }else{
            childLevel.setText("Form: " + childlevel );
        }

        TextView subject_text = myLayout.findViewById(R.id.subjecttxt);
        subject_text.setText("Subject: " + subject);

        // TextView session = myLayout.findViewById(R.id.sessiontxt);
        // session.setText("Subject: " + subject);

        TextView dateTime = myLayout.findViewById(R.id.datetxt);
        dateTime.setText("Start Date: " + date);

        TextView locationName = myLayout.findViewById(R.id.loctxt);
        if(location.equals("In my location")){
            locationName.setText("Location: " + address);
        }else{
            locationName.setText("Location: " + location);
        }

        mainLayout.addView(myLayout);

    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }

    private interface FirebaseCallbackAcceptJob {
        void onCallBack(ArrayList<Parent> parent);
    }

    private interface CallbackGetAppliedPost{
        void onCallBack(ArrayList<String> PostingKeys);
    }

    private interface CallbackGetTutorKeys{
        void onCallBack(ArrayList<String> TutorKeys);
    }


}

