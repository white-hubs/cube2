package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordVO;
import lombok.Data;

import java.util.List;

/**
 * 任务详情-执行进度返回对象
 */
@Data
public class DetailRateResult {

    /**
     * 任务进度列表
     */
    List<TaskRateResult> taskRateResults;

    Long total;
    /**
     * 巡检结果列表
     */
    List<InspectRecordVO> recordTaskResultList;

}
