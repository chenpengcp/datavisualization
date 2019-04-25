package com.snh48.datavisualization.core;

import com.snh48.datavisualization.dao.DataDao;
import com.snh48.datavisualization.dao.UrlDao;
import com.snh48.datavisualization.kafka.KafkaSender;
import com.snh48.datavisualization.pojo.*;
import com.snh48.datavisualization.service.DataService;
import com.snh48.datavisualization.utils.*;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class TotalRank {
    private static final Logger LOG = LoggerFactory.getLogger(TotalRank.class);
    @Autowired
    private DataDao dataDao;
    @Autowired
    private UrlDao urlDao;
    private final static long SECOND = 1 * 1000;
//    @Autowired
//    private KafkaSender kafkaSender;

    //    @Scheduled(fixedRate = SECOND * 60 * 10)
    @Scheduled(cron = "0 30 1,2 * * ?")
    public void update() {
        Jedis jedis = RedisUtils.getJedis();
        List<User> list = dataDao.findTotal();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String date = df.format(new Date());
        //urlDao.dropRank("total");
        Float allMoney = 0f;
        if (list.size() > 0) {
            for (User u : list
                    ) {
                allMoney += u.getMoney();
                jedis.set(u.getId() + "a", JSON.toJSONString(u));
                jedis.set(u.getId() + "a" + date, JSON.toJSONString(u));
                jedis.expire(u.getId() + "a", 60 * 60 * 25);
                jedis.expire(u.getId() + "a" + date, 60 * 60 * 48);
//                String s = kafkaSender.send(JSON.toJSONString(u));
//                System.out.println(s);
                Node node = new Node();
                node.setName(u.getName());
                node.setMoney(u.getMoney());
                node.setCount(u.getCount());
                node.setCreateDate(date);
                urlDao.insertNode(node);
            }
            jedis.set("allMoney", String.valueOf(allMoney));
            jedis.expire("allMoney", 60 * 60 * 25);
            List<Analysis> team = getTeam(list, allMoney);
            jedis.set("team", JSON.toJSONString(team));
            jedis.expire("team", 60 * 60 * 25);
            List<Analysis> party = getParty(list, allMoney);
            jedis.set("party", JSON.toJSONString(party));
            jedis.expire("party", 60 * 60 * 25);
            List<Analysis> periods = getPeriods(list, allMoney);
            jedis.set("periods", JSON.toJSONString(periods));
            jedis.expire("periods", 60 * 60 * 25);
            LOG.info("TotalRank" + date + "更新成功!");
        }
    }

    public List<Analysis> getTeam(List<User> list, Float allMoney) {
        Float a = 0f;
        Float b = 0f;
        Float c = 0f;
        Float d = 0f;
        Float e = 0f;
        Float f = 0f;
        Float g = 0f;
        Float h = 0f;
        Float i = 0f;
        Float j = 0f;
        Map<String, Float> map = new HashMap<>();
        for (User u : list
                ) {
            switch (u.getTeam()) {
                case "Team SII":
                    a += u.getMoney();
                    break;
                case "Team NII":
                    b += u.getMoney();
                    break;
                case "Team HII":
                    c += u.getMoney();
                    break;
                case "Team X":
                    d += u.getMoney();
                    break;
                case "Team B":
                    f += u.getMoney();
                    break;
                case "Team E":
                    g += u.getMoney();
                    break;
                case "Team J":
                    h += u.getMoney();
                    break;
                case "Team G":
                    i += u.getMoney();
                    break;
                case "Team NIII":
                    j += u.getMoney();
                    break;
                case "Team Z":
                    e += u.getMoney();
                    break;
            }
        }
        map.put("Team SII", a);
        map.put("Team NII", b);
        map.put("Team HII", c);
        map.put("Team X", d);
        map.put("Team B", f);
        map.put("Team E", g);
        map.put("Team J", h);
        map.put("Team G", i);
        map.put("Team NIII", j);
        map.put("Team Z", e);
        return getList(map, allMoney);
    }

    public List<Analysis> getPeriods(List<User> list, Float allMoney) {
        Float a = 0f;
        Float b = 0f;
        Float c = 0f;
        Float d = 0f;
        Float e = 0f;
        Float f = 0f;
        Float g = 0f;
        Map<String, Float> map = new HashMap<>();
        for (User u : list
                ) {
            switch (u.getPeriods()) {
                case "SNH48 一期生":
                    a += u.getMoney();
                    break;
                case "SNH48 二期生":
                    b += u.getMoney();
                    break;
                case "SNH48 三期生":
                    c += u.getMoney();
                    break;
                case "SNH48 四期生":
                    d += u.getMoney();
                    break;
                case "SNH48 五期生":
                    e += u.getMoney();
                    break;
                case "SNH48 六期生":
                    f += u.getMoney();
                    break;
                default:
                    g += u.getMoney();
            }
        }
        map.put("一期生", a);
        map.put("二期生", b);
        map.put("三期生", c);
        map.put("四期生", d);
        map.put("五期生", e);
        map.put("六期生", f);
        map.put("其他期", g);
        return getList(map, allMoney);
    }

    public List<Analysis> getParty(List<User> list, Float allMoney) {
        Float a = 0f;
        Float b = 0f;
        Float c = 0f;
//        Float d = 0f;
//        Float e = 0f;
        Map<String, Float> map = new HashMap<>();
        for (User u : list
                ) {
            switch (u.getParty()) {
                case "SNH48":
                    a += u.getMoney();
                    break;
                case "BEJ48":
                    b += u.getMoney();
                    break;
                case "GNZ48":
                    c += u.getMoney();
                    break;
//                case "SHY48":
//                    d += u.getMoney();
//                    break;
//                case "CKG48":
//                    e += u.getMoney();
//                    break;
            }
        }
        map.put("SNH48", a);
        map.put("BEJ48", b);
        map.put("GNZ48", c);
//        map.put("SHY48", d);
//        map.put("CKG48", e);
        return getList(map, allMoney);
    }

    public List<Analysis> getList(Map<String, Float> map, Float allMoney) {
        List<Analysis> analyses = new ArrayList<>();
        for (String key : map.keySet()
                ) {
            Analysis an = new Analysis();
            an.setName(key);
            an.setMoney(map.get(key));
            an.setAllMoney(allMoney);
            an.setScale(ScalaUtils.scala(an.getMoney() * 100 / an.getAllMoney()) + "%");
            analyses.add(an);
        }
        Collections.sort(analyses, new AnlysisUtils());
        for (int index = 0; index < analyses.size(); index++) {
            analyses.get(index).setId(index + 1);
        }
        return analyses;
    }
}
