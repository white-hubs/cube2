package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;

import java.util.List;

@Data
public class InspectRepairBatchCommand {

    /**
     * 修复状态
     */
    private String repairStatus;

    /**
     * 处理内容，描述具体的修复操作或处理过程
     */
    private String content;

    /**
     * 业务类型，标识该记录所属的业务场景
     */
    private String businessType;

    /**
     * 文件id集合
     */
    private List<Long> fileIdList;
    /**
     * 记录id集合
     */
    private List<Long> recordIdList;
    /**
     * 任务id集合
     */
    private List<Long> taskIdList;
}
