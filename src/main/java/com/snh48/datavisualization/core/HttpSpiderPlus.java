package com.snh48.datavisualization.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.snh48.datavisualization.utils.JDBCUtils;
import com.snh48.datavisualization.utils.SpiderUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpSpiderPlus implements Runnable {
    private String id;
    private String size;
    private String name;

    public HttpSpiderPlus(String id, String size, String name) {
        this.id = id;
        this.size = size;
        this.name = name;
    }

    public Map<String, String> getData(String id, String size) throws IOException {
        double count = 0;
        try {
            count = Double.parseDouble(size);
        } catch (Exception e) {
            count = 20;
        }
        Map<String, String> map = new HashMap<>();
        for (int j = 1; j < Math.ceil(count / 20) + 1; j++) {
            String html = SpiderUtils.getHtml("https://zhongchou.modian.com/realtime/ajax_dialog_user_list?" +
                    "jsonpcallback=jQuery11110984149251830938_1526284807212&origin_id=" + id + "&type=backer_list&page=" + j + "&page_size=20&cate=2&_=1526284807216");
            String content = html.replace("window[decodeURIComponent('jQuery11110984149251830938_1526284807212')](", "").replace(");", "");
            JSONObject object = JSON.parseObject(content);
            String body = object.getString("html");
            Document document = Jsoup.parse(body);
            Elements elements = document.select("li.wds_item");
            for (int i = 0; i < elements.size(); i++) {
                Elements item = elements.get(i).select("div.item_cont");
                map.put(item.select("p").get(0).html(), item.select("p").get(1).html().replace("¥", ""));
            }
        }
        for (String key : map.keySet()
                ) {
            System.out.println(key + "====>" + map.get(key));
        }
        return map;
    }

    public void insert(Map<String, String> map, String name) throws Exception {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "INSERT INTO " + name + " (name,money) " +
                "VALUES (?,?)";
        for (String key : map.keySet()
                ) {
            if (!map.get(key).equals("**")) {
                int update = queryRunner.update(sql, key, map.get(key).contains(",") ? map.get(key).replace(",", "") : map.get(key));
                if (update == 1) {
                    //System.out.println("success!");
                } else {
                    //System.out.println("插入失败！");
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            this.insert(this.getData(id, size), name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(1);
//        cachedThreadPool.submit(new HttpSpider("44336", "500", "wxqj"));
//    }
}


