/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base.file;


import android.net.sip.SipProfile;

import com.xygame.sg.utils.Constants;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import base.test.Test;


/**
 * @author
 */
public class PropUtil {
    public Map<String, Object> getMap() {
        Map<String, Object> map = new TreeMap<String, Object>();
        Enumeration eu = props.propertyNames();
        while (eu.hasMoreElements()) {
            String element = eu.nextElement().toString();
            map.put(element, props.get(element));
        }
        return map;
    }

    //属性文件的路径
    String profilepath = "mail.properties";
    /**
     * 采用静态方法
     */
    private Properties props = new Properties();

    public PropUtil(InputStream profilepath) {
        try {
            props.load(profilepath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            System.exit(-1);
        }
    }

    public PropUtil(String profilepath) {
        if (profilepath.contains("/")) {

        } else {
            profilepath = Test.TEST_PATH + profilepath;
        }
        if (!new File(profilepath).getParentFile().exists()) {
            new File(profilepath).getParentFile().mkdirs();
        }
        if (!new File(profilepath).exists()) {
            try {
                new File(profilepath).createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PropUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.profilepath = profilepath;
        try {
            props.load(new FileInputStream(profilepath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            System.exit(-1);
        }
    }

    /**
     * 读取属性文件中相应键的值
     *
     * @param key 主键
     * @return String
     */
    public String getKeyValue(String key) {
        return props.getProperty(key);
    }

    /**
     * 根据主键key读取主键的值value
     *
     * @param filePath 属性文件路径
     * @param key      键名
     */
    public static String readValue(String filePath, String key) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(
                    filePath));
            props.load(in);
            String value = props.getProperty(key);
            System.out.println(key + "键的值是：" + value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新（或插入）一对properties信息(主键及其键值)
     * 如果该主键已经存在，更新该主键的值；
     * 如果该主键不存在，则插件一对键值。
     *
     * @param keyname  键名
     * @param keyvalue 键值
     */
    public void writeProperties(String keyname, String keyvalue) {
        try {
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。 
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。 
            OutputStream fos = new FileOutputStream(profilepath);
            props.setProperty(keyname, keyvalue);
            // 以适合使用 load 方法加载到 Properties 表中的格式， 
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流 
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }

    /**
     * 更新properties文件的键值对
     * 如果该主键已经存在，更新该主键的值；
     * 如果该主键不存在，则插件一对键值。
     *
     * @param keyname  键名
     * @param keyvalue 键值
     */
    public void updateProperties(String keyname, String keyvalue) {
        try {
            props.load(new FileInputStream(profilepath));
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。 
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。 
            OutputStream fos = new FileOutputStream(profilepath);
            props.setProperty(keyname, keyvalue);
            // 以适合使用 load 方法加载到 Properties 表中的格式， 
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流 
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }
} 


