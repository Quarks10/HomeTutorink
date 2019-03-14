package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChildInfo extends AppCompatActivity {

    Button btnAddChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_info);

        btnAddChild = findViewById(R.id.updateChildbtn);

        btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddNewChild = new Intent(ChildInfo.this,AddNewChild.class);
                startActivity(toAddNewChild);
            }
        });


    }
}
