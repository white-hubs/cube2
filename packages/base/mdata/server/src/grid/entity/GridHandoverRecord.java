package com.guhecloud.cube.biz.mdata.server.grid.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.mdata.server.grid.enumeration.GridHandoverStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 网格交接记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "grid_handover_record")
public class GridHandoverRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    /**
     * 记录名称
     */
    private String recordName;

    /**
     * 网格区域id集合（逗号分隔）
     */
    private String gridAreaIds;

    /**
     * 交班人
     */
    private Long shiftOverUser;

    /**
     * 交班时间
     */
    private LocalDateTime shiftOverTime;

    /**
     * 接班人
     */
    private Long takeOverUser;

    /**
     * 接班时间
     */
    private LocalDateTime takeOverTime;

    /**
     * 状态
     */
    private GridHandoverStatusEnum status;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 交班人签名文件ID
     */
    private Long shiftSignFileId;

    /**
     * 接班人签名文件ID
     */
    private Long takeSignFileId;
}
