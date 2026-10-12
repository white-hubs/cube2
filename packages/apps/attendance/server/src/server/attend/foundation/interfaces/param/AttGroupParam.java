package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttGroupRangesEnum;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 考勤组入参对象
 */
@Data
public class AttGroupParam {
    private Long id;
    /**
     * 考勤组名称
     */
    @NotNull(message = "考勤组名称不能为空")
    private String name;
    /**
     * 打卡规则id
     */
    private Long clockRuleId;
    /**
     * 排班规则id
     */
    private Long schedulingRuleId;
    /**
     * 补卡规则ids
     */
    private String cardRuleIds;
    /**
     * 加班规则id
     */
    private Long overtimeRuleId;
    /**
     * 请假规则 ids
     */
    @Length(max = 500, message = "假期类型过多")
    private String holidayRuleIds;
    /**
     * 离岗规则id
     */
    private Long leavePostRuleId;
    /**
     * 出差规则id;
     */
    private Long awayBusinessRuleId;
    /**
     * 考勤组范围(所有人，部分人。枚举AttGroupRangesEnum）
     */
    @NotNull(message = "适用范围不能为空")
    private String ranges;
    /**
     * 考勤组人员
     */
    private List<AttGroupStaffParam> groupStaffList;
    /**
     * 考勤组使用班次
     */
    private String groupClassIds;

    /**
     * 业务验证
     */
    public void validBusiness() {
        if (AttGroupRangesEnum.PART.name().equals(ranges) && (groupStaffList == null || groupStaffList.isEmpty())) {
            throw new ServiceException("请选择考勤组人员");
        }
        if (AttGroupRangesEnum.ALL.name().equals(ranges) && !(groupStaffList == null || groupStaffList.isEmpty())) {
            throw new ServiceException("适用范围为全部，无需选择人员");
        }
    }

}
