package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 假期管理
 * @date 2023/6/14 13:45
 */
@Data
public class HolidayOperation {

    private Long id;

    @DiffLogField(name = "适用范围")
    private String scope;

    @DiffLogField(name = "请假限制")
    private Integer timeOfInduction;

    @DiffLogField(name = "适用人员", function = "DIFF_LIST")
    private List<String> scopeStaff;

    @DiffLogField(name = "最小请假单位")
    private String unit;

    @DiffLogField(name = "半天折算时长_上午")
    private Float forenoon;

    @DiffLogField(name = "半天折算时长下午")
    private Float afternoon;

    @DiffLogField(name = "一天折算时长")
    private Float day;

    @DiffLogField(name = "请假时长核算")
    private String accounting;

    @DiffLogField(name = "是否限额")
    private String quota;

    @DiffLogField(name = "发放方式")
    private String grantWay;

    @DiffLogField(name = "发放日期")
    private String grantDate;

    @DiffLogField(name = "额度配置")
    private String quotaConfig;

    @DiffLogField(name = "有效期")
    private String validity;
}
