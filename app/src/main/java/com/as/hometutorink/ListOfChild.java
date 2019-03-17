package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListOfChild extends AppCompatActivity {

    Button doneButton, addChildButton , btncopy;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_child);

        doneButton = findViewById(R.id.doneChildbtn);
        addChildButton = findViewById(R.id.addNewChildbtn);
        btncopy = findViewById(R.id.btncardlistchild);
        mAuth = FirebaseAuth.getInstance();

        init();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toHomePage = new Intent(ListOfChild.this, HomePage.class);
                startActivity(toHomePage);

            }
        });

        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toChildInfo = new Intent(ListOfChild.this, ChildInfo.class);
                startActivity(toChildInfo);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();



    }

    public void init()
    {

        String userID = mAuth.getUid();

        DatabaseReference refChildren = FirebaseDatabase.getInstance()
                .getReference("children").child(userID);

        refChildren.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                    //Loop 1 to go through all the child nodes of users
                   /* for(DataSnapshot booksSnapshot : uniqueKey.child("Books").getChildren()){
                        //loop 2 to go through all the child nodes of books node
                        String bookskey = booksSnapshot.getKey();
                        String booksValue = booksSnapshot.getValue();
                    }
                    */
                  //  Children children = uniqueKeySnapshot.getValue(Children.class);

                    String childrenname = uniqueKeySnapshot.child("name").getValue().toString();
                    String childrenedulevel = uniqueKeySnapshot.child("edulevel").getValue().toString();
                    String childrenlevel = uniqueKeySnapshot.child("level").getValue().toString();
                    String childrenid = uniqueKeySnapshot.child("id").getValue().toString();

                    Children children = new Children(childrenname,childrenedulevel,childrenlevel,childrenid);
                    generateChildList(children);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void generateChildList(Children children)
    {
        LinearLayout mainLayout = findViewById(R.id.linearlayoutListChild);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.layout_button_list_child, mainLayout, false);

        TextView childname = myLayout.findViewById(R.id.listchildNamelbl);
        String newchildname = "Name: " + children.getChildname();
        childname.setText(newchildname);

        TextView childedulevel = myLayout.findViewById(R.id.listchildEdulbl);
        String newchildedulevel = children.getEdulevel();
        childedulevel.setText(newchildedulevel);
       // childedulevel.setText(children.getEdulevel());


        TextView childlevel = myLayout.findViewById(R.id.listchildLevellbl);
        if(children.getEdulevel().equals("Primary School"))
        {
            String newchildlevel = "Standard " + children.getLevel();
            childlevel.setText(newchildlevel);
        }else
        {
            String newchildlevel = "Form " + children.getLevel();
            childlevel.setText(newchildlevel);
        }


        mainLayout.addView(myLayout);

    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }

}
