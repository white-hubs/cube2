package com.guhecloud.cube.srv.gen.personnel.management.server.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2023/02/17
 * @description 流程表单数据公共业务表
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("flow_form_data")
@EqualsAndHashCode(callSuper = false)
public class FlowFormDataPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 申请人ID
     */
    private Long applicantId;

    /**
     * 申请人姓名
     */
    private String applicantName;

    /**
     * 申请人部门ID
     */
    private Long applicantOrganizationId;

    /**
     * 申请人部门名称
     */
    private String applicantOrganizationName;

    /**
     * 申请人所在岗位ID
     */
    private String applicantPostId;

    /**
     * 申请人所在岗位名称
     */
    private String applicantPostName;

    /**
     * 申请人所在考勤组Id
     */
    private Long attendanceGroupId;

    /**
     * 申请业务类型（如招聘、转正、离职等）
     */
    private String businessType;

    /**
     * 表单json
     */
    private String jsonForm;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 处理时间
     */
    private LocalDateTime applyDoneTime;

    /**
     * 审批状态
     */
    private String status;

    /**
     * 是否更新成功业务
     */
    private Boolean updateBusiness;

}