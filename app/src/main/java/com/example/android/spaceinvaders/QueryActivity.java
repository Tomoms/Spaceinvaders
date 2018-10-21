package com.example.android.spaceinvaders;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class QueryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        BottomNavigationView menu = findViewById(R.id.bottom_bar);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.faq:
                        Intent faqIntent = new Intent(getApplicationContext(), FaqActivity.class);
                        faqIntent.putExtra("checked", id);
                        startActivity(faqIntent);
                        return true;
                    case R.id.query:
                        Intent videoIntent = new Intent(getApplicationContext(), QueryActivity.class);
                        videoIntent.putExtra("checked", id);
                        startActivity(videoIntent);
                        return true;
                    case R.id.launches:
                        Intent launchIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(launchIntent);
                        return true;
                }
                return true;
            }
        });

        TextView next = findViewById(R.id.next);
        TextView from = findViewById(R.id.from);
        TextView to = findViewById(R.id.to);
        TextView launchview = findViewById(R.id.launch);
        next.setTextColor(getResources().getColor(R.color.text_color));
        from.setTextColor(getResources().getColor(R.color.text_color));
        to.setTextColor(getResources().getColor(R.color.text_color));
        launchview.setTextColor(getResources().getColor(R.color.text_color));

        final EditText nextn = findViewById(R.id.nextn);
        final EditText launch = findViewById(R.id.launch_name);
        final EditText fromDate = findViewById(R.id.from_date);
        final EditText toDate = findViewById(R.id.to_date);

        nextn.setTextColor(getResources().getColor(R.color.text_color));
        launch.setTextColor(getResources().getColor(R.color.text_color));
        fromDate.setTextColor(getResources().getColor(R.color.text_color));
        toDate.setTextColor(getResources().getColor(R.color.text_color));

        Button queryButton = findViewById(R.id.querybutton);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nextStr = nextn.getText().toString();
                String launchStr = launch.getText().toString();
                String fromStr = fromDate.getText().toString();
                String toStr = toDate.getText().toString();

                if (!validateInput(nextStr, launchStr, fromStr, toStr)) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.invalid, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    String finalURL = "https://launchlibrary.net/1.4/launch/";
                    if (!TextUtils.isEmpty(nextStr))
                        finalURL += ("next/" + nextStr);
                    if (!TextUtils.isEmpty(launchStr))
                        finalURL += "/" + launchStr;
                    if (!TextUtils.isEmpty(fromStr))
                        finalURL += "/" + fromStr;
                    if (!TextUtils.isEmpty(toStr))
                        finalURL += "/" + toStr;
                    intent.putExtra("url", finalURL);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validateInput(String s1, String s2, String s3, String s4) {
        if ((!s1.isEmpty() && s2.isEmpty() && s3.isEmpty() && s4.isEmpty()) ||
                (s1.isEmpty() && !s2.isEmpty() && s3.isEmpty() && s4.isEmpty()) ||
                (s1.isEmpty() && s2.isEmpty() && !s3.isEmpty() && s4.isEmpty()) ||
                (s1.isEmpty() && s2.isEmpty() && !s3.isEmpty() && !s4.isEmpty()))
            return true;
        return false;
    }

}
