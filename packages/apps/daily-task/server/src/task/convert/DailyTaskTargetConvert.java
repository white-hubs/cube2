package com.guhecloud.cube.biz.daily.task.task.convert;

import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskTargetResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskTargetVO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetPO;
import com.guhecloud.cube.biz.daily.task.record.dto.DailyRecordTargetErrorDTO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/13 16:12
 * @Description:
 */
public class DailyTaskTargetConvert {
    public static List<DailyTaskTargetVO> convertToVO(List<DailyTaskTargetPO> dailyTaskTargetPOS) {
        if (CollectionUtils.isEmpty(dailyTaskTargetPOS)) {
            return new ArrayList<>();
        }
        List<DailyTaskTargetVO> dailyTaskTargetVOS = new ArrayList<>();
        for (DailyTaskTargetPO dailyTaskTargetPO : dailyTaskTargetPOS) {
            DailyTaskTargetVO dailyTaskTargetVO = new DailyTaskTargetVO();
            dailyTaskTargetVO.setId(dailyTaskTargetPO.getId());
            dailyTaskTargetVO.setTargetId(dailyTaskTargetPO.getTargetId());
            dailyTaskTargetVOS.add(dailyTaskTargetVO);
        }
        return dailyTaskTargetVOS;
    }

    public static DailyTaskTargetResult convertToResult(DailyTaskTargetPO record) {
        DailyTaskTargetResult result = new DailyTaskTargetResult();
        result.setId(record.getTargetId());
        result.setTaskId(record.getTaskId());
        result.setTargetName(record.getTargetName());
        result.setTargetType(record.getTargetCategory());
        result.setStatus(record.getStatus());
        result.setTargetCode(record.getTargetCode());
        result.setAreaName(record.getAreaName());
        result.setLocation(record.getLocation());
        result.setTargetLntAndLat(record.getTargetLntAndLat());
        result.setRuleId(record.getRuleId());
        return result;
    }

    public static void setRecordResult(DailyTaskTargetResult result,
                                       Map<String, DailyRecordTargetErrorDTO> recordDataMap
                                      ) {
        var recordResult = recordDataMap.getOrDefault(
                String.valueOf(result.getTaskId()) + result.getId(), new DailyRecordTargetErrorDTO());
        result.setExecutorId(recordResult.getExecutorId());
        result.setExecutorName(recordResult.getExecutorName());
        result.setExecutionTime(recordResult.getExecutionTime());
        result.setExecutionResult(recordResult.getExecutionResult());
        result.setExceptionDetail(recordResult.getTargetErrorNames());
        result.setRecordId(recordResult.getRecordId());
    }
}
