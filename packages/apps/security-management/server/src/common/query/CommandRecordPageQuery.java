package com.guhecloud.cube.srv.gen.securitymanagement.server.common.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * 获取指令下发记录列表的查询参数
 */
@Data
public class CommandRecordPageQuery extends Paging {
    /**
     * 模块应用
     */
    private String applicationCode;
    /**
     * 任务类型
     */
    private String taskType;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 发送状态 0:成功，1：失败
     */
    private Integer sendStatus;
}
