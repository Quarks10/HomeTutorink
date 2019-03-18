package com.as.hometutorink;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private Spinner spinner_child_hire, spinner_location_list;
    private RadioGroup rgSubject;
    private RadioButton rbSubject;
    private FirebaseAuth mAuth;
    private EditText Date,Time;
    private static final String TAG = "HireTutor";

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

        Time = findViewById(R.id.TimeStarttxt);
        Date = findViewById(R.id.dateStarttxt);
        // spinner_child_hire = findViewById(R.id.spinnerChildList);
        //  spinner_location_list = findViewById(R.id.spinnerLocationList);
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


        Time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                            Time.setText( selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                } else {
                    //Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });


        setListenerOnButton();

    }

    private void updateLabel(Calendar myCalendar) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Date.setText(sdf.format(myCalendar.getTime()));
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
        Time = findViewById(R.id.TimeStarttxt);
        rgSubject = findViewById(R.id.rgSubjectHire);
        spinner_location_list = findViewById(R.id.spinnerLocationList);
        spinner_child_hire = findViewById(R.id.spinnerChildList);


        postjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int index_selected = spinner_child_hire.getSelectedItemPosition();
                int selectedID = rgSubject.getCheckedRadioButtonId();
                rbSubject = findViewById(selectedID);

                String location = String.valueOf(spinner_location_list.getSelectedItem());
                String childselected = String.valueOf(spinner_child_hire.getSelectedItem());
                String subject = rbSubject.getText().toString();
                String date = Date.getText().toString();
                String time = Time.getText().toString();


                start_db(index_selected, childselected, subject, location, date, time);

                Intent toJobList = new Intent(HireTutor.this,TutorRequestOpen.class);
                startActivity(toJobList);
            }
        });
    }

    public void InsertPostingDB(String childID, String childEduLevel,String childLevel, String childselected, String subject, String location, String date, String time)
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
        job_details.put("time", time);
        job_details.put("status", "true");


        myRef.child(currentUser.getUid()).child(postingID).setValue(job_details);
    }


    public void start_db(final int index_selected, final String childselected, final String subject, final String location, final String date, final String time)
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


                InsertPostingDB(arrayListChildren.get(index_selected).getChildID(), arrayListChildren.get(index_selected).getEdulevel(),  arrayListChildren.get(index_selected).getLevel(),childselected, subject, location, date, time);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error: " + databaseError);
            }
        });



     //   return arrayListChildren.get(index).getChildID();

    }

}
