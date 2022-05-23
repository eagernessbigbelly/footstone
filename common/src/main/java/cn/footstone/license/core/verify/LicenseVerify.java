package cn.footstone.license.core.verify;

import lombok.Data;

/**
 * @description: 校验参数
 * @author: liuyan
 * @create: 2022−05-18 2:11 PM
 */
@Data
public class LicenseVerify {
    /**
     * 证书subject
     */
    private String subject;

    /**
     * 别称
     */
    private String alias;

    /**
     * 访问公钥库的密码
     */
    private String storePass;

    /**
     * 证书生成路径
     */
    private String licensePath;

    /**
     * 公钥库存储路径
     */
    private String publicCertsStorePath;
}
