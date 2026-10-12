package com.guhecloud.cube.srv.gen.maintain.server.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 14:53
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectionRecordNumMetricDTO {

    /**
     * 巡检次数
     */
    private Long inspectionRecordNum;

    /**
     * 巡检次数最高的五个人
     */
    private List<InspectionRecordUserNumVO> topInspectUser;

    /**
     * 巡检次数最低的五个人
     */
    private List<InspectionRecordUserNumVO> bottomInspectUser;
}
