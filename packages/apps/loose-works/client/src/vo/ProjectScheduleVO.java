package com.guhecloud.cube.srv.gen.loose.works.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/8 14:18
 * @Description:
 */

@Data
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
     * 工程地点
     */
    private String address;

    /**
     * 工程类型
     */
    private Long projectType;
    /**
     * 工程类型名称
     */
    private String projectTypeName;
    /**
     * 标签ID
     */
    private List<Long> tagIds;
    /**
     * 标签名称
     */
    private String tagNames;
    private String name;
    private String typeId;
    private String typeName;
    private List<Long> departmentIds;
    private String departmentNames;
    private String areaId;
    private String areaName;
}
