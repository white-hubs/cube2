package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.maintain;

import com.guhecloud.cube.srv.gen.maintain.dto.InspectionRecordUserNumVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/3 14:59
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeadInspectionRecordNumMetricDTO {

    /**
     * 巡检次数
     */
    private Long inspectionRecordNum;

    /**
     * 巡检次数最高的五个市场
     */
    private List<InspectionRecordUserNumVO> topInspectMarket;

    /**
     * 巡检次数最低的五个市场
     */
    private List<InspectionRecordUserNumVO> bottomInspectMarket;

    /**
     * 巡检次数最高的五个人
     */
    private List<InspectionRecordUserNumVO> topInspectUser;

    /**
     * 巡检次数最低的五个人
     */
    private List<InspectionRecordUserNumVO> bottomInspectUser;
}
