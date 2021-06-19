package com.example.newsapp.newslist;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsapp.MainActivity;
import com.example.newsapp.R;
import com.example.newsapp.models.News;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;


public class NewsListFragment extends Fragment {
    NewsListViewModel viewModel;
    ContentLoadingProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(NewsListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_news_list, container, false);
        try {
            if(viewModel.getWatchedNewsList().isEmpty()) {
                FileInputStream fileInputStream = this.requireContext().openFileInput("watched.news");
                byte[] buffer = new byte[fileInputStream.available()];
                fileInputStream.read(buffer);
                ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
                ObjectInputStream ois = new ObjectInputStream(bis);
                Object newsList = ois.readObject();
                viewModel.setWatchedNewsList((List<News>) newsList);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        progressBar = root.findViewById(R.id.progress_bar);
        bottomNavigationView = root.findViewById(R.id.bottom_nav);
        RecyclerView recyclerView = root.findViewById(R.id.news_list_view);
        recyclerView.setAdapter(new NewsListAdapter(this.requireContext(), viewModel));
        layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null) layoutManager.scrollToPosition(viewModel.getCurrentPosition());
        viewModel.getSelectedNews().observe(getViewLifecycleOwner(), news -> {
            FragmentActivity activity = getActivity();
            if (activity instanceof MainActivity && news != null) {
                ((MainActivity) activity).ShowNewsDetails(news);
                viewModel.getSelectedNews().setValue(null);
            }
        });
        viewModel.getLoadInProgress().observe(getViewLifecycleOwner(), isLoadInProgress -> {
            if (isLoadInProgress) {
                progressBar.show();
            } else {
                progressBar.hide();
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_new) {
                layoutManager.scrollToPosition(viewModel.switchToNewNews());
                return true;
            } else if (itemId == R.id.action_showed) {
                layoutManager.scrollToPosition(viewModel.switchToShowedNews());
                return true;
            }
            return false;
        });
        return root;
    }

    @Override
    public void onStop() {
        List<News> watchedNewsList = viewModel.getWatchedNewsList();
        if(watchedNewsList.isEmpty()) {
            super.onStop();
            return;
        }
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(watchedNewsList);
            byte[] bytes = bos.toByteArray();
            FileOutputStream fileOutputStream = requireContext().openFileOutput("watched.news", Context.MODE_PRIVATE);
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }
}


