package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2023/01/05
 * @description app端值班表
 */
@Data
public class DutyWorkAppVO {

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date;

    /**
     * 员工列表
     */
    private List<Staff> staffList;

    @Data
    public static class Staff {

        /**
         * 员工ID
         */
        private Long staffId;

        /**
         * 员工姓名
         */
        private String staffName;

        /**
         * 是否是当前用户
         */
        private Boolean isCurrentUser;

        /**
         * 值班班次ID
         */
        private String dutyWorkClassesIds;

    }

}