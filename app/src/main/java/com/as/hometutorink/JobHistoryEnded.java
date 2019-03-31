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
import java.util.Iterator;

public class JobHistoryEnded extends AppCompatActivity {

    ImageButton btnHomePage;
    Button job_ongoing;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_history_ended);

        mAuth = FirebaseAuth.getInstance();

        btnHomePage = findViewById(R.id.homebtn);
        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomePage = new Intent(getApplicationContext(),HomePageTutor.class);
                startActivity(toHomePage);
            }
        });

        job_ongoing = findViewById(R.id.ongoingbtnH);

        job_ongoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSecondary = new Intent(JobHistoryEnded.this,JobHistoryPrimary.class);
                startActivity(toSecondary);
            }
        });

        init();

    }

    public void init() {

        readData(new TutorDashboardCal.FirebaseCallbackTutorDashboard() {
            @Override
            public void onCallBack(final ArrayList<String> onGoingJob, final ArrayList<String> parentKey) {

                getParentsData(new TutorAcceptJobsPrimary.FirebaseCallbackAcceptJob() {
                    @Override
                    public void onCallBack(ArrayList<Parent> parent) {


                        for (int i = 0; i<onGoingJob.size(); i++){

                            Parent currparent = new Parent();

                            for (Parent tmp: parent) {
                                if(tmp.getParentID().equals(parentKey.get(i))){
                                    currparent = tmp;
                                }
                            }
                            generatePost(onGoingJob.get(i), parentKey.get(i),currparent);
                        }
                    }
                });
            }
        });

    }

    private void readData(final TutorDashboardCal.FirebaseCallbackTutorDashboard firebaseCallbackTutorDashboard){

        FirebaseUser curruser = mAuth.getCurrentUser();
        final ArrayList<String> ongoingjob = new ArrayList<String>();
        final ArrayList<String> parentkey = new ArrayList<String>();
        DatabaseReference refParents = FirebaseDatabase.getInstance().getReference("jobaccepted");
        refParents.child(curruser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot value: dataSnapshot.getChildren())
                {
                    if(value.child("status").getValue().equals("Ended")){
                        ongoingjob.add(value.getKey());
                        parentkey.add(value.child("parent_id").getValue().toString());
                    }
                }

                firebaseCallbackTutorDashboard.onCallBack(ongoingjob,parentkey);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getParentsData(final TutorAcceptJobsPrimary.FirebaseCallbackAcceptJob firebaseCallbackAcceptJob) {


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


    public void generatePost(final String post_id, String parent_id, final Parent currparent){

        DatabaseReference refposting = FirebaseDatabase.getInstance().getReference("jobposting");
        refposting.child(parent_id).child(post_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String parentname = currparent.getFirst_name() + " " + currparent.getLast_name();
                String childname = dataSnapshot.child("child_name").getValue().toString();
                String childlevel = dataSnapshot.child("level").getValue().toString();
                String childedulevel = dataSnapshot.child("edu_level").getValue().toString();
                String location = dataSnapshot.child("location").getValue().toString();
                String address = currparent.getAddress();
                String subject = dataSnapshot.child("subject").getValue().toString();
                String datetime = dataSnapshot.child("date").getValue().toString();

                ArrayList<SessionData> jobsessionData = new ArrayList<>();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("session").getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    String day = next.child("day").getValue().toString();
                    String st = next.child("start_time").getValue().toString();
                    String et = next.child("end_time").getValue().toString();
                    jobsessionData.add(new SessionData(day,st,et));
                }

                generatePostList(parentname, childname,childlevel,childedulevel,location,address,subject,datetime);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void generatePostList(String parentname, String childname, String childlevel, String childedulevel, String location, String address, String subject, String date) {

        LinearLayout mainLayout = findViewById(R.id.jobHistoryLLEnded);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.tutor_job_app_pending, mainLayout, false);

        Button contactbtn = myLayout.findViewById(R.id.contactParentbtn);
        contactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMessage = new Intent(JobHistoryEnded.this,Message.class);
                startActivity(toMessage);
            }
        });

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

    public interface FirebaseCallbackTutorDashboard {
        void onCallBack(ArrayList<String> onGoingJob, ArrayList<String> parentKey);
    }

}
