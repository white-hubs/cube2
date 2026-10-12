package com.guhecloud.cube.base.cloud.qr.code.server.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "cloud_qr_code_type")
public class CloudQrCodeTypePO extends BasicEntity<Long> {

    private Long id;

    private String code;

    private String name;

    private String grantClientId;

    private String remark;
}

