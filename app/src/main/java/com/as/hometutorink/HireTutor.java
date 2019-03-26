package com.as.hometutorink;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.se.omapi.Session;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
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
import java.util.Locale;
import java.util.Map;

public class HireTutor extends AppCompatActivity {

    private ImageButton btnHomePage;
    private Button postjob;
    private Spinner spinner_child_hire, spinner_location_list, spinner_num_sessions;
    private RadioGroup rgSubject;
    private RadioButton rbSubject;
    private FirebaseAuth mAuth;
    private EditText Date;
    private static final String TAG = "HireTutor";

    //for sessiondata

    private LinearLayout session1, session2, session3, session4, session5;
    private EditText start1, start2, start3, start4, start5;
    private EditText end1, end2, end3, end4, end5;
    private Spinner spin1, spin2, spin3, spin4, spin5;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_tutor);
        btnHomePage = findViewById(R.id.homebtn);
        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomePage = new Intent(getApplicationContext(),HomePage.class);
                startActivity(toHomePage);
            }
        });

        Date = findViewById(R.id.dateStarttxt);
        mAuth = FirebaseAuth.getInstance();
        initdbchildren();

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

        };

        Date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(HireTutor.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                } else {
                    // Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });

        initSessionSpinner();
        setListenerOnButton();

    }

    private void updateLabel(Calendar myCalendar) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Date.setText(sdf.format(myCalendar.getTime()));
    }


    private void runTimeSelector(final EditText TimeStart, final EditText TimeEnd){


        TimeStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(HireTutor.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            TimeStart.setText( selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                } else {
                    //Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });

        TimeEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(HireTutor.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            TimeEnd.setText( selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                } else {
                    //Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void initSessionSpinner(){
        spinner_num_sessions = findViewById(R.id.spinnerSessionsList);
        session1 = findViewById(R.id.entriesSessionsI);
        session2 = findViewById(R.id.entriesSessionsII);
        session3 = findViewById(R.id.entriesSessionsIII);
        session4 = findViewById(R.id.entriesSessionsIV);
        session5 = findViewById(R.id.entriesSessionsV);

        spin1 = findViewById(R.id.spinnerDayListI);
        spin2 = findViewById(R.id.spinnerDayListII);
        spin3 = findViewById(R.id.spinnerDayListIII);
        spin4 = findViewById(R.id.spinnerDayListIV);
        spin5 = findViewById(R.id.spinnerDayListV);

        start1 = findViewById(R.id.TimeStarttxtI);
        start2 = findViewById(R.id.TimeStarttxtII);
        start3 = findViewById(R.id.TimeStarttxtIII);
        start4 = findViewById(R.id.TimeStarttxtIV);
        start5 = findViewById(R.id.TimeStarttxtV);

        end1 = findViewById(R.id.TimeEndtxtI);
        end2 = findViewById(R.id.TimeEndtxtII);
        end3 = findViewById(R.id.TimeEndtxtIII);
        end4 = findViewById(R.id.TimeEndtxtIV);
        end5 = findViewById(R.id.TimeEndtxtV);


        ArrayAdapter<String> adapterCourts = new ArrayAdapter<String>(HireTutor.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.numsessions));
        adapterCourts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_num_sessions.setAdapter(adapterCourts);


        ArrayAdapter<String> adapterDaysName = new ArrayAdapter<String>(HireTutor.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.daysname));
        adapterDaysName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapterDaysName);
        spin2.setAdapter(adapterDaysName);
        spin3.setAdapter(adapterDaysName);
        spin4.setAdapter(adapterDaysName);
        spin5.setAdapter(adapterDaysName);

        runTimeSelector(start1,end1);
        runTimeSelector(start2,end2);
        runTimeSelector(start3,end3);
        runTimeSelector(start4,end4);
        runTimeSelector(start5,end5);


        spinner_num_sessions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String curr_sessions = String.valueOf(spinner_num_sessions.getSelectedItem());

                if(curr_sessions.equals("1")){
                    session1.setVisibility(View.VISIBLE);
                }

                if(curr_sessions.equals("2")){
                    session1.setVisibility(View.VISIBLE);
                    session2.setVisibility(View.VISIBLE);
                }

                if(curr_sessions.equals("3")){
                    session1.setVisibility(View.VISIBLE);
                    session2.setVisibility(View.VISIBLE);
                    session3.setVisibility(View.VISIBLE);
                }

                if(curr_sessions.equals("4")){
                    session1.setVisibility(View.VISIBLE);
                    session2.setVisibility(View.VISIBLE);
                    session3.setVisibility(View.VISIBLE);
                    session4.setVisibility(View.VISIBLE);
                }

                if(curr_sessions.equals("5")){
                    session1.setVisibility(View.VISIBLE);
                    session2.setVisibility(View.VISIBLE);
                    session3.setVisibility(View.VISIBLE);
                    session4.setVisibility(View.VISIBLE);
                    session5.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void initdbchildren() {

        String userID = mAuth.getUid();
        final ArrayList<Children> arrayListChildren = new ArrayList<Children>();
        final DatabaseReference refChildren = FirebaseDatabase.getInstance()
                .getReference("children").child(userID);

        refChildren.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {


                    String childrenname = uniqueKeySnapshot.child("name").getValue().toString();
                    String childrenedulevel = uniqueKeySnapshot.child("edulevel").getValue().toString();
                    String childrenlevel = uniqueKeySnapshot.child("level").getValue().toString();
                    String childrenid = uniqueKeySnapshot.child("id").getValue().toString();

                    Children children = new Children(childrenname, childrenedulevel, childrenlevel, childrenid);
                    arrayListChildren.add(children);

                }

                runspinner(arrayListChildren);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error: " + databaseError);
            }
        });

    }


    public void runspinner(ArrayList<Children> arrayListChildren)
    {
        int sizeListChild = arrayListChildren.size();
        String[] arraySpinner = new String[sizeListChild];

        for (int j = 0; j < arrayListChildren.size(); j++) {

            arraySpinner[j] = arrayListChildren.get(j).getChildname();
        }

        spinner_child_hire = findViewById(R.id.spinnerChildList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_child_hire.setAdapter(adapter);

        spinner_location_list = findViewById(R.id.spinnerLocationList);
        ArrayAdapter<String> adapterCourts = new ArrayAdapter<String>(HireTutor.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.locationlist));
        adapterCourts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_location_list.setAdapter(adapterCourts);
    }


    public void setListenerOnButton() {
        postjob = findViewById(R.id.postJobbtn);
        Date = findViewById(R.id.dateStarttxt);
        rgSubject = findViewById(R.id.rgSubjectHire);
        spinner_location_list = findViewById(R.id.spinnerLocationList);
        spinner_child_hire = findViewById(R.id.spinnerChildList);

        //sessions

        session1 = findViewById(R.id.entriesSessionsI);
        session2 = findViewById(R.id.entriesSessionsII);
        session3 = findViewById(R.id.entriesSessionsIII);
        session4 = findViewById(R.id.entriesSessionsIV);
        session5 = findViewById(R.id.entriesSessionsV);

        spin1 = findViewById(R.id.spinnerDayListI);
        spin2 = findViewById(R.id.spinnerDayListII);
        spin3 = findViewById(R.id.spinnerDayListIII);
        spin4 = findViewById(R.id.spinnerDayListIV);
        spin5 = findViewById(R.id.spinnerDayListV);

        start1 = findViewById(R.id.TimeStarttxtI);
        start2 = findViewById(R.id.TimeStarttxtII);
        start3 = findViewById(R.id.TimeStarttxtIII);
        start4 = findViewById(R.id.TimeStarttxtIV);
        start5 = findViewById(R.id.TimeStarttxtV);

        end1 = findViewById(R.id.TimeEndtxtI);
        end2 = findViewById(R.id.TimeEndtxtII);
        end3 = findViewById(R.id.TimeEndtxtIII);
        end4 = findViewById(R.id.TimeEndtxtIV);
        end5 = findViewById(R.id.TimeEndtxtV);



        postjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int index_selected = spinner_child_hire.getSelectedItemPosition();
                int selectedID = rgSubject.getCheckedRadioButtonId();
                rbSubject = findViewById(selectedID);
                ArrayList<SessionData> list_sessionData = new ArrayList<SessionData>();

                if(session1.getVisibility() == View.VISIBLE){
                    String st = start1.getText().toString();
                    String et = end1.getText().toString();
                    String day = String.valueOf(spin1.getSelectedItem());
                    SessionData sessionData = new SessionData(day,st,et);
                    list_sessionData.add(sessionData);
                }


                if(session2.getVisibility() == View.VISIBLE){
                    String st = start2.getText().toString();
                    String et = end2.getText().toString();
                    String day = String.valueOf(spin2.getSelectedItem());
                    SessionData sessionData = new SessionData(day,st,et);
                    list_sessionData.add(sessionData);
                }

                if(session3.getVisibility() == View.VISIBLE){
                    String st = start3.getText().toString();
                    String et = end3.getText().toString();
                    String day = String.valueOf(spin3.getSelectedItem());
                    SessionData sessionData = new SessionData(day,st,et);
                    list_sessionData.add(sessionData);
                }

                if(session4.getVisibility() == View.VISIBLE){
                    String st = start4.getText().toString();
                    String et = end4.getText().toString();
                    String day = String.valueOf(spin2.getSelectedItem());
                    SessionData sessionData = new SessionData(day,st,et);
                    list_sessionData.add(sessionData);
                }

                if(session5.getVisibility() == View.VISIBLE){
                    String st = start5.getText().toString();
                    String et = end5.getText().toString();
                    String day = String.valueOf(spin5.getSelectedItem());
                    SessionData sessionData = new SessionData(day,st,et);
                    list_sessionData.add(sessionData);
                }

                String location = String.valueOf(spinner_location_list.getSelectedItem());
                String childselected = String.valueOf(spinner_child_hire.getSelectedItem());
                String subject = rbSubject.getText().toString();
                String date = Date.getText().toString();


                start_db(index_selected, childselected, subject, location, date, list_sessionData);

                Intent toJobList = new Intent(HireTutor.this,TutorRequestOpen.class);
                startActivity(toJobList);
            }
        });
    }

    public void start_db(final int index_selected, final String childselected, final String subject, final String location, final String date, final ArrayList<SessionData> sessionData)
    {
        String userID = mAuth.getUid();
        final ArrayList<Children> arrayListChildren = new ArrayList<Children>();
        final DatabaseReference refChildren = FirebaseDatabase.getInstance()
                .getReference("children").child(userID);

        refChildren.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {


                    String childrenname = uniqueKeySnapshot.child("name").getValue().toString();
                    String childrenedulevel = uniqueKeySnapshot.child("edulevel").getValue().toString();
                    String childrenlevel = uniqueKeySnapshot.child("level").getValue().toString();
                    String childrenid = uniqueKeySnapshot.child("id").getValue().toString();


                    Children children = new Children(childrenname, childrenedulevel, childrenlevel, childrenid);
                    arrayListChildren.add(children);

                }


                InsertPostingDB(arrayListChildren.get(index_selected).getChildID(), arrayListChildren.get(index_selected).getEdulevel(),  arrayListChildren.get(index_selected).getLevel(),childselected, subject, location, date, sessionData);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error: " + databaseError);
            }
        });

    }


    public void InsertPostingDB(String childID, String childEduLevel,String childLevel, String childselected, String subject, String location, String date, ArrayList<SessionData> sessionData)
    {

        FirebaseUser currentUser = mAuth.getCurrentUser();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("jobposting");

        String postingID = FirebaseDatabase.getInstance().getReference().child("jobposting").push().getKey();


        Map<String, String> job_details = new HashMap<String, String>();
        job_details.put("child_id",  childID);
        job_details.put("edu_level",  childEduLevel);
        job_details.put("level",  childLevel);
        job_details.put("child_name", childselected);
        job_details.put("post_id", postingID);
        job_details.put("subject", subject);
        job_details.put("location", location);
        job_details.put("date", date);
        job_details.put("status", "true");

        myRef.child(currentUser.getUid()).child(postingID).setValue(job_details);

        for (SessionData sd:sessionData) {
            String sessionID = FirebaseDatabase.getInstance().getReference().child("jobposting").child(currentUser.getUid()).child(postingID).child("session").push().getKey();
            myRef.child(currentUser.getUid()).child(postingID).child("session").child(sessionID).child("day").setValue(sd.getDay());
            myRef.child(currentUser.getUid()).child(postingID).child("session").child(sessionID).child("start_time").setValue(sd.getStart_time());
            myRef.child(currentUser.getUid()).child(postingID).child("session").child(sessionID).child("end_time").setValue(sd.getEnd_time());
        }
    }



}
