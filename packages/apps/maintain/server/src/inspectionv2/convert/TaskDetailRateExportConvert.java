package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.convert;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.TaskDetailRateExportDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 巡检任务详情巡检项列表导出转换器
 */
public class TaskDetailRateExportConvert {

    private TaskDetailRateExportConvert() {}

    public static List<TaskDetailRateExportDTO> convert(List<InspectRecordVO> records) {
        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>();
        }
        List<TaskDetailRateExportDTO> result = new ArrayList<>();
        for (InspectRecordVO record : records) {
            TaskDetailRateExportDTO dto = new TaskDetailRateExportDTO();
            dto.setTaskName(record.getTaskName());
            dto.setTargetName(record.getTargetName());
            dto.setItemName(record.getItemName());
            dto.setResult(resolveResult(record.getResult()));
            dto.setInspectorName(record.getNickname());
            dto.setInspectionTime(record.getTime());
            dto.setRemark(record.getRemark());
            result.add(dto);
        }
        return result;
    }

    private static String resolveResult(String result) {
        if (result == null || result.isEmpty()) {
            return "";
        }
        try {
            return InspectRecordResultEnum.getDescription(result);
        } catch (IllegalArgumentException exception) {
            return result;
        }
    }
}
