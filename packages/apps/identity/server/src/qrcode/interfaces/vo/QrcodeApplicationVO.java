package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bq
 * @date 2022/7/22
 * @description 二维码功能
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrcodeApplicationVO {
    private Long id;
    /**
     * 编号
     */
    private String applicationCode;

    /**
     * 名称
     */
    private String applicationName;

    /**
     * 图标
     */
    private String icon;

    private String thirdUrl;

    public QrcodeApplicationVO(String applicationCode, String applicationName) {
        this.applicationCode = applicationCode;
        this.applicationName = applicationName;
    }

    public QrcodeApplicationVO(String applicationCode, String applicationName, String thirdUrl) {
        this.applicationCode = applicationCode;
        this.applicationName = applicationName;
        this.thirdUrl = thirdUrl;
    }
}