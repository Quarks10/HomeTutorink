package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    Button dashboard, hire_tutor, tutor_request, messages, children, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        dashboard = findViewById(R.id.dashboard_sidebar);
        hire_tutor = findViewById(R.id.hiretutor_sidebar);
        tutor_request = findViewById(R.id.tutorreq_sidebar);
        messages = findViewById(R.id.message_sidebar);
        children = findViewById(R.id.children_sidebar);
        logout = findViewById(R.id.logout_sidebar);


        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toDashboardParent = new Intent(HomePage.this,ParentDashboard.class);
                startActivity(toDashboardParent);
            }
        });

        hire_tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHireTutor = new Intent(HomePage.this,HireTutor.class);
                startActivity(toHireTutor);
            }
        });

        tutor_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTutorRequest = new Intent(HomePage.this,TutorRequestOpen.class);
                startActivity(toTutorRequest);
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMessages = new Intent(HomePage.this,Message.class);
                startActivity(toMessages);
            }
        });

        children.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toChildren = new Intent(HomePage.this,AddNewChild.class);
                startActivity(toChildren);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogout = new Intent(HomePage.this,MainActivity.class);
                startActivity(toLogout);
            }
        });


    }
}
