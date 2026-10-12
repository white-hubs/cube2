package com.guhecloud.cube.biz.mdata.server.scene.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("universal_panel")
public class UniversalPanel extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 所属项目id
     */
    private Long projectId;
    private String tableOneName;
    private String tableOne;
    private String tableTwoName;
    private String tableTwo;
    private String tableThreeName;
    private String tableThree;
    private String items;
    private Long businessId;
}
