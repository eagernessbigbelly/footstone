package cn.footstone.license.core.verify;

import de.schlichtherle.license.LicenseManager;

/**
 * @description: License
 * @author: liuyan
 * @create: 2022−05-18 1:58 PM
 */
public class ClientLicenseManagerHolder {

    private static volatile LicenseManager CLIENT_LICENSE_MANAGER;

    /**
     * @return 获取LicenseManager
     */
    public static LicenseManager getInstance() {
        if (CLIENT_LICENSE_MANAGER == null) {
            synchronized (ClientLicenseManagerHolder.class) {
                if (CLIENT_LICENSE_MANAGER == null) {
                    ClientLicenseManager clientLicenseManager = new ClientLicenseManager();
                    clientLicenseManager.initLicenseParam();
                    CLIENT_LICENSE_MANAGER = clientLicenseManager;
                }
            }
        }
        return CLIENT_LICENSE_MANAGER;
    }
}
