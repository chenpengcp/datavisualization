package com.snh48.datavisualization.service;

import com.snh48.datavisualization.dao.ManageDao;
import com.snh48.datavisualization.dao.UrlDao;
import com.snh48.datavisualization.pojo.Item;
import com.snh48.datavisualization.pojo.Member;
import com.snh48.datavisualization.pojo.UrlModel;
import com.snh48.datavisualization.utils.PYUtils;
import com.snh48.datavisualization.utils.SpiderUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author: chenpeng
 */
@Service
@Transactional
public class ManageService {
    @Autowired
    private ManageDao manageDao;
    @Autowired
    private UrlDao urlDao;

    public List<Member> findMember() {
        return manageDao.findMember();
    }

    public void updateMember(String id) {
        manageDao.updateMember(id);
    }

    public List<UrlModel> findUrlModel(String name) {
        return manageDao.findUrlModel(PYUtils.getUrl(name));
    }

    public void updateUrl(String name, String id, String status) {
        manageDao.updateUrl(PYUtils.getUrl(name), id, status);
    }

    public Item findItem(String username) {
        return manageDao.findItem(username);
    }

    public void addUrl(String url, String name) throws Exception {
        //https://zhongchou.modian.com/item/55469.html
        UrlModel urlModel = new UrlModel();
        urlModel.setName(name);
        if (url.startsWith("https")) {
            urlModel.setUid(url.replace("https://zhongchou.modian.com/item/", "").replace(".html", ""));
        } else {
            urlModel.setUid(url.replace("http://zhongchou.modian.com/item/", "").replace(".html", ""));
        }
        String html = SpiderUtils.getHtml(url);
        Document body = Jsoup.parse(html);
        String title = body.select("div.short-cut h3 span").html();
        urlModel.setXmname(title);
        urlModel.setSize("1500");
        urlModel.setStatus("1");
        String author = body.select("div.name.team.clearfix span").html();
        urlModel.setAuthor(author);
        urlDao.insertUrlModel2(PYUtils.getUrl(name), urlModel);
    }

//    public static void main(String[] args) throws Exception{
//        addUrl("https://zhongchou.modian.com/item/55469.html","刘增艳");
//    }
}
