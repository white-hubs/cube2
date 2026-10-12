package com.guhecloud.cube.base.license.server.creator;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.base.license.server.creator.param.LicenseCreatorParam;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.license.LicenseManagerHolder;
import com.guhecloud.platform.license.keystore.CustomKeyStoreParam;
import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;
import lombok.extern.slf4j.Slf4j;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.text.MessageFormat;
import java.util.Date;
import java.util.prefs.Preferences;

@Slf4j
public class LicenseCreator {

    private final static X500Principal DEFAULT_HOLDER_AND_ISSUER = new X500Principal("CN=localhost, OU=localhost, O=localhost, L=SH, ST=SH, C=CN");
    private final LicenseCreatorParam param;

    public LicenseCreator(LicenseCreatorParam param) {
        this.param = param;
    }

    /**
     * 生成License证书
     *
     * @return boolean
     *
     * @since 1.0.0
     */
    public boolean generateLicense() {
        try {
            LicenseManager licenseManager = LicenseManagerHolder.getInstance(initLicenseParam());
            LicenseContent licenseContent = initLicenseContent();

            licenseManager.store(licenseContent, new File(param.getLicensePath()));

            return true;
        } catch (Exception e) {
            log.error(MessageFormat.format("证书生成失败：{0}", param), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化证书生成参数
     *
     * @return de.schlichtherle.license.LicenseParam
     *
     * @since 1.0.0
     */
    private LicenseParam initLicenseParam() {
        Preferences preferences = Preferences.userNodeForPackage(LicenseCreator.class);

        //设置对证书内容加密的秘钥
        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam privateStoreParam = new CustomKeyStoreParam(LicenseCreator.class
                , param.getPrivateKeysStorePath()
                , param.getPrivateAlias()
                , param.getStorePass()
                , param.getKeyPass());

        return new DefaultLicenseParam(param.getSubject()
                , preferences
                , privateStoreParam
                , cipherParam);
    }

    /**
     * 设置证书生成正文信息
     *
     * @return de.schlichtherle.license.LicenseContent
     *
     * @since 1.0.0
     */
    private LicenseContent initLicenseContent() {
        LicenseContent licenseContent = new LicenseContent();
        licenseContent.setHolder(DEFAULT_HOLDER_AND_ISSUER);
        licenseContent.setIssuer(DEFAULT_HOLDER_AND_ISSUER);

        licenseContent.setSubject(param.getSubject());
        licenseContent.setIssued(new Date(DateUtils.toTimestamp(param.getIssuedTime())));
        licenseContent.setNotBefore(new Date(DateUtils.toTimestamp(param.getIssuedTime())));
        licenseContent.setNotAfter(new Date(DateUtils.toTimestamp(param.getExpiryTime())));
        licenseContent.setConsumerType(param.getConsumerType());
        licenseContent.setConsumerAmount(param.getConsumerAmount());
        licenseContent.setInfo(param.getDescription());
        //扩展校验服务器硬件信息
        licenseContent.setExtra(JSON.toJSONString(param.getLicenseCheckModel()));
        return licenseContent;
    }
}
