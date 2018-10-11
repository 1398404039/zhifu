package com.lwz.pay_sys.utils.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class SecurityUtil {

    private static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    /**
     * 加密字符串
     *
     * @param str       源文字符串
     * @param secretKey 秘钥
     * @return 加密后的字符串
     */
    public static String encrypt(String str, String secretKey) {
        try {
            byte[] datasource = str.getBytes("utf-8");
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(secretKey.getBytes("utf-8"));
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            byte[] bytes = cipher.doFinal(datasource);
            String encryptStr = new BASE64Encoder().encode(bytes);
            return encryptStr;

        } catch (Throwable e) {
            logger.error("des加密字符串异常." + str, e);
        }
        return null;
    }

    /**
     * 界面字符串
     *
     * @param encryptStr 密文字符串
     * @param secretKey  秘钥
     * @return 明文字符串
     */
    public static String decrypt(String encryptStr, String secretKey) {
        try {
            byte[] src = new BASE64Decoder().decodeBuffer(encryptStr);
            // DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(secretKey.getBytes("utf-8"));
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正开始解密操作
            byte[] result = cipher.doFinal(src);
            return new String(result, "utf-8");
        } catch (Throwable e) {
            logger.error("des解密字符串异常." + encryptStr, e);
        }
        return null;
    }

    /**
     * MD5加密
     *
     * @param encryptStr 加密字符串
     * @return
     */
    public static String md5(String encryptStr) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(encryptStr.getBytes());
            byte[] bPwd = md.digest();
            String pwd = new BigInteger(1, bPwd).toString(16);
            if (pwd.length() % 2 == 1) {
                pwd = "0" + pwd;
            }
            return pwd;
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    /**
     * 使用jdk的base64 加密字符串
     */
    public static String jdkBase64Encoder(byte[] context) {
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(context);
        return encode;
    }


    /**
     * 使用jdk的base64 解密字符串
     * 返回为null表示解密失败
     */
    public static byte[] jdkBase64Decoder(String str) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] decode = null;
        try {
            decode = decoder.decodeBuffer(str);
        } catch (IOException e) {
            logger.error("BASE64解密字符串异常." + str, e);
        }
        return decode;
    }
}
