package com.as.hometutorink;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class ListOfChild extends AppCompatActivity {

    Button addButton, btncopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_child);

        addButton = findViewById(R.id.doneChildbtn);
        btncopy = findViewById(R.id.btncardlistchild);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // get a reference to the already created main layout
                LinearLayout mainLayout = (LinearLayout) findViewById(R.id.linearlayoutListChild);

                // inflate (create) another copy of our custom layout
                LayoutInflater inflater = getLayoutInflater();
                View myLayout = inflater.inflate(R.layout.layout_button_list_child, mainLayout, false);

                // make changes to our custom layout and its subviews
              //  myLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
             //   TextView textView = (TextView) myLayout.findViewById(R.id.textView);
               // textView.setText("New Layout");

                Button button = myLayout.findViewById(R.id.testbutton);
                button.setText("Tukar value dalam");

                // add our custom layout to the main layout
                mainLayout.addView(myLayout);


                /*


                LinearLayout linearLayout = findViewById(R.id.linearlayoutListChild);

                // Create Button Dynamically
                Button btnShow = LayoutInflater.from(ListOfChild.this).inflate(R.layout.)
             //   btnShow.setId(3);
                btnShow.setText("test");
             //   btnShow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(btnShow);
                // Add Button to LinearLayout
              //  if (linearLayout != null) {
              //      linearLayout.addView(btnShow);
              //  }

*/
                /*
                Button myButton = new Button(ListOfChild.this);
                myButton.setText("Add Me");

                LinearLayout ll = (LinearLayout)findViewById(R.id.linearlayoutListChild);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll.addView(myButton, lp);
                */

             //   Button button = new Button(ListOfChild.this);
               // LinearLayout layoutlist = findViewById(R.id.linearlayoutListChild);

               // layoutlist.addView(button);


                //Toast.makeText(getApplicationContext(),"runbutton",1)
                Toast.makeText(ListOfChild.this, "This is my Toast message!",
                        Toast.LENGTH_LONG).show();
            }
        });


    }
}
