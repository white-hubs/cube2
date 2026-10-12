package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 巡检档案详情返回对象
 */
@Data
public class RecordDetailResult {
    private Long id;
    /**
     * 对象id
     */
    private Long targetId;
    /**
     * 对象名称
     */
    private String targetName;

    /**
     * 对象类型
     */
    private InspectTargetCategoryEnum targetCategory;

    /**
     * 所在区域
     */
    private String areaName;

    /**
     * 所属机构名称
     */
    private String organizationName;

    /**
     * 巡检人名称
     */
    private String nickname;

    /**
     * 最后一次巡检时间
     */
    private LocalDateTime time;

    /**
     * 巡检结果
     */
    private InspectRecordResultEnum result;

    /**
     * 日常巡检次数
     */
    private Long dailyNum;

    /**
     * 巡检结果异常次数
     */
    private Long dailyErrNum;

    /**
     * 日常巡检记录
     */
    private List<InspectRecordVO> dailyList;

    /**
     * 临时巡检次数
     */
    private Long temporaryNum;

    /**
     * 临时巡检结果异常数
     */
    private Long temporaryErrNum;

    /**
     * 临时巡检记录
     */
    private List<InspectRecordVO> temporaryList;

    /**
     * 异常描述
     */
    private List<String> errorDesc;
}
