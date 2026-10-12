package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 巡检任务分页入参对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskPageParam extends Paging {

    /**
     * id
     */
    private Long id;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 对象类型id
     */
    private String typeId;

    /**
     * 对象类别
     */
    private String targetCategory;

    /**
     * 任务状态
     */
    private String status;
    /**
     * 任务结果
     */
    private String result;

    /**
     * 日期类型
     */
    private String dateType;

    /**
     * 查询时间
     */
    private Period period;

    private Long planId;

    private Long taskId;
    private Long targetId;

    /**
     * 接收部门
     */
    private String depId;

    /**
     * 是否是拆解任务
     */
    private boolean isSplit;

    /**
     * 超时条件(NO_TIMEOUT未超时，TIMEOUT超时)
     */
    private String taskTimeout;

    private List<Long> taskIdList;
}
