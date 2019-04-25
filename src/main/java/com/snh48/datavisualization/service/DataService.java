package com.snh48.datavisualization.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.snh48.datavisualization.dao.DataDao;
import com.snh48.datavisualization.dao.UrlDao;
import com.snh48.datavisualization.kafka.KafkaSender;
import com.snh48.datavisualization.pojo.*;
import com.snh48.datavisualization.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DataService {
    @Autowired
    private KafkaSender kafkaSender;
    @Autowired
    private DataDao dataDao;
    @Autowired
    private UrlDao urlDao;

    //查看近期pk
    public List<User> findAll(String id) {
        List<User> list = new ArrayList<>();
        Jedis jedis = RedisUtils.getJedis();
        Pkmodel pkData = dataDao.findPkData(id);
        List<UserInfo> userInfos = dataDao.findUserInfo(pkData.getId());
        userInfos.forEach(u -> {
            Map<String, String> map = jedis.hgetAll(pkData.getName() + u.getName());
            User user = new User();
            user.setName(u.getName());
            user.setRatio(u.getRatio());
            user.setCount((long) map.size());
            Float dd = 0f;
            for (String key : map.keySet()
                    ) {
                dd += Float.parseFloat(map.get(key));
            }
            user.setMoney(Float.parseFloat(ScalaUtils.scala(dd)));
            user.setTotalMoney(Float.parseFloat(ScalaUtils.scala(dd * user.getRatio())));
            user.setPid(u.getPid());
            list.add(user);
        });
        list.sort(Comparator.comparing(User::getTotalMoney));
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(i + 1);
        }
        return list;
    }

    //查看pk成员的集资列表
    public List<Model> resultPk(String name, String id) {
        List<Model> list = new ArrayList<>();
        Jedis jedis = RedisUtils.getJedis();
        Pkmodel pkData = dataDao.findPkData(id);
        Map<String, String> map = jedis.hgetAll(pkData.getName() + name);
        map.forEach((k, v) -> {
            Model model = new Model();
            model.setName(k);
            model.setMoney(v);
            list.add(model);
        });
        list.sort(Comparator.comparing(Model::getMoney));
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(String.valueOf(i + 1));
            list.get(i).setMoney(ScalaUtils.scala(Float.parseFloat(list.get(i).getMoney())));
        }
        return list;
