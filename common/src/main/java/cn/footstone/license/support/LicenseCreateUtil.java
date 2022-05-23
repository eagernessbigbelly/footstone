package cn.footstone.license.support;

import cn.footstone.license.constant.LicenseConstant;
import cn.footstone.license.core.LicenseExtra;
import cn.footstone.license.core.LicenseKeyStore;
import cn.footstone.license.core.generate.LicenseCreate;
import cn.footstone.license.core.generate.ServerLicenseManagerHolder;
import cn.footstone.utils.SecurityUtil;
import cn.hutool.json.JSONUtil;
import de.schlichtherle.license.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.Preferences;

/**
 * @description: License 证书生成
 * @author: liuyan
 * @create: 2022−05-18 1:47 PM
 */
@Slf4j
public class LicenseCreateUtil {

    public static void main(String[] args) {
        createLicense();
    }

    /**
     * License 证书生成
     *
     * @return
     */
    public static boolean createLicense() {
        try {
            LicenseCreate licenseCreateParam = initLicenseCreateParam();
            if (licenseCreateParam == null) {
                return false;
            }
            LicenseParam licenseParam = initLicenseParam(licenseCreateParam);
            if (licenseParam == null) {
                return false;
            }
            LicenseManager licenseManager = ServerLicenseManagerHolder.getInstance(licenseParam);
            LicenseContent licenseContent = initLicenseContent(licenseCreateParam);
            licenseManager.store(licenseContent, new File(licenseCreateParam.getLicensePath()));
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            log.info(MessageFormat.format("证书生成成功\n------------------" +
                            "\n生成日期:{0}" +
                            "\n证书有效期：{1} 至 {2}" +
                            "\n证书路径：{3}" +
                            "\n唯一识别码：{4}" +
                            "\n描述： {5}\n------------------",
                    format.format(licenseCreateParam.getIssuedTime()),
                    format.format(licenseContent.getNotBefore()),
                    format.format(licenseContent.getNotAfter()),
                    licenseCreateParam.getLicensePath(),
                    licenseCreateParam.getMachineCode(),
                    licenseCreateParam.getDescription()));
            return true;
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
        }
        return false;
    }


    /**
     * 初始化LicenseContent
     *
     * @param licenseCreateParam licenseCreateParam
     * @return LicenseContent
     */
    private static LicenseContent initLicenseContent(LicenseCreate licenseCreateParam) throws IOException {
        if (licenseCreateParam == null) {
            return null;
        }

        LicenseContent licenseContent = new LicenseContent();
        X500Principal holder = new X500Principal(
                "CN=company, OU=company, O=company, L=BJ, ST=BJ, C=CN");
        licenseContent.setHolder(holder);
        licenseContent.setIssuer(null);
        licenseContent.setSubject(licenseCreateParam.getSubject());
        licenseContent.setIssued(licenseCreateParam.getIssuedTime());
        licenseContent.setNotBefore(licenseCreateParam.getIssuedTime());
        licenseContent.setNotAfter(licenseCreateParam.getExpiryTime());
        licenseContent.setConsumerType(licenseCreateParam.getConsumerType());
        licenseContent.setConsumerAmount(licenseCreateParam.getConsumerAmount());
        licenseContent.setInfo(licenseCreateParam.getDescription());
        LicenseExtra licenseExtra = new LicenseExtra(licenseCreateParam.getMachineCode(), LicenseConstant.LICENSE_VERSION);
//        String aa = holder.getName();
//        DistinguishedNameParser distinguishedNameParser = new DistinguishedNameParser(holder);
//
//        String a = distinguishedNameParser.findMostSpecific("CN");
        licenseContent.setExtra(JSONUtil.toJsonStr(licenseExtra));
        return licenseContent;
    }

    /**
     * 初始化License参数
     *
     * @param licenseCreateParam licenseCreateParam
     * @return LicenseParam
     */
    private static LicenseParam initLicenseParam(LicenseCreate licenseCreateParam) {
        if (licenseCreateParam == null) {
            return null;
        }
        Preferences preferences = Preferences.userNodeForPackage(LicenseCreateUtil.class);
        //设置对证书内容加密的秘钥
        CipherParam cipherParam = new DefaultCipherParam(licenseCreateParam.getStorePass());
        KeyStoreParam privateStoreParam = new LicenseKeyStore(LicenseCreateUtil.class
                , licenseCreateParam.getPrivateKeysStorePath()
                , licenseCreateParam.getPrivateAlias()
                , licenseCreateParam.getStorePass()
                , licenseCreateParam.getKeyPass());

        LicenseParam licenseParam = new DefaultLicenseParam(licenseCreateParam.getSubject()
                , preferences
                , privateStoreParam
                , cipherParam);

        return licenseParam;
    }

