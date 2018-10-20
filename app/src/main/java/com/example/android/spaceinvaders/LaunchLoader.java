package com.example.android.spaceinvaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;
public class LaunchLoader extends AsyncTaskLoader<List<Launch>> {
    private String mUrl;
    public LaunchLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Launch> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Launch> launches = QueryUtils.fetchLaunchData(mUrl);
        return launches;
    }
}
