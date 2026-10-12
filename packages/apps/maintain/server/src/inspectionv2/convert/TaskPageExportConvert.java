package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.convert;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.TaskPageExportDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskDetailResult;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 巡检任务分页导出转换器
 */
public class TaskPageExportConvert {
    private static final BigDecimal PERCENT_SCALE = BigDecimal.valueOf(100);

    private TaskPageExportConvert() {}

    public static List<TaskPageExportDTO> convert(List<TaskDetailResult> records) {
        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>();
        }
        List<TaskPageExportDTO> result = new ArrayList<>();
        for (TaskDetailResult record : records) {
            TaskPageExportDTO dto = new TaskPageExportDTO();
            dto.setTaskName(record.getName());
            dto.setTypeName(record.getTypeName());
            dto.setTaskType(resolveTaskType(record.getIsCheck()));
            dto.setExecutorName(record.getReceiveNames());
            dto.setPublisherName(record.getPublisherName());
            dto.setPublishTime(record.getCreateTime());
            dto.setValidEndTime(record.getValidEndTime());
            dto.setTargetNum(record.getTargetNum());
            dto.setInspectProgress(formatRate(record.getRecordCompletionRate()));
            dto.setStatus(resolveStatus(record.getStatus()));
            dto.setErrCount(record.getErrCount());
            dto.setRepairCount(record.getRepairCount());
            result.add(dto);
        }
        return result;
    }

    private static String resolveTaskType(Boolean isCheck) {
        return Boolean.TRUE.equals(isCheck) ? "稽查任务" : "普通任务";
    }

    private static String resolveStatus(String status) {
        String description = InspectTaskStatusEnum.getDescription(status);
        return description == null || description.isEmpty() ? status : description;
    }

    private static String formatRate(double rate) {
        return BigDecimal.valueOf(rate)
                .multiply(PERCENT_SCALE)
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString() + "%";
    }
}
