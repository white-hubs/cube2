package com.guhecloud.cube.srv.gen.maintain.server.repair.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 委外维修
 * @date 2024/1/8 15:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutsourceWorkOrder extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 编号
     */
    private String code;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 维修对象名称
     */
    private String objName;
    /**
     * 维修对象id
     */
    private Long objId;
    /**
     * 维修理由
     */
    private String reason;
    /**
     * 附件图片
     */
    private String attachment;
    /**
     * 确认结果
     */
    private String identifyResult;
    /**
     * 确认描述
     */
    private String identifyRemarks;
    /**
     * 委外公司
     */
    private Long companyId;
    /**
     * 委外模式
     */
    private Long typeId;
    /**
     * 处理结果
     */
    private String handleResult;
    /**
     * 处理描述
     */
    private String handleRemarks;
    /**
     * 效果评估
     */
    private String appraise;
    /**
     * 评估描述
     */
    private String appraiseRemarks;
    /**
     * 评估附件
     */
    private String appraiseAttachment;
}
