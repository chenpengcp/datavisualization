package com.snh48.datavisualization.core;

import com.snh48.datavisualization.pojo.UrlModel;
import com.snh48.datavisualization.utils.SpiderUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: chenpeng
 */
@Component
public class UrlSpiderPlus {
    public List<UrlModel> getUrl(String name) {
        ArrayList<UrlModel> urlModels = new ArrayList<>();
        try {
            String html = SpiderUtils.getHtml("https://zhongchou.modian.com/es/search_list?jsonpcallback=jQuery111104494141676590844_1551685549776&key=" + getEncode(name) + "&type=all&page=1&size=100&_=1551685549777");
            String content = html.replace("window[decodeURIComponent('jQuery111104494141676590844_1551685549776')](", "").replace(");", "");
            JSONObject object = JSON.parseObject(content);
            JSONObject data = object.getJSONObject("data");
            JSONArray products = data.getJSONArray("products");
            if (products.size() > 0) {
                for (int i = 0; i < products.size(); i++) {
                    JSONObject jsonObject = products.getJSONObject(i);
                    String ctime = jsonObject.getString("ctime").substring(0, 10).replace("-", "");
                    if (Integer.parseInt(ctime) > 20180729) {
                        UrlModel urlModel = new UrlModel();
                        urlModel.setName(name);
                        urlModel.setUid(jsonObject.getString("id"));
                        urlModel.setSize("1500");
                        urlModel.setAuthor(jsonObject.getJSONObject("author_info").getString("nickname"));
                        urlModel.setXmname(jsonObject.getString("name"));
                        if (urlModel.getXmname().contains(name)) {
                            urlModel.setStatus("1");
                        } else {
                            urlModel.setStatus("0");
                        }
//                    System.out.println(urlModel);
                        urlModels.add(urlModel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlModels;
    }

    public static String getEncode(String url) throws Exception {
        return URLEncoder.encode(url, "utf-8");
    }

    public static void main(String[] args) {
        UrlSpiderPlus urlSpider = new UrlSpiderPlus();
        List<UrlModel> urlModels = urlSpider.getUrl("苏杉杉");
        for (UrlModel u : urlModels
                ) {
            System.out.println(u);
        }
    }
}
