package com.example.newsapp.newslist;

import com.example.newsapp.models.News;

import java.util.List;

public interface LoadNewsFinishedCallback {
     void onLoadNewsFinished(List<News> newsList);
}
