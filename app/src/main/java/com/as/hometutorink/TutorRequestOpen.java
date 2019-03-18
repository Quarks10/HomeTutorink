package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TutorRequestOpen extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_request_open);
        mAuth = FirebaseAuth.getInstance();
        init();

/*        view_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toviewapplication = new Intent(TutorRequestOpen.this,TutorRequestViewApp.class);
                startActivity(toviewapplication);
            }
        });

        view_recommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toviewrecommmended = new Intent(TutorRequestOpen.this,TutorRequestRecommend.class);
                startActivity(toviewrecommmended);
            }
        });

        */
    }

    public void init()
    {

        String userID = mAuth.getUid();

      //  showMessage(userID);

        DatabaseReference refChildren = FirebaseDatabase.getInstance()
                .getReference("jobposting").child(userID);

        refChildren.addValueEventListener(new ValueEventListener() {
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
                    String jobtime = uniqueKeySnapshot.child("time").getValue().toString();
                    String jobstatus = uniqueKeySnapshot.child("status").getValue().toString();

                    JobPosting jobPosting = new JobPosting(jobpostid,jobchildid,jobchildname,jobedulevel,joblevel,jobsubject,joblocation,jobdate,jobtime,jobstatus);

                    if (jobPosting.getStatus().equals("true"))
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
        String datetimetext = jobPosting.getDate() + ", " + jobPosting.getTime();
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
