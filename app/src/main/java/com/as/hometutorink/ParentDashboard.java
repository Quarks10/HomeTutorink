package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

public class ParentDashboard extends AppCompatActivity {

    Button viewChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        viewChild = findViewById(R.id.childrenViewDB);

        viewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toChildDetail = new Intent(ParentDashboard.this,ChildDetail.class);
                startActivity(toChildDetail);
            }
        });


    }
}
