package com.snh48.datavisualization.core;


import com.alibaba.fastjson.JSON;
import com.snh48.datavisualization.dao.DataDao;
import com.snh48.datavisualization.dao.UrlDao;
import com.snh48.datavisualization.kafka.KafkaSender;
import com.snh48.datavisualization.pojo.Member;
import com.snh48.datavisualization.pojo.Model;
import com.snh48.datavisualization.utils.ModelSortUtils;
import com.snh48.datavisualization.utils.PYUtils;
import com.snh48.datavisualization.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.*;

@Component
public class TaskRank {
    @Autowired
    private KafkaSender kafkaSender;
    @Autowired
    private DataDao dataDao;
    @Autowired
    private UrlDao urlDao;
    private final static long SECOND = 1 * 1000;

    @Scheduled(fixedRate = SECOND * 60 * 90)
    public void update() {
        Jedis jedis = RedisUtils.getJedis();
        List<Model> list = new ArrayList<>();
        try {
            List<Member> members = urlDao.findMember();
            String[] set = new String[members.size()];
            for (int i = 0; i < members.size(); i++) {
                List<Model> result = dataDao.result(PYUtils.getTotal(members.get(i).getName()));
                for (int j = 0; j < result.size(); j++) {
                    jedis.zadd(members.get(i).getName() + "sort", Double.parseDouble(result.get(j).getMoney()), result.get(j).getName());
                }
                set[i] = members.get(i).getName() + "sort";
            }
            jedis.zunionstore("richRank", set);
            jedis.del(set);
            Set<String> richRank = jedis.zrevrange("richRank", 0, 19);
            richRank.forEach(name->{
                Model model = new Model();
                model.setName(name);
                model.setMoney(String.valueOf(jedis.zscore("richRank", name)));
                list.add(model);
            });
            Long totalCount = jedis.zcard("richRank");
            Collections.sort(list, new ModelSortUtils());
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setId(String.valueOf(i + 1));
            }
            Long wcount = 0L;
            Long kcount = 0L;
            Long kkcount = 0L;
            if (list.size() > 0) {
                wcount = jedis.zcount("richRank", 9999, Double.parseDouble(list.get(0).getMoney()));
                kcount = jedis.zcount("richRank", 4999, 9999);
                kkcount = jedis.zcount("richRank", 999, 4999);
            }
            jedis.set("wcount", String.valueOf(wcount));
            jedis.set("kcount", String.valueOf(kcount));
            jedis.set("kkcount", String.valueOf(kkcount));
            jedis.set("rank", JSON.toJSONString(list));
//            kafkaSender.send(JSON.toJSONString(list));
            jedis.expire("rank", 60 * 60 * 2);
            jedis.set("totalCount", String.valueOf(totalCount));
            jedis.expire("totalCount", 60 * 60 * 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
