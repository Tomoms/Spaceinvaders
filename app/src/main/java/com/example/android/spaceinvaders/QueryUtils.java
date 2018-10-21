package com.example.android.spaceinvaders;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Launch> fetchLaunchData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Launch> launches = extractFeatureFromJson(jsonResponse);

        return launches;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the launches JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Launch> extractFeatureFromJson(String launchJSON) {
        if (TextUtils.isEmpty(launchJSON)) {
            return null;
        }

        List<Launch> launches = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(launchJSON);

            JSONArray launchArray = baseJsonResponse.getJSONArray("launches");

            for (int i = 0; i < launchArray.length(); i++) {
                try {
                    JSONObject level1 = launchArray.getJSONObject(i);

                    String name = level1.getString("name");
                    String date = level1.getString("net");
                    String videoURL = level1.getString("vidURL");
                    if (videoURL == null)
                        videoURL = "";

                    // go down
                    JSONObject level2 = level1.getJSONObject("location");
                    JSONArray level3 = level2.getJSONArray("pads");

                    String place = level2.getString("name");
                    String mapURL = level3.getJSONObject(0).getString("mapURL");

                    // go down
                    level2 = level1.getJSONObject("rocket");
                    String nameRocket = level2.getString("name");
                    String imageURL = level2.getString("imageURL");


                    // go down
                    JSONArray level4 = level1.getJSONArray("missions");
                    String nameMission = level4.getJSONObject(0).getString("name");
                    String descriptionMission = level4.getJSONObject(0).getString("description");

                    // go down
                    level2 = level1.getJSONObject("lsp");
                    String nameLSP = level2.getString("name");
                    String countryCode = level2.getString("countryCode");

                    Launch launch = new Launch(name, date, videoURL, place, mapURL, nameRocket, imageURL, nameMission, descriptionMission, nameLSP, countryCode);

                    launches.add(launch);
                } catch (JSONException e) {
                    continue;
                }
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the launches JSON results", e);
        }

        return launches;
    }

}
