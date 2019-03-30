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
import java.util.Iterator;

public class TutorAcceptJobsPrimary extends AppCompatActivity {

    FirebaseAuth mAuth;
    ImageButton btnHomePage;
    private static final String TAG = "AcceptJob";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_accept_jobs_primary);

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
    }

    public void init() {

        getAppliedPost(new CallbackGetAppliedPost() {
            @Override
            public void onCallBack(final ArrayList<String> PostingKeys, final ArrayList<String> ParentKeys) {
                getParentsData(new FirebaseCallbackAcceptJob() {
                    @Override
                    public void onCallBack(ArrayList<Parent> parent) {

                        Log.d(TAG, "onCallBack: " + Integer.toString(PostingKeys.size()));

                        for (int i = 0; i<PostingKeys.size(); i++){

                            Parent currparent = new Parent();

                            for (Parent tmp: parent) {
                                if(tmp.getParentID().equals(ParentKeys.get(i))){
                                    currparent = tmp;
                                }
                            }
                            generateParentApplyList(PostingKeys.get(i),ParentKeys.get(i),currparent);
                        }

                    }
                });
            }
        });


    }


    private void getAppliedPost(final CallbackGetAppliedPost callbackGetAppliedPost){

        final String userID = mAuth.getUid();
        final ArrayList<String> postingKey = new ArrayList<>();
        final ArrayList<String> parentKey = new ArrayList<>();
        DatabaseReference refjobapplyparents = FirebaseDatabase.getInstance().getReference("jobapplyparent");
        refjobapplyparents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    if(ds.child("status").getValue().toString().equals("Apply")){
                        String parent_key = ds.child("parent_id").getValue().toString();
                        String post_key = ds.getKey();
                        Iterable<DataSnapshot> snapshotIterator = ds.child("tutor_apply").getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                        while (iterator.hasNext()) {
                            DataSnapshot next = (DataSnapshot) iterator.next();
                            String tutor_key = next.child("tutor").getValue().toString();

                            if(tutor_key.equals(userID)){
                                postingKey.add(post_key);
                                parentKey.add(parent_key);
                            }

                        }
                    }

                }

                callbackGetAppliedPost.onCallBack(postingKey,parentKey);
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

    private void generateParentApplyList(final String post_key, final String parent_key, final Parent currparent) {

        DatabaseReference refposting = FirebaseDatabase.getInstance().getReference("jobposting");
        refposting.child(parent_key).child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {

                if(ds.child("status").getValue().toString().equals("Pending")){

                    String parentname = currparent.getFirst_name() + " " + currparent.getLast_name();
                    String childname = ds.child("child_name").getValue().toString();
                    String childlevel = ds.child("level").getValue().toString();
                    String childedulevel = ds.child("edu_level").getValue().toString();
                    String location = ds.child("location").getValue().toString();
                    String address = currparent.getAddress();
                    String subject = ds.child("subject").getValue().toString();
                    String date = ds.child("date").getValue().toString();

                    generateApplyList(parentname,childname,childlevel,childedulevel,location,address,subject,date, post_key, parent_key);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setTutorValue(final String tutor_id, final String postingID, final String parentID){
        DatabaseReference refTutor = FirebaseDatabase.getInstance().getReference("tutor");
        refTutor.child(tutor_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String tutorName = dataSnapshot.child("first_name").getValue().toString() + " " + dataSnapshot.child("last_name").getValue().toString();
                FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                DatabaseReference myRef2 = database2.getReference("jobposting");

                Log.d(TAG, "onDataChange: " + tutorName);

                myRef2.child(parentID).child(postingID).child("status").setValue("OnGoing");
                myRef2.child(parentID).child(postingID).child("tutor_id").setValue(tutor_id);
                myRef2.child(parentID).child(postingID).child("tutor_name").setValue(tutorName);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void rejectAllJobApply(final String tutorID, final String postingID){

        final ArrayList<String> tutorkeys = new ArrayList<>();
        DatabaseReference refTutor = FirebaseDatabase.getInstance().getReference("jobapply");
        refTutor.child(postingID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds:dataSnapshot.getChildren()) {

                    tutorkeys.add(ds.getKey());

                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("jobapply");

                for (String keys:tutorkeys) {

                    if(keys.equals(tutorID)){
                        myRef.child(postingID).child(keys).child("status").setValue("Approve");
                    }else{
                        myRef.child(postingID).child(keys).child("status").setValue("Rejected");;
                    }

                }



            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    private void AcceptJobParent(String postingID, String parentID){
        FirebaseUser currentuser = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("jobapplyparent");

        myRef.child(postingID).child("status").setValue("Approve");

        FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        DatabaseReference myRef3 = database3.getReference("jobaccepted");

        String pushID = FirebaseDatabase.getInstance().getReference().child("jobaccepted").push().getKey();
        myRef3.child(currentuser.getUid()).child(postingID).child("status").setValue("OnGoing");
        myRef3.child(currentuser.getUid()).child(postingID).child("parent_id").setValue(parentID);
        myRef3.child(currentuser.getUid()).child(postingID).child("post_id").setValue(postingID);

        rejectAllJobApply(currentuser.getUid(),postingID);
        setTutorValue(currentuser.getUid(),postingID,parentID);

        Intent toDashboard = new Intent(TutorAcceptJobsPrimary.this, TutorDashboardCal.class);
       // startActivity(toDashboard);
    }


    private void generateApplyList(String parentname, String childname, String childlevel, String childedulevel, String location, String address, String subject, String date, final String postID, final String parentID) {
        LinearLayout mainLayout = findViewById(R.id.acceptjobsll);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.jobacceptcard, mainLayout, false);


        Button acceptbtn = myLayout.findViewById(R.id.acceptbtn);
        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptJobParent(postID,parentID);
            }
        });

        Button contactbtn = myLayout.findViewById(R.id.contactParentbtn);
        contactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMessage = new Intent(TutorAcceptJobsPrimary.this,Message.class);
                startActivity(toMessage);
            }
        });

        TextView parentName = myLayout.findViewById(R.id.parentNametxt);
        parentName.setText(parentname);

        TextView childName = myLayout.findViewById(R.id.childNametxt);
        childName.setText(childname);

        TextView childLevel = myLayout.findViewById(R.id.childEdulvltxt);
        if(childedulevel.equals("Primary School")){
            childLevel.setText("Standard: " + childlevel );
        }else{
            childLevel.setText("Form: " + childlevel );
        }

        TextView subject_text = myLayout.findViewById(R.id.subjecttxt);
        subject_text.setText("Subject: " + subject);

        TextView dateTime = myLayout.findViewById(R.id.datetxt);
        dateTime.setText("DateTime: " + date);

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

    public interface FirebaseCallbackAcceptJob {
        void onCallBack(ArrayList<Parent> parent);
    }

    private interface CallbackGetAppliedPost{
        void onCallBack(ArrayList<String> PostingKeys,ArrayList<String> ParentKeys);
    }

    private interface CallbackGetTutorKeys{
        void onCallBack(ArrayList<String> TutorKeys);
    }


}
