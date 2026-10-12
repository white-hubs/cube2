package com.guhecloud.cube.property.management.client.risk.source.param;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  17:23
 * @Description:
 * @Version: 1.0
 */
@Data
public class PartsDetailsParam {
    private Long id;
    /**
     * 配件编号
     */
    private String code;
    /**
     * 配件名称
     */
    private String name;
    /**
     * 到期日期
     */
    private LocalDateTime expireDate;
    /**
     * 备注
     */
    private String remarks;

}
