package com.snh48.datavisualization.pojo;

/**
 * @description:
 * @author: chenpeng
 */
public class Record {
    private Integer id;
    private String ip;
    private String accessTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }
}
