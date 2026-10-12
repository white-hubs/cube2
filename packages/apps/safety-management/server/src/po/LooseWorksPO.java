package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 小散工程
 * @date 2023/9/19 15:18
 */
@Data
@TableName("loose_works")
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LooseWorksPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 工程编号
     */
    private String projectNum;
    /**
     * 小散工程名称
     */
    private String projectName;
    /**
     * 乙方
     */
    private String partyB;
    /**
     * 完成时限
     */
    private LocalDateTime completionTime;
    /**
     * 监管负责人
     */
    private Long superviseUser;
    /**
     * 监管负责人名称
     */
    private String superviseUserName;
    /**
     * 监管部门
     */
    private Long superviseDep;
    /**
     * 监管部门名称
     */
    private String superviseDepName;
    /**
     * 工程状态
     */
    private String status;
    /**
     * 工程是否存在问题
     */
    private Boolean hasProblem;
    /**
     * 完成进度
     */
    private Integer schedule;
    /**
     * 所属市场
     */
    private Integer marketTenant;
    private String marketName;
}
