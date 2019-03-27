package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParentHistoryPostClosed extends AppCompatActivity {

    FirebaseAuth mAuth;
    ImageButton btnHomePage;
    Button btnClassOnGoing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_history_closed);

        btnHomePage = findViewById(R.id.homebtn);
        btnClassOnGoing = findViewById(R.id.ongoingbtn);

        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomePage = new Intent(getApplicationContext(),HomePage.class);
                startActivity(toHomePage);
            }
        });

        btnClassOnGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toEndedClass = new Intent(ParentHistoryPostClosed.this,ParentHistoryPost.class);
                startActivity(toEndedClass);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        getPostedJob();

    }

    private void getPostedJob(){

        String user_id = mAuth.getUid();

        DatabaseReference refChildren = FirebaseDatabase.getInstance()
                .getReference("jobposting");

        refChildren.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot jobSnapshot : dataSnapshot.getChildren()){

                    String jobstatus = jobSnapshot.child("status").getValue().toString();
                    if (jobstatus.equals("Ended"))
                    {
                        String jobpostid = jobSnapshot.child("post_id").getValue().toString();
                        String jobchildid = jobSnapshot.child("child_id").getValue().toString();
                        String jobchildname = jobSnapshot.child("child_name").getValue().toString();
                        String jobedulevel = jobSnapshot.child("edu_level").getValue().toString();
                        String joblevel = jobSnapshot.child("level").getValue().toString();
                        String jobsubject = jobSnapshot.child("subject").getValue().toString();
                        String joblocation = jobSnapshot.child("location").getValue().toString();
                        String jobdate = jobSnapshot.child("date").getValue().toString();
                        String tutor_name = jobSnapshot.child("tutor_name").getValue().toString();

                        ArrayList<SessionData> jobsessionData = new ArrayList<>();
                        Iterable<DataSnapshot> snapshotIterator = jobSnapshot.child("session").getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                        while (iterator.hasNext()) {
                            DataSnapshot next = (DataSnapshot) iterator.next();
                            String day = next.child("day").getValue().toString();
                            String st = next.child("start_time").getValue().toString();
                            String et = next.child("end_time").getValue().toString();
                            jobsessionData.add(new SessionData(day,st,et));
                        }


                        JobPosting jobPosting = new JobPosting(jobpostid,jobchildid,jobchildname,jobedulevel,joblevel,jobsubject,joblocation,jobdate,jobsessionData,jobstatus);


                        generateAvailableJobList(tutor_name, jobPosting);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


    public void ApplyJob(String postingID){

        FirebaseUser currentUser = mAuth.getCurrentUser();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("jobapply");

        // String applicationID = FirebaseDatabase.getInstance().getReference().child("jobapply").push().getKey();


        Map<String, String> application_details = new HashMap<String, String>();
        application_details.put("status",  "Apply");


        myRef.child(postingID).child(currentUser.getUid()).setValue(application_details);

        //Intent toDashboard = new Intent(JobListPrimary.this, TutorDashboardCal.class);
        //  startActivity(toDashboard);

    }



    public void generateAvailableJobList(String tutorName, final JobPosting jobPosting) {

        LinearLayout mainLayout = findViewById(R.id.postHistoryEndedLayout);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.posthistcard2, mainLayout, false);


        TextView childname = myLayout.findViewById(R.id.dashCardNameTxt);
        childname.setText(jobPosting.getChildName());

        TextView tutorname = myLayout.findViewById(R.id.dashCardTutorTxt);
        tutorname.setText("Tutor: " + tutorName);

        TextView subject = myLayout.findViewById(R.id.dashCardSubjectTxt);
        subject.setText(jobPosting.getSubject());

        TextView datetime = myLayout.findViewById(R.id.dashCardTimeTxt);
        datetime.setText("Date Start: " + jobPosting.getDate());

        mainLayout.addView(myLayout);

    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }

}
