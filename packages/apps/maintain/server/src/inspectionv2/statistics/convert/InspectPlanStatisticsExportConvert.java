package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.convert;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.dto.InspectPlanStatisticsExportDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.vo.InspectPlanStatisticsVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class InspectPlanStatisticsExportConvert {

    private InspectPlanStatisticsExportConvert() {}

    public static List<InspectPlanStatisticsExportDTO> convert(List<InspectPlanStatisticsVO> records) {
        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>();
        }
        List<InspectPlanStatisticsExportDTO> result = new ArrayList<>(records.size());
        for (InspectPlanStatisticsVO record : records) {
            InspectPlanStatisticsExportDTO dto = new InspectPlanStatisticsExportDTO();
            dto.setName(record.getName());
            dto.setTypeName(record.getTypeName());
            dto.setTaskTotal(record.getTaskTotal());
            dto.setCompletedTaskTotal(record.getCompletedTaskTotal());
            dto.setPendingTaskTotal(record.getPendingTaskTotal());
            dto.setObjectTotal(record.getObjectTotal());
            dto.setInspectedObjectTotal(record.getInspectedObjectTotal());
            dto.setPendingObjectTotal(record.getPendingObjectTotal());
            dto.setAbnormalItemTotal(record.getAbnormalItemTotal());
            dto.setUnhandledAbnormalItemTotal(record.getUnhandledAbnormalItemTotal());
            dto.setHandledAbnormalItemTotal(record.getHandledAbnormalItemTotal());
            result.add(dto);
        }
        return result;
    }
}
