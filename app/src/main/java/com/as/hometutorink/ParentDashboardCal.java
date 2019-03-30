package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

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

        DOW = findViewById(R.id.text_dow);
        init();





    }


    public void init() {
        CalendarController();
    }

    private void CalendarController(){
        cv = findViewById(R.id.calendarViewParent);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                LinearLayout mainLayout = findViewById(R.id.dashboardParentll);
                mainLayout.removeAllViews();

                ArrayList<DateData> listDateData = getListDateData(year,month,dayOfMonth);
                generateDashBoard(listDateData);

            }
        });
    }

    private ArrayList<DateData> getListDateData(int year, int month, int dayOfMonth){

        ArrayList<Calendar> list_calendar = new ArrayList<>();
        ArrayList<Date> list_date = new ArrayList<>();
        ArrayList<DateData> list_date_data = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfday = new SimpleDateFormat("EEEE");
        Calendar calendar = new GregorianCalendar( year, month, dayOfMonth );


        for(int i = 0; i<7; i++){

            if(i==0){
                Date date = calendar.getTime();
                list_date.add(date);
            }else{
                calendar.add(Calendar.DAY_OF_MONTH,1);
                Date date = calendar.getTime();
                list_date.add(date);
                list_calendar.add(calendar);
            }

        }

        for (Date dates:list_date) {
            Date date = dates;
            String formattedDate = sdf.format(date);
            String formattedDay = sdfday.format(dates);
            DateData dateData = new DateData(date,formattedDate,formattedDay);
            list_date_data.add(dateData);
        }

        return list_date_data;


    }


    public void generateDashBoard(final ArrayList<DateData> listDateData){

        FirebaseUser currUser = mAuth.getCurrentUser();
        DatabaseReference refposting = FirebaseDatabase.getInstance().getReference("jobposting");
        refposting.child(currUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {


                    if(ds.child("status").getValue().equals("OnGoing")){

                        String childname = ds.child("child_name").getValue().toString();
                        String tutorname = ds.child("tutor_name").getValue().toString();
                        String subject = ds.child("subject").getValue().toString();
                        String startdate = ds.child("date").getValue().toString();


                        ArrayList<SessionData> jobsessionData = new ArrayList<>();
                        Iterable<DataSnapshot> snapshotIterator = ds.child("session").getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                        while (iterator.hasNext()) {
                            DataSnapshot next = (DataSnapshot) iterator.next();
                            String day = next.child("day").getValue().toString();
                            String st = next.child("start_time").getValue().toString();
                            String et = next.child("end_time").getValue().toString();
                            jobsessionData.add(new SessionData(day,st,et));
                        }

                        Log.d(TAG, "Size: " + Integer.toString(jobsessionData.size()));


                        for (DateData dd:listDateData) {

                            for (SessionData sd:jobsessionData) {

                                if(sd.getDay().equals(dd.getDay())){
                                    generateDashboardList(childname,tutorname,subject,startdate,dd,sd);
                                }

                            }

                        }

                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void generateDashboardList(String childname, String tutorname, String subject, String startdate, DateData dateData, SessionData sessionData) {


        final String postID = getIntent().getStringExtra("post_id");
        LinearLayout mainLayout = findViewById(R.id.dashboardParentll);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.parentdbcard, mainLayout, false);


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

        TextView dates = myLayout.findViewById(R.id.dashCardDayTxt);
        dates.setText("Day: " + dateData.getDay() + ", " + dateData.getSdate());

        TextView times = myLayout.findViewById(R.id.dashCardTimeTxt);
        times.setText("Time: " + sessionData.getStart_time() + " - " + sessionData.getEnd_time());

        mainLayout.addView(myLayout);

    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }

}
