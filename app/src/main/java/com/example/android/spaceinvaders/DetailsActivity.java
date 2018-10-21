package com.example.android.spaceinvaders;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final Launch curLaunch = (Launch) getIntent().getSerializableExtra("launch");
        ImageView img = findViewById(R.id.img);
        String imgURI = curLaunch.getImgURL();
        Picasso.with(getBaseContext()).load(imgURI).into(img);
        TextView missionName = findViewById(R.id.mission_name);
        missionName.setText(curLaunch.getName());
        missionName.setTextColor(getResources().getColor(R.color.text_color));
        TextView rocketName = findViewById(R.id.rocket_name);
        rocketName.setText(curLaunch.getMissionName());
        rocketName.setTextColor(getResources().getColor(R.color.text_color));
        TextView date = findViewById(R.id.date);
        date.setText(curLaunch.getDate().replaceAll("-", " - "));
        date.setTextColor(getResources().getColor(R.color.text_color));
        TextView description = findViewById(R.id.desc);
        description.setText(curLaunch.getDescription());
        description.setTextColor(getResources().getColor(R.color.text_color));
        TextView agency = findViewById(R.id.agency);
        agency.setText(getResources().getString(R.string.agency_prefix) + " " + curLaunch.getLsp());
        agency.setTextColor(getResources().getColor(R.color.text_color));
        TextView location = findViewById(R.id.location);
        location.setText(curLaunch.getLocation());
        location.setTextColor(getResources().getColor(R.color.text_color));
        getSupportActionBar().setTitle(curLaunch.getRocketName());
        ImageButton mapButton = findViewById(R.id.map);
        final String url = curLaunch.getMapURL();
        if (url == null || url.equals(""))
            mapButton.setVisibility(View.INVISIBLE);
        else {
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                }
            });
        }
    }

}
