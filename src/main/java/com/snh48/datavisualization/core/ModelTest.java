package com.snh48.datavisualization.core;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class ModelTest {

    public static void main(String[] args) {
//        DataDao dataDao = new DataDao();
//        List<Model> result = dataDao.result(PYUtils.getTotal("刘增艳"));
//        MemberInfo memberInfo = new MemberInfo();
//        Integer dt = 0;
//        Integer st = 0;
//        Integer et = 0;
//        Integer lh = 0;
//        Integer xh = 0;
//        Integer w_1 = 0;
//        Integer k_5 = 0;
//        Integer k_1 = 0;
//        Integer b_5 = 0;
//        Integer b_1 = 0;
//        Integer ticket = 0;
//        Integer rt = 0;
//        DataService dataService = new DataService();
//        for (Model m : result
//                ) {
//            List<GrSearch> grSearches = dataService.findSearch(m.getName());
//            if (grSearches.size() == 1 && Float.parseFloat(m.getMoney()) < 10) {
//                xh += 1;
//            } else if (grSearches.size() == 1 && Float.parseFloat(m.getMoney()) >= 10) {
//                dt += 1;
//            } else if (grSearches.size() > 1 && grSearches.get(0).getMemberName().equals(u.getName())) {
//                st += 1;
//            } else if (grSearches.size() > 1 && grSearches.get(1).getMemberName().equals(u.getName())) {
//                et += 1;
//            } else {
//                lh += 1;
//            }
//        }
//        memberInfo.setW_1(w_1);
//        memberInfo.setK_5(k_5);
//        memberInfo.setK_1(k_1);
//        memberInfo.setB_5(b_5);
//        memberInfo.setB_1(b_1);
//        memberInfo.setTicket(ticket);
//        memberInfo.setRt(rt);
//        memberInfo.setDt(dt);
//        memberInfo.setSt(st);
//        memberInfo.setEt(et);
//        memberInfo.setLh(lh);
//        memberInfo.setXh(xh);
    }
}
