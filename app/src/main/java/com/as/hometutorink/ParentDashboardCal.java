package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
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

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

public class ParentDashboardCal extends AppCompatActivity {

    ImageButton btnHomePage;
    CalendarView cv;
    FirebaseAuth mAuth;
    TextView DOW;
    private static final String TAG = "ParentDashboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard_cal);

        btnHomePage = findViewById(R.id.homebtn);

        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomePage = new Intent(getApplicationContext(),HomePage.class);
                startActivity(toHomePage);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        cv = findViewById(R.id.calendarViewParent);
        DOW = findViewById(R.id.text_dow);
        init();

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {


                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
               // System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);

               // DateTime today = DateTime.today(TimeZone.getDefault());
            //    DateTime firstDayThisWeek = today; //start value
             //   int todaysWeekday = today.getWeekDay();
              //  int SUNDAY = 1;
             //   if(todaysWeekday > SUNDAY){
                 //   int numDaysFromSunday = todaysWeekday - SUNDAY;
               //     firstDayThisWeek = today.minusDays(numDaysFromSunday);



                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.SUNDAY);

               // DOW.setText(Integer.toString(dayOfWeek));
                DOW.setText(formattedDate);

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
                        String datetime = ds.child("date").getValue().toString();

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



        Button unsubscribebtn = myLayout.findViewById(R.id.btn_unsub);
        unsubscribebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // AcceptJob(postID, tutorKey, tutorApplications);
                showMessage("unsubscribe");
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
        TextView childName = myLayout.findViewById(R.id.dashCardNameTxt);
        childName.setText(childname);

        TextView tutorName = myLayout.findViewById(R.id.dashCardTutorTxt);
        tutorName.setText("Tutor: " + tutorname);

        TextView subject_text = myLayout.findViewById(R.id.dashCardSubjectTxt);
        subject_text.setText("Subject: " + subject);

        TextView dateTime = myLayout.findViewById(R.id.dashCardTimeTxt);
        String testnewline = "Session 1: \nDay: Monday\nStart Time: 13:30\nEndTime: 14:30\n\nSession 2: \nDay: Monday\nStart Time: 13:30\nEndTime: 14:30";
        //dateTime.setText(testnewline);
        dateTime.setText("Date: " + datetime);

        mainLayout.addView(myLayout);

    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }

}
