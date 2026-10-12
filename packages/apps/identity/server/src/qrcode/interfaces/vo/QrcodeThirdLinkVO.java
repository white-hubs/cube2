package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author bq
 * @date 2022/7/20 13:30
 * @description
 */
@Data
public class QrcodeThirdLinkVO {

    /**
     * 编号
     */
    @NotEmpty(message = "第三方链接名称不能为空")
    private String thirdLinkName;

    /**
     * 名称
     */
    @NotEmpty(message = "第三方链接URL不能为空!")
    private String thirdLinkUrl;

}
