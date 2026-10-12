package com.guhecloud.platform.license;

import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.license.verify.param.LicenseVerifyParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LicenseCheckListener {

    /**
     * 证书subject
     */
    @Value("${license.subject:}")
    private String subject;

    /**
     * 公钥别称
     */
    @Value("${license.publicAlias:}")
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    @Value("${license.storePass:}")
    private String storePass;

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath:}")
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    @Value("${license.publicKeysStorePath:}")
    private String publicKeysStorePath;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (StringUtils.isNotBlank(licensePath)) {
            log.info("++++++++ 开始安装证书 ++++++++");
            LicenseVerifyParam param = new LicenseVerifyParam();
            param.setSubject(subject);
            param.setPublicAlias(publicAlias);
            param.setStorePass(storePass);
            param.setLicensePath(licensePath);
            param.setPublicKeysStorePath(publicKeysStorePath);
            LicenseVerifyHolder.install(param);
            //安装证书
            log.info("++++++++ 证书安装结束 ++++++++");
        }
    }
}
