package com.guhecloud.cube.base.dataintegrate.server.entity;

import lombok.Data;

import java.util.List;

/**
 * @author Appet
 * @Description 小散数据采集配置
 * @date 2023-09-13 15:08:36
 */
@Data
public class LooseWorks {
    /**
     * 启动的小散工程
     */
    private List<ProjectSchedule> projectScheduleList;

    /**
     * 工程整改单
     */
    private List<ProjectReform> projectReformList;

}