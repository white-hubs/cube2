package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InspectRecordQueryParam extends Paging {

    private Long id;

    /**
     * 任务IDS
     */
    private String taskIds;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 目标类别
     */
    private String targetCategory;

    /**
     * 对象id
     */
    private Long targetId;

    /**
     * 记录类型，0||null:全部记录，1日常记录，2临时记录
     */
    private Integer temporary;

    /**
     * 巡检结果
     */
    private String result;

    /**
     * 处理人
     */
    private Long userId;

    /**
     * 记录状态
     */
    private InspectRecordStatusEnum status;

    /**
     * 日期类型
     */
    private String dateType;

    /**
     * 查询时间
     */
    private Period period;

    /**
     * 区域id
     */
    private String areaId;

    /**
     * 组织机构ID
     */
    private Long organizationId;

    /**
     * 查询修复对象
     */
    private Boolean repairBool;

    /**
     * 修复状态
     */
    private String repairStatus;

    /**
     * 时间段开始
     */
    private String timeStart;

    /**
     * 时间段结束
     */
    private String timeEnd;
}
