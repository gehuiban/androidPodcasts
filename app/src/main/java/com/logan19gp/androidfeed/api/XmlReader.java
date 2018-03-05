package com.logan19gp.androidfeed.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.logan19gp.androidfeed.BuildConfig;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by george on 3/4/2018.
 */

public class XmlReader {

    private static final String XML_UPDATE_TIME = "XML_UPDATE_TIME";
    private static final String PREFMAIN_FILE = "prefs_file";

    public ArrayList<Article> fetchXML(Context context, String urlString) {
        ArrayList<Article> rssObjects = new ArrayList<>();
        try {
            String fileName = "android_articles_feed.xml";
            SharedPreferences prefs = context.getSharedPreferences(PREFMAIN_FILE, Context.MODE_PRIVATE);
            Long lastXmlUpdate = prefs.getLong(XML_UPDATE_TIME + fileName, 0l);
            InputStream stream;
            File tempFile = new File(context.getCacheDir(), fileName);
            if (isNetworkAvailable(context) && (!tempFile.exists() || lastXmlUpdate < System.currentTimeMillis() - 600000 /* 10 minutes */)) {
                URL url = new URL(urlString);
                logMsg("urlString:" + urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                // Starts the query
                conn.connect();
                stream = conn.getInputStream();
                copyInputStreamToFile(stream, tempFile);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(XML_UPDATE_TIME + fileName, System.currentTimeMillis());
                editor.apply();
            }
            FileInputStream fileIn = new FileInputStream(tempFile);
            BufferedInputStream bis = new BufferedInputStream(fileIn);
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(bis, null);

            rssObjects.addAll(parseXMLAndStoreIt(myparser));
            fileIn.close();
            bis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return rssObjects;
        }
    }

    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file, false);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context is the app context
     * @return if network connection is available or not.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Log only if is a debug build
     *
     * @param textToLog
     */
    private static void logMsg(String textToLog) {
        if (BuildConfig.DEBUG) {
            Log.d(XmlReader.class.toString(), textToLog);
        }
    }

    private ArrayList<Article> parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        ArrayList<Article> articles = new ArrayList<>();
        try {
            event = myParser.getEventType();
            String fieldName = "";
            String text = "";
            Article articleParsed = new Article();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        fieldName = myParser.getName();
                        if (fieldName.startsWith("enclosure")) {
                            articleParsed.setAudioLink(text);
                            String media = myParser.getAttributeValue(null, "url");
                            articleParsed.setAudioLink(media);
                        } else if (fieldName.startsWith("media:content")) {
                            if (articleParsed.getAudioLink() == null) {
                                String media = myParser.getAttributeValue(null, "url");
                                articleParsed.setAudioLink(media);
                            }
                        } else if (fieldName.startsWith("media:content")) {
                            String media = myParser.getAttributeValue(null, "url");
                            articleParsed.setTitle(media);
                        } else if (fieldName.startsWith("media:thumbnail")) {
                            String media = myParser.getAttributeValue(null, "url");
                            articleParsed.setAudioImage(media);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        if (fieldName.equalsIgnoreCase("title")) {
                            articleParsed.setTitle(text);
                        } else if (fieldName.equalsIgnoreCase("description")) {
                            articleParsed.setDescription(text);
                        } else if (fieldName.equalsIgnoreCase("author")) {
                            articleParsed.setAuthor(text);
                        } else if (fieldName.equalsIgnoreCase("link")) {
                            articleParsed.setLink(text);
                        } else if (fieldName.startsWith("itunes:subtitle")) {
                            articleParsed.setSubTitle(text);
                        } else if (fieldName.startsWith("pubDate")) {
                            articleParsed.setPubDate(text);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        fieldName = myParser.getName();
                        break;
                }
                if (event == XmlPullParser.END_TAG && fieldName.equals("item")) {
                    articles.add(new Article(articleParsed));
                    articleParsed = new Article();
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return articles;
        }
    }

}
