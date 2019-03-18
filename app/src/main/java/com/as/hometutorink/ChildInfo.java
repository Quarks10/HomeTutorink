package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChildInfo extends AppCompatActivity {

    private ImageButton btnHomePage;
    private Button btnAddChild;
    private EditText child_name;
    private RadioGroup rgEducation;
    private RadioButton rbEducation;
    private FirebaseAuth mAuth;
    private Spinner spinnerPrimary, spinnerSecondary;
    private static final String TAG = "childinfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_info);

        btnHomePage = findViewById(R.id.homebtn);

        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomePage = new Intent(getApplicationContext(),HomePage.class);
                startActivity(toHomePage);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        runSpinner();
        addListenerOnButton();


    }

    @Override
    protected void onStart() {
        super.onStart();

        //FirebaseUser currentUser = mAuth.getCurrentUser();
      //  if(currentUser != null)
    //    {
          //  Log.d(TAG,currentUser.getEmail());
      //  }else
      //  {
           // Log.d(TAG,"no user");
     //   }

    }


    public void addListenerOnButton()
    {
        btnAddChild = findViewById(R.id.updateChildbtn);
        child_name = findViewById(R.id.childNameInput);
        rgEducation = findViewById(R.id.rgEducation);
        spinnerPrimary = findViewById(R.id.spinner_primary);
        spinnerSecondary = findViewById(R.id.spinner_secondary);


        btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedID = rgEducation.getCheckedRadioButtonId();
                rbEducation = findViewById(selectedID);

                Log.d(TAG, "edulevel: " + selectedID);

                String primary = String.valueOf(spinnerPrimary.getSelectedItem());
                String secondary = String.valueOf(spinnerSecondary.getSelectedItem());

                String edulevel = rbEducation.getText().toString();
                String childname = child_name.getText().toString();


                InsertChildrenDB(edulevel,childname,primary,secondary);

                Intent toListOfChild = new Intent(ChildInfo.this,ListOfChild.class);
                startActivity(toListOfChild);
            }
        });

    }


    public void InsertChildrenDB(String edulevel, String childname, String primary, String secondary)
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String curr_level;

        if (edulevel.equals("Primary School"))
        {
            curr_level = primary;
        }
        else
        {
            curr_level = secondary;
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("children");

        String childrenid = FirebaseDatabase.getInstance().getReference().child("children").push().getKey();


        Map<String, String> child_details = new HashMap<String, String>();
        child_details.put("name",  childname);
        child_details.put("edulevel", edulevel);
        child_details.put("level", curr_level);
        child_details.put("id", childrenid);

        myRef.child(currentUser.getUid()).child(childrenid).setValue(child_details);

    }


    public void runSpinner(){
        spinnerPrimary = findViewById(R.id.spinner_primary);
        ArrayAdapter<String> adapterCourts = new ArrayAdapter<String>(ChildInfo.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.primaryschoollv));
        adapterCourts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrimary.setAdapter(adapterCourts);

        spinnerSecondary = findViewById(R.id.spinner_secondary);
        ArrayAdapter<String> adapterTime1 = new ArrayAdapter<String>(ChildInfo.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.secondaryschoollv));
        adapterTime1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSecondary.setAdapter(adapterTime1);

    }

}
