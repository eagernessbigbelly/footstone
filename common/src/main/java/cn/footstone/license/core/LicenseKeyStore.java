package cn.footstone.license.core;

import de.schlichtherle.license.AbstractKeyStoreParam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @description: LicenseKeyStore
 * @author: liuyan
 * @create: 2022−05-18 3:11 PM
 */
public class LicenseKeyStore extends AbstractKeyStoreParam {

    private String storePath;
    private String alias;
    private String storePwd;
    private String keyPwd;

    protected LicenseKeyStore(Class aClass, String storePath) {
        super(aClass, storePath);
    }

    public LicenseKeyStore(Class aClass, String storePath, String alias, String storePwd, String keyPwd) {
        super(aClass, storePath);
        this.storePath = storePath;
        this.alias = alias;
        this.storePwd = storePwd;
        this.keyPwd = keyPwd;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getStorePwd() {
        return storePwd;
    }

    @Override
    public String getKeyPwd() {
        return keyPwd;
    }

    @Override
    public InputStream getStream() throws IOException {
        final InputStream in = new FileInputStream(storePath);
        if (null == in) {
            throw new FileNotFoundException(storePath);
        }
        return in;
    }
}
