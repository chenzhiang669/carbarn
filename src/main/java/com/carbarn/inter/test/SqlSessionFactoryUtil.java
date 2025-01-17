package com.carbarn.inter.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class SqlSessionFactoryUtil {
    public static SqlSessionFactory sqlSessionFactory = null;

    private SqlSessionFactoryUtil() {}

    // 使用静态代码块保证线程安全问题
    static {
        String resource = "mybatis-config.xml";

        try {
            Reader reader = Resources.getResourceAsReader(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
