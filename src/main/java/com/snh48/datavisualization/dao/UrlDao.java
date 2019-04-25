package com.snh48.datavisualization.dao;

import com.snh48.datavisualization.pojo.*;
import com.snh48.datavisualization.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UrlDao {
    public List<Member> findMember() {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String ss = "1";
        String sql = "select * from member where status=?";
        try {
            return queryRunner.query(sql, new BeanListHandler<Member>(Member.class), ss);
        } catch (Exception e) {
            return null;
        }
    }

    //如果已经存在url，就不插入
    public void insertUrlModel(String name, UrlModel u) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        if (u.getXmname().contains(u.getName())) {
            u.setStatus("1");
        } else {
            u.setStatus("0");
        }
        String ss = "select count(*) from " + name + " where uid=?";
        String sql = "INSERT INTO " + name + " (name,uid,size,author,Xmname,status) " +
                "VALUES (?,?,?,?,?,?)";
        try {
            Long count = queryRunner.query(ss, new ScalarHandler<Long>(), u.getUid());
            if (count == 0) {
                int update = queryRunner.update(sql, u.getName(), u.getUid(), u.getSize(), u.getAuthor(), u.getXmname(), u.getStatus());
                if (update == 1) {
                    //System.out.println("success!");
                } else {
                    //System.out.println("插入失败！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertUrlModel2(String name, UrlModel u) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String ss = "select count(*) from " + name + " where uid=?";
        String sql = "INSERT INTO " + name + " (name,uid,size,author,Xmname,status) " +
                "VALUES (?,?,?,?,?,?)";
        try {
            Long count = queryRunner.query(ss, new ScalarHandler<Long>(), u.getUid());
            if (count == 0) {
                int update = queryRunner.update(sql, u.getName(), u.getUid(), u.getSize(), u.getAuthor(), u.getXmname(), u.getStatus());
                if (update == 1) {
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<UrlModel> findUrlModel(String name) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String ss = "1";
        String sql = "select * from " + name + " where status=?";
        try {
            return queryRunner.query(sql, new BeanListHandler<UrlModel>(UrlModel.class), ss);
        } catch (Exception e) {
            return null;
        }
    }

    public String findUrlAuthor(String name) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "SELECT p.au FROM (SELECT t.author au,MAX(t.c) FROM (SELECT author,COUNT(*) c FROM " + name + " GROUP BY author) t) p";
        try {
            return queryRunner.query(sql, new ScalarHandler<String>());
        } catch (Exception e) {
            return null;
        }
    }

    public void insertRank(Model m) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "INSERT INTO rank (name,money) " +
                "VALUES (?,?)";
        try {
            int update = queryRunner.update(sql, m.getName(), m.getMoney());
            if (update == 1) {
                //System.out.println("success!");
            } else {
                //System.out.println("插入失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Model> findRank() {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from rank";
        try {
            return queryRunner.query(sql, new BeanListHandler<Model>(Model.class));
        } catch (Exception e) {
            return null;
        }
    }

    public void dropRank(String name) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "TRUNCATE TABLE " + name;
        try {
            queryRunner.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertTotal(User u) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "INSERT INTO total (name,money,totalMoney,count,ratio) " +
                "VALUES (?,?,?,?,?)";
        try {
            int update = queryRunner.update(sql, u.getName(), u.getMoney(), u.getTotalMoney(), u.getCount(), u.getRatio());
            if (update == 1) {
                //System.out.println("success!");
            } else {
                //System.out.println("插入失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> findTotalPlus() {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from total";
        try {
            return queryRunner.query(sql, new BeanListHandler<User>(User.class));
        } catch (Exception e) {
            return null;
        }
    }

    public void insertNode(Node n) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "INSERT INTO t_node (name,money,count,createDate) " +
                "VALUES (?,?,?,?)";
        try {
            int update = queryRunner.update(sql, n.getName(), n.getMoney(), n.getCount(), n.getCreateDate());
            if (update == 1) {
                //System.out.println("success!");
            } else {
                //System.out.println("插入失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertRecord(Record record) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "INSERT INTO t_record (ip,accessTime) " +
                "VALUES (?,?)";
        try {
            int update = queryRunner.update(sql, record.getIp(), record.getAccessTime());
            if (update == 1) {
                //System.out.println("success!");
            } else {
                //System.out.println("插入失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
