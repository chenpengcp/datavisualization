package com.snh48.datavisualization.core;

import com.alibaba.fastjson.JSON;
import com.snh48.datavisualization.dao.DataDao;
import com.snh48.datavisualization.pojo.GrSearch;
import com.snh48.datavisualization.pojo.MemberInfo;
import com.snh48.datavisualization.pojo.Model;
import com.snh48.datavisualization.pojo.User;
import com.snh48.datavisualization.service.DataService;
import com.snh48.datavisualization.utils.PYUtils;
import com.snh48.datavisualization.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ModelRank {
    private static final Logger LOG = LoggerFactory.getLogger(ModelRank.class);
    @Autowired
    private DataService dataService;
    @Autowired
    private DataDao dataDao;
    private final static long SECOND = 1 * 1000;
//    @Autowired
//    private KafkaSender kafkaSender;

    //    @Scheduled(fixedRate = SECOND * 60 * 10)
    @Scheduled(cron = "0 0 2 * * ?")
    public void update() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String date = df.format(new Date());
        Jedis jedis = RedisUtils.getJedis();
        List<User> list = dataService.findTotal();
        //使用队列
        Queue<User> queue = new LinkedList<>();
        queue.addAll(list);
        while (queue.size() > 0) {
            User u = queue.poll();
            if (u != null) {
                MemberInfo memberInfo = resultCount(u);
                jedis.set(u.getName() + "count", JSON.toJSONString(memberInfo));
                jedis.expire(u.getName() + "count", 60 * 60 * 25);
            }
        }
        if (list.size() > 0) {
            list.forEach(user -> {
                MemberInfo memberInfo = resultCount(user);
                jedis.set(user.getName() + "count", JSON.toJSONString(memberInfo));
                jedis.expire(user.getName() + "count", 60 * 60 * 25);
            });
        }
        LOG.info("ModelRank" + date + "更新成功!");
    }

    //记录单个成员的集资分析
    public MemberInfo resultCount(User u) {
        MemberInfo memberInfo = new MemberInfo();
        try {
            memberInfo.setUser(u);
            memberInfo.setId(u.getId());
            List<Model> result = dataDao.result(PYUtils.getTotal(u.getName()));
            Integer dt = 0;
            Integer st = 0;
            Integer et = 0;
            Integer lh = 0;
            Integer xh = 0;
            Integer w_1 = 0;
            Integer k_5 = 0;
            Integer k_1 = 0;
            Integer b_5 = 0;
            Integer b_1 = 0;
            Integer ticket = 0;
            Integer rt = 0;
            for (Model m : result
                    ) {
                if (Float.parseFloat(m.getMoney()) >= 10000) {
                    w_1 += 1;
                } else if (Float.parseFloat(m.getMoney()) >= 5000 && Float.parseFloat(m.getMoney()) < 10000) {
                    k_5 += 1;
                } else if (Float.parseFloat(m.getMoney()) >= 1000 && Float.parseFloat(m.getMoney()) < 5000) {
                    k_1 += 1;
                } else if (Float.parseFloat(m.getMoney()) >= 500 && Float.parseFloat(m.getMoney()) < 1000) {
                    b_5 += 1;
                } else if (Float.parseFloat(m.getMoney()) >= 100 && Float.parseFloat(m.getMoney()) < 500) {
                    b_1 += 1;
                } else if (Float.parseFloat(m.getMoney()) >= 35 && Float.parseFloat(m.getMoney()) < 100) {
                    ticket += 1;
                } else {
                    rt += 1;
                }
                List<GrSearch> grSearches = dataService.findSearch(m.getName());
                if (grSearches.size() == 1 && Float.parseFloat(m.getMoney()) < 10) {
                    xh += 1;
                } else if (grSearches.size() == 1 && Float.parseFloat(m.getMoney()) >= 10) {
                    dt += 1;
                } else if (grSearches.size() > 1 && grSearches.get(0).getMemberName().equals(u.getName())) {
                    st += 1;
                } else if (grSearches.size() > 1 && grSearches.get(1).getMemberName().equals(u.getName())) {
                    et += 1;
                } else {
                    lh += 1;
                }
            }
            memberInfo.setW_1(w_1);
            memberInfo.setK_5(k_5);
            memberInfo.setK_1(k_1);
            memberInfo.setB_5(b_5);
            memberInfo.setB_1(b_1);
            memberInfo.setTicket(ticket);
            memberInfo.setRt(rt);
            memberInfo.setDt(dt);
            memberInfo.setSt(st);
            memberInfo.setEt(et);
            memberInfo.setLh(lh);
            memberInfo.setXh(xh);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return memberInfo;
    }
}
