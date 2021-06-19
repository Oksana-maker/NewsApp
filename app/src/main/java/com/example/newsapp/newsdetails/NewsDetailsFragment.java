package com.example.newsapp.newsdetails;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapp.R;
import com.example.newsapp.models.News;
import com.squareup.picasso.Picasso;

public class NewsDetailsFragment extends Fragment {

    private News newNews;
    private TextView title, subTitle, content, authors, pubDate;
    private ImageView image;
    private WebView webView;
    private ContentLoadingProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news_details, container, false);
        progressBar = root.findViewById(R.id.progress_bar);
        NewsDetailsViewModel viewModel = new ViewModelProvider(this).get(NewsDetailsViewModel.class);
        webView = root.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.hide();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });
        /*image = root.findViewById(R.id.detail_image);
        title = root.findViewById(R.id.detail_title_text);
        subTitle = root.findViewById(R.id.detail_sub_title_text);
        content = root.findViewById(R.id.detail_content_text);
        authors = root.findViewById(R.id.detail_authors_text);
        pubDate = root.findViewById(R.id.detail_pub_date_text);*/
        viewModel.getCurrentNews().setValue(newNews);
        viewModel.getCurrentNews().observe(getViewLifecycleOwner(), news -> {
            /*if(!news.getImageURL().isEmpty()) Picasso.get().load(news.getImageURL()).into(image);
            title.setText(news.getTitle());
            subTitle.setText(news.getDescription());
            content.setText(news.getContent());
            pubDate.setText(news.getPubDateString());
            authors.setText(news.getAuthorsString());*/
            progressBar.show();
            webView.loadUrl(news.getUrl());
        });
        return root;
    }

    public void ShowNews(News news){
        this.newNews = news;
    }
}