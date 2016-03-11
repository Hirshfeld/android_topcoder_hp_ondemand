package com.topcoder.hp.idol.ondemand.RestEntites;

import com.google.gson.annotations.Expose;

public class TextDocument {

    @Expose
    public String title = null;
    @Expose
    public String reference = null;
    @Expose
    public String content = null;

    @Expose
    public LanguageIdentification language_identification = null;

    @Expose
    public SentimentAnalysis sentiment_analysis = null;


    public TextDocument(String i_Title, String i_Reference, String i_Content, LanguageIdentification i_languageIdentification, SentimentAnalysis i_sentimentAnalysis) {
        title = i_Title;
        reference = i_Reference;
        content = i_Content;
        language_identification = i_languageIdentification;
        sentiment_analysis = i_sentimentAnalysis;
    }

    @Override
    public String toString() {
        return title + " " + reference + " " + content;
    }

}
