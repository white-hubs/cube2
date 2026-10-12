package com.guhecloud.cube.base.cloud.qr.code.server.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "cloud_qr_code")
public class CloudQrCodePO extends BasicEntity<Long> {

    private Long id;

    private String code;

    private String name;

    private String shortCode;

    private Long qrTypeId;

    private String qrContentType;

    private String qrContent;

    /**
     * 行为方式：ROUTE-路由跳转、TEXT-文本内容
     */
    private String mode;

    /**
     * 行为内容：路由地址或文本内容
     */
    private String modeContent;

    private String grantClientId;

    private Boolean enabled;
}
