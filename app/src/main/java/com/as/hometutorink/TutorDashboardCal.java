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

public class TutorDashboardCal extends AppCompatActivity {

    FirebaseAuth mAuth;
    ImageButton btnHomePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_dashboard_cal);
        mAuth = FirebaseAuth.getInstance();
        btnHomePage = findViewById(R.id.homebtn);

        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomePage = new Intent(getApplicationContext(),HomePageTutor.class);
                startActivity(toHomePage);
            }
        });
        init();
    }


    public void init() {
        //generateDashBoard();

        readData(new FirebaseCallbackTutorDashboard() {
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
                            generateDashBoard(onGoingJob.get(i), parentKey.get(i),currparent);
                        }
                    }
                });




            }

        });

    }

    private void readData(final FirebaseCallbackTutorDashboard firebaseCallbackTutorDashboard){

        FirebaseUser curruser = mAuth.getCurrentUser();
        final ArrayList<String> ongoingjob = new ArrayList<String>();
        final ArrayList<String> parentkey = new ArrayList<String>();
        DatabaseReference refParents = FirebaseDatabase.getInstance().getReference("jobaccepted");
        refParents.child(curruser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot value: dataSnapshot.getChildren())
                {
                    if(value.child("status").getValue().equals("OnGoing")){
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


    public void generateDashBoard(String post_id, String parent_id, final Parent currparent){

        DatabaseReference refposting = FirebaseDatabase.getInstance().getReference("jobposting");
        refposting.child(parent_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {


                    if(ds.child("status").getValue().equals("OnGoing")){


                        String parentname = currparent.getFirst_name() + " " + currparent.getLast_name();
                        String childname = ds.child("child_name").getValue().toString();
                        String childlevel = ds.child("level").getValue().toString();
                        String childedulevel = ds.child("edu_level").getValue().toString();
                        String location = ds.child("location").getValue().toString();
                        String address = currparent.getAddress();
                        String subject = ds.child("subject").getValue().toString();
                        String datetime = ds.child("date").getValue().toString();

                        generateDashboardList(childname,childlevel,childedulevel,location,address,subject,datetime);
                    }

                    /*
                    readData(new FirebaseCallbackTutorDashboard() {
                        @Override
                        public void onCallBack(ArrayList<String> onGoingJob, ArrayList<String> parentKey) {

                            for (int i = 0; i<onGoingJob.size(); i++){
                                generateDashBoard(onGoingJob.get(i), parentKey.get(i));
                            }

                        }

                    });
                    */

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void generateDashboardList(String childname, String childlevel, String childedulevel, String location, String address, String subject, String datetime) {

        LinearLayout mainLayout = findViewById(R.id.Tutordbll);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.tutordbcard, mainLayout, false);


        /*
        Button applybtn = myLayout.findViewById(R.id.hirebtn);
        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptJob(postID, tutorKey, tutorApplications);
            }
        });
*/
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
        TextView childName = myLayout.findViewById(R.id.dashCardNameTxt);
        childName.setText(childname);

        TextView childLevel = myLayout.findViewById(R.id.dashCardLvltxt);
        if(childedulevel.equals("Primary School")){
            childLevel.setText("Standard: " + childlevel );
        }else{
            childLevel.setText("Form: " + childlevel );
        }

        TextView subject_text = myLayout.findViewById(R.id.dashCardSubjectTxt);
        subject_text.setText("Subject: " + subject);

        TextView dateTime = myLayout.findViewById(R.id.dashCardTimeTxt);
        dateTime.setText("DateTime: " + datetime);

        TextView locationName = myLayout.findViewById(R.id.dashCardLocTxt);
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
