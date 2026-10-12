package com.guhecloud.cube.biz.todo.server.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 待办任务新增参数
 *
 * @author lanchun
 * @date 2022/7/23 17:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoTaskModifyParam {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务时间
     */
    private LocalDateTime taskTime;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 业务主键
     */
    private String businessKey;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务类型名称
     */
    private String businessTypeName;

    /**
     * 业务状态
     */
    private String businessStatus;

    /**
     * 业务状态名称
     */
    private String businessStatusName;

    /**
     * 跳转地址
     */
    private String url;

    /**
     * 用户主键集合
     */
    private List<Long> userIds;

    /**
     * 业务实体信息
     */
    private String businessPrincipal;
}
