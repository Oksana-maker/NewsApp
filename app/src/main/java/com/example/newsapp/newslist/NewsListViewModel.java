package com.example.newsapp.newslist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.newsapp.NewsManager;
import com.example.newsapp.models.News;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NewsListViewModel extends ViewModel implements INewsManager, LoadNewsFinishedCallback {
    private final NewsManager newsManager;
    private final List<News> newsList;
    private List<News> watchedNewsList;
    private final MutableLiveData<News> selectedNews;
    private final MutableLiveData<Boolean> loadInProgress;
    private int currentNewNewsPosition = 0;
    private int currentWatchedNewsPosition = 0;
    private Boolean isCurrentNewNews = true;
    private NewsListAdapter newsListAdapter;

    public MutableLiveData<News> getSelectedNews() {
        return selectedNews;
    }

    public MutableLiveData<Boolean> getLoadInProgress() {
        return loadInProgress;
    }

    public List<News> getWatchedNewsList() {
        return watchedNewsList;
    }

    public void setWatchedNewsList(List<News> watchedNewsList) {
        watchedNewsList.sort((news1, news2) -> Integer.compare(news2.getWatchCount(), news1.getWatchCount()));
        this.watchedNewsList = watchedNewsList;
    }

    public NewsListViewModel() {
        newsManager = new NewsManager();
        newsList = new ArrayList<>();
        watchedNewsList = new ArrayList<>();
        selectedNews = new MutableLiveData<>();
        loadInProgress = new MutableLiveData<>(false);
    }

    @Override
    public void OnNewsSelected(News news) {
        for (int i = 0; i < watchedNewsList.size(); i++) {
            News watchedNews = watchedNewsList.get(i);
            if(news.getUrl().equals(watchedNews.getUrl())){
                watchedNews.IncrementWatches();
                selectedNews.postValue(news);
                return;
            }
        }
        news.IncrementWatches();
        watchedNewsList.add(news);
        selectedNews.postValue(news);
    }

    public void loadMoreNews() {
        if (!isCurrentNewNews) return;
        loadInProgress.setValue(true);
        newsManager.loadNews(false, this);
    }

    @Override
    public void onLoadNewsFinished(List<News> addingNewsList) {
        int oldSize = this.newsList.size();
        this.newsList.addAll(addingNewsList);
        if (newsListAdapter != null)
            newsListAdapter.notifyItemRangeInserted(oldSize, addingNewsList.size());
        loadInProgress.setValue(false);
    }

    public News getNewsItem(int index) {
        if (!isCurrentNewNews) {
            currentWatchedNewsPosition = index;
            return watchedNewsList.get(index);
        }
        if (index + 2 >= getItemCount()) loadMoreNews();
        currentNewNewsPosition = index;
        return newsList.get(index);
    }

    @Override
    public int getItemCount() {
        return isCurrentNewNews ? newsList.size() : watchedNewsList.size();
    }

    @Override
    public void setNewsListAdapter(NewsListAdapter adapter) {
        this.newsListAdapter = adapter;
        if (newsList.isEmpty()) {
            loadInProgress.setValue(true);
            newsManager.loadNews(true, this);
        }
    }

    public int getCurrentPosition() {
        return isCurrentNewNews ? currentNewNewsPosition : currentWatchedNewsPosition;
    }

    public int switchToNewNews() {
        isCurrentNewNews = true;
        int switchToPosition = getCurrentPosition();
        newsListAdapter.notifyDataSetChanged();
        return switchToPosition;
    }

    public int switchToShowedNews() {
        isCurrentNewNews = false;
        int switchToPosition = getCurrentPosition();
        newsListAdapter.notifyDataSetChanged();
        return switchToPosition;
    }
}
