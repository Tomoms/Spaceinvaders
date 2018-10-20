package com.example.android.spaceinvaders;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Launch>> {

    private static final String URL = "https://launchlibrary.net/1.4/launch/next/10";
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

        mAdapter = new LaunchAdapter(this, new ArrayList<Launch>());
        grid.setAdapter(mAdapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
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

            //mEmptyStateTextView.setText(R.string.no_launches);

            if (launches != null && !launches.isEmpty()) {
                for (int i = 0; i < launches.size(); i++)
                    mAdapter.add(launches.get(i));
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Launch>> loader) {
            //mAdapter.clear();
        }
}
