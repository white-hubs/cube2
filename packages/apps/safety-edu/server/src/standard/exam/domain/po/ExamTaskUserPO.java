package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.permission.SafetyEducationCurrentUserDeptFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 考试人员
 *
 * @author lanchun
 * @date 2023/4/19 14:38
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exam_task_user")
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "user_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "user_department_id",
                             fieldFilter = SafetyEducationCurrentUserDeptFieldFilter.class
        ),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "user_department_id")
}
)
public class ExamTaskUserPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 考试任务主键
     */
    private Long examTaskId;

    /**
     * 用户唯一身份标识
     */
    private String userIdentify;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 用户部门主键
     */
    private Long userDepartmentId;

    /**
     * 用户部门名称
     */
    private String userDepartmentName;

    /**
     * 用户岗位
     */
    private Long userPostId;

    /**
     * 用户岗位名称
     */
    private String userPostName;

    /**
     * 用户考试记录主键(目前取最高得分记录)
     */
    private Long userExamTaskRecordId;

    /**
     * 考试状态
     */
    private UserExamStatusEnum examStatus;

    /**
     * 考试次数
     */
    private Integer examNumber;

    /**
     * 是否通过考试
     */
    private Boolean passed;

    /**
     * 的分数(目前取最高得分)
     */
    private Float score;
}
