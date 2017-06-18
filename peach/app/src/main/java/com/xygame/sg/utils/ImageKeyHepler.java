package com.xygame.sg.utils;

import java.io.UnsupportedEncodingException;

import com.xygame.sg.utils.AliPreferencesUtil;

import android.content.Context;

public class ImageKeyHepler {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		System.out.println("加密后：");
//		System.out.println(setEncrypt("a+b=c"));
//		System.out.println("解密后：");
//		System.out.println(getEncrypt(setEncrypt("a+b=c")));
		//使用
				//加密：
//				encrypt("test","");

				//解密：
//				encrypt(encrypt("test"));
	}
	/**
     * 使用异或进行简单的密码加密
     * @return <code>String[]</code> 加密后字符串
     * @author Administrator
     * @since 1.0 2005/11/28
     */

//    public static String setEncrypt(Context context,String str){
//        String sn=AliPreferencesUtil.getAuthPicKey(context); //密钥
//        int[] snNum=new int[str.length()];
//        String result="";
//        String temp="";
//
//        for(int i=0,j=0;i<str.length();i++,j++){
//            if(j==sn.length())
//                j=0;
//            snNum[i]=str.charAt(i)^sn.charAt(j);
//        }
//
//        for(int k=0;k<str.length();k++){
//
//            if(snNum[k]<10){
//                temp="00"+snNum[k];
//            }else{
//                if(snNum[k]<100){
//                    temp="0"+snNum[k];
//                }
//            }
//            result+=temp;
//        }
//        return result;
//    }
    
//    public static byte[] setEncrypt(Context context,byte[] byteStr) throws UnsupportedEncodingException{
//    	byte[] finalByte;
//    	String str=new String(byteStr, "UTF-8");
//        String sn=AliPreferencesUtil.getAuthPicKey(context); //密钥
//        int[] snNum=new int[str.length()];
//        String result="";
//        String temp="";
//
//        for(int i=0,j=0;i<str.length();i++,j++){
//            if(j==sn.length())
//                j=0;
//            snNum[i]=str.charAt(i)^sn.charAt(j);
//        }
//
//        for(int k=0;k<str.length();k++){
//
//            if(snNum[k]<10){
//                temp="00"+snNum[k];
//            }else{
//                if(snNum[k]<100){
//                    temp="0"+snNum[k];
//                }
//            }
//            result+=temp;
//        }
//        finalByte=result.getBytes("UTF-8");
//        return finalByte;
//    }

    /**
     * 密码解密,虽然用不到
     * @return <code>String[]</code> 加密后字符串
     * @author Administrator
     * @since 1.0 2005/11/28
     */
//    public static String getEncrypt(Context context,String str){
//        String sn=AliPreferencesUtil.getAuthPicKey(context);  //密钥
//        char[] snNum=new char[str.length()/3];
//        String result="";
//
//        for(int i=0,j=0;i<str.length()/3;i++,j++){
//            if(j==sn.length())
//                j=0;
//            int n=Integer.parseInt(str.substring(i*3,i*3+3));
//            snNum[i]=(char)((char)n^sn.charAt(j));
//        }
//
//        for(int k=0;k<str.length()/3;k++){
//            result+=snNum[k];
//        }
//        return result;
//    }


}
