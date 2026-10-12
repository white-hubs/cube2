package com.guhecloud.cube.biz.emergency.client.vo;

import lombok.Data;

import java.util.List;

@Data
public class EmergencyFileSafetyReportVO {

    private Long totalNum;

    private List<EmergencyFileEventTypeVO> emergencyFileEventTypeList;

    private List<EmergencyFileEventTypeVO1> noSetEmergencyFileList;
}
