package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class CollectConfigure extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //周期
    private Integer timeCycle;
    //采集时间
    private String readeTime;
    //应用
    private String applicationCode;
    //配电任务id
    private String distributionTaskId;
    //用电任务id
    private String consumptionTaskId;

}
