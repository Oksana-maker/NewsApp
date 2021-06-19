package com.example.newsapp.newslist;

import com.example.newsapp.models.News;

import java.util.List;

public interface INewsManager {
    void OnNewsSelected(News news);
    News getNewsItem(int index);
    int getItemCount();
    void setNewsListAdapter(NewsListAdapter adapter);
}
