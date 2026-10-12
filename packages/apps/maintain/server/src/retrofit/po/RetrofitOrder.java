package com.guhecloud.cube.srv.gen.maintain.server.retrofit.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 更新改造2.0
 *
 * @author lanchun
 * @date 2024/1/8 15:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetrofitOrder extends BasicEnhancedTenantEntity<Long> {

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
     * 改造对象id
     */
    private Long objId;

    /**
     * 改造对象名称
     */
    private String objName;

    /**
     * 申报理由
     */
    private String reason;

    /**
     * 期望解决日期
     */
    private LocalDateTime expectedResolutionDate;

    /**
     * 附件图片
     */
    private String attachment;

    /**
     * 改造前状态
     */
    private String retrofitBeforeStatus;

    /**
     * 确认结果
     */
    private String identifyResult;

    /**
     * 确认描述
     */
    private String identifyRemarks;

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

    /**
     * 改造后状态
     */
    private String retrofitAfterStatus;
}
