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

        runFirstTime();
        init();
    }


    private void runFirstTime(){
        cv = findViewById(R.id.calendarViewParent);

        Date date = new Date(cv.getDate());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        EnableEmptyLayout();
        removeAllViewsInDaysLayout();
        ArrayList<DateData> listDateData = getListDateData(year,month,day);
        generateDashBoard(listDateData);

    }

    public void init() {
        CalendarController();
    }

    private void CalendarController(){
        cv = findViewById(R.id.calendarViewParent);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                EnableEmptyLayout();
                removeAllViewsInDaysLayout();
                ArrayList<DateData> listDateData = getListDateData(year,month,dayOfMonth);
                generateDashBoard(listDateData);

            }
        });
    }

    private void removeAllViewsInDaysLayout(){
        LinearLayout monday = findViewById(R.id.mondayCardLayout);
        LinearLayout tuesday = findViewById(R.id.tuesCardLayout);
        LinearLayout wednesday = findViewById(R.id.wedCardLayout);
        LinearLayout thursday = findViewById(R.id.thursCardLayout);
        LinearLayout friday = findViewById(R.id.friCardLayout);
        LinearLayout saturday = findViewById(R.id.satCardLayout);
        LinearLayout sunday = findViewById(R.id.sunCardLayout);
         monday.removeAllViews();
         tuesday.removeAllViews();
         wednesday.removeAllViews();
         thursday.removeAllViews();
         friday.removeAllViews();
         saturday.removeAllViews();
         sunday.removeAllViews();
    }

    private void EnableEmptyLayout() {

        LinearLayout monday = findViewById(R.id.mondayCardLayout);
        LinearLayout tuesday = findViewById(R.id.tuesCardLayout);
        LinearLayout wednesday = findViewById(R.id.wedCardLayout);
        LinearLayout thursday = findViewById(R.id.thursCardLayout);
        LinearLayout friday = findViewById(R.id.friCardLayout);
        LinearLayout saturday = findViewById(R.id.satCardLayout);
        LinearLayout sunday = findViewById(R.id.sunCardLayout);

        TextView mondaytxt = findViewById(R.id.monTxtp);
        TextView tuesdaytxt = findViewById(R.id.tuesTxtp);
        TextView wednesdaytxt = findViewById(R.id.wedTxtp);
        TextView thursdaytxt = findViewById(R.id.thurTxtp);
        TextView fridaytxt = findViewById(R.id.friTxtp);
        TextView saturdaytxt = findViewById(R.id.satTxtp);
        TextView sundaytxt = findViewById(R.id.sunTxtp);

        if(monday.getChildCount() == 0){
            monday.setVisibility(View.VISIBLE);
            mondaytxt.setVisibility(View.VISIBLE);
        }

        if(tuesday.getChildCount() == 0){
            tuesday.setVisibility(View.VISIBLE);
            tuesdaytxt.setVisibility(View.VISIBLE);
        }

        if(wednesday.getChildCount() == 0){
            wednesday.setVisibility(View.VISIBLE);
            wednesdaytxt.setVisibility(View.VISIBLE);
        }

        if(thursday.getChildCount() == 0){
            thursday.setVisibility(View.VISIBLE);
            thursdaytxt.setVisibility(View.VISIBLE);
        }

        if(friday.getChildCount() == 0){
            friday.setVisibility(View.VISIBLE);
            fridaytxt.setVisibility(View.VISIBLE);
        }

        if(saturday.getChildCount() == 0){
            saturday.setVisibility(View.VISIBLE);
            saturdaytxt.setVisibility(View.VISIBLE);
        }

        if(sunday.getChildCount() == 0){
            sunday.setVisibility(View.VISIBLE);
            sundaytxt.setVisibility(View.VISIBLE);
        }


    }

    private ArrayList<DateData> getListDateData(int year, int month, int dayOfMonth){

        ArrayList<Calendar> list_calendar = new ArrayList<>();
        ArrayList<Date> list_date = new ArrayList<>();
        ArrayList<DateData> list_date_data = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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

                        Log.d(TAG, "datedata: " + listDateData.size());
                        Log.d(TAG, "jobsession: " + jobsessionData.size());

                        for (DateData dd:listDateData) {

                            for (SessionData sd:jobsessionData) {

                                if(sd.getDay().equals(dd.getDay())){
                                    generateDashboardList(childname,tutorname,subject,startdate,dd,sd);
                                    Log.d(TAG, "getday: " + sd.getDay());
                                }
                            }
                        }
                    }
                }

                DisableEmptyLayout();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void DisableEmptyLayout() {

        LinearLayout monday = findViewById(R.id.mondayCardLayout);
        LinearLayout tuesday = findViewById(R.id.tuesCardLayout);
        LinearLayout wednesday = findViewById(R.id.wedCardLayout);
        LinearLayout thursday = findViewById(R.id.thursCardLayout);
        LinearLayout friday = findViewById(R.id.friCardLayout);
        LinearLayout saturday = findViewById(R.id.satCardLayout);
        LinearLayout sunday = findViewById(R.id.sunCardLayout);

        TextView mondaytxt = findViewById(R.id.monTxtp);
        TextView tuesdaytxt = findViewById(R.id.tuesTxtp);
        TextView wednesdaytxt = findViewById(R.id.wedTxtp);
        TextView thursdaytxt = findViewById(R.id.thurTxtp);
        TextView fridaytxt = findViewById(R.id.friTxtp);
        TextView saturdaytxt = findViewById(R.id.satTxtp);
        TextView sundaytxt = findViewById(R.id.sunTxtp);

        if(monday.getChildCount() == 0){
            monday.setVisibility(View.GONE);
            mondaytxt.setVisibility(View.GONE);
        }

        if(tuesday.getChildCount() == 0){
            tuesday.setVisibility(View.GONE);
            tuesdaytxt.setVisibility(View.GONE);
        }

        if(wednesday.getChildCount() == 0){
            wednesday.setVisibility(View.GONE);
            wednesdaytxt.setVisibility(View.GONE);
        }

        if(thursday.getChildCount() == 0){
            thursday.setVisibility(View.GONE);
            thursdaytxt.setVisibility(View.GONE);
        }

        if(friday.getChildCount() == 0){
            friday.setVisibility(View.GONE);
            fridaytxt.setVisibility(View.GONE);
        }

        if(saturday.getChildCount() == 0){
            saturday.setVisibility(View.GONE);
            saturdaytxt.setVisibility(View.GONE);
        }

        if(sunday.getChildCount() == 0){
            sunday.setVisibility(View.GONE);
            sundaytxt.setVisibility(View.GONE);
        }


    }


    public void generateDashboardList(String childname, String tutorname, String subject, String startdate, DateData dateData, SessionData sessionData) {


        final String postID = getIntent().getStringExtra("post_id");

        LinearLayout mainLayout = findViewById(R.id.mondayCardLayout);


        if(dateData.getDay().equals("Monday")){
            mainLayout = findViewById(R.id.mondayCardLayout);
        }

        if(dateData.getDay().equals("Tuesday")){
            mainLayout = findViewById(R.id.tuesCardLayout);
        }

        if(dateData.getDay().equals("Wednesday")){
            mainLayout = findViewById(R.id.wedCardLayout);
        }

        if(dateData.getDay().equals("Thursday")){
            mainLayout = findViewById(R.id.thursCardLayout);
        }

        if(dateData.getDay().equals("Friday")){
            mainLayout = findViewById(R.id.friCardLayout);
        }

        if(dateData.getDay().equals("Saturday")){
            mainLayout = findViewById(R.id.satCardLayout);
        }

        if(dateData.getDay().equals("Sunday")){
            mainLayout = findViewById(R.id.sunCardLayout);
        }


        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.parentdbcard, mainLayout, false);


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
