package cn.footstone.license.constant;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @description: License常量
 * @author: liuyan
 * @create: 2022−05-18 2:14 PM
 */
@Slf4j
public class LicenseConstant {

    private static Properties properties = null;

    static {
        try {
            if (properties == null) {
                properties = new Properties();
            }
            InputStream in = LicenseConstant.class.getResourceAsStream("/config/license.properties");
            properties.load(in);
        } catch (IOException e) {
            log.error("LicenseConstant license初始化异常", e);
        }
    }

    public static long CLIENT_LICENSE_LAST_UPDATE_DATE;


    /**
     * 证书版本
     */
    public final static Double LICENSE_VERSION = Double.valueOf(properties.getProperty("license.version"));

    /**
     * 证书subject
     */
    public final static String LICENSE_SUBJECT = properties.getProperty("license.subject");
    /**
     * 公钥别称
     */
    public final static String LICENSE_ALIAS = properties.getProperty("license.alias");

    /**
     * 访问公钥库的密码
     */
    public final static String LICENSE_PUBLICSTOREPASS = properties.getProperty("license.publicstorepass");


    /**
     * 证书生效时间
     */
    public final static String LICENSE_ISSUEDTIME = properties.getProperty("license.issuedtime");
    /**
     * 证书失效时间
     */
    public final static String LICENSE_EXPIRYTIME = properties.getProperty("license.expirytime");

//    #密钥库存储路径
//    license.publickeysstorepath=certs/privateKeys.keystore
    /**
     * 公钥库存储路径
     */
    public final static String LICENSE_PUBLICCERTSSTOREPATH = properties.getProperty("license.publicCertsstorepath");

    /**
     * 私钥库存储路径
     */
    public final static String LICENSE_PRIVATEKEYSSTOREPATH = properties.getProperty("license.privateKeysstorepath");

    /**
     * 证书生成路径
     */
    public final static String LICENSE_LICENSEPATH = properties.getProperty("license.licensepath");


    /**
     * 证书使用的机器码
     */
    public final static String LICENSE_MACHINECODE = properties.getProperty("license.machinecode");


    /**
     * 描述
     */
    public final static String LICENSE_DESCRIPTION = properties.getProperty("license.description");

    /**
     * 公钥密码
     */
    public final static String LICENSE_PUBLICKEYPASS = properties.getProperty("license.publickeypass");


}
