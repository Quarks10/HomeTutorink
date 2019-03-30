package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class TutorDashboardCal extends AppCompatActivity {

    FirebaseAuth mAuth;
    ImageButton btnHomePage;
    CalendarView cv;

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
        CalendarController();
    }

    private void CalendarController(){
        cv = findViewById(R.id.calendarViewTutor);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                EnableEmptyLayout();
                removeAllViewsInDaysLayout();
                final ArrayList<DateData> listDateData = getListDateData(year,month,dayOfMonth);
               // generateDashBoard(listDateData);

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
                                    generateDashBoard(onGoingJob.get(i), parentKey.get(i),currparent, listDateData);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void removeAllViewsInDaysLayout(){
        LinearLayout monday = findViewById(R.id.monCardLayoutT);
        LinearLayout tuesday = findViewById(R.id.tuesCardLayoutT);
        LinearLayout wednesday = findViewById(R.id.wedCardLayoutT);
        LinearLayout thursday = findViewById(R.id.thursCardLayoutT);
        LinearLayout friday = findViewById(R.id.friCardLayoutT);
        LinearLayout saturday = findViewById(R.id.satCardLayoutT);
        LinearLayout sunday = findViewById(R.id.sunCardLayoutT);
        monday.removeAllViews();
        tuesday.removeAllViews();
        wednesday.removeAllViews();
        thursday.removeAllViews();
        friday.removeAllViews();
        saturday.removeAllViews();
        sunday.removeAllViews();
    }

    private void EnableEmptyLayout() {

        LinearLayout monday = findViewById(R.id.monCardLayoutT);
        LinearLayout tuesday = findViewById(R.id.tuesCardLayoutT);
        LinearLayout wednesday = findViewById(R.id.wedCardLayoutT);
        LinearLayout thursday = findViewById(R.id.thursCardLayoutT);
        LinearLayout friday = findViewById(R.id.friCardLayoutT);
        LinearLayout saturday = findViewById(R.id.satCardLayoutT);
        LinearLayout sunday = findViewById(R.id.sunCardLayoutT);

        TextView mondaytxt = findViewById(R.id.monTxt);
        TextView tuesdaytxt = findViewById(R.id.tuesTxt);
        TextView wednesdaytxt = findViewById(R.id.wedTxt);
        TextView thursdaytxt = findViewById(R.id.thursTxt);
        TextView fridaytxt = findViewById(R.id.friTxt);
        TextView saturdaytxt = findViewById(R.id.satTxt);
        TextView sundaytxt = findViewById(R.id.sunTxt);

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


    public void generateDashBoard(String post_id, String parent_id, final Parent currparent, final ArrayList<DateData> dateData){

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

                        for (DateData dd:dateData) {

                            for (SessionData sd:jobsessionData) {

                                if(sd.getDay().equals(dd.getDay())){
                                    generateDashboardList(childname,childlevel,childedulevel,location,address,subject,datetime,dd,sd);
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

        LinearLayout monday = findViewById(R.id.monCardLayoutT);
        LinearLayout tuesday = findViewById(R.id.tuesCardLayoutT);
        LinearLayout wednesday = findViewById(R.id.wedCardLayoutT);
        LinearLayout thursday = findViewById(R.id.thursCardLayoutT);
        LinearLayout friday = findViewById(R.id.friCardLayoutT);
        LinearLayout saturday = findViewById(R.id.satCardLayoutT);
        LinearLayout sunday = findViewById(R.id.sunCardLayoutT);

        TextView mondaytxt = findViewById(R.id.monTxt);
        TextView tuesdaytxt = findViewById(R.id.tuesTxt);
        TextView wednesdaytxt = findViewById(R.id.wedTxt);
        TextView thursdaytxt = findViewById(R.id.thursTxt);
        TextView fridaytxt = findViewById(R.id.friTxt);
        TextView saturdaytxt = findViewById(R.id.satTxt);
        TextView sundaytxt = findViewById(R.id.sunTxt);

        if (monday.getChildCount() == 0) {
            monday.setVisibility(View.GONE);
            mondaytxt.setVisibility(View.GONE);
        }

        if (tuesday.getChildCount() == 0) {
            tuesday.setVisibility(View.GONE);
            tuesdaytxt.setVisibility(View.GONE);
        }

        if (wednesday.getChildCount() == 0) {
            wednesday.setVisibility(View.GONE);
            wednesdaytxt.setVisibility(View.GONE);
        }

        if (thursday.getChildCount() == 0) {
            thursday.setVisibility(View.GONE);
            thursdaytxt.setVisibility(View.GONE);
        }

        if (friday.getChildCount() == 0) {
            friday.setVisibility(View.GONE);
            fridaytxt.setVisibility(View.GONE);
        }

        if (saturday.getChildCount() == 0) {
            saturday.setVisibility(View.GONE);
            saturdaytxt.setVisibility(View.GONE);
        }

        if (sunday.getChildCount() == 0) {
            sunday.setVisibility(View.GONE);
            sundaytxt.setVisibility(View.GONE);
        }

    }

    public void generateDashboardList(String childname, String childlevel, String childedulevel, String location, String address, String subject, String datetime,DateData dateData, SessionData sd) {

       // LinearLayout mainLayout = findViewById(R.id.Tutordbll);
        LinearLayout mainLayout = findViewById(R.id.monCardLayoutT);


        if(dateData.getDay().equals("Monday")){
            mainLayout = findViewById(R.id.monCardLayoutT);
        }

        if(dateData.getDay().equals("Tuesday")){
            mainLayout = findViewById(R.id.tuesCardLayoutT);
        }

        if(dateData.getDay().equals("Wednesday")){
            mainLayout = findViewById(R.id.wedCardLayoutT);
        }

        if(dateData.getDay().equals("Thursday")){
            mainLayout = findViewById(R.id.thursCardLayoutT);
        }

        if(dateData.getDay().equals("Friday")){
            mainLayout = findViewById(R.id.friCardLayoutT);
        }

        if(dateData.getDay().equals("Saturday")){
            mainLayout = findViewById(R.id.satCardLayoutT);
        }

        if(dateData.getDay().equals("Sunday")){
            mainLayout = findViewById(R.id.sunCardLayoutT);
        }

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.tutordbcard, mainLayout, false);


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

        TextView Date = myLayout.findViewById(R.id.dashCardDayTxt);
        Date.setText("Day: " + dateData.getDay() + ", " + dateData.getSdate());

        TextView Time = myLayout.findViewById(R.id.dashCardTimeTxt);
        Time.setText("Time: " + sd.getStart_time() + " - " + sd.getEnd_time());

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
