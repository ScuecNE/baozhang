package com.sugus.baozhang.common.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;

public class EncryptedUtil {
    /**
     * 加盐字符串，长度必须为16位。
     */
    private final static String SALT = "iwanttoplayaball";

    /**
     * 生成含有加盐的密码
     */
    public static String generate(String password) {
        return generate(password, null);
    }

    /**
     * 生成含有加盐的密码
     */
    public static String generate(String password, String userName) {
        String salt = getSalt(userName);
        if (StringUtils.isEmpty(password)) {
            return "";
        }
        password = md5Hex(password + salt);
        char[] cs1 = password.toCharArray();
        char[] cs2 = salt.toCharArray();
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = cs1[i / 3 * 2];
            cs[i + 1] = cs2[i / 3];
            cs[i + 2] = cs1[i / 3 * 2 + 1];
        }
        return new String(cs).toUpperCase();
    }

    public static String getSalt(String salt) {
        if (salt == null || salt.length() == 0) {
            return EncryptedUtil.SALT;
        }
        if (salt.length() <= 16) {
            return getStringLen8(salt) + EncryptedUtil.SALT.substring(7, 15);
        } else {
            return salt.substring(4, 12) + EncryptedUtil.SALT.substring(2, 10);
        }
    }

    private static String getStringLen8(String salt) {
        if (salt.length() == 8) {
            return salt;
        }
        if (salt.length() > 8) {
            return salt.substring(1, 9);
        } else {
            return getStringLen8(salt + salt);
        }
    }

    /**
     * 校验密码是否正确
     */
    public static boolean verify(String password, String md5) {
        char[] cs = md5.toLowerCase().toCharArray();
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = cs[i];
            cs1[i / 3 * 2 + 1] = cs[i + 2];
            cs2[i / 3] = cs[i + 1];
        }
        String salt = new String(cs2);
        String md5Hex = md5Hex(password + salt);
        if (StringUtils.isEmpty(md5Hex)) {
            return false;
        }
        return md5Hex.equals(new String(cs1));
    }

    /**
     * 获取十六进制字符串形式的MD5摘要
     */
    private static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return "";
        }
    }

    public static String getMD5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
}

