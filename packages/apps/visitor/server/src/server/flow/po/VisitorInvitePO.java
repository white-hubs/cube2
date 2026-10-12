package com.guhecloud.cube.srv.gen.visitor.management.server.flow.po;

import annotation.FormField;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/13 10:08
 * @Description: 访客邀约实体数据
 */
@Data
public class VisitorInvitePO {

    @FormField(label = "访客姓名")
    @NotNull(message = "流程配置访客姓名未配置")
    private String visitorName;
    @NotNull(message = "流程配置身份证号未配置")
    @FormField(label = "访客身份证号")
    private String visitorIdCard;
    @FormField(label = "访客联系电话")
    private String visitorPhone;
    @FormField(label = "访问区域")
    private List<Long> areaId;
    @FormField(label = "访问日期")
    private String accessDate;
    private Period accessPeriod;
    @FormField(label = "访问时段")
    private List<String> timeStr;
    @FormField(label = "访问事由")
    private String reason;
    @FormField(label = "车牌号码")
    private String plateNumber;
    @FormField(label = "是否有随访人员")
    private String followUp;
    @FormField(label = "随访人员")
    private List<FollowVisitorPO> followVisitors;
    @FormField(label = "随访人员/姓名")
    private String followName;
    @FormField(label = "随访人员/电话")
    private String followPhone;
    @FormField(label = "随访人员/身份证号")
    private String followIdCard;
    @FormField(label = "随访人员/车牌号码")
    private String followPlateNumber;

    public Period getAccessPeriod() {

        var begin = DateUtils.parse(accessDate + " " + timeStr.get(0) + ":00", "yyyy-MM-dd HH:mm:ss");
        var end = DateUtils.parse(accessDate + " " + timeStr.get(1) + ":00", "yyyy-MM-dd HH:mm:ss");
        if ("00:00:00".equals(timeStr.get(1))) {
            end = DateUtils.offsetDay(end, 1);
        }
        return new Period(begin, end);
    }
}
