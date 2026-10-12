package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmergencyFileSafetyReportVO {

    private Long totalNum;

    private List<EmergencyFileEventTypeVO> emergencyFileEventTypeList;

    private List<EmergencyFileEventTypeVO1> noSetEmergencyFileList;
}
