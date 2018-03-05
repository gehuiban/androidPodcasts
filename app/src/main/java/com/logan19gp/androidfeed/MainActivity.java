package com.logan19gp.androidfeed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.logan19gp.androidfeed.api.Article;
import com.logan19gp.androidfeed.api.XmlReader;
import com.logan19gp.androidfeed.ui.ArticlesAdapter;

import java.util.ArrayList;

/**
 * Created by george on 3/4/2018.
 */

public class MainActivity extends AppCompatActivity {

    public static String URL = "http://feeds.feedburner.com/blogspot/AndroidDevelopersBackstage?format=xml";
    private static AsyncTask<String, Integer, ArrayList<Article>> task;
    private ArrayList<Article> articlesServer;
    private ArticlesAdapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView articlesReclView = findViewById(R.id.articles_view);

        articlesReclView.setLayoutManager(new LinearLayoutManager(this));
        articleAdapter = new ArticlesAdapter(this);
        articlesReclView.setAdapter(articleAdapter);
        getArticles();
        if (!XmlReader.isNetworkAvailable(this)) {
            Toast.makeText(this, "Network not available.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh_id) {
            getArticles();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getArticles() {
        FeedUpdater updater = new FeedUpdater(this, new FeedUpdater.OnFindDriveFolder() {
            @Override
            public void onFeedUpdated(ArrayList<Article> articles) {
                articlesServer = articles;
                articleAdapter.clearData();
                articleAdapter.addArticles(articlesServer);
            }
        });
        updater.execute(URL);
    }
}
