package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.StandardTreeVO;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

/**
 * 报表提交对象
 */
@Data
public class DetectionReportCommandParam {
    /**
     * 标准项目树形集合
     */
    private List<StandardTreeVO> list;
    /**
     * 设备id集合
     */
    private List<Long> deviceIdList;
    /**
     * 时间段
     */
    private Period period;
}
