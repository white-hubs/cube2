package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 16:54
 * @Description:
 */
@Data
public class InspectionCommonParam {

    /**
     * 时间
     */
    private Period period;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 计划id集合
     */
    private List<Long> planIdList;

    /**
     * 巡检对象
     */
    private String areaName;

    /**
     * 对象类型
     */
    private String objectType;

    /**
     * 项名称
     */
    private String itemName;

    /**
     * 是否超时
     */
    private Boolean overTimeBool;

    /**
     * 状态
     */
    private String status;

    /**
     * 状态集合
     */
    private List<String> statusList;

}
