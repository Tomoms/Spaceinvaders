package com.example.android.spaceinvaders;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.spaceinvaders.Launch;

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

    private static List<Launch> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Launch> launches = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray launchArray = baseJsonResponse.getJSONArray("launches");

            // For each launch in the launchArray, create an {@link launch} object
            for (int i = 0; i < launchArray.length(); i++) {

                // Get a single Launch from the list
                JSONObject level1 = launchArray.getJSONObject(i);

                String name = level1.getString("name");
                String date = level1.getString("net");
                String videoURL = level1.getString("vidURL");
                if(videoURL == null)
                    videoURL = "";

                // go down
                JSONObject level2 = level1.getJSONObject("location");
                JSONArray level3 = level2.getJSONArray("pads");

                String place = level2.getString("name");
                String mapURL = "";//level3.getJSONObject(0).getString("mapURL");

                // go down
                level2 = level1.getJSONObject("rocket");
                String nameRocket = level2.getString("name");
                String imageURL = level2.getString("imageURL");


                // go down
                JSONArray level4 = level1.getJSONArray("missions");
                String nameMission = "";//level4.getJSONObject(0).getString("name");
                String descriptionMission = "";//level4.getJSONObject(0).getString("description");

                // go down
                level2 = level1.getJSONObject("lsp");
                String nameLSP = level2.getString("name");
                String countryCode = level2.getString("countryCode");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                Launch launch = new Launch(name, date, videoURL, place, mapURL, nameRocket, imageURL,nameMission, descriptionMission, nameLSP, countryCode);

                // Add the new {@link Earthquake} to the list of earthquakes.
                launches.add(launch);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return launches;
    }

}
