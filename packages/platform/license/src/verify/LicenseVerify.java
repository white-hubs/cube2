package com.guhecloud.platform.license.verify;

import com.guhecloud.platform.license.LicenseManagerHolder;
import com.guhecloud.platform.license.keystore.CustomKeyStoreParam;
import com.guhecloud.platform.license.verify.param.LicenseVerifyParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseParam;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

/**
 * License校验类
 *
 * @author lanchun
 * @date 2025/4/8 11:16
 */
@Slf4j
public class LicenseVerify {

    /**
     * 安装License证书
     *
     * @since 1.0.0
     */
    public synchronized LicenseContent install(LicenseVerifyParam param) {
        LicenseContent result = null;
        var format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            var licenseManager = LicenseManagerHolder.getInstance(initLicenseParam(param));
            result = licenseManager.install(param.getLicensePath());
            log.info("证书安装成功，证书有效期：{} - {}", format.format(result.getNotBefore()), format.format(result.getNotAfter()));
        } catch (Exception e) {
            log.error("证书安装失败！", e);
        }

        return result;
    }

    /**
     * 校验License证书
     *
     * @return boolean
     *
     * @since 1.0.0
     */
    public boolean verify() {
        var licenseManager = LicenseManagerHolder.getInstance(null);
        var format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            var licenseContent = licenseManager.verify();
            if (log.isDebugEnabled()) {
                log.debug("证书校验通过，证书有效期：{} - {}", format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter()));
            }
            return true;
        } catch (Exception e) {
            log.error("证书校验失败！", e);
            return false;
        }
    }

    /**
     * 初始化证书生成参数
     *
     * @param param License校验类需要的参数
     *
     * @return de.schlichtherle.license.LicenseParam
     *
     * @since 1.0.0
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param) {
        var preferences = Preferences.userNodeForPackage(LicenseVerify.class);
        var cipherParam = new DefaultCipherParam(param.getStorePass());
        var publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class
                , param.getPublicKeysStorePath()
                , param.getPublicAlias()
                , param.getStorePass()
                , null);
        return new DefaultLicenseParam(param.getSubject()
                , preferences
                , publicStoreParam
                , cipherParam);
    }
}
