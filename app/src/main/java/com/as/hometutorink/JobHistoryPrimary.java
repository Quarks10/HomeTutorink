package com.as.hometutorink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JobHistoryPrimary extends AppCompatActivity {

    Button job_secondary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_history_primary);

        job_secondary = findViewById(R.id.secondarybtnhistory);

        job_secondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSecondary = new Intent(JobHistoryPrimary.this,JobHistorySecondary.class);
                startActivity(toSecondary);
            }
        });

    }
}
