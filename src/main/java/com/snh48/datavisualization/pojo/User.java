package com.snh48.datavisualization.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Document(indexName = "userinfo", type = "user", shards = 1, replicas = 0)
public class User implements Serializable {
    @Id
    private Integer id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;
    @Field(type = FieldType.Float)
    private Float money;
    @Field(type = FieldType.Float)
    private Float totalMoney;
    @Field(type = FieldType.Long)
    private Long count;
    @Field(type = FieldType.Float)
    private Float ratio;
    @Field(index = false)
    private Integer pid;
    @Field(type = FieldType.Keyword)
    private String party;
    @Field(type = FieldType.Keyword)
    private String team;
    @Field(type = FieldType.Keyword)
    private String periods;

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

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Float getRatio() {
        return ratio;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Float totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money=" + money +
                ", totalMoney=" + totalMoney +
                ", count=" + count +
                ", ratio=" + ratio +
                ", pid=" + pid +
                ", party='" + party + '\'' +
                ", team='" + team + '\'' +
                ", periods='" + periods + '\'' +
                '}';
    }
}
