package com.snh48.datavisualization.core;


import com.snh48.datavisualization.pojo.User;
import com.snh48.datavisualization.service.DataService;
import com.snh48.datavisualization.utils.ScalaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description:
 * @author: chenpeng
 */
@Component
public class CheckTask {
    @Autowired
    private DataService dataService;
    @Autowired
    private TaskSpider3 taskSpider3;

    @Scheduled(cron = "0 50 1 * * ?")
    public void findAdd() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        Date endDate = new Date();
        c.setTime(endDate);
        c.add(Calendar.DATE, -1);
        Date startDate = c.getTime();
        String date = sdf.format(startDate);
        List<User> list = new ArrayList<>();
        Map<String, User> mapNow = dataService.getMap("a");
        Map<String, User> map = dataService.getMap("a" + date);
        mapNow.forEach((k, v) -> {
            if (map.containsKey(k)) {
                User now = mapNow.get(k);
                User yes = map.get(k);
                User user = new User();
                user.setName(now.getName());
                user.setMoney(ScalaUtils.scalaFloat(now.getMoney() - yes.getMoney()));
                if (user.getMoney() < 0) {
                    list.add(user);
                }
            }
        });
        list.forEach(user -> taskSpider3.updateByName(user.getName()));
    }
}
