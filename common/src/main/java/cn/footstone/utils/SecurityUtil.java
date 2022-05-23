package cn.footstone.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 加密工具类
 * @author: liuyan
 * @create: 2022−05-18 2:15 PM
 */
public class SecurityUtil {
    /**
     * 密钥
     */
    private final static byte[] key = "0CoJUm6Qyw8W8jud".getBytes();
    /**
     * 构建
     */
    private final static SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES.getValue(), key);


    public static String encryptAES(String content) {
        if (StringUtils.isNotBlank(content)) {
            return aes.encryptHex(content);
        }
        return null;
    }

    public static String decryptAES(String content) {
        if (StringUtils.isNotBlank(content)) {
            return aes.decryptStr(content, CharsetUtil.CHARSET_UTF_8);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(encryptAES("12345678A"));
        System.out.println(decryptAES(encryptAES("12345678A")));
    }
}
