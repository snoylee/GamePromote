package com.xygame.sg.task.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.xygame.sg.bean.comm.ProvinceBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by huiming on 2015/7/23.
 */
public class AssetDataBaseManager {
    private static final String DB_NAME = "city.db";
    private static final int DB_VERSION = 1;

    private static final String city_table = "area";
    private static final String[] city_columns = new String[]{"id", "parentId", "name", "type"};

    private static String tag = "assetsdatabase";
    private static String databasepath = "/data/data/%s/database";

    private Map<String, SQLiteDatabase> databases = new HashMap<String, SQLiteDatabase>();
    private Context context = null;
    private static AssetDataBaseManager mInstance = null;

    public static void initDataBaseManager(Context context) {
        if (mInstance == null) {
            mInstance = new AssetDataBaseManager(context);
        }
    }

    public static AssetDataBaseManager getManager() {
        return mInstance;
    }

    private AssetDataBaseManager(Context context) {
        this.context = context;
    }

    public SQLiteDatabase getDatabase(String dbfile) {
        if (databases.get(dbfile) != null) {
            Log.i(tag, String.format("Return a database copy of %s", dbfile));
            return databases.get(dbfile);
        }
        if (context == null)
            return null;

        Log.i(tag, String.format("Create database %s", dbfile));
        String spath = getDatabaseFilepath();
        String sfile = getDatabaseFile(dbfile);

        File file = new File(sfile);
        SharedPreferences dbs = context.getSharedPreferences(AssetDataBaseManager.class.toString(), 0);
        boolean flag = dbs.getBoolean(dbfile, false); // Get Database file flag, if true means this database file was copied and valid
        if (!flag || !file.exists()) {
            file = new File(spath);
            if (!file.exists() && !file.mkdirs()) {
                Log.i(tag, "Create \"" + spath + "\" fail!");
                return null;
            }
            if (!copyAssetsToFilesystem(dbfile, sfile)) {
                Log.i(tag, String.format("Copy %s to %s fail!", dbfile, sfile));
                return null;
            }

            dbs.edit().putBoolean(dbfile, true).commit();
        }

        SQLiteDatabase db = SQLiteDatabase.openDatabase(sfile, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        if (db != null) {
            databases.put(dbfile, db);
        }
        return db;
    }

    private String getDatabaseFilepath() {
        return String.format(databasepath, context.getApplicationInfo().packageName);
    }

    private String getDatabaseFile(String dbfile) {
        return getDatabaseFilepath() + "/" + dbfile;
    }

    private boolean copyAssetsToFilesystem(String assetsSrc, String des) {
        Log.i(tag, "Copy " + assetsSrc + " to " + des);
        InputStream istream = null;
        OutputStream ostream = null;
        try {
            AssetManager am = context.getAssets();
            istream = am.open(assetsSrc);
            ostream = new FileOutputStream(des);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = istream.read(buffer)) > 0) {
                ostream.write(buffer, 0, length);
            }
            istream.close();
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (istream != null)
                    istream.close();
                if (ostream != null)
                    ostream.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * Close assets database
     *
     * @param dbfile, the assets file which will be closed soon
     * @return, the status of this operating
     */
    public boolean closeDatabase(String dbfile) {
        if (databases.get(dbfile) != null) {
            SQLiteDatabase db = databases.get(dbfile);
            db.close();
            databases.remove(dbfile);
            return true;
        }
        return false;
    }

    /**
     * Close all assets database
     */
    static public void closeAllDatabase() {
        Log.i(tag, "closeAllDatabase");
        if (mInstance != null) {
            for (int i = 0; i < mInstance.databases.size(); ++i) {
                if (mInstance.databases.get(i) != null) {
                    mInstance.databases.get(i).close();
                }
            }
            mInstance.databases.clear();
        }
    }

    public ArrayList queryCitiesByParentId(int parentId) {
        CityBean cityBean;

        ArrayList<CityBean> listCity = new ArrayList<CityBean>();
        Cursor cursor = getDatabase(DB_NAME).query(city_table, city_columns, "parentId=?", new String[]{parentId + ""}, null, null, null);
        while (cursor.moveToNext()) {
            if (parentId == 0) {
                cityBean = new ProvinceBean();
            } else {
                cityBean = new com.xygame.sg.bean.comm.CityBean();
            }
            cityBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            cityBean.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
            cityBean.setName(cursor.getString(cursor.getColumnIndex("name")));
            cityBean.setType(cursor.getInt(cursor.getColumnIndex("type")));
            listCity.add(cityBean);
        }
        Collections.sort(listCity, new Comparator<CityBean>() {
            @Override
            public int compare(CityBean lhs, CityBean rhs) {
                return lhs.getId() - rhs.getId();
            }
        });
        cursor.close();
        return listCity;
    }
    
    public ProvinceBean queryProviceById(int proviceId) {
    	ProvinceBean cityBean=null;

        Cursor cursor = getDatabase(DB_NAME).query(city_table, city_columns, "id=?", new String[]{proviceId + ""}, null, null, null);
        while (cursor.moveToNext()) {
        	cityBean = new ProvinceBean();
            cityBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            cityBean.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
            cityBean.setName(cursor.getString(cursor.getColumnIndex("name")));
            cityBean.setType(cursor.getInt(cursor.getColumnIndex("type")));
        }
        cursor.close();
        return cityBean;
    }

    public static class CityBean implements Serializable{
        private int id;
        private int parentId;
        private String name;
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public CityBean queryCityByParentId(int id, int parentId) {
        CityBean cityBean = null;
        Cursor cursor = getDatabase(DB_NAME).query(city_table, city_columns, "id=? and parentId =?", new String[]{id + "", parentId + ""}, null, null, null);
        while (cursor.moveToNext()) {
            cityBean = new CityBean();
            cityBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            cityBean.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
            cityBean.setName(cursor.getString(cursor.getColumnIndex("name")));
            cityBean.setType(cursor.getInt(cursor.getColumnIndex("type")));
            break;
        }
        cursor.close();
        return cityBean;
    }

    Map<Integer, CityBean> map = new TreeMap();

    public CityBean queryCityById(int id) {
        CityBean cityBean;
        if (id == 0) {
            cityBean = new ProvinceBean();
        } else {
            cityBean = new com.xygame.sg.bean.comm.CityBean();
        }
        if (map.containsKey(id)) {
            return map.get(id);
        } else {
            Cursor cursor = getDatabase(DB_NAME).query(city_table, city_columns, "id=?", new String[]{id + ""}, null, null, null);

            try {
                while (cursor.moveToNext()) {


                    cityBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    cityBean.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
                    cityBean.setName(cursor.getString(cursor.getColumnIndex("name")));
                    cityBean.setType(cursor.getInt(cursor.getColumnIndex("type")));
                    map.put(id, cityBean);
                    break;
                }
                cursor.close();
            } catch (Exception e) {

                return null;
            }
        }
        return cityBean;
    }

    public CityBean queryCityByName(String name) {
        CityBean cityBean = null;
        Cursor cursor = getDatabase(DB_NAME).query(city_table, city_columns, "type = 2 and name=?", new String[]{name + ""}, null, null, null);
        while (cursor.moveToNext()) {
            cityBean = new CityBean();
            cityBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            cityBean.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
            cityBean.setName(cursor.getString(cursor.getColumnIndex("name")));
            cityBean.setType(cursor.getInt(cursor.getColumnIndex("type")));
            break;
        }
        cursor.close();
        return cityBean;
    }

    public CityBean queryProvinceByName(String name) {
        CityBean cityBean = null;
        Cursor cursor = getDatabase(DB_NAME).query(city_table, city_columns, "type = 1 and name=?", new String[]{name + ""}, null, null, null);
        while (cursor.moveToNext()) {
            cityBean = new CityBean();
            cityBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            cityBean.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
            cityBean.setName(cursor.getString(cursor.getColumnIndex("name")));
            cityBean.setType(cursor.getInt(cursor.getColumnIndex("type")));
            break;
        }
        cursor.close();
        return cityBean;
    }

    public static List<CityBean> listProvince;
    public static List<CityBean> listCity;
}
