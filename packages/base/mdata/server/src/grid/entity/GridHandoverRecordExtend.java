package com.guhecloud.cube.biz.mdata.server.grid.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网格交接记录扩展信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "grid_handover_record_extend")
public class GridHandoverRecordExtend extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 网格交接记录ID
     */
    private Long handoverId;

    private String attInfo1;

    private Long file1;

    private String attInfo2;

    private Long file2;

    private String attInfo3;

    private Long file3;

    private String attInfo4;

    private Long file4;
}
