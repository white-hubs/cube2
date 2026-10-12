package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 工程进度VO
 * @date 2023/9/14 19:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProjectScheduleVO {
    /**
     * 进度id
     */
    private Long id;
    /**
     * 工程编号
     */
    private String projectNum;
    /**
     * 小散工程名称
     */
    private String projectName;
    /**
     * 乙方
     */
    private String partyB;
    /**
     * 完成时限
     */
    private LocalDateTime completionTime;
    /**
     * 监管部门
     */
    private String depName;
    /**
     * 监管负责人
     */
    private String superviseUserName;
    /**
     * 监管负责人Id
     */
    private String userId;
    /**
     * 工程状态
     */
    private String status;
    /**
     * 工程状态枚举Code
     */
    private String statusCode;
    /**
     * 完成进度情况
     */
    private Integer schedule;

    /**
     * 工程类型
     */
    private Long projectType;
    /**
     * 工期
     */
    private String duration;
    /**
     * 工程类型名称
     */
    private String projectTypeName;
    /**
     * 施工地点
     */
    private String constructionSite;
    /**
     * 验收人
     */
    private String acceptanceUserName;
    /**
     * 验收时间
     */
    private LocalDateTime acceptanceTime;
    /**
     * 验收结果
     */
    private String acceptanceResult;
}
