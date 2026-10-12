package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 抄表周期
 * @date 2022-07-04 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeterReadeCycle extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //执行时间
    private String workTime;
    //周期时间
    private String readeVulue;
    //抄表周期
    private String readeType;
    //任务id
    private String taskId;

}