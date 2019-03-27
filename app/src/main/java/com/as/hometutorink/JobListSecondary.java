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

public class JobListSecondary extends AppCompatActivity {

    ImageButton btnHomePage;
    Button job_primary;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list_secondary);

        btnHomePage = findViewById(R.id.homebtn);

        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomePage = new Intent(getApplicationContext(),HomePageTutor.class);
                startActivity(toHomePage);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        init();



        job_primary = findViewById(R.id.primarybtnlist);

        job_primary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSecondary = new Intent(JobListSecondary.this,JobListPrimary.class);
                startActivity(toSecondary);
            }
        });


    }

    public void init()
    {

        readData(new JobListPrimary.FirebaseCallback() {
            @Override
            public void onCallBack(ArrayList<JobPosting> jobposting, ArrayList<String> parentskey) {

                for (int i = 0; i<jobposting.size(); i++){
                    getParentsData(parentskey.get(i),jobposting.get(i));
                }
            }
        });

    }


    private void readData(final JobListPrimary.FirebaseCallback firebaseCallback){
        final ArrayList<JobPosting> listjob = new ArrayList<JobPosting>();
        final ArrayList<String> parentskey = new ArrayList<String>();


        DatabaseReference refChildren = FirebaseDatabase.getInstance()
                .getReference("jobposting");

        refChildren.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot parentsKey : dataSnapshot.getChildren()){
                    for(DataSnapshot jobSnapshot : parentsKey.getChildren()){

                        //  getParentsData(parentsKey.getKey().toString());

                        String jobpostid = jobSnapshot.child("post_id").getValue().toString();
                        String jobchildid = jobSnapshot.child("child_id").getValue().toString();
                        String jobchildname = jobSnapshot.child("child_name").getValue().toString();
                        String jobedulevel = jobSnapshot.child("edu_level").getValue().toString();
                        String joblevel = jobSnapshot.child("level").getValue().toString();
                        String jobsubject = jobSnapshot.child("subject").getValue().toString();
                        String joblocation = jobSnapshot.child("location").getValue().toString();
                        String jobdate = jobSnapshot.child("date").getValue().toString();
                        String jobstatus = jobSnapshot.child("status").getValue().toString();

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

                        if (jobPosting.getStatus().equals("Pending") && jobPosting.getEduLevel().equals("Secondary School"))
                        {
                            listjob.add(jobPosting);
                            parentskey.add(parentsKey.getKey());
                        }

                    }

                }

                firebaseCallback.onCallBack(listjob,parentskey);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private interface FirebaseCallback {
        void onCallBack(ArrayList<JobPosting> jobposting, ArrayList<String> parentskey);
    }


    public void getParentsData(final String parentskey, final JobPosting jobPosting){

        DatabaseReference refParents = FirebaseDatabase.getInstance().getReference("parent");

        refParents.child(parentskey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String parentName = dataSnapshot.child("first_name").getValue().toString() + " " + dataSnapshot.child("last_name").getValue().toString();
                String parentAddress = dataSnapshot.child("address").getValue().toString();

                if (jobPosting.getStatus().equals("Pending")) {
                    generateAvailableJobList(parentName, parentAddress, jobPosting);
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

     //   String applicationID = FirebaseDatabase.getInstance().getReference().child("jobapply").push().getKey();


        Map<String, String> application_details = new HashMap<String, String>();
        application_details.put("status",  "Apply");


        myRef.child(postingID).child(currentUser.getUid()).setValue(application_details);


    }


    public void generateAvailableJobList(String parentsName, String parentsAddress, final JobPosting jobPosting) {

        LinearLayout mainLayout = findViewById(R.id.joblistsecondaryll);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.joblistcard, mainLayout, false);


        Button applybtn = myLayout.findViewById(R.id.applybtn);
        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Successfully Applied");
                ApplyJob(jobPosting.getPostID());
            }
        });

        Button contactbtn = myLayout.findViewById(R.id.contactParentbtn);
        contactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMessage = new Intent(getApplicationContext(),Message.class);
                startActivity(toMessage);
            }
        });

        /*
        CardView cv = myLayout.findViewById(R.id.parentRequestCard);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTutorViewApp = new Intent(TutorRequestOpen.this, TutorRequestViewApp.class);
                startActivity(toTutorViewApp);
            }
        });
*/

        TextView parentname = myLayout.findViewById(R.id.parentNametxt);
        parentname.setText(parentsName);

        TextView childname = myLayout.findViewById(R.id.childNametxt);
        childname.setText("Child's Name: " + jobPosting.getChildName());

        TextView childedulevel = myLayout.findViewById(R.id.childEdulvltxt);
        if (jobPosting.getEduLevel().equals("Primary School")){
            childedulevel.setText("Standard: " + jobPosting.getLevel());
        }else{
            childedulevel.setText("Form: " + jobPosting.getLevel());
        }

        TextView subject = myLayout.findViewById(R.id.subjecttxt);
        subject.setText(jobPosting.getSubject());

        TextView datetime = myLayout.findViewById(R.id.datetxt);
        String datetimetext = jobPosting.getDate();
        datetime.setText("When: " + datetimetext);

        TextView location = myLayout.findViewById(R.id.loctxt);
        if(jobPosting.getLocation().equals("In my location")){
            location.setText("Location: " + parentsAddress);
        }else{
            location.setText(jobPosting.getLocation());
        }

        mainLayout.addView(myLayout);

    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }



}
