package base.data.net.http;


import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;

import javax.crypto.Cipher;

public class DesUtils {

    /**
     * 默认密钥
     */
//    private static String strDefaultKey = "7c61558c012666bd63be22476b891b57@*";
      private static String strDefaultKey = "74b2150d7017fa8a312456c36b0b0e08a#@&*";//生产环境的密钥(仅限打包时开启)

    /**
     * 加密工具
     */
    private static Cipher encryptCipher = null;

    /**
     * 解密工具
     */
    private static Cipher decryptCipher = null;

    private static boolean isInit = false;

    static{
        try {
            Key key = getKey(hexStr2ByteArr(byteArr2HexStr(strDefaultKey.getBytes())));
            encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
            isInit = true;
        }catch (Exception e){
            isInit = false;
        }
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    private static String byteArr2HexStr(byte[] arrB) throws Exception {
//    	System.out.println(new String(arrB));
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
     */
    private static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 指定密钥构造方法
     *
     * @throws Exception
     */
    private static void init() throws Exception {
//        Security.addProvider(new com.sun.crypto.provider.SunJCE());
//        Key key = getKey(strDefaultKey.getBytes());
        if(isInit){
            return;
        }
        Key key = getKey(hexStr2ByteArr(byteArr2HexStr(strDefaultKey.getBytes())));
        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
        isInit = true;
    }


    /**
     * 加密字节数组
     *
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    private static byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     *
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String encrypt(String strIn) throws Exception {
        if (!isInit) {
            init();
        }
        return new String(Base64.encodeBase64(encrypt(strIn.getBytes())));
    }

    /**
     * 解密字节数组
     *
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    private static byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     *
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decrypt(String strIn) throws Exception {
        if (!isInit) {
            init();
        }
        return new String(decrypt(Base64.decodeBase64(strIn.getBytes())));
//        return new String(decrypt(hexStr2ByteArr(strIn)));
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     * @throws Exception
     */
    private static Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];

        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

        return key;
    }

    public static void main(String[] args) {
        String str;
        InputStreamReader stdin = new InputStreamReader(System.in);
        BufferedReader bufin = new BufferedReader(stdin);
        while (true) {
            try {
                System.out.println("请输入要加密的字符串： ");
                str = bufin.readLine();
                System.out.println("加密后的字符：" + DesUtils.encrypt(str));
            } catch (Exception e) {
                System.out.println("错误");
            }
        }
    }
}