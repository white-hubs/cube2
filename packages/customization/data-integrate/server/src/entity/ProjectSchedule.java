package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/13 10:58
 * @Description:
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("loose_works")
public class ProjectSchedule extends BasicEnhancedTenantEntity<Long> {

    //id
    private Long id;
    //工程编号
    private String projectNum;
    //小散工程名称
    private String projectName;
    //乙方
    private String partyB;
    //完成时限
    private LocalDateTime completionTime;
    //监管负责人
    private Long superviseUser;
    //监管负责人名称
    private String superviseUserName;
    //监管部门
    private Long superviseDep;
    //监管部门名称
    private String superviseDepName;
    //工程状态
    private String status;
    //工程是否存在问题
    private Boolean hasProblem;
    //完成进度
    private Integer schedule;
    //市场租户
    private Integer marketTenant;
    //市场名称
    private String marketName;

}
