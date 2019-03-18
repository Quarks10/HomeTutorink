package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

public class TutorRequestViewApp extends AppCompatActivity {

    FirebaseAuth mAuth;
    private static final String TAG = "ViewApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_request_view_app);
        mAuth = FirebaseAuth.getInstance();
        init();
    }



    public void init()
    {

        readData(new FirebaseCallbackViewApp() {
            @Override
            public void onCallBack(ArrayList<TutorApplication> tutorApplication) {

                for (TutorApplication tutorapp: tutorApplication)
                {
                    generateApplication(tutorapp.getTutorKey(),tutorapp.getAppStatus(), tutorApplication);
                }

            }
        });

    }


    private void readData(final FirebaseCallbackViewApp firebaseCallbackViewApp){
        String postID = getIntent().getStringExtra("post_id");
        final ArrayList<TutorApplication> tutors = new ArrayList<TutorApplication>();
        DatabaseReference refParents = FirebaseDatabase.getInstance().getReference("jobapply");
        refParents.child(postID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot value: dataSnapshot.getChildren())
                {
                    // generateApplication(value.getKey(),value.child("status").getValue().toString());
                    TutorApplication tutorApplication = new TutorApplication(value.getKey(),value.child("status").getValue().toString());
                    tutors.add(tutorApplication);
                }


                // generateAvailableJobList(parentName,parentAddress,jobPosting);
                firebaseCallbackViewApp.onCallBack(tutors);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void generateApplication(final String tutor_key, final String status, final ArrayList<TutorApplication> tutorApplication){

        DatabaseReference refTutor = FirebaseDatabase.getInstance().getReference("tutor");
        refTutor.child(tutor_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                   // showMessage(value.child("first_name").getValue().toString());
                    if (status.equals("Apply")){
                        ArrayList<String> list_qualifications = new ArrayList<String>();
                        String tutorname = dataSnapshot.child("first_name").getValue().toString() + " " + dataSnapshot.child("last_name").getValue().toString();
                        String experience = dataSnapshot.child("job_title").getValue().toString() + "-" + dataSnapshot.child("job_desc").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("qualifications").getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                        while (iterator.hasNext()) {
                            DataSnapshot next = (DataSnapshot) iterator.next();
                            String subject =  next.child("qualifications").getValue().toString();
                            list_qualifications.add(subject);
                        }
                        generateApplicationJobList(tutorname, experience, list_qualifications, address, tutor_key, tutorApplication);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void AcceptJob(String postingID, String tutorID, String tutorName, ArrayList<TutorApplication> tutorApplications){

        FirebaseUser currentuser = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("jobapply");

        for (TutorApplication tp: tutorApplications) {


            if(tp.tutorKey.equals(tutorID)){
                myRef.child(postingID).child(tutorID).child("status").setValue("Approve");
            }else{
                myRef.child(postingID).child(tp.getTutorKey()).child("status").setValue("Rejected");
            }

        }

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database2.getReference("jobposting");

        myRef2.child(currentuser.getUid()).child(postingID).child("status").setValue("Accepted");
        myRef2.child(currentuser.getUid()).child(postingID).child("tutor_id").setValue(tutorID);
        myRef2.child(currentuser.getUid()).child(postingID).child("tutor_name").setValue(tutorName);

        Intent toDashboard = new Intent(TutorRequestViewApp.this, ParentDashboardCal.class);
        startActivity(toDashboard);

    }

    public void generateApplicationJobList(final String tutorName, String experience, ArrayList<String> qualification_list, String Address, final String tutorKey, final ArrayList<TutorApplication> tutorApplications) {

        final String postID = getIntent().getStringExtra("post_id");
        LinearLayout mainLayout = findViewById(R.id.jobPostedAppll);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.jobpostedviewapp, mainLayout, false);


        Button applybtn = myLayout.findViewById(R.id.hirebtn);
        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptJob(postID, tutorKey, tutorName, tutorApplications);
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

        TextView tutorname = myLayout.findViewById(R.id.tutorNametxt);
        tutorname.setText(tutorName);

        TextView tutorexp = myLayout.findViewById(R.id.tutorExptxt);
        tutorexp.setText("Experience: " + experience);

        /*
        TextView childedulevel = myLayout.findViewById(R.id.childEdulvltxt);
        if (jobPosting.getEduLevel().equals("Primary School")){
            childedulevel.setText("Standard: " + jobPosting.getLevel());
        }else{
            childedulevel.setText("Form: " + jobPosting.getLevel());
        }
        */

        String exp = "Qualifications: ";
        for (String qualifications: qualification_list) {

            exp = exp + " " + qualifications;

        }

        TextView tutoredu = myLayout.findViewById(R.id.tutorEdutxt);
        tutoredu.setText(exp);

        TextView address = myLayout.findViewById(R.id.tutorAddresstxt);
        address.setText("Address: " + Address);

        mainLayout.addView(myLayout);

    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }

    public interface FirebaseCallbackViewApp {
        void onCallBack(ArrayList<TutorApplication> tutorApplication);
    }



}
