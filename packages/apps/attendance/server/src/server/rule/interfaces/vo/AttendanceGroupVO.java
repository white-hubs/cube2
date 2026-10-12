package com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo;

import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceTypeEnum;
import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤组
 */
@Data
public class AttendanceGroupVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 考勤组类型（排班制、值班制）
     */
    private String type;

    /**
     * 考勤组类型名称
     */
    private String typeName;

    /**
     * 适用范围(所有人、部分人)
     */
    private String ranges;

    /**
     * 班次类型（固定班制、排班制）
     */
    private String classesType;

    /**
     * 考勤类型name
     */
    private String classesTypeName;

    /**
     * 考勤组人数
     */
    private long num;

    /**
     * 考勤时间
     */
    private String dateStr;

    /**
     * 是否需要打卡
     */
    private Boolean isPunchCard;

    /**
     * 是否允许补卡
     */
    private Boolean isSupplementCard;

    /**
     * 是否允许加班
     */
    private Boolean isWorkOvertime;

    /**
     * 版本（1：草稿，2：正式）
     */
    private Integer version;

    /**
     * 节假日自动排休
     */
    private Boolean autoRest;

    /**
     * 打卡方式
     */
    private List<String> punchCardWay;

    /**
     * 打卡方式字符串
     */
    private String punchCardWayStr;
    /**
     * 包含班次
     */
    private String classesIds;
    /**
     * 包含班次集合
     */
    private List<ClassesVO> containClassesVOS;
    private List<AttendanceGroupStaffVO> staffVOS;

    private List<AttendanceGroupClassesVO> classesVOS;

    private List<AttendanceGroupPunchCardWayVO> punchCardWayWifi;

    private List<AttendanceGroupPunchCardWayVO> punchCardWayGps;

    private List<AttendanceGroupPunchCardWayVO> punchCardWayMachine;

    private List<AttendanceGroupSupplementCardRuleVO> supplementCardRuleVOS;

    private List<AttendanceGroupWorkOvertimeRuleVO> workOvertimeRuleVOS;

    public void setPunchCardWayStr(String punchCardWayStr) {
        this.punchCardWayStr = punchCardWayStr;
        if (StringUtils.isNotEmpty(punchCardWayStr)) {
            String[] split = punchCardWayStr.split(",");
            this.punchCardWay = Arrays.asList(split);
        } else {
            this.punchCardWay = Collections.emptyList();
        }
    }

    public void setType(String type) {
        this.type = type;
        if (StringUtils.isNotEmpty(type)) {
            this.typeName = AttendanceGroupTypeEnum.valueOf(type).getValue();
        }
    }

    public void setClassesType(String classesType) {
        this.classesType = classesType;
        if (StringUtils.isNotEmpty(classesType)) {
            this.classesTypeName = AttendanceTypeEnum.valueOf(classesType).getValue();
        }
    }

}