package com.example.android.spaceinvaders;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Launch>> {

    public static String URL = "https://launchlibrary.net/1.4/launch/next/10";
    private static final int LAUNCH_LOADER_ID = 1;
    private LaunchAdapter mAdapter;
    private TextView mEmptyStateTextView;
    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid = findViewById(R.id.grid);
        mEmptyStateTextView = findViewById(R.id.empty_view);
        grid.setEmptyView(mEmptyStateTextView);

        Serializable data = getIntent().getSerializableExtra("url");
        if (data != null)
            URL = (String) data;

        mAdapter = new LaunchAdapter(this, new ArrayList<Launch>());
        grid.setAdapter(mAdapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Launch curLaunch = (Launch) grid.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("launch", (Serializable) curLaunch);
                startActivity(intent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LAUNCH_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_conn);
        }

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
    }

        @Override
        public Loader<List<Launch>> onCreateLoader(int i, Bundle bundle) {
            Uri baseUri = Uri.parse(URL);

            return new LaunchLoader(this, baseUri.toString());
        }

        @Override
        public void onLoadFinished(Loader<List<Launch>> loader, List<Launch> launches) {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_launches);

            if (launches != null && !launches.isEmpty()) {
                mAdapter.clear();
                for (int i = 0; i < launches.size(); i++)
                    mAdapter.add(launches.get(i));
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Launch>> loader) {
            mAdapter.clear();
        }
    }

