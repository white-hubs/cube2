package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wql
 * @describe 小散工程节点进度列表
 * @date 2023/9/22 15:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProjectNodeInfo {
    private Long id;
    /**
     * 项目id
     */
    private Long scheduleId;
    /**
     * 工程名称
     */
    private String projectName;
    /**
     * 工程编号
     */
    private String projectNum;
    /**
     * 工程状态
     */
    private String status;
    /**
     * 申请单流程实例id
     */
    private String processInsId;
    /**
     * 工程节点
     */
    private List<String> nodes;
    /**
     * 施工方
     */
    private String partyB;
    /**
     * 完成时限
     */
    private LocalDateTime completionTime;
    /**
     * 监管部门
     */
    private String superviseDepName;
    /**
     * 监管负责人
     */
    private List<String> superviseUserList;
}
