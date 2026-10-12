package com.guhecloud.cube.srv.gen.loose.works.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.loose.works.filter.ProjectScheduleDeptFieldFilter;
import com.guhecloud.cube.srv.gen.loose.works.filter.ProjectScheduleDeptFieldParamConvert;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.annotation.DataPermissionMoreField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.guhecloud.platform.database.enumeration.ConditionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 工程进度
 * @date 2023/9/14 17:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("project_schedule")
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", fields = {
                @DataPermissionMoreField(field = "supervise_users", condition = ConditionEnum.LIKE),
                @DataPermissionMoreField(field = "start_user", condition = ConditionEnum.EQ)
        }
        ),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "supervise_dep",
                             condition = ConditionEnum.LIKE,
                             fieldFilter = ProjectScheduleDeptFieldFilter.class
        ),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "supervise_dep",
                             condition = ConditionEnum.LIKE,
                             fieldParamConvert = ProjectScheduleDeptFieldParamConvert.class
        )
}
)
public class ProjectSchedulePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 请示id
     */
    private Long applyId;
    /**
     * 签约id
     */
    private Long contractId;
    /**
     * 发起人
     */
    private Long startUser;
    /**
     * 工单编号
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
     * 验收通过时间
     */
    private LocalDateTime overTime;
    /**
     * 监管负责人
     */
    private String superviseUsers;
    /**
     * 监管负责人名称
     */
    private String superviseUserName;
    /**
     * 监管部门
     */
    private String superviseDep;
    /**
     * 工程状态
     */
    private String status;
    /**
     * 进度
     */
    private Integer schedule;
    /**
     * 施工地点
     */
    private String constructionSite;

    /**
     * 类型
     */
    private Long typeId;
    private String remarks;
    private String projectFiles;

    /**
     * 工期
     */
    private String duration;
}
