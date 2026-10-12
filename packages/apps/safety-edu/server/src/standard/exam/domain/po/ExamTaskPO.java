package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskPaperModeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.permission.CreateByCustomizeDeptFieldFilter;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.permission.CreateByLoginDeptFieldFilter;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.permission.CreateByLoginUserFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.Isolatable;
import com.guhecloud.platform.database.entity.enhanced.EnhancedCreatable;
import com.guhecloud.platform.database.entity.enhanced.EnhancedUpdatable;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 考试任务
 *
 * @author lanchun
 * @date 2023/4/19 11:47
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName(value = "exam_task")
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "create_by", fieldFilter = CreateByLoginUserFieldFilter.class)
        , @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "create_by", fieldFilter = CreateByLoginDeptFieldFilter.class)
        , @DataPermissionField(permission = "CUSTOM_DEPT", field = "create_by", fieldParamConvert = CreateByCustomizeDeptFieldFilter.class)
}
)
public class ExamTaskPO implements Isolatable<Long>, EnhancedCreatable<Long>, EnhancedUpdatable<Long> {

    private Long id;

    /**
     * 考试任务名称
     */
    private String name;

    /**
     * 主题主键
     */
    private Long themeId;

    /**
     * 主题名称
     */
    private String themeName;

    /**
     * 任务来源编号
     */
    private String sourceCode;
    /**
     * 任务来源名称
     */
    private String sourceName;

    /**
     * 任务来源键
     */
    private String sourceKey;

    /**
     * 考试开始时间
     */
    private LocalDateTime examStartTime;

    /**
     * 考试结束时间
     */
    private LocalDateTime examEndTime;

    /**
     * 负责人主键
     */
    private Long chargeUserId;

    /**
     * 负责人名称
     */
    private String chargeUserName;

    /**
     * 是否限时
     */
    private Boolean timed;

    /**
     * 考试时长
     */
    private Integer examDuration;

    /**
     * 考试时长单位
     */
    private String examDurationUnit;

    /**
     * 是否支持复考
     */
    private Boolean retake;

    /**
     * 复考次数
     */
    private Integer retakeNumber;

    /**
     * 试卷类型（统一题目 / 随机出题）
     */
    private ExamTaskPaperModeEnum examPaperMode;

    /**
     * 考试类型
     */
    private ExamTaskTypeEnum examTaskType;

    /**
     * 参加考试人数
     */
    private Integer examUserNumber;

    /**
     * 任务状态
     */
    private ExamTaskStatusEnum examTaskStatus;

    /**
     * 发布时间
     */
    private LocalDateTime publishedTime;

    /**
     * 考试说明
     */
    private String remark;

    /**
     * 市场租户编号(当使用方为集团时可用)
     */
    private Integer marketTenant;

    private Integer tenant;

    private Long deleted;

    private String createBy;

    private String creatorId;

    private ActorTypeEnum creatorType;

    private LocalDateTime createTime;

    private String updateBy;

    private String updaterId;

    private ActorTypeEnum updaterType;

    private LocalDateTime updateTime;
}
