package com.guhecloud.cube.srv.gen.identity.server.qrcode.po;

import lombok.Data;

/**
 * @author wql
 * @describe 二维码历史数据迁移
 * @date 2023/12/19 17:01
 */
@Data
public class OldQrcodePO {
    /**
     * 二维码名称
     */
    private String qrcodeName;
    /**
     * 二维码编号
     */
    private String qrcodeCode;
    /**
     * 二维码暗码
     */
    private String qrcodeHiddenCode;
    /**
     * 关联url
     */
    private String referUrl;
    /**
     * 食安编码
     */
    private String securityCode;
    /**
     * 对象类型:asset资产 gear挡位 point现场点
     */
    private String bindObjectType;
    /**
     * 对象id
     */
    private String bindObjectId;
    /**
     * 对象名称
     */
    private String bindObjectName;
    /**
     * 对象区域id
     */
    private String areaId;
    /**
     * 二维码样式
     */
    private String qrcodeStyle;
    /**
     * 二维码序列号
     */
    private String qrcodeNum;
}
