package cn.footstone.license.core.verify;

import cn.footstone.license.constant.LicenseConstant;
import cn.footstone.license.core.LicenseExtra;
import cn.footstone.license.core.LicenseKeyStore;
import cn.footstone.license.support.LicenseVerifyUtil;
import cn.footstone.utils.HardwareInfoUtil;
import cn.footstone.utils.SecurityUtil;
import cn.hutool.json.JSONUtil;
import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.prefs.Preferences;

/**
 * @description: ClientLicenseManager
 * @author: liuyan
 * @create: 2022−05-18 1:59 PM
 */
public class ClientLicenseManager extends LicenseManager {


    /**
     * 校验
     *
     * @param licenseNotary the license notary used to verify the current license key
     *                      - may <em>not</em> be {@code null}.
     * @return
     * @throws LicenseContentException
     */
    @Override
    protected synchronized LicenseContent verify(LicenseNotary licenseNotary) throws LicenseContentException {
        LicenseContent licenseContent;
        try {
            licenseContent = super.verify(licenseNotary);
            this.validate(licenseContent);
        } catch (Exception e) {
            throw new LicenseContentException(e.getLocalizedMessage());
        }
        return licenseContent;
    }

    /**
     * @param licenseContent the license content
     *                       - may <em>not</em> be {@code null}.
     * @throws LicenseContentException
     */
    @Override
    protected synchronized void validate(LicenseContent licenseContent) throws LicenseContentException {
        GenericCertificate certificate = this.getCertificate();
        if (null == certificate) {
            super.validate(licenseContent);
            // 这里在校验一下最后一次更新时间, 防止用户通过篡改服务器时间来绕过有效期校验
            if (LicenseConstant.CLIENT_LICENSE_LAST_UPDATE_DATE > System.currentTimeMillis()) {
                throw new LicenseContentException(LicenseMessage.EXC_LICENSE_IS_NOT_YET_VALID);
            }
            LicenseConstant.CLIENT_LICENSE_LAST_UPDATE_DATE = System.currentTimeMillis();
            //判断是否过期
            Date now = new Date();
            if (now.after(licenseContent.getNotAfter())
                    || now.before(licenseContent.getNotBefore())) {
                throw new LicenseContentException(LicenseMessage.EXC_LICENSE_IS_NOT_YET_VALID);
            }
            //校验licenseExtra信息
            String strExtra = JSONUtil.isNull(licenseContent.getExtra()) || !JSONUtil.isJson(licenseContent.getExtra().toString())
                    ? null : licenseContent.getExtra().toString();
            LicenseExtra licenseExtra = JSONUtil.toBean(strExtra, LicenseExtra.class);
            //判断授权码是否正确
            if (StringUtils.isBlank(licenseExtra.getMachineCode())) {
                throw new LicenseContentException(LicenseMessage.EXC_LICENSE_NOMACHINECODE);
            }
            String machineCodeReal = HardwareInfoUtil.getMachineCode();
            if (StringUtils.isBlank(machineCodeReal)) {
                throw new RuntimeException(LicenseMessage.EXC_NOT_MACHINE_INFO);
            }
            if (!licenseExtra.getMachineCode().equals(machineCodeReal)) {
                throw new RuntimeException(LicenseMessage.EXC_MACHINE_NO_AUTHORIZATION);
            }
            //判断版本
            Double version = licenseExtra.getVersion();
            if (null == version) {
                throw new RuntimeException(LicenseMessage.EXC_LICENSE_NOVERSION);
            }
            if (version < LicenseConstant.LICENSE_VERSION) {
                throw new RuntimeException(LicenseMessage.EXC_LICENSE_VERSIONLATE);
            }
            this.setCertificate(certificate);
        }
    }


    /**
     * 初始化 LicenseVerify
     *
     * @return
     */
    private LicenseVerify getLicenseVerify() {
        LicenseVerify licenseVerify = new LicenseVerify();
        licenseVerify.setSubject(LicenseConstant.LICENSE_SUBJECT);
        licenseVerify.setAlias(LicenseConstant.LICENSE_ALIAS);
        if (StringUtils.isNotEmpty(LicenseConstant.LICENSE_PUBLICSTOREPASS)) {
            licenseVerify.setStorePass(SecurityUtil.decryptAES(LicenseConstant.LICENSE_PUBLICSTOREPASS));
        }
        licenseVerify.setLicensePath(LicenseConstant.LICENSE_LICENSEPATH);
        String path = "/" + this.getClass().getResource("/").getPath().substring(1) + LicenseConstant.LICENSE_PUBLICCERTSSTOREPATH;
        licenseVerify.setPublicCertsStorePath(path);
        return licenseVerify;
    }

    /**
     * 初始化LicenseParam
     */
    public void initLicenseParam() {
        LicenseVerify licenseVerify = getLicenseVerify();
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerifyUtil.class);
        CipherParam cipherParam = new DefaultCipherParam(licenseVerify.getStorePass());
        KeyStoreParam publicStoreParam = new LicenseKeyStore(LicenseVerifyUtil.class, licenseVerify.getPublicCertsStorePath(), licenseVerify.getAlias(), licenseVerify.getStorePass(), null);
        DefaultLicenseParam defaultLicenseParam = new DefaultLicenseParam(licenseVerify.getSubject(), preferences, publicStoreParam, cipherParam);
        this.setLicenseParam(defaultLicenseParam);
    }
}
