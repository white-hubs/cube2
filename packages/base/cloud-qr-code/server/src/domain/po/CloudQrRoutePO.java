package com.guhecloud.cube.base.cloud.qr.code.server.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("cloud_qr_route")
public class CloudQrRoutePO extends BasicEntity<Long> {

    private Long id;

    //关联二维码
    private Long qrCodeId;

    // 短码，与cloud_qr_code.short_code关联
    private String shortCode;

    //实际跳转的目标URL
    private String targetUrl;
    //跳转参数（JSON格式）
    private String targetParams;
}
