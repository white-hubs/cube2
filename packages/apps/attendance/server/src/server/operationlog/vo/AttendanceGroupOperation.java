package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 考勤组管理操作日志记录字段
 * @date 2023/6/14 9:21
 */
@Data
public class AttendanceGroupOperation {
    private Long id;
    @DiffLogField(name = "名称")
    private String name;
    @DiffLogField(name = "考勤组类型")
    private String type;
    @DiffLogField(name = "适用范围")
    private String ranges;
    @DiffLogField(name = "考勤类型")
    private String classesTypeName;
    @DiffLogField(name = "是否需要打卡", function = "BOOLEAN")
    private Boolean isPunchCard;
    @DiffLogField(name = "是否需要补卡", function = "BOOLEAN")
    private Boolean isSupplementCard;
    @DiffLogField(name = "是否允许加班", function = "BOOLEAN")
    private Boolean isWorkOvertime;
    @DiffLogField(name = "节假日自动排班", function = "BOOLEAN")
    private Boolean autoRest;

    @DiffLogField(name = "适用人员", function = "DIFF_LIST")
    private List<String> applicablePersonnel;
    @DiffLogField(name = "班次选择", function = "DIFF_LIST")
    private List<String> classes;
    @DiffLogField(name = "打卡方式", function = "DIFF_LIST")
    private List<String> punchCardWayStrList;
    @DiffLogField(name = "考勤地点", function = "DIFF_LIST")
    private List<String> attendancePlace;
    @DiffLogField(name = "考勤机", function = "DIFF_LIST")
    private List<String> attendanceMachine;
    @DiffLogField(name = "补卡规则", function = "DIFF_LIST")
    private List<String> supplementCardRule;
    @DiffLogField(name = "加班规则", function = "DIFF_LIST")
    private List<String> overtimeRule;
}
