package com.guhecloud.cube.srv.gen.safety.personnel.server.person.param;

import lombok.Data;

import java.time.LocalDate;

/**
 * 安全履历查询参数。
 */
@Data
public class ResumeQueryParam {

    /**
     * 开始日期；可为空，为空时默认最近5天。
     */
    private LocalDate startDate;

    /**
     * 结束日期；可为空，为空时默认今天。
     */
    private LocalDate endDate;

    /**
     * 履职类型，patrol/alarm/approval/meeting/training/other；可为空表示全部类型。
     */
    private String type;
}
