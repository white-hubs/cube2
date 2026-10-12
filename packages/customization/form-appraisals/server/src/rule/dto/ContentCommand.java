package com.guhecloud.cube.srv.gen.appraisals.server.rule.dto;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalContentTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalSourceTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 考核规则内容命令对象
 * 用于创建或更新考核规则的内容部分
 */
@Data
public class ContentCommand {
    /**
     * 内容 ID（更新时使用）
     */
    private Long id;
    /**
     * 内容类型（如检查项、评分项等）
     */
    @NotNull(message = "内容类型不能为空")
    private AppraisalContentTypeEnum contentType;
    /**
     * 来源类型（数据来源的类型）
     */
    @NotNull(message = "来源类型不能为空")
    private AppraisalSourceTypeEnum sourceType;
    /**
     * 底稿关联 ID（关联的外部数据 ID）
     */
    @NotBlank(message = "底稿关联不能为空")
    private String sourceRefId;
    /**
     * 底稿关联 ID（关联的外部数据 ID）
     */
    private String sourceRefCode;
    /**
     * 底稿关联名称
     */
    private String sourceRefName;
    /**
     * 内容描述
     */
    @NotBlank(message = "内容描述不能为空")
    private String contentDesc;
    /**
     * 标准描述（评分标准说明）
     */
    private String standardDesc;
    /**
     * 排序号
     */
    private Integer sort;
    /**
     * 规则项列表
     */
    @NotNull(message = "规则项不能为空")
    private List<ItemCommand> items;
}
