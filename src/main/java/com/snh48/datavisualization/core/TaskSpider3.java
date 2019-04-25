package com.snh48.datavisualization.core;

import com.snh48.datavisualization.dao.DataDao;
import com.snh48.datavisualization.dao.UrlDao;
import com.snh48.datavisualization.pojo.Member;
import com.snh48.datavisualization.pojo.UrlModel;
import com.snh48.datavisualization.pojo.User;
import com.snh48.datavisualization.utils.PYUtils;
import com.snh48.datavisualization.utils.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TaskSpider3 {
    private static final Logger LOG = LoggerFactory.getLogger(TaskSpider3.class);
    @Autowired
    private UrlDao urlDao;
//    @Autowired
//    private DataDao dataDao;
    private final static long SECOND = 1 * 1000;

    //    @Scheduled(fixedRate = SECOND * 60 * 60 * 24)
    @Scheduled(cron = "0 30 0 * * ?")
    public void update() {
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(5);
        List<Member> members = urlDao.findMember();
        members.forEach(m->{
            List<UrlModel> list = urlDao.findUrlModel(PYUtils.getUrl(m.getName()));
            //按发起者多的链接为准，避免出现多抓
//            String urlAuthor = urlDao.findUrlAuthor(PYUtils.getUrl(m.getName()));
//            User one = dataDao.findOne(PYUtils.getTotal(m.getName()), m.getName(), 1f, m);
//            Float money = one.getMoney();
            TableUtils.createTable(PYUtils.getTotal(m.getName()));
//            List<UrlModel> urlModels = duplicate(members, list);
            excute(list, cachedThreadPool);
        });
    }

    //如果链接发起者包含其他成员名字显示要去除提高链接准确率
    public List<UrlModel> duplicate(List<Member> members, List<UrlModel> urlModels) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getName().equals(urlModels.get(0).getName())) {
                members.remove(i);
            }
        }
        for (int i = 0; i < urlModels.size(); i++) {
            for (int j = 0; j < members.size(); j++) {
                if (urlModels.get(i).getAuthor().contains(members.get(j).getName())) {
                    urlModels.remove(i);
                }
            }
        }
        return urlModels;
    }

    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(5);
        TableUtils.createTable(PYUtils.getTotal("刘增艳"));
        cachedThreadPool.submit(new HttpSpiderPlus("44543", "1500", PYUtils.getTotal("刘增艳")));
        cachedThreadPool.submit(new HttpSpiderPlus("41955", "1500", PYUtils.getTotal("刘增艳")));
        cachedThreadPool.submit(new HttpSpiderPlus("47771", "1500", PYUtils.getTotal("刘增艳")));
        cachedThreadPool.submit(new HttpSpiderPlus("49477", "1500", PYUtils.getTotal("刘增艳")));
        cachedThreadPool.submit(new HttpSpiderPlus("50613", "1500", PYUtils.getTotal("刘增艳")));
        cachedThreadPool.submit(new HttpSpiderPlus("55049", "1500", PYUtils.getTotal("刘增艳")));
        cachedThreadPool.submit(new HttpSpiderPlus("56614", "1500", PYUtils.getTotal("刘增艳")));
    }

    public void updateByName(String name) {
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(1);
        List<UrlModel> list = urlDao.findUrlModel(PYUtils.getUrl(name));
        TableUtils.createTable(PYUtils.getTotal(name));
        excute(list, cachedThreadPool);
    }

    public void excute(List<UrlModel> list, ExecutorService cachedThreadPool) {
        if (list.size() > 0) {
            for (UrlModel u : list
                    ) {
                //个例不好避免，待优化
                if (!"总选前链接".equals(u.getXmname())) {
                    LOG.info(u.getName() + "======>" + u.getXmname());
                    cachedThreadPool.submit(new HttpSpiderPlus(u.getUid(), u.getSize(), PYUtils.getTotal(u.getName())));
                }
            }
        }
    }
}
