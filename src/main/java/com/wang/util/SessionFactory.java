package com.wang.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author 王念
 * @create 2019-03-26 19:45
 */
public class SessionFactory {
    private static final String PATH="config.properties";
    private static Properties pro=null;
    private static SessionFactory instance=null;
    public SessionFactory(){
        pro=new Properties();
        try {
            InputStream in=SessionFactory.class.getClassLoader().getResourceAsStream(PATH);
            pro.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static SessionFactory getInstance(){
        return instance==null?new SessionFactory():instance;
    }
    public Properties getPro(){
        return pro;
    }
}
