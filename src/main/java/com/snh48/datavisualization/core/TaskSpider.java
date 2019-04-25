package com.snh48.datavisualization.core;

import com.snh48.datavisualization.dao.DataDao;
import com.snh48.datavisualization.pojo.Pkmodel;
import com.snh48.datavisualization.pojo.UserInfo;
import com.snh48.datavisualization.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

@Component
public class TaskSpider {
    @Autowired
    private DataDao dataDao;
    private final static long SECOND = 1 * 1000;

    @Scheduled(fixedRate = SECOND * 60 * 5)
    public void update() {
        Jedis jedis = RedisUtils.getJedis();
        List<Pkmodel> grPkName = dataDao.findGrPkName();
        grPkName.forEach(p -> {
            if ("1".equals(p.getStatus())) {
                List<UserInfo> UserInfos = dataDao.findUserInfo(p.getId());
                for (UserInfo u : UserInfos
                        ) {
                    try {
                        Map<String, String> map = PKSpider.getData(u.getUrl(), "500");
                        if (jedis.exists(p.getName() + u.getName())) {
                            jedis.del(p.getName() + u.getName());
                        }
                        jedis.hset(p.getName() + u.getName(), map);
                        jedis.expire(p.getName() + u.getName(), 60 * 60 * 24 * 10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//        List<UserInfo> UserInfos = dataDao.findUserInfo();
//        for (UserInfo u : UserInfos
//                ) {
//            TableUtils.createTable(PYUtils.getPinyin(u.getName()));
//            Executors.newCachedThreadPool().submit(new HttpSpider(u.getUrl(), "500", PYUtils.getPinyin(u.getName())));
//        }
    }
}
