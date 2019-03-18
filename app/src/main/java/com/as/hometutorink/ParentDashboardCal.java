package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class ParentDashboardCal extends AppCompatActivity {

    CalendarView cv;
    FirebaseAuth mAuth;
    TextView DOW;
    private static final String TAG = "ParentDashboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard_cal);
        mAuth = FirebaseAuth.getInstance();
        cv = findViewById(R.id.calendarViewParent);
        DOW = findViewById(R.id.text_dow);
        init();

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                DOW.setText(Integer.toString(dayOfWeek));

                Log.d(TAG, "onSelectedDayChange: " + dayOfWeek);

            }
        });



    }


    public void init() {
        generateDashBoard();
    }

    public void generateDashBoard(){

        FirebaseUser currUser = mAuth.getCurrentUser();
        DatabaseReference refposting = FirebaseDatabase.getInstance().getReference("jobposting");
        refposting.child(currUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {


                    if(ds.child("status").getValue().equals("Accepted")){

                        String childname = ds.child("child_name").getValue().toString();
                        String tutorname = ds.child("tutor_name").getValue().toString();
                        String subject = ds.child("subject").getValue().toString();
                        String datetime = ds.child("date").getValue().toString() + " " + ds.child("time").getValue().toString();

                        generateDashboardList(childname,tutorname,subject,datetime);

                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void generateDashboardList(String childname, String tutorname, String subject, String datetime) {


        final String postID = getIntent().getStringExtra("post_id");
        LinearLayout mainLayout = findViewById(R.id.dashboardParentll);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.parentdbcard, mainLayout, false);


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

        TextView tutorName = myLayout.findViewById(R.id.dashCardTutorTxt);
        tutorName.setText("Tutor: " + tutorname);

        TextView subject_text = myLayout.findViewById(R.id.dashCardSubjectTxt);
        subject_text.setText("Subject: " + subject);

        TextView dateTime = myLayout.findViewById(R.id.dashCardTimeTxt);
        dateTime.setText("DateTime: " + datetime);

        mainLayout.addView(myLayout);

    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }

    public interface FirebaseCallbackViewApp {
        void onCallBack(ArrayList<TutorApplication> tutorApplication);
    }

}
