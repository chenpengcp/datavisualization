package com.snh48.datavisualization.core;

import com.snh48.datavisualization.utils.JDBCUtils;
import com.snh48.datavisualization.utils.SpiderUtils;
import org.apache.commons.dbutils.QueryRunner;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpSpider implements Runnable {
    private String id;
    private String size;
    private String name;

    public HttpSpider(String id, String size, String name) {
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
            String html = unicodeToString(SpiderUtils.getHtml("https://zhongchou.modian.com/realtime/ajax_dialog_user_list?" +
                    "jsonpcallback=jQuery11110984149251830938_1526284807212&origin_id=" + id + "&type=backer_list&page=" + j + "&page_size=20&cate=2&_=1526284807216"));
            //String regexStr = "&lt;p&gt;(.+?)&lt;";
            System.out.println(html);
            String regexStr = "<p>(.+?)<\\\\/p>\\\\n";
            Pattern pattern = Pattern.compile(regexStr);
            Matcher matcher = pattern.matcher(html);
            List<String> list = new ArrayList<>();
            while (matcher.find()) {
                System.out.println(matcher.group().substring(3, matcher.group().lastIndexOf("<")));
                list.add(matcher.group().substring(3, matcher.group().lastIndexOf("<")));
            }

            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    map.put(list.get(i), list.get(i + 1).replace("¥", ""));
                }
                if (i % 2 == 0) {
                    map.put(list.get(i), list.get(i + 1).replace("¥", ""));
                }
            }
        }
        for (String key : map.keySet()
                ) {
            System.out.println(key + "====>" + map.get(key));
        }
        return map;
    }

    public String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
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

