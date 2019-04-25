package com.snh48.datavisualization.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class TableUtils {

    public static void createTable(String name) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        try {
            String sql1 = "DROP TABLE IF EXISTS " + name;
            queryRunner.update(sql1);
        } catch (Exception e) {
        }
        String sql = "CREATE TABLE `" + name + "` (\n" +
                "  `id` INT(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `NAME` VARCHAR(200) DEFAULT NULL,\n" +
                "  `money` DOUBLE DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4";
        try {
            queryRunner.update(sql);
        } catch (Exception e3) {

        }
    }

    public static void createUrlTable(String name) {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        try {
//            String sql1 = "DROP TABLE IF EXISTS " + name;
//            queryRunner.update(sql1);
            String sql1 = "SELECT COUNT(*) FROM information_schema.TABLES WHERE table_name =?";
            Long count = queryRunner.query(sql1, new ScalarHandler<Long>(), name);
            if (count == 0) {
                String sql = "CREATE TABLE `" + name + "` (\n" +
                        "  `id` INT(11) NOT NULL AUTO_INCREMENT,\n" +
                        "  `NAME` VARCHAR(200) DEFAULT NULL,\n" +
                        "  `uid` VARCHAR(200) DEFAULT NULL,\n" +
                        "  `size` VARCHAR(200) DEFAULT NULL,\n" +
                        "  `author` VARCHAR(200) DEFAULT NULL,\n" +
                        "  `Xmname` VARCHAR(200) DEFAULT NULL,\n" +
                        "  `status` VARCHAR(32) DEFAULT NULL,\n" +
                        "  PRIMARY KEY (`id`)\n" +
                        ")";
                queryRunner.update(sql);
            }
        } catch (Exception e3) {

        }
    }
}
