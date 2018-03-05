package com.logan19gp.androidfeed;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.logan19gp.androidfeed.api.Article;
import com.logan19gp.androidfeed.api.XmlReader;

import java.util.ArrayList;

/**
 * Created by george on 3/4/2018.
 */

public class FeedUpdater extends AsyncTask<String, Integer, ArrayList<Article>> {

    private static String URL = "http://feeds.feedburner.com/blogspot/AndroidDevelopersBackstage?format=xml";
    private Activity activity;
    private OnFindDriveFolder onFeedUpdated;


    /**
     *  Interface to update the UI when the feed is updated
     */
    public interface OnFindDriveFolder
    {
        /**
         *
         * @param articles is the list of articles received
         */
        void onFeedUpdated(ArrayList<Article> articles);
    }


    public FeedUpdater(Activity activity, OnFindDriveFolder onFeedUpdated) {
        this.activity = activity;
        this.onFeedUpdated = onFeedUpdated;
    }

    @Override
    protected ArrayList<Article> doInBackground(String... params) {
        Log.d("List", "params size:" + (params == null ? 0 : params.length));
        String url = URL;
        if (params != null && params.length > 0) {
            url = params[0];
        }
        final XmlReader obj = new XmlReader();
        ArrayList<Article> listArticles = obj.fetchXML(activity, url);
        return listArticles;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {
        Log.d("List OnPostExecute", "list size post:" + (articles == null ? 0 : articles.size()));
        if (onFeedUpdated != null) {
            onFeedUpdated.onFeedUpdated(articles);
        }
    }
}
