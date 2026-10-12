package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class InspectTaskQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 类型
     */
    private String typeId;

    /**
     * 任务类别
     */
    private String targetCategory;

    /**
     * 候选人
     */
    private Long userId;

    /**
     * 任务状态
     */
    private InspectTaskStatusEnum status;

    /**
     * 日期类型
     */
    private String dateType;

    /**
     * 临时巡检
     */
    private Boolean temp;

    /**
     * 查询时间
     */
    private Period period;

    // 任务-详情-对象的标准列表
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 对象id
     */
    private Long targetId;

    /**
     * 任务id集合
     */
    private List<Long> idList;

    /**
     * 记录的状态
     */
    private String recordStatus;
}
