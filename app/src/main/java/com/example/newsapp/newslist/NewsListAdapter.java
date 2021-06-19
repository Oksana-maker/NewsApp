package com.example.newsapp.newslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.models.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder>  {

    private final LayoutInflater inflater;
    private final INewsManager newsManager;

    public NewsListAdapter(Context context, INewsManager newsManager) {
        inflater = LayoutInflater.from(context);
        this.newsManager = newsManager;
        newsManager.setNewsListAdapter(this);
    }

    @NonNull
    @Override
    public NewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_list_item, parent, false);
        return new NewsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsListAdapter.NewsListViewHolder holder, int position) {
        News news = newsManager.getNewsItem(position);
        holder.itemView.setOnClickListener(v -> newsManager.OnNewsSelected(news));
        if (!news.getImageURL().isEmpty())
            Picasso.get().load(news.getImageURL()).into(holder.imageView);
        holder.titleTextView.setText(news.getTitle());
        holder.subTitleTextView.setText(news.getDescription());
        holder.pubDateTextView.setText(news.getPubDateString());
    }

    @Override
    public int getItemCount() {
        return newsManager.getItemCount();
    }




    static class NewsListViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, subTitleTextView, pubDateTextView;
        ImageView imageView;

        public NewsListViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.card_title);
            subTitleTextView = itemView.findViewById(R.id.card_sub_title);
            imageView = itemView.findViewById(R.id.card_image);
            pubDateTextView = itemView.findViewById(R.id.card_pub_date);
        }
    }
}
