package com.guhecloud.platform.license.controller;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.license.LicenseManagerHolder;
import com.guhecloud.platform.license.vo.LicenseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@RestController
@Slf4j
public class LicenseController {

    /**
     * 获取证书基本信息
     *
     * @return
     */
    @GetMapping("/public/license")
    public LicenseVO getLicense() {
        try {
            LicenseVO licenseContent = LicenseManagerHolder.getInstance().getLicenseContent();
            if (Objects.isNull(licenseContent)) {
                return null;
            }
            LocalDateTime now = DateUtils.beginOfDay(LocalDateTime.now());
            long betweenDay = DateUtils.between(now, licenseContent.getExpiryTime(), ChronoUnit.DAYS);
            licenseContent.setExpiryDays((int) betweenDay);
            return licenseContent;
        } catch (Exception e) {
            log.error("获取证书信息失败", e);
            throw new ServiceException("获取证书信息失败");
        }
    }
}
