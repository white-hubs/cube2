package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class BroadcastPlayRecordQuery extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100个字符")
    private String keyword;

    /**
     * 广播模板主键
     */
    private Long templateId;

    /**
     * 时间范围参数
     */
    private Period period;
}
