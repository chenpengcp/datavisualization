package com.snh48.datavisualization.pojo;

public class ModelPlus {
    private String id;
    private String name;
    private String money;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getMoney() {
        if (money.contains(".")&&(money.indexOf(".")+3)<money.length()) {
            return money.substring(0,money.indexOf(".")+3);
        } else {
            return money;
        }
    }

    public void setMoney(String money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "ModelPlus{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", money='" + money + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
