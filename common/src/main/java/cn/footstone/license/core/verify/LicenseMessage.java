package cn.footstone.license.core.verify;

import de.schlichtherle.util.ObfuscatedString;

/**
 * @description: LicenseMessage信息
 * @author: liuyan
 * @create: 2022−05-18 3:09 PM
 */
public class LicenseMessage {

    public static final String EXC_NOT_MACHINE_INFO = "INVALID MACHINE INFO!";
    public static final String EXC_MACHINE_NO_AUTHORIZATION = "THE MACHINE IS NOT AUTHORIZED!";
    public static final String EXC_LICENSE_VERIFY_ERROR = "license VERIFY ERROR!";
    public static final String EXC_LICENSE_TIME_INVALID = "LICENSE TIME INVALID!";
    public static final String EXC_LICENSE_NOINFO = "LICENSE NO INFO!";
    public static final String EXC_LICENSE_NOEXTRA = "LICENSE NO EXTRA!";
    public static final String EXC_LICENSE_NOMACHINECODE = "LICENSE NO MACHINECODE!";
    public static final String EXC_LICENSE_NOVERSION = "LICENSE NO VERSION!";
    public static final String EXC_LICENSE_VERSIONLATE = "LICENSE VERSION LATE!";

    public static final String EXC_LICENSE_IS_NOT_YET_VALID;

    static {
        EXC_LICENSE_IS_NOT_YET_VALID = (new ObfuscatedString(new long[]{5434633639502011825L, -3406117476263181371L, 6903673940810780388L, -6816911225052310716L})).toString();
    }
}
