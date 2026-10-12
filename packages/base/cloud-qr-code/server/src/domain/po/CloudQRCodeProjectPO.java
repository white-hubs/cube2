package com.guhecloud.cube.base.cloud.qr.code.server.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("cloud_qr_code_project")
public class CloudQRCodeProjectPO extends BasicEntity<Long> {

    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目编号
     */
    private String code;

    /**
     * 项目密钥
     */
    private String secret;
}
