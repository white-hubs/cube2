package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command;

import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;
import org.hibernate.validator.constraints.Length;
import com.guhecloud.platform.base.util.CollectionUtils;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * @author bq
 * @date 2022/11/14
 * @description 提交考勤组信息
 */
@Data
public class SubmitAttendanceGroupCommand {

    private Long id;

    /**
     * 名称
     */
    @NotEmpty(message = "考勤组名称不能为空")
    @Length(max = 20, message = "名称限制在20字内")
    private String name;

    /**
     * 考勤类型（排班制、值班制）
     */
    @NotEmpty(message = "考勤组类型必须选择")
    private String type;

    /**
     * 适用范围(所有人、部分人)
     */
    @NotEmpty(message = "适用范围必须选择")
    private String ranges;

    /**
     * 班次类型（固定班制、排班制）
     */
    private String classesType;

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
     * 节假日自动排休
     */
    private Boolean autoRest;

    /**
     * 打卡方式
     */
    private List<String> punchCardWay;

    /**
     * 包含班次
     */
    private String classesIds;

    /**
     * 打卡方式字符串
     */
    private String punchCardWayStr;

    private List<SubmitAttendanceGroupStaffCommand> staffPOS;

    private List<SubmitAttendanceGroupClassesCommand> classesPOS;

    private List<SubmitAttendanceGroupPunchCardWayCommand> punchCardWayWifi;

    private List<SubmitAttendanceGroupPunchCardWayCommand> punchCardWayGps;

    private List<SubmitAttendanceGroupPunchCardWayCommand> punchCardWayMachine;

    private List<SubmitAttendanceGroupSupplementCardRuleCommand> supplementCardRulePOS;

    private List<SubmitAttendanceGroupWorkOvertimeRuleCommand> workOvertimeRulePOS;

    public void setPunchCardWay(List<String> punchCardWay) {
        this.punchCardWay = punchCardWay;
        if (!CollectionUtils.isEmpty(punchCardWay)) {
            this.punchCardWayStr = String.join(",", punchCardWay);
        }
    }

    public void setPunchCardWayStr(String punchCardWayStr) {
        if (StringUtils.isEmpty(this.punchCardWayStr)) {
            this.punchCardWayStr = punchCardWayStr;
        }
    }
}
