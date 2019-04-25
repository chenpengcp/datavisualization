package com.snh48.datavisualization.core;

import com.snh48.datavisualization.utils.SpiderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PKSpider {
    public static Map<String, String> getData(String id, String size) throws IOException {
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
            //System.out.println(html);
            String regexStr = "<p>(.+?)<\\\\/p>\\\\n";
            Pattern pattern = Pattern.compile(regexStr);
            Matcher matcher = pattern.matcher(html);
            List<String> list = new ArrayList<>();
            while (matcher.find()) {
                //System.out.println(matcher.group().substring(3, matcher.group().lastIndexOf("<")));
                list.add(matcher.group().substring(3, matcher.group().lastIndexOf("<")));
            }
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    map.put(list.get(i), list.get(i + 1).replace("¥", "").replace(",", ""));
                }
                if (i % 2 == 0) {
                    map.put(list.get(i), list.get(i + 1).replace("¥", "").replace(",", ""));
                }
            }
        }
        return map;
    }

    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }
}

