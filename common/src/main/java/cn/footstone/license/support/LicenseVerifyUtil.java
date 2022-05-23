package cn.footstone.license.support;

import cn.footstone.license.constant.LicenseConstant;
import cn.footstone.license.core.verify.ClientLicenseManagerHolder;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

/**
 * @description: License 证书校验
 * @author: liuyan
 * @create: 2022−05-18 1:47 PM
 */
@Slf4j
public class LicenseVerifyUtil {


    /**
     * 安装证书
     */
    public static synchronized void clientLicenseInstall() throws Exception {
        try {
            log.info("++++++++++++++++++++++License 初始化安装中++++++++++++++++++++++");
            LicenseManager licenseManager = ClientLicenseManagerHolder.getInstance();
            licenseManager.uninstall();
            LicenseContent licenseContent = licenseManager.install(new File(LicenseConstant.LICENSE_LICENSEPATH));
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            log.info(MessageFormat.format("++++++++证书安装成功，证书有效期：{0} - {1}++++++++", format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter())));
        } catch (Exception e) {
            log.error("++++++++++++++++++++++License 证书安装失败++++++++++++++++++++++", e);
            throw e;
        }
    }


    /**
     * filter 校验中使用
     * @return LicenseContent
     * @throws Exception e
     */
    public static synchronized LicenseContent clientLicenseVerify() throws Exception {
        LicenseManager licenseManager = ClientLicenseManagerHolder.getInstance();
        LicenseContent licenseContent = licenseManager.verify();
        //更新时间, 可以将最后一次的更新时间保存，防止用户通过篡改服务器时间来绕过有效期校验
        LicenseConstant.CLIENT_LICENSE_LAST_UPDATE_DATE = System.currentTimeMillis();
        return licenseContent;
    }
}
