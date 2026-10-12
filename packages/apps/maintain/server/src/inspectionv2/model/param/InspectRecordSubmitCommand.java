package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/13 17:47
 * @Description: 执行巡检几条指令
 */

@Data
public class InspectRecordSubmitCommand {
    /**
     * 对象类型
     */
    private String targetCategory;
    /**
     * 对象id
     */
    private Long targetId;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 巡检对象名称
     */
    private String targetName;

    /**
     * 巡检标准
     */
    private List<InspectRecordItemCommandModel> commandModels;

    private List<InspectRecordSubmitCommand> recordSubmitCommandList;
}
