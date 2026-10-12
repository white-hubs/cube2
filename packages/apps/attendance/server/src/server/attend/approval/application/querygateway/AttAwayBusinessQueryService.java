package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAwayBusinessFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param.AttAwayBusinessDurationParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.infrastructure.mapper.AttAwayBusinessRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.infrastructure.po.AttAwayBusinessRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.enums.AttDurationCalcWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingQueryService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 出差查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAwayBusinessQueryService {
    private final AttGroupRepository attGroupRepository;
    private final AttAwayBusinessRuleMapper attAwayBusinessRuleMapper;
    private final AttSchedulingQueryService attSchedulingQueryService;

    /**
     * 校验出差规则
     *
     * @param param 出差单
     *
     * @return 出差规则
     */
    public AttAwayBusinessRulePO awayBusinessCheck(AttAwayBusinessFromDTO param) {
        AttAwayBusinessRulePO result = new AttAwayBusinessRulePO();
        // 获取规则
        AttGroupPO groupPO = attGroupRepository.getGroupByStaffId(param.getApplicantId());
        if (groupPO != null && groupPO.getAwayBusinessRuleId() != null) {
            AttAwayBusinessRulePO awayRulePO = attAwayBusinessRuleMapper.selectById(groupPO.getAwayBusinessRuleId());
            if (awayRulePO != null) {
                result = awayRulePO;
            }
        }
        // 校验规则
        if (result.getId() != null) {
            // 提前发起
            if (Boolean.TRUE.equals(result.getBeforeApply()) && LocalDateTime.now().isAfter(param.getAwayStartTime())) {
                throw new ServiceException("必须提前发起申请");
            }
            // 最小申请时间
            if (result.getMinAskUnit() != null && result.getMinAskUnit() > AttConstants.zeroInt &&
                result.getMinAskUnit() > param.getAwayNumber()) {
                throw new ServiceException("申请时长不得低于" + result.getMinAskUnit() + "小时");
            }
        }
        return result;
    }

    /**
     * 出差时间计算
     *
     * @param param 请假入参
     *
     * @return 请假时间
     */
    public String calcAwayBusinessDuration(AttAwayBusinessDurationParam param) {

        Assert.isParameterError(param == null || param.getStaffId() == null || param.getStartTime() == null ||
                                param.getEndTime() == null, "请填写完整参数！");

        Assert.isParameterError(
                DateUtils.toTimestamp(param.getStartTime()) >= DateUtils.toTimestamp(param.getEndTime()), "开始时间必须小于结束时间！");
        // 获取出差规则
        AttAwayBusinessRulePO awayRulePO = null;
        AttGroupPO groupPO = attGroupRepository.getGroupByStaffId(param.getStaffId());
        if (groupPO != null && groupPO.getAwayBusinessRuleId() != null) {
            awayRulePO = attAwayBusinessRuleMapper.selectById(groupPO.getAwayBusinessRuleId());
        }

        if (awayRulePO == null) {
            log.error("出差规则不存在！{}", JsonUtils.serialize(param));
        }
        long duration = DateUtils.betweenMs(param.getStartTime(), param.getEndTime());
        // 按工作日计算
        if (awayRulePO != null && AttDurationCalcWayEnum.WORKING_DAY.name().equals(awayRulePO.getCalculationWay())) {
            duration = attSchedulingQueryService.getStaffWorkTime(param.getStaffId(), new Period(param.getStartTime(), param.getEndTime()));
        }

        return AttHolidayDomain.longToUnit(duration, AttConstants.HOUR, AttConstants.dayOfHour, false);
    }

}
