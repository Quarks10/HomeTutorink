package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomePageTutor extends AppCompatActivity {

    Button dashboard, job_list, job_history, accept_jobs, messages, logout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_tutor);

        dashboard = findViewById(R.id.dashboard_sidebar_tutor);
        job_list = findViewById(R.id.joblist_sidebar);
        job_history = findViewById(R.id.jobhistory_sidebar);
        accept_jobs = findViewById(R.id.acceptjobs_sidebar);
        messages = findViewById(R.id.message_sidebar_tutor);
        logout = findViewById(R.id.logout_sidebar);
        mAuth = FirebaseAuth.getInstance();

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toDashboardTutor = new Intent(HomePageTutor.this,TutorDashboard.class);
                startActivity(toDashboardTutor);
            }
        });

        job_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toJobList = new Intent(HomePageTutor.this,JobListPrimary.class);
                startActivity(toJobList);
            }
        });

        job_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toJobHistory = new Intent(HomePageTutor.this,JobHistoryPrimary.class);
                startActivity(toJobHistory);
            }
        });

        accept_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAcceptJobs = new Intent(HomePageTutor.this,TutorAcceptJobsPrimary.class);
                startActivity(toAcceptJobs);
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMessages = new Intent(HomePageTutor.this,Message.class);
                startActivity(toMessages);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent toLogout = new Intent(HomePageTutor.this,MainActivity.class);
                startActivity(toLogout);
            }
        });


    }
}
