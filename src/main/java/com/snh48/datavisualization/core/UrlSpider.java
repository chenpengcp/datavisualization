package com.snh48.datavisualization.core;

import com.snh48.datavisualization.pojo.UrlModel;
import com.snh48.datavisualization.utils.SpiderUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UrlSpider {
    public List<UrlModel> getUrl(String name) {
        List<UrlModel> urlModels = new ArrayList<>();
        try {
            String html = SpiderUtils.getHtml("https://zhongchou.modian.com/search?key=" + getEncode(name));
            Document document = getDocument(html);
            Elements elements = document.select("div.wrap.item_content div.procon.clearfix div.myproject.clearfix ul.pro_ul li");
            if (elements != null && elements.size() > 0) {
                for (int i = 0; i < elements.size(); i++) {
                    UrlModel urlModel = new UrlModel();
                    urlModel.setName(name);
                    if (elements.get(i).attr("data-pro-id").length() == 5) {
                        Elements elements1 = elements.get(i).select("a div.pro_logo img");
                        String author = elements.get(i).select("div.author a p").html();
                        urlModel.setAuthor(author);
                        String src = elements1.attr("src");
                        urlModel.setUid(elements.get(i).attr("data-pro-id"));
                        urlModel.setXmname(elements.get(i).select("div.pro_txt_field a h3.pro_title").html());
                        if (Integer.parseInt(handle(src)) > 20181129) {
                            urlModel.setStatus("1");
                        } else {
                            urlModel.setStatus("0");
                        }
                    }
                    if ("李艺彤".equals(name) || "黄婷婷".equals(name)) {
                        urlModel.setSize("2000");
                    } else {
                        urlModel.setSize("1000");
                    }
                    urlModels.add(urlModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlModels;
    }

    //解析网页
    public Document getDocument(String html) {
        Document document = Jsoup.parse(html);
        return document;
    }

    public String getEncode(String url) throws Exception {
        return URLEncoder.encode(url, "utf-8");
    }

    //        public static void main(String[] args) {
//        UrlSpider urlSpider = new UrlSpider();
//        List<UrlModel> list = urlSpider.getUrl("苏杉杉");
//        for (UrlModel u :
//                list) {
//            System.out.println(u);
//        }
//
//    }
    public Map<String, String> getMap(String name) {
        Map<String, String> map = new HashMap<>();
        List<UrlModel> list = getUrl(name);
        for (UrlModel u : list
                ) {
            map.put(u.getUid(), u.getSize());
        }
        return map;
    }

    public String handle(String src) {
        String sss = "";
        if (src.contains("https://p.moimg.net/bbs_attachments/")) {
            sss = src.replace("https://p.moimg.net/bbs_attachments/", "");
            sss = sss.replace("/", "");
            return sss.substring(0, 8);
        } else if (src.contains("https://p.moimg.net/project/project_")) {
            sss = src.replace("https://p.moimg.net/project/project_", "");
            sss = sss.replace("/", "");
            return sss.substring(0, 8);
        } else if (src.contains("https://p.moimg.net/project/")) {
            sss = src.replace("https://p.moimg.net/project/", "");
            sss = sss.replace("/", "");
            return sss.substring(0, 8);
        } else if (src.contains("https://u.moimg.net/project/project_")) {
            sss = src.replace("https://u.moimg.net/project/project_", "");
            sss = sss.replace("/", "");
            return sss.substring(0, 8);
        } else if (src.contains("https://u.moimg.net/bbs_attachments/")) {
            sss = src.replace("https://u.moimg.net/bbs_attachments/", "");
            sss = sss.replace("/", "");
            return sss.substring(0, 8);
        } else {
            return "20180729";
        }
    }

    public static void main(String[] args) {
        UrlSpider urlSpider = new UrlSpider();
        List<UrlModel> list = urlSpider.getUrl("冯薪朵");
        for (UrlModel u:list
             ) {
            System.out.println(u);
        }
    }
}
