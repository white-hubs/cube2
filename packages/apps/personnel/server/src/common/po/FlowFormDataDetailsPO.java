package com.guhecloud.cube.srv.gen.personnel.management.server.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author lijie
 * @date 2023/04/28
 * @description 流程表单数据详情表
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("flow_form_data_details")
@EqualsAndHashCode(callSuper = false)
public class FlowFormDataDetailsPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;
    /**
     * 轮岗审批id
     */
    private Long flowFormDataId;
    /**
     * 伦岗员工id
     */
    private Long staffId;
    /**
     * 现所在部门id
     */
    private Long organizationId;
    /**
     * 轮岗部门id
     */
    private Long rotationOrganizationId;
    /**
     * 轮岗时间
     */
    private String rotationTime;
}
