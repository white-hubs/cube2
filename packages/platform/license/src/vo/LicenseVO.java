package com.guhecloud.platform.license.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.license.model.LicenseCheckModel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 证书信息
 *
 * @author lanchun
 * @date 2025/4/25 16:09
 */
@Data
public class LicenseVO {

    /**
     * 证书subject
     */
    private String subject;

    /**
     * 证书生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime issuedTime = DateUtils.now();

    /**
     * 证书失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expiryTime;

    /**
     * 描述信息
     */
    private String description = "";

    /**
     * 到期天数
     */
    private Integer expiryDays;

    /**
     * 额外的服务器硬件校验信息
     */
    @JsonIgnore
    private LicenseCheckModel licenseCheckModel;
}
