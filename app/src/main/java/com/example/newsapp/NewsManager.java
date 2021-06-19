package com.example.newsapp;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.newsapp.models.News;
import com.example.newsapp.newslist.LoadNewsFinishedCallback;

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
import java.security.Security;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NewsManager {
    private static final String MAIN_URL = "http://api.datanews.io/v1/news?country=ua";
    private static final String API_KEY = "0jfueu2e086oe1oihori3ufx0";
    private static int page = 0;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public void loadNews(Boolean firstLoad, LoadNewsFinishedCallback callback) {
        executor.execute(() -> {
            List<News> newsList = new ArrayList<>();
            if (firstLoad) page = 0;
            String urlString = MAIN_URL + "&page=" + page + "&size=10&sortBy=date";
            URL url;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                handler.post(() -> callback.onLoadNewsFinished(newsList));
                return;
            }
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if(responseCode != 200) return;
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String currentLine;
                while ((currentLine = reader.readLine()) != null){
                    response.append(currentLine);
                }
                inputStream.close();
                reader.close();
                connection.disconnect();
                String body = response.toString();
                JSONObject jsonRoot = new JSONObject(body);
                JSONArray array = jsonRoot.getJSONArray("hits");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String urlNews = object.getString("url");
                    String source = object.getString("source");
                    String title = object.getString("title");
                    String pubDateString = object.getString("pubDate");
                    LocalDateTime pubDate = LocalDateTime.parse(pubDateString, DateTimeFormatter.ISO_DATE_TIME);
                    String description = object.getString("description");
                    String imageURL = object.getString("imageUrl");
                    String content = object.getString("content");
                    JSONArray authors = object.getJSONArray("authors");
                    List<String> authorList = new ArrayList<>();
                    for (int j = 0; j < authors.length(); j++) {
                        authorList.add(authors.getString(j));
                    }
                    newsList.add(new News(
                            urlNews,
                            source,
                            title,
                            pubDate,
                            description,
                            imageURL,
                            content,
                            authorList
                    ));
                }
                page++;
                handler.post(() -> callback.onLoadNewsFinished(newsList));
                Log.d("MYTAG", "loaded page " + page);

            } catch (IOException | JSONException e) {
                Log.e("MYTAG", e.toString());
            }
        });

    }
}
