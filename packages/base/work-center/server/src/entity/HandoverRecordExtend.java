package com.guhecloud.cube.biz.work.center.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 交班记录扩展信息
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HandoverRecordExtend extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 交接记录ID
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
