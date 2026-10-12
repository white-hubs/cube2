package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;

import java.util.List;

/**
 * 通过计划创建的任务巡检提交
 */
@Data
public class InspectTaskRecordSubmitCommand {

    /**
     * App 端单项操作幂等标识。为空时按老在线提交逻辑处理。
     */
    private String clientOperationId;

    /**
     * App 缓存记录时看到的服务端 revision。
     */
    private Long recordRevision;

    /**
     * 记录id
     */
    private Long recordId;

    /**
     * 巡检对象 id。用于离线提交诊断和前后端对账。
     */
    private Long targetId;

    /**
     * 巡检标准
     */
    private List<InspectRecordItemCommandModel> commandModels;
}
