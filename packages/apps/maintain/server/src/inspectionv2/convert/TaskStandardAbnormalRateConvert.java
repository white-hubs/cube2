package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.convert;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.TaskStandardAbnormalRateExportDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TaskStandardAbnormalRateVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务标准项异常率转换
 */
public class TaskStandardAbnormalRateConvert {
    private TaskStandardAbnormalRateConvert() {}

    public static List<TaskStandardAbnormalRateExportDTO> convertExport(List<TaskStandardAbnormalRateVO> records) {
        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>();
        }
        List<TaskStandardAbnormalRateExportDTO> result = new ArrayList<>();
        for (TaskStandardAbnormalRateVO record : records) {
            TaskStandardAbnormalRateExportDTO dto = new TaskStandardAbnormalRateExportDTO();
            dto.setTargetName(record.getTargetName());
            dto.setNickname(record.getNickname());
            dto.setUpdateTime(record.getUpdateTime());
            dto.setStandardName(record.getStandardName());
            dto.setTaskName(record.getTaskName());
            dto.setTotalItemCount(record.getTotalItemCount());
            dto.setAbnormalItemCount(record.getAbnormalItemCount());
            dto.setAbnormalRate(record.getAbnormalRate());
            result.add(dto);
        }
        return result;
    }
}
