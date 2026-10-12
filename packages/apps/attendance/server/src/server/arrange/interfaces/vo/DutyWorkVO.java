package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/24
 * @description 值班
 */
@Data
public class DutyWorkVO {

    private Long id;

    /**
     * 员工ID
     */
    private Long staffId;

    /**
     * 员工姓名
     */
    private String staffName;

    //部门名称
    private String deptName;
    //考勤组名称
    private String groupName;

    /**
     * 员工电话
     */
    private String staffMobile;

    /**
     * 每日排班数据
     */
    private List<DateOfClasses> dayOfClasses;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DateOfClasses {

        public DateOfClasses(LocalDateTime date) {
            this.date = date;
        }

        /**
         * 月份
         */
        private String month;

        /**
         * 周几
         */
        private Integer week;

        /**
         * 日期
         */
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime date;

        /**
         * 班次信息
         */
        private List<Classes> classesVOS;

    }

}