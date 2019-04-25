package com.snh48.datavisualization.dao;

import com.snh48.datavisualization.pojo.*;
import com.snh48.datavisualization.utils.JDBCUtils;
import com.snh48.datavisualization.utils.PYUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Repository
public class DataDao {
    @Autowired
    private UrlDao urlDao;

    public User findOne(String id, String name, Float ratio, Member m) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        User user = new User();
        try {
            String sql1 = "select sum(money) from " + id;
            String sql2 = "SELECT COUNT(b.name) FROM (SELECT * FROM " + id + " t GROUP BY t.NAME) b";
            Double money = queryRunner.query(sql1, new ScalarHandler<Double>());
            Long count = queryRunner.query(sql2, new ScalarHandler<Long>());
            user.setId(1);
            user.setName(name);
            user.setRatio(ratio);
            user.setMoney(0f);
            if (money != null) {
                user.setMoney(Float.parseFloat(String.valueOf(money)));
            }
            user.setTotalMoney(user.getMoney() * ratio);
            user.setCount(count);
            user.setParty(m.getParty());
            user.setTeam(m.getTeam());
            user.setPeriods(m.getPeriods());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<Model> result(String id) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "SELECT * FROM (SELECT id,NAME,SUM(money) money FROM " + id + " GROUP BY NAME) t ORDER BY t.money DESC";
        try {
            return queryRunner.query(sql, new BeanListHandler<Model>(Model.class));
        } catch (Exception e) {
            return null;
        }
    }

    public List<UserInfo> findUserInfo(String id) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "SELECT * FROM userInfo WHERE pid=?";
        try {
            return queryRunner.query(sql, new BeanListHandler<UserInfo>(UserInfo.class), id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> findTotal() {
        List<User> list = new ArrayList<>();
        try {
            List<Member> members = urlDao.findMember();
            members.forEach(m-> list.add(findOne(PYUtils.getTotal(m.getName()), m.getName(), 1f, m)));
            list.sort(Comparator.comparing(User::getTotalMoney));
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setId(i + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Model findGrMoney(String name, String id) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "SELECT * FROM (SELECT id,NAME,SUM(money) money FROM " + id + " GROUP BY NAME) t WHERE t.NAME=?";
        try {
            return queryRunner.query(sql, new BeanHandler<Model>(Model.class), name);
        } catch (Exception e) {
            return null;
        }
    }

    public Long findGrCount(String name, String id) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "SELECT COUNT(*) FROM " + id + " WHERE NAME=?";
        try {
            return queryRunner.query(sql, new ScalarHandler<Long>(), name);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Model> findMax(String id) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "SELECT * FROM (SELECT id,NAME,SUM(money) money FROM " + id + " GROUP BY NAME) t ORDER BY t.money DESC LIMIT 0,5";
        try {
            return queryRunner.query(sql, new BeanListHandler<Model>(Model.class));
        } catch (Exception e) {
            return null;
        }
    }

    public List<Pkmodel> findGrPkName() {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "SELECT * FROM pk ORDER BY id DESC LIMIT 0,5";
        try {
            return queryRunner.query(sql, new BeanListHandler<Pkmodel>(Pkmodel.class));
        } catch (Exception e) {
            return null;
        }
    }

    public Pkmodel findPkData(String id) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "SELECT * FROM pk WHERE id=?";
        try {
            return queryRunner.query(sql, new BeanHandler<Pkmodel>(Pkmodel.class), id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Node> findTrend(String name, String start, String end) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "SELECT * FROM t_node WHERE NAME=? AND createDate BETWEEN ? AND ?";
        try {
            return queryRunner.query(sql, new BeanListHandler<Node>(Node.class), name, start, end);
        } catch (Exception e) {
            return null;
        }
    }
}
