package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import java.util.Iterator;

public class TutorRequestOpen extends AppCompatActivity {

    ImageButton btnHomePage;
    FirebaseAuth mAuth;
    private static final String TAG = "TutorRqOpen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_request_open);

        btnHomePage = findViewById(R.id.homebtn);

        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomePage = new Intent(getApplicationContext(),HomePage.class);
                startActivity(toHomePage);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        init();
    }

    public void init()
    {

        String userID = mAuth.getUid();

      //  showMessage(userID);

        DatabaseReference refChildren = FirebaseDatabase.getInstance()
                .getReference("jobposting").child(userID);

        refChildren.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                    //Loop 1 to go through all the child nodes of users
                   /* for(DataSnapshot booksSnapshot : uniqueKey.child("Books").getChildren()){
                        //loop 2 to go through all the child nodes of books node
                        String bookskey = booksSnapshot.getKey();
                        String booksValue = booksSnapshot.getValue();
                    }
                    */
                    //  Children children = uniqueKeySnapshot.getValue(Children.class);

                    String jobpostid = uniqueKeySnapshot.child("post_id").getValue().toString();
                    String jobchildid = uniqueKeySnapshot.child("child_id").getValue().toString();
                    String jobchildname = uniqueKeySnapshot.child("child_name").getValue().toString();
                    String jobedulevel = uniqueKeySnapshot.child("edu_level").getValue().toString();
                    String joblevel = uniqueKeySnapshot.child("level").getValue().toString();
                    String jobsubject = uniqueKeySnapshot.child("subject").getValue().toString();
                    String joblocation = uniqueKeySnapshot.child("location").getValue().toString();
                    String jobdate = uniqueKeySnapshot.child("date").getValue().toString();
                    String jobstatus = uniqueKeySnapshot.child("status").getValue().toString();

                    ArrayList<SessionData> jobsessionData = new ArrayList<>();
                    Iterable<DataSnapshot> snapshotIterator = uniqueKeySnapshot.child("session").getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        String day = next.child("day").getValue().toString();
                        Log.d(TAG, "day: " + day);
                        String st = next.child("start_time").getValue().toString();
                        Log.d(TAG, "start: " + st);
                        String et = next.child("end_time").getValue().toString();
                        Log.d(TAG, "end: " + et);
                        jobsessionData.add(new SessionData(day,st,et));
                    }





                    JobPosting jobPosting = new JobPosting(jobpostid,jobchildid,jobchildname,jobedulevel,joblevel,jobsubject,joblocation,jobdate,jobsessionData,jobstatus);

                    if (jobPosting.getStatus().equals("Pending"))
                   {
                        generateParentJobList(jobPosting);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void generateParentJobList(final JobPosting jobPosting) {

        LinearLayout mainLayout = findViewById(R.id.jobPostedll);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.jobposted, mainLayout, false);


        CardView cv = myLayout.findViewById(R.id.parentRequestCard);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTutorViewApp = new Intent(TutorRequestOpen.this, TutorRequestViewApp.class);
                toTutorViewApp.putExtra("post_id",jobPosting.getPostID());
                toTutorViewApp.putExtra("subject",jobPosting.getSubject());
                startActivity(toTutorViewApp);
            }
        });


        TextView childname = myLayout.findViewById(R.id.jPNamelbl);
        //String newchildname = "Name: " + children.getChildname();
        childname.setText(jobPosting.getChildName());

        TextView subject = myLayout.findViewById(R.id.jPSubjectlbl);
        //String newchildedulevel = children.getEdulevel();
        subject.setText("Subject: " + jobPosting.getSubject());

        TextView datetime = myLayout.findViewById(R.id.jPTime2);
        String datetimetext = jobPosting.getDate();
        datetime.setText("When: " + datetimetext);

        TextView location = myLayout.findViewById(R.id.jPLoc);
        //String newchildedulevel = children.getEdulevel();
        location.setText("Location: " + jobPosting.getLocation());

        mainLayout.addView(myLayout);

    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }

}
