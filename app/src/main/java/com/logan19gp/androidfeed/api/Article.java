package com.logan19gp.androidfeed.api;

/**
 * Created by george on 3/4/2018.
 */

public class Article {
    private String title;
    private String link;
    private String author;
    private String pubDate;
    private String description;
    private String summary;
    private String subTitle;
    private String audioLink;
    private String audioImage;

    public Article() {

    }

    public Article(Article articleParsed) {
        setAudioImage(articleParsed.getAudioImage());
        setAudioLink(articleParsed.getAudioLink());
        setAuthor(articleParsed.getAuthor());
        setDescription(articleParsed.getDescription());
        setLink(articleParsed.getLink());
        setPubDate(articleParsed.getPubDate());
        setSubTitle(articleParsed.getSubTitle());
        setSummary(articleParsed.getSummary());
        setTitle(articleParsed.getTitle());
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }

    public String getAudioImage() {
        return audioImage;
    }

    public void setAudioImage(String audioImage) {
        this.audioImage = audioImage;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
