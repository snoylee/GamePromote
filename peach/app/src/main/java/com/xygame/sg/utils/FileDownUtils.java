package com.xygame.sg.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileDownUtils {
    private String SDPath;
    
    public FileDownUtils(String fileDir){
        //得到当前外部存储设备的目录
        this.SDPath=fileDir;
    }
    
    /**
     * 在SD卡上创建文件
     * @param fileName
     * @return
*/
    public File createSDFile(String fileName){
        File file=new File(SDPath+fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    
    /**
     * 在SD卡上创建目录
     * @return
*/
    public void createSDDir(){
        File file=new File(SDPath);
        if (!file.exists()){
            file.mkdir();
        }
    }
    
    /**
     * 判断SD卡上文件是否存在
     * @param fileName
     * @return
*/
    public boolean isFileExist(String fileName){
        File file=new File(SDPath+fileName);
        return file.exists();
    }
    /**
     * 将一个inputStream里面的数据写到SD卡中
     * @param fileName
     * @return
*/
    public File writeToSDfromInput(String fileName,InputStream fosfrom){
        File file=null;
        try {
            createSDDir();
            OutputStream fosto = new FileOutputStream(SDPath.concat(fileName));
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            file= new File(SDPath.concat(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}