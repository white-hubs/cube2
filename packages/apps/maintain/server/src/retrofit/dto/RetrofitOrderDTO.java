package com.guhecloud.cube.srv.gen.maintain.server.retrofit.dto;

import annotation.FormField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 更新改造2.0 关注的表单内容
 *
 * @author lanchun
 * @date 2024/1/8 16:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RetrofitOrderDTO {

    @FormField(label = "更新改造对象")
    private Long[] retrofitObj;

    /**
     * 申报理由
     */
    @FormField(label = "申报理由")
    private String reason;

    /**
     * 期望解决日期
     */
    @FormField(label = "期望解决日期")
    private LocalDateTime expectedResolutionDate;

    /**
     * 附件图片
     */
    @FormField(label = "附件图片")
    private String attachment;

    /**
     * 改造前状态
     */
    @FormField(label = "改造前状态")
    private String retrofitBeforeStatus;

    /**
     * 确认结果
     */
    @FormField(label = "确认结果")
    private String identifyResult;

    /**
     * 处理结果
     */
    @FormField(label = "处理结果")
    private String handleResult;

    /**
     * 改造后状态
     */
    @FormField(label = "改造后状态")
    private String retrofitAfterStatus;
}
