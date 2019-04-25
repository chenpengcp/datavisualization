package com.snh48.datavisualization.pojo;


import com.snh48.datavisualization.utils.PYUtils;

public class Member {
    private String id;
    private String name;
    private String party;
    private String team;
    private String periods;
    private String status;
    private String pic;

    public String getPic() {
        return PYUtils.getPinyin(name) + ".jpg";
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
