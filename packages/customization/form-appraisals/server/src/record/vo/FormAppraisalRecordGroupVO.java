package com.guhecloud.cube.srv.gen.appraisals.server.record.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 考核记录分组分页节点 VO
 */
@Data
public class FormAppraisalRecordGroupVO {
    /**
     * 分组 ID
     */
    private Long formGroupId;
    /**
     * 分组名称
     */
    private String formGroupName;
    /**
     * 分组层级
     */
    private Integer level;
    /**
     * 子分组
     */
    private List<FormAppraisalRecordGroupVO> children = new ArrayList<>();
    /**
     * 考核记录列表
     */
    private List<FormAppraisalRecordPageVO> records = new ArrayList<>();
}
