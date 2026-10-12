package com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.application.dto;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import lombok.Data;

@Data
public class AttClockDTO {
    int noClockStartNum = AttConstants.zeroInt;
    int noClockEndNum = AttConstants.zeroInt;
    // 缺卡
    int noClockNum = AttConstants.zeroInt;
    // 矿工
    float skipWorkDay = AttConstants.zeroFloat;
    // 迟到
    int lateNum = AttConstants.zeroInt;
    // 早退
    int earlyNum = AttConstants.zeroInt;
    // 迟到分钟数
    float lateTotalTime = AttConstants.zeroFloat;
    // 早退分钟数
    float earlyTotalTime = AttConstants.zeroFloat;
}