//        return dataDao.result(id);
    }

    //查看单个成员的集资列表
    public List<Model> result(String id) {
        return dataDao.result(id);
    }

    //查看单个成员的集资趋势
    public List<Node> trend(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        Date endDate = new Date();
        String endTime = sdf.format(endDate);
        c.setTime(endDate);
        c.add(Calendar.DATE, -6);
        Date startDate = c.getTime();
        String date = sdf.format(startDate);
        return dataDao.findTrend(name, date, endTime);
    }

    //查看单个成员的集资分析
    public MemberInfo resultCount(String id) {
        Jedis jedis = RedisUtils.getJedis();
        String str = jedis.get(id + "count");
        MemberInfo memberInfo = JSON.parseObject(str, MemberInfo.class);
        return memberInfo;
    }

    //查看所有成员
    public List<User> findTotal() {
        Jedis jedis = RedisUtils.getJedis();
        //return urlDao.findTotalPlus();
        List<User> list = new ArrayList<>();
        for (int i = 1; i < 67; i++) {
            String ss = jedis.get(i + "a");
            JSONObject jsonObject = (JSONObject) JSON.parse(ss);
            User user = new User();
            user.setId(i);
            user.setName(jsonObject.getString("name"));
            user.setMoney(jsonObject.getFloat("money"));
            user.setCount(jsonObject.getLong("count"));
            user.setTotalMoney(jsonObject.getFloat("totalMoney"));
            user.setRatio(jsonObject.getFloat("ratio"));
            user.setParty(jsonObject.getString("party"));
            user.setTeam(jsonObject.getString("team"));
            user.setPeriods(jsonObject.getString("periods"));
            list.add(user);
        }
        return list;
    }

    //查看每日增长
    public List<User> findAdd() {
        Jedis jedis = RedisUtils.getJedis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        Date endDate = new Date();
        String endTime = sdf.format(endDate);
        c.setTime(endDate);
        c.add(Calendar.DATE, -1);
        Date startDate = c.getTime();
        String date = sdf.format(startDate);
        //return urlDao.findTotalPlus();
        List<User> list = new ArrayList<>();
        Map<String, User> mapNow = getMap("a");
        Map<String, User> map = getMap("a" + date);
        mapNow.forEach((k, v) -> {
            if (map.containsKey(k)) {
                User now = mapNow.get(k);
                User yes = map.get(k);
                User user = new User();
                user.setName(now.getName());
                user.setMoney(ScalaUtils.scalaFloat(now.getMoney() - yes.getMoney()));
                user.setCount(now.getCount() - yes.getCount());
                user.setTotalMoney(ScalaUtils.scalaFloat(now.getTotalMoney() - yes.getTotalMoney()));
                user.setRatio(now.getRatio());
                user.setParty(now.getParty());
                user.setTeam(now.getTeam());
                user.setPeriods(now.getPeriods());
                if (user.getMoney() > 0) {
                    list.add(user);
                }
            }
        });
        list.sort(Comparator.comparing(User::getTotalMoney));
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(i + 1);
        }
        return list;
    }

    //个人搜索
    public List<GrSearch> findSearch(String name) {
        List<GrSearch> list = new ArrayList<>();
        try {
            List<Member> members = urlDao.findMember();
            members.forEach(m -> {
                if (dataDao.findGrMoney(name, PYUtils.getTotal(m.getName())) != null) {
                    Model model = dataDao.findGrMoney(name, PYUtils.getTotal(m.getName()));
                    GrSearch grSearch = new GrSearch();
                    grSearch.setFansName(name);
                    grSearch.setMemberName(m.getName());
                    grSearch.setMoney(model.getMoney());
                    User user = dataDao.findOne(PYUtils.getTotal(m.getName()), m.getName(), 1f, m);
                    grSearch.setTotalMoney(String.valueOf(user.getTotalMoney()));
                    grSearch.setCount(String.valueOf(dataDao.findGrCount(name, PYUtils.getTotal(m.getName()))));
                    list.add(grSearch);
                }
            });
            list.sort(Comparator.comparing(GrSearch::getMoney));
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setId(i + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //土豪排行榜
    public String findMax() {
        Jedis jedis = RedisUtils.getJedis();
        return jedis.get("rank");
        //return urlDao.findRank();
    }

    public List<Pkmodel> findPkName() {
        return dataDao.findGrPkName();
    }

    public List<Member> findMember() {
        return urlDao.findMember();
    }

    //获取现在全团集资总额
    public String getAllMoney() {
        Jedis jedis = RedisUtils.getJedis();
        return jedis.get("allMoney");
    }

    //获取现在全团集资人数
    public String getTotalCount() {
        Jedis jedis = RedisUtils.getJedis();
        return jedis.get("totalCount");
    }

    //获取全团统计分析
    public String findAnalysis(String id) {
        Jedis jedis = RedisUtils.getJedis();
        String ss = "";
        switch (id) {
            case "1":
                ss = jedis.get("team");
                break;
            case "2":
                ss = jedis.get("party");
                break;
            case "3":
                ss = jedis.get("periods");
                break;
        }
        return ss;
    }

    public Map<String, String> getRichCount() {
        Jedis jedis = RedisUtils.getJedis();
        Map<String, String> map = new HashMap<>();
        map.put("wcount", jedis.get("wcount"));
        map.put("kcount", jedis.get("kcount"));
        map.put("kkcount", jedis.get("kkcount"));
        return map;
    }

    public Map<String, User> getMap(String key) {
        Jedis jedis = RedisUtils.getJedis();
        Map<String, User> map = new HashMap<>();
        for (int i = 1; i < 67; i++) {
            if (jedis.exists(i + key)) {
                String ss = jedis.get(i + key);
                JSONObject jsonObject = (JSONObject) JSON.parse(ss);
                User user = new User();
                user.setId(i);
                user.setName(jsonObject.getString("name"));
                user.setMoney(jsonObject.getFloat("money"));
                user.setCount(jsonObject.getLong("count"));
                user.setTotalMoney(jsonObject.getFloat("totalMoney"));
                user.setRatio(jsonObject.getFloat("ratio"));
                user.setParty(jsonObject.getString("party"));
                user.setTeam(jsonObject.getString("team"));
                user.setPeriods(jsonObject.getString("periods"));
                map.put(user.getName(), user);
            }
        }
        return map;
    }

    public void record(String host) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(d);
        Record record = new Record();
        record.setIp(host);
        record.setAccessTime(date);
        kafkaSender.send(JSON.toJSONString(record));
    }

}
