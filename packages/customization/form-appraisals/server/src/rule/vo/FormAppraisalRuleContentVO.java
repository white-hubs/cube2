package com.guhecloud.cube.srv.gen.appraisals.server.rule.vo;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalContentTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalSourceTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 考核规则内容 VO
 * 用于返回考核规则的内容信息
 */
@Data
public class FormAppraisalRuleContentVO {
    /**
     * 内容 ID
     */
    private Long id;
    /**
     * 内容类型
     */
    private AppraisalContentTypeEnum contentType;
    /**
     * 来源类型
     */
    private AppraisalSourceTypeEnum sourceType;
    /**
     * 来源引用 ID
     */
    private String sourceRefId;
    /**
     * 来源引用 code
     */
    private String sourceRefCode;
    /**
     * 来源引用名称
     */
    private String sourceRefName;
    /**
     * 内容描述
     */
    private String contentDesc;
    /**
     * 标准描述
     */
    private String standardDesc;
    /**
     * 排序号
     */
    private Integer sort;
    /**
     * 规则项列表
     */
    private List<FormAppraisalRuleItemVO> items;
}
