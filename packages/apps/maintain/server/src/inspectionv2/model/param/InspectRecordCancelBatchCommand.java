package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;

import java.util.List;

@Data
public class InspectRecordCancelBatchCommand {
    /**
     * 记录id集合
     */
    private List<Long> recordIdList;
    /**
     * 处理内容，描述具体的内容
     */
    private String content;

    /**
     * 文件id集合
     */
    private List<Long> fileIdList;

    /**
     * 业务类型，标识该记录所属的业务场景
     */
    private String businessType;
}
