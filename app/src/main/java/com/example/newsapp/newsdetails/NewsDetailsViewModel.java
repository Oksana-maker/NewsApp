package com.example.newsapp.newsdetails;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.newsapp.models.News;

public class NewsDetailsViewModel extends ViewModel {
    private final MutableLiveData<News> currentNews;
    public NewsDetailsViewModel(){
        currentNews = new MutableLiveData<>();
    }

    public MutableLiveData<News> getCurrentNews() {
        return currentNews;
    }
}
