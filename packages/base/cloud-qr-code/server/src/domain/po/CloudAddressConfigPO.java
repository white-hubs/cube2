package com.guhecloud.cube.base.cloud.qr.code.server.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "cloud_address_config")
public class CloudAddressConfigPO extends BasicEntity<Long> {

    private Long id;

    /**
     * 域名地址
     */
    private String ipAddress;
}
