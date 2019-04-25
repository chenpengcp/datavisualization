package com.snh48.datavisualization.pojo;


import com.snh48.datavisualization.utils.PYUtils;

/**
 * @description:
 * @author: chenpeng
 */
public class MemberInfo {
    private Integer id;
    private User user;
    private String pic;
    private Integer dt;
    private Integer st;
    private Integer et;
    private Integer lh;
    private Integer xh;
    private Integer w_1;
    private Integer k_5;
    private Integer k_1;
    private Integer b_5;
    private Integer b_1;
    private Integer ticket;
    private Integer rt;

    public Integer getRt() {
        return rt;
    }

    public void setRt(Integer rt) {
        this.rt = rt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Integer getSt() {
        return st;
    }

    public void setSt(Integer st) {
        this.st = st;
    }

    public Integer getEt() {
        return et;
    }

    public void setEt(Integer et) {
        this.et = et;
    }

    public Integer getLh() {
        return lh;
    }

    public void setLh(Integer lh) {
        this.lh = lh;
    }

    public Integer getXh() {
        return xh;
    }

    public void setXh(Integer xh) {
        this.xh = xh;
    }

    public Integer getW_1() {
        return w_1;
    }

    public void setW_1(Integer w_1) {
        this.w_1 = w_1;
    }

    public Integer getK_5() {
        return k_5;
    }

    public void setK_5(Integer k_5) {
        this.k_5 = k_5;
    }

    public Integer getK_1() {
        return k_1;
    }

    public void setK_1(Integer k_1) {
        this.k_1 = k_1;
    }

    public Integer getB_5() {
        return b_5;
    }

    public void setB_5(Integer b_5) {
        this.b_5 = b_5;
    }

    public Integer getB_1() {
        return b_1;
    }

    public void setB_1(Integer b_1) {
        this.b_1 = b_1;
    }

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    public String getPic() {
        return PYUtils.getPinyin(user.getName()) + ".jpg";
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "MemberInfo{" +
                "id=" + id +
                ", user=" + user +
                ", dt=" + dt +
                ", st=" + st +
                ", et=" + et +
                ", lh=" + lh +
                ", xh=" + xh +
                ", w_1=" + w_1 +
                ", k_5=" + k_5 +
                ", k_1=" + k_1 +
                ", b_5=" + b_5 +
                ", b_1=" + b_1 +
                ", ticket=" + ticket +
                ", rt=" + rt +
                '}';
    }
}
