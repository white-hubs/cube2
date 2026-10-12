package com.guhecloud.cube.srv.gen.maintain.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 14:53
 * @Description:
 */
@Data
public class InspectionRecordNumMetricDTO {

    /**
     * 巡检次数
     */
    private Integer inspectionRecordNum;

    /**
     * 巡检次数最高的五个人
     */
    private List<InspectionRecordUserNumVO> topInspectUser;

    /**
     * 巡检次数最低的五个人
     */
    private List<InspectionRecordUserNumVO> bottomInspectUser;
}
