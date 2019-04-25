package com.snh48.datavisualization.pojo;

public class UrlModel {
    private String id;
    private String name;
    private String uid;
    private String size;
    private String author;
    private String Xmname;
    private String status;//1表示重筹已结束，0表示未结束

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXmname() {
        return Xmname;
    }

    public void setXmname(String xmname) {
        Xmname = xmname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "UrlModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", size='" + size + '\'' +
                ", author='" + author + '\'' +
                ", Xmname='" + Xmname + '\'' +
                '}';
    }
}
