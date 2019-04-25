package com.snh48.datavisualization.dao;


import com.snh48.datavisualization.pojo.Item;
import com.snh48.datavisualization.pojo.Member;
import com.snh48.datavisualization.pojo.UrlModel;
import com.snh48.datavisualization.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: chenpeng
 */
@Repository
public class ManageDao {
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

//    public List<Member> findMember() {
//        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
//        String sql = "select * from member";
//        try {
//            return queryRunner.query(sql, new BeanListHandler<Member>(Member.class));
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public void updateMember(String id) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql1 = "select * from member where id=?";
        String sql = "UPDATE member SET STATUS=? WHERE id=?";
        try {
            Member member = queryRunner.query(sql1, new BeanHandler<Member>(Member.class), id);
            if (member.getStatus().equals("1")) {
                queryRunner.update(sql, "0", id);
            } else {
                queryRunner.update(sql, "1", id);
            }
        } catch (Exception e) {
        }
    }

    public void updateUrl(String name, String id, String status) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "UPDATE " + name + " SET STATUS=? WHERE id=?";
        try {
            if (status.equals("1")) {
                queryRunner.update(sql, "0", id);
            } else {
                queryRunner.update(sql, "1", id);
            }
        } catch (Exception e) {
        }
    }

    public List<UrlModel> findUrlModel(String name) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from " + name;
        try {
            return queryRunner.query(sql, new BeanListHandler<UrlModel>(UrlModel.class));
        } catch (Exception e) {
            return null;
        }
    }

    public Item findItem(String username) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from item where username=?";
        try {
            return queryRunner.query(sql, new BeanHandler<Item>(Item.class), username);
        } catch (Exception e) {
            return null;
        }
    }
}
