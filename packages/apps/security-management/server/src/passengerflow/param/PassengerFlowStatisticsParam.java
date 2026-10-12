package com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

@Data
public class PassengerFlowStatisticsParam {
    /**
     * 时间类型 TODAY:'今日',THIS_WEEK:'本周', THIS_MONTH:'本月',THIS_YEAR:'今年'
     */
    private String type;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 时间范围
     */
    private Period period;

    /**
     * 报警对象编号
     */
    private String alarmTargetCode;

    /**
     * 设备主键集合
     */
    private List<Long> deviceIds;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 应用编号
     */
    private String applicationCode;
}
