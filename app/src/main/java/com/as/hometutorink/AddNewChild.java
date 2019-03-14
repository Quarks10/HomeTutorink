package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class AddNewChild extends AppCompatActivity {

    Button addChild, doneaddChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_child);

        doneaddChild = findViewById(R.id.doneChildbtn);
        addChild = findViewById(R.id.addNewChildbtn);

        // Find the ScrollView
     //   final ScrollView scrollView = findViewById(R.id.scrollView4);

        // Create a LinearLayout element
       // final LinearLayout linearLayout = new LinearLayout(this);
       // linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Add Buttons
       // Button button = new Button(this);
       // button.setText("Some text");
        //linearLayout.addView(button);

        doneaddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add the LinearLayout element to the ScrollView
               // scrollView.addView(linearLayout);

                Intent toHomepage = new Intent(AddNewChild.this,HomePage.class);
                startActivity(toHomepage);
            }
        });


        addChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddMoreChild = new Intent(AddNewChild.this,ChildInfo.class);
                startActivity(toAddMoreChild);
            }
        });


    }
}
