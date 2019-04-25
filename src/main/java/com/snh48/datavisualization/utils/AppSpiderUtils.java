package com.snh48.datavisualization.utils;

import com.snh48.datavisualization.pojo.ModelPlus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppSpiderUtils {

    public static Integer getData(String page, String name, String post_id, String pid) throws Exception {
        String page_rows = "10";
        String code = "089d8d9e1fc32718";
        String client = "2";
        String user_id = "0";
        String page_index = page;
//        String moxi_post_id = "63682";//zx
        String moxi_post_id = post_id;
//        String pro_id = "43166";//zx
        String pro_id = pid;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        String signContent = "page_rows=" + page_rows + "&code=" + code + "&client=" + client + "&user_id="
//                + user_id + "&page_index=" + page_index + "&moxi_post_id=" + moxi_post_id + "&pro_id=" + pro_id;
        NameValuePair pair = new BasicNameValuePair("page_rows", page_rows);
        NameValuePair pair2 = new BasicNameValuePair("code", code);
        NameValuePair pair3 = new BasicNameValuePair("client", client);
        NameValuePair pair4 = new BasicNameValuePair("user_id", user_id);
        NameValuePair pair5 = new BasicNameValuePair("page_index", page_index);
        NameValuePair pair6 = new BasicNameValuePair("moxi_post_id", moxi_post_id);
        NameValuePair pair7 = new BasicNameValuePair("pro_id", pro_id);
        params.add(pair);
        params.add(pair2);
        params.add(pair3);
        params.add(pair4);
        params.add(pair5);
        params.add(pair6);
        params.add(pair7);
        String postForString = AppSpiderUtils.postWithParamsForString("http://orderapi.modian.com/v40_7/product/comment_list", params);
        System.out.println("postForString:"+postForString);
//        String str = StringEscapeUtils.unescapeJava(postForString);
        JSONObject object = JSON.parseObject(postForString);
        JSONArray data = object.getJSONArray("data");
        if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                JSONObject obj = (JSONObject) data.get(i);
                if (obj.getString("content").startsWith("支持了")) {
                    String ctime = obj.getString("ctime");
                    String content = obj.getString("content").replace("支持了", "").replace("元", "").trim();
                    JSONObject user_info = obj.getJSONObject("user_info");
                    String username = user_info.getString("username");
                    ModelPlus model = new ModelPlus();
                    model.setName(username);
                    model.setMoney(content);
                    model.setCreateTime(ctime);
//                System.out.println(model);
                    insert(model, name);
                }
            }
        }
        return data.size();
    }

    public static String postWithParamsForString(String url, List<NameValuePair> params) {
        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        String s = "";
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpPost.setHeader("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 5.1.1; vivo x5s l Build/LYZ28N)");
            httpPost.setHeader("Accept-Encoding", "gzip");
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                s = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void insert(ModelPlus model, String name) throws Exception {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "INSERT INTO " + name + " (name,money,createTime) " +
                "VALUES (?,?,?)";
        int update = queryRunner.update(sql, model.getName(), model.getMoney(), model.getCreateTime());
        if (update == 1) {
//            System.out.println("success!");
        } else {
//            System.out.println("插入失败！");
        }
    }

    public static void main(String[] args) throws Exception {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String table = "rst";
        String aql = "CREATE TABLE `" + table + "` (" +
                "  `id` int(11) DEFAULT NULL," +
                "  `NAME` varchar(200) DEFAULT NULL," +
                "  `money` double DEFAULT NULL," +
                "  `createTime` varchar(200) DEFAULT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
        queryRunner.update(aql);
        for (int i = 0; i < 100000; i++) {
            Integer data = getData(String.valueOf(i), table, "61153", "40632");
            if (data == 0) {
                break;
            }
        }
        String sql1 = "CREATE TABLE " + table + 1 + " AS SELECT DISTINCT NAME,money,createTime FROM " + table;
        String sql2 = "DROP TABLE " + table;
        String sql3 = "CREATE TABLE " + table + " AS (SELECT @rownum := @rownum +1 AS id,b.* FROM " + table + 1 + " b,(SELECT @rownum:=0) AS i )";
        String sql4 = "DROP TABLE " + table + 1;
        List<String> list = new ArrayList<>();
        list.add(sql1);
        list.add(sql2);
        list.add(sql3);
        list.add(sql4);
        for (int i = 0; i < list.size(); i++) {
            queryRunner.update(list.get(i));
        }
    }
}
