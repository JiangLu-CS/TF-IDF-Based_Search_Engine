package com.cose.ir.exp.bean;

public class Article {
    public Integer docId; //文章编号
    public String title; //文章标题
    public String time; //发布时间
    public String abstra; //文章摘要
    public String url; //链接地址
    public Double sim; //相关度

    public Article(Integer docId, String title, String time, String abstra, String url) {
        this.docId = docId;
        this.title = title;
        this.time = time;
        this.abstra = abstra;
        this.url = url;
    }

    public Article(Integer integer) {
        this.docId = docId;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAbstra() {
        return abstra;
    }

    public void setAbstra(String abstra) {
        this.abstra = abstra;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getSim() {
        return sim;
    }

    public void setSim(Double sim) {
        this.sim = sim;
    }
}
