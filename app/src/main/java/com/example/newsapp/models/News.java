package com.example.newsapp.models;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class News implements Serializable {
    private final String url;
    private final String source;
    private final List<String> authors;
    private final String title;
    private final LocalDateTime pubDate;
    private final String description;
    private final String imageURL;
    private final String content;
    private int watchCount = 0;

    public News(String url, String source, String title, LocalDateTime pubDate, String description, String imageURL, String content, List<String> authors) {
        this.url = url;
        this.source = source;
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.imageURL = imageURL;
        this.content = content;
        this.authors = authors;
    }

    public int getWatchCount() {
        return watchCount;
    }

    public String getUrl() {
        return url;
    }

    public String getSource() {
        return source;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getPubDate() {
        return pubDate;
    }
    public String getPubDateString() {
        return pubDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorsString(){
        return String.join("\n", authors);
    }

    public void IncrementWatches(){
        ++watchCount;
    }

}
