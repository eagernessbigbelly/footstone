package cn.footstone.license.core.generate;

import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;


/**
 * @description: ServerLicenseManagerHolder
 * @author: liuyan
 * @create: 2022−05-18 2:14 PM
 */
public class ServerLicenseManagerHolder {

    private static volatile ServerLicenseManager SERVER_LICENSE_MANAGER;

    public static LicenseManager getInstance(LicenseParam licenseParam) throws LicenseContentException {
        if (SERVER_LICENSE_MANAGER == null) {
            synchronized (ServerLicenseManagerHolder.class) {
                if (SERVER_LICENSE_MANAGER == null) {
                    SERVER_LICENSE_MANAGER = new ServerLicenseManager(licenseParam);
                }
            }
        }
        return SERVER_LICENSE_MANAGER;
    }
}