    /**
     * 初始化lic 参数
     *
     * @return LicenseCreate
     */
    private static LicenseCreate initLicenseCreateParam() {
        String licenseSubject = LicenseConstant.LICENSE_SUBJECT;
        if (StringUtils.isEmpty(licenseSubject)) {
            log.info("请配置证书subject！");
            return null;
        }
        String licensePrivatealias = LicenseConstant.LICENSE_ALIAS;
        if (StringUtils.isEmpty(licensePrivatealias)) {
            log.info("请配置密钥别称！");
            return null;
        }
        String licenseKeypass = LicenseConstant.LICENSE_PUBLICKEYPASS;
        if (StringUtils.isEmpty(licenseSubject)) {
            log.info("请配置密钥密码！");
            return null;
        }
        licenseKeypass = SecurityUtil.decryptAES(licenseKeypass);
        String licensePrivatekeysstorepath = LicenseConstant.LICENSE_PRIVATEKEYSSTOREPATH;
        if (StringUtils.isEmpty(licenseSubject)) {
            log.info("请配置密钥库存储路径！");
            return null;
        }
        String licenseStorepass = LicenseConstant.LICENSE_PUBLICSTOREPASS;
        if (StringUtils.isEmpty(licenseStorepass)) {
            log.info("请配置访问秘钥库的密码！");
            return null;
        }
        licenseStorepass = SecurityUtil.decryptAES(licenseStorepass);
        String licenseIssuedtime = LicenseConstant.LICENSE_ISSUEDTIME;
        if (StringUtils.isEmpty(licenseIssuedtime)) {
            log.info("请配置证书生效时间，格式： yyyy-MM-dd HH:mm:ss！");
            return null;
        }
        String licenseExpirytime = LicenseConstant.LICENSE_EXPIRYTIME;
        if (StringUtils.isEmpty(licenseExpirytime)) {
            log.info("请配置证书失效时间，格式： yyyy-MM-dd HH:mm:ss！");
            return null;
        }
        String licenseMachinecode = LicenseConstant.LICENSE_MACHINECODE;
        if (StringUtils.isBlank(licenseMachinecode)) {
            log.info("请配置证书使用的机器码！");
            return null;
        }
        String licenseDescription = LicenseConstant.LICENSE_DESCRIPTION;
        String licenseLicensepath = LicenseConstant.LICENSE_LICENSEPATH;
        if (StringUtils.isEmpty(licenseLicensepath)) {
            log.info("请配置证书生成路径！");
            return null;
        }
        LicenseCreate licenseCreateParam = new LicenseCreate();
        licenseCreateParam.setSubject(licenseSubject);
        licenseCreateParam.setPrivateAlias(licensePrivatealias);
        licenseCreateParam.setKeyPass(licenseKeypass);
        licenseCreateParam.setStorePass(licenseStorepass);
        licenseCreateParam.setLicensePath(licenseLicensepath);
        licenseCreateParam.setPrivateKeysStorePath(licensePrivatekeysstorepath);
        SimpleDateFormat simpleDateFormat = null;
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date issuedDate = simpleDateFormat.parse(licenseIssuedtime);
            licenseCreateParam.setIssuedTime(issuedDate);
        } catch (ParseException e) {
            log.info("证书生效时间不合法！");
            return null;
        }
        try {
            Date expiryDate = simpleDateFormat.parse(licenseExpirytime);
            licenseCreateParam.setExpiryTime(expiryDate);
        } catch (ParseException e) {
            log.info("证书失效时间不合法！");
            return null;
        }
//        licenseCreateParam.setConsumerType();
//        licenseCreateParam.setConsumerAmount();
        licenseCreateParam.setMachineCode(licenseMachinecode);
        licenseCreateParam.setDescription(licenseDescription);
        return licenseCreateParam;
    }

    private static String getRealPath() {
        String rootPath = LicenseCreateUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (StringUtils.isNotEmpty(rootPath)) {
            if (SystemUtils.IS_OS_WINDOWS) {
                if (rootPath.startsWith("/")) {
                    rootPath = rootPath.length() >= 2 ? rootPath.substring(1) : "";
                }
                rootPath = rootPath.replaceAll("/", "\\\\");
                rootPath = rootPath.substring(0, rootPath.lastIndexOf(File.separator)) + File.separator;
            }
        }
        return rootPath;
    }
}
