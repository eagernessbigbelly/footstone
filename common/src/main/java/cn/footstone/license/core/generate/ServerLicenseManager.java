package cn.footstone.license.core.generate;


import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @description: ServerLicenseManager
 * @author: liuyan
 * @create: 2022−05-18 2:14 PM
 */
@Slf4j
public class ServerLicenseManager extends LicenseManager {

    //XML编码
    private static final String XML_CHARSET = "UTF-8";
    //默认BUFSIZE
    private static final int DEFAULT_BUFSIZE = 8 * 1024;

    public ServerLicenseManager(LicenseParam licenseParam) {
        super(licenseParam);
    }

    protected ServerLicenseManager() {
        super();
    }

    @Override
    protected synchronized byte[] create(LicenseContent licenseContent, LicenseNotary licenseNotary) throws Exception {
        super.create(licenseContent, licenseNotary);
        this.validateCreate(licenseContent);
        final GenericCertificate certificate = licenseNotary.sign(licenseContent);
        return getPrivacyGuard().cert2key(certificate);
    }

    private void validateCreate(LicenseContent licenseContent) throws LicenseContentException {
        final Date now = new Date();
        final Date notBefore = licenseContent.getNotBefore();
        final Date notAfter = licenseContent.getNotAfter();
        if (null != notAfter && now.after(notAfter)) {
            throw new LicenseContentException("证书失效时间不能早于当前时间");
        }
        if (null != notBefore && null != notAfter && notAfter.before(notBefore)) {
            throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
        }
        final String consumerType = licenseContent.getConsumerType();
        if (null == consumerType) {
            throw new LicenseContentException("用户类型不能为空");
        }
    }

}
