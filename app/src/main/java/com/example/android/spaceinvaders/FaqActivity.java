package com.example.android.spaceinvaders;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.Serializable;

public class FaqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        WebView webpage = findViewById(R.id.webpage);
        WebSettings webSettings = webpage.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webpage.loadUrl("http://Tomoms.github.io");

        webpage.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        Serializable checkedIDS = getIntent().getSerializableExtra("checked");
        int checkedID = (Integer) checkedIDS;

        BottomNavigationView menu = findViewById(R.id.bottom_bar);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.faq:
                        Intent faqIntent = new Intent(getApplicationContext(), FaqActivity.class);
                        startActivity(faqIntent);
                        return true;
                    case R.id.query:
                        Intent videoIntent = new Intent(getApplicationContext(), QueryActivity.class);
                        startActivity(videoIntent);
                        return true;
                    case R.id.launches:
                        Intent launchIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(launchIntent);
                        finish();
                        return true;
                }
                return true;
            }
        });

        menu.setSelectedItemId(checkedID);
    }
}
