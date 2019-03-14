package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class AddNewChild extends AppCompatActivity {

    Button addChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_child);

        addChild = findViewById(R.id.doneChildbtn);

        // Find the ScrollView
     //   final ScrollView scrollView = findViewById(R.id.scrollView4);

        // Create a LinearLayout element
       // final LinearLayout linearLayout = new LinearLayout(this);
       // linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Add Buttons
       // Button button = new Button(this);
       // button.setText("Some text");
        //linearLayout.addView(button);

        addChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add the LinearLayout element to the ScrollView
               // scrollView.addView(linearLayout);

                Intent toHomepage = new Intent(AddNewChild.this,HomePage.class);
                startActivity(toHomepage);
            }
        });



    }
}
