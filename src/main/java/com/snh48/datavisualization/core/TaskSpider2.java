package com.snh48.datavisualization.core;


import com.snh48.datavisualization.dao.UrlDao;
import com.snh48.datavisualization.pojo.Member;
import com.snh48.datavisualization.pojo.UrlModel;
import com.snh48.datavisualization.utils.PYUtils;
import com.snh48.datavisualization.utils.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskSpider2 {
    @Autowired
    private UrlDao urlDao;
    @Autowired
    private UrlSpider urlSpider;
    @Autowired
    private UrlSpiderPlus urlSpiderPlus;
    private final static long SECOND = 1 * 1000;

    //    @Scheduled(fixedRate = SECOND * 60 * 60 * 12)
    @Scheduled(cron = "0 0 12,0 * * ?")
    public void updateUrlModel() {
        List<Member> members = urlDao.findMember();
        members.forEach(m->{
            TableUtils.createUrlTable(PYUtils.getUrl(m.getName()));
            List<UrlModel> urlModels = urlSpiderPlus.getUrl(m.getName());
            urlModels.forEach(u->{
                if (u.getUid() != null) {
                    urlDao.insertUrlModel(PYUtils.getUrl(u.getName()), u);
                }
            });
        });
    }
}
