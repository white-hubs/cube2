package com.guhecloud.cube.property.management.client.property.param.property;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PropertyWeedOutPageParam extends Paging {
    /**
     * 状态
     */
    private String status;
    /**
     * 清理日期
     */
    private LocalDateTime manageTime;
    /**
     * 清理原因
     */
    private Long causeId;
    /**
     * 资产类别
     */
    private Long typeId;
    /**
     * 资产名称、编码
     */
    private String keyword;
}
