package com.example.newsapp;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.example.newsapp.models.News;
import com.example.newsapp.newsdetails.NewsDetailsFragment;
import com.example.newsapp.newslist.NewsListFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new NewsListFragment()).commitAllowingStateLoss();
    }

    public void ShowNewsDetails(News news){
        NewsDetailsFragment newsDetailsFragment = new NewsDetailsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, newsDetailsFragment).addToBackStack(null).commit();
        newsDetailsFragment.ShowNews(news);
    }

}