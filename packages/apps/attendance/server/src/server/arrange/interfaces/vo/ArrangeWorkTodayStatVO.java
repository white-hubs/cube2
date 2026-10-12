package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author bq
 * @date 2023/03/24
 * @description 统计今日当班人员
 */
@Data
public class ArrangeWorkTodayStatVO {

    private String statNum;

    private List<Staff> staffList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Staff {

        private Long id;

        private Long userId;

        private String name;

        private String mobile;

        private String className;

        private Boolean onDuty;

        private Integer tenant;

    }

}