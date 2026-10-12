package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param;

import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto.AttHolidayGrantQuotaDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto.AttHolidayGrantValidityDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 假期规则入参对象(新增/修改
 */
@Data
public class AttHolidayRuleParam {
    private Long id;
    /**
     * 假期名称
     */
    @NotNull(message = "名称不能为空")
    private String name;

    /*
     * 假期申请规则
     */
    /**
     * 最小请假单位(低于此不能请假
     */
    @NotNull(message = "最小请假单位不能为空")
    @Min(message = "最小请假单位不能小于0", value = 0)
    private Integer minAskUnit;
    /**
     * 是否计入缺勤(true请该假期算缺勤，false请该假期算正常出勤
     */
    private Boolean intoAbsence;
    /**
     * 假期类型(法定类型)AttLegalHolidayEnum
     */
    private String holidayType;
    /**
     * 一天折算小时
     */
    private Integer dayOfHour;
    /**
     * 请假时间计算方式（工作日/自然日
     */
    @NotNull(message = "请假时间计算方式不能为空")
    private String calculationWay;

    /*
     * 假期发放规则
     */

    /**
     * 不限额(true表示不限额
     */
    @NotNull(message = "是否限额不能为空")
    private Boolean notLimit;

    /**
     * 发放方式（每年发放
     */
    private String grantWay;
    /**
     * 发放时间（入职日发放，每年1月1发放
     */
    private String grantDate;
    /**
     * 额度配置(json
     */
    private String quota;

    // 长期有效
    /**
     * 有效期（json
     */
    private String validity;

    public AttHolidayRulePO convertPO() {
        AttHolidayRulePO po = AttHolidayRulePO.builder().build();
        po.setId(this.id);
        po.setValidity(this.validity);
        po.setNotLimit(this.notLimit);
        po.setQuota(this.quota);
        po.setName(this.name);
        po.setMinAskUnit(this.minAskUnit);
        po.setDayOfHour(this.dayOfHour);
        po.setCalculationWay(this.calculationWay);
        po.setGrantWay(this.grantWay);
        po.setGrantDate(this.grantDate);
        po.setQuota(this.quota);
        po.setValidity(this.validity);
        po.setIntoAbsence(this.intoAbsence);
        po.setHolidayType(this.holidayType);
        if (StringUtils.isNotBlank(this.quota)) {
            AttHolidayGrantQuotaDTO quotaDTO = JsonUtils.deserialize(this.getQuota(), AttHolidayGrantQuotaDTO.class);
            if (quotaDTO != null) {
                po.setQuotaType(quotaDTO.getQuotaType());
                po.setFixedNumber(quotaDTO.getFixedNumber());
                po.setFixedUnit(quotaDTO.getFixedUnit());
            }

        }
        if (StringUtils.isNotBlank(this.getValidity())) {
            AttHolidayGrantValidityDTO validityDTO = JsonUtils.deserialize(this.getValidity(), AttHolidayGrantValidityDTO.class);
            if (validityDTO != null) {
                po.setLongTerm(validityDTO.getLongTerm());
                po.setNatureYear(validityDTO.getNatureYear());
                po.setValidityNumber(validityDTO.getValidityNumber());
                po.setValidityUnit(validityDTO.getValidityUnit());
            }

        }
        return po;
    }
}
