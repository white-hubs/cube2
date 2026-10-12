package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 稽查结果匹配率
 * @date 2023/4/18 15:52
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckResultMatchVO {
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务发布时间
     */
    private String releaseTime;
    /**
     * 责任人
     */
    private String dutyOfficer;
    /**
     * 稽查结果匹配率
     */
    private String matchRatio;
    /**
     * 不匹配项数
     */
    private Integer notMatchNum;
}
