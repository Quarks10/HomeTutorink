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

    ImageButton btnHomePage;
    FirebaseAuth mAuth;
    private static final String TAG = "ViewApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_request_view_app);

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

        getTutorsApplied(new FirebaseCallbackViewApp() {
            @Override
            public void onCallBack(ArrayList<TutorApplication> tutorApplication) {

                for (TutorApplication tutorapp: tutorApplication)
                {
                    generateApplication(tutorapp.getTutorKey(),tutorapp.getAppStatus(), tutorApplication);
                }

            }
        });

        getTutorsRecommended();

    }


    private void getTutorsRecommended (){
        final String curr_subject = getIntent().getStringExtra("subject");
        DatabaseReference reftutors = FirebaseDatabase.getInstance().getReference("tutor");
        reftutors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot value: dataSnapshot.getChildren()) {

                    Boolean subject_qualified = false;
                    ArrayList<String> list_qualifications = new ArrayList<String>();
                    ArrayList<String> list_subjects = new ArrayList<String>();
                    String tutorFN = value.child("first_name").getValue().toString();
                    String tutorLN = value.child("last_name").getValue().toString();
                    String job_title = value.child("job_title").getValue().toString();
                    String job_desc = value.child("job_desc").getValue().toString();
                    String address = value.child("address").getValue().toString();

                    Iterable<DataSnapshot> snapshotIterator = value.child("qualifications").getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        String subject = next.child("qualifications").getValue().toString();
                        list_qualifications.add(subject);
                    }

                    Iterable<DataSnapshot> snapshotIterator2 = value.child("subjects").getChildren();
                    Iterator<DataSnapshot> iterator2 = snapshotIterator2.iterator();

                    while (iterator2.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator2.next();
                        String subject = next.child("subject").getValue().toString();
                        if(subject.equals(curr_subject)){
                            subject_qualified = true;
                        }
                        list_subjects.add(subject);
                    }

                    TutorData tutorData = new TutorData(tutorFN,tutorLN,address,job_title,job_desc,list_qualifications,list_subjects);

                    if(subject_qualified){
                        generateApplicationTutorList(tutorData);
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void generateApplicationTutorList(TutorData tutorData) {

        final String postID = getIntent().getStringExtra("post_id");
        LinearLayout mainLayout = findViewById(R.id.jobPostedRecll);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.jobpostedviewapp, mainLayout, false);


        Button applybtn = myLayout.findViewById(R.id.hirebtn);
        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   HireTutorRecommendation();
               // AcceptJob(postID, tutorKey, tutorName, tutorApplications);
            }
        });

        Button contactbtn = myLayout.findViewById(R.id.contactTutorbtn);
        contactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMessage = new Intent(getApplicationContext(),Message.class);
                startActivity(toMessage);
            }
        });

        TextView tutorname = myLayout.findViewById(R.id.tutorNametxt);
        tutorname.setText(tutorData.getFirst_name()+ " " + tutorData.getLast_name());

        TextView tutorexp = myLayout.findViewById(R.id.tutorExptxt);
        tutorexp.setText("Experience: " + tutorData.getJob_title() + " - " + tutorData.getJob_desc());


        String exp = "Qualifications: ";
        for (String qualifications: tutorData.getQualifications()) {

            exp = exp + " " + qualifications;

        }

        TextView tutoredu = myLayout.findViewById(R.id.tutorEdutxt);
        tutoredu.setText(exp);

        TextView address = myLayout.findViewById(R.id.tutorAddresstxt);
        address.setText("Address: " + tutorData.getAddress());

        mainLayout.addView(myLayout);

    }

    public void HireTutorRecommendation(String postingID, String tutorID, String tutorName, ArrayList<TutorApplication> tutorApplications){

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

        FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        DatabaseReference myRef3 = database2.getReference("jobaccepted");

        myRef3.child(tutorID).child(postingID).child("status").setValue("OnGoing");
        myRef3.child(tutorID).child(postingID).child("parent_id").setValue(currentuser.getUid());

        Intent toDashboard = new Intent(TutorRequestViewApp.this, ParentDashboardCal.class);
        startActivity(toDashboard);

    }


    private void getTutorsApplied(final FirebaseCallbackViewApp firebaseCallbackViewApp){
        String postID = getIntent().getStringExtra("post_id");
        final ArrayList<TutorApplication> tutors = new ArrayList<TutorApplication>();
        DatabaseReference refParents = FirebaseDatabase.getInstance().getReference("jobapply");
        refParents.child(postID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot value: dataSnapshot.getChildren())
                {
                    TutorApplication tutorApplication = new TutorApplication(value.getKey(),value.child("status").getValue().toString());
                    tutors.add(tutorApplication);
                }

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

        myRef2.child(currentuser.getUid()).child(postingID).child("status").setValue("OnGoing");
        myRef2.child(currentuser.getUid()).child(postingID).child("tutor_id").setValue(tutorID);
        myRef2.child(currentuser.getUid()).child(postingID).child("tutor_name").setValue(tutorName);

        FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        DatabaseReference myRef3 = database2.getReference("jobaccepted");

        myRef3.child(tutorID).child(postingID).child("status").setValue("OnGoing");
        myRef3.child(tutorID).child(postingID).child("parent_id").setValue(currentuser.getUid());

        Intent toDashboard = new Intent(TutorRequestViewApp.this, ParentDashboardCal.class);
        startActivity(toDashboard);

    }

    public void generateApplicationJobList(final String tutorName, String experience,
                                           ArrayList<String> qualification_list,
                                           String Address, final String tutorKey,
                                           final ArrayList<TutorApplication> tutorApplications) {

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

        Button contactbtn = myLayout.findViewById(R.id.contactTutorbtn);
        contactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMessage = new Intent(getApplicationContext(),Message.class);
                startActivity(toMessage);
            }
        });

        TextView tutorname = myLayout.findViewById(R.id.tutorNametxt);
        tutorname.setText(tutorName);

        TextView tutorexp = myLayout.findViewById(R.id.tutorExptxt);
        tutorexp.setText("Experience: " + experience);


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
