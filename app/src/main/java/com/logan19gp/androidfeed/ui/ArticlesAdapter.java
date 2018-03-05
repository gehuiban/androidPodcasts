package com.logan19gp.androidfeed.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.logan19gp.androidfeed.R;
import com.logan19gp.androidfeed.api.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 3/4/2018.
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {
    private List<Article> articleItems = new ArrayList<>();
    private Activity mActivity;

    public void addArticles(List<Article> newDays) {
        articleItems.addAll(newDays);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return articleItems.size();
    }

    public ArticlesAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Article article = articleItems.get(position);
        if (article == null) {
            return;
        }
        if (article.getTitle() != null) {
            holder.titleText.setText(article.getTitle());
        }
        if (article.getPubDate() != null) {
            holder.publishTimeText.setText(article.getPubDate());
        }
        if (article.getSubTitle() != null) {
            String subTitle = article.getSubTitle();
            holder.subTitleText.setText(subTitle);
        }
        Picasso.with(mActivity).load(article.getAudioImage()).placeholder(R.mipmap.ic_launcher).into(holder.photo);
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(article.getAudioLink()), "audio/*");
                mActivity.startActivity(intent);
            }
        });
    }

    public void clearData() {
        articleItems.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView subTitleText;
        TextView publishTimeText;
        ImageView photo;
        ImageView play;

        private ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_card, parent, false));
            titleText = (TextView) itemView.findViewById(R.id.title_text);
            subTitleText = (TextView) itemView.findViewById(R.id.subtitle_text);
            publishTimeText = (TextView) itemView.findViewById(R.id.publish_date_text);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            play = (ImageView) itemView.findViewById(R.id.play_audio);
        }
    }
}