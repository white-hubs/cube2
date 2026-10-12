package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po;

import annotation.FormField;
import lombok.Data;

/**
 * @author bq
 * @date 2023/02/24
 * @description 考核审批表
 */
@Data
public class AssessPO {

    @FormField(label = "考核结果")
    private String assessResult;

    @FormField(label = "考核分数")
    private Integer assessScore;

    @FormField(label = "考核时间")
    private String assessTime;

}