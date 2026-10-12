package com.guhecloud.cube.srv.gen.maintain.server.repair.dto;

import annotation.FormField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 委外维修2.0
 * @date 2024/1/8 14:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OutsourceWorkOrderDTO {
    @FormField(label = "维修对象")
    private Long[] outsourceObj;
    @FormField(label = "维修理由")
    private String reason;
    @FormField(label = "附件图片")
    private String attachment;
    @FormField(label = "确认结果")
    private String identifyResult;
    @FormField(label = "确认描述")
    private String identifyRemarks;
    @FormField(label = "委外公司")
    private String companyId;
    @FormField(label = "委外模式")
    private String typeId;
    @FormField(label = "处理结果")
    private String handleResult;
    @FormField(label = "处理描述")
    private String handleRemarks;
    @FormField(label = "效果评估")
    private String appraise;
    @FormField(label = "评估描述")
    private String appraiseRemarks;
    @FormField(label = "评估附件")
    private String appraiseAttachment;
}
