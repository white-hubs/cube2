package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttLeavePostFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param.AttLeavePostDurationParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.enums.AttDurationCalcWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.infrastructure.mapper.AttLeavePostRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.infrastructure.po.AttLeavePostRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingQueryService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 离岗单查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttLeavePostQueryService {

    private final AttGroupRepository attGroupRepository;
    private final AttLeavePostRuleMapper attLeavePostRuleMapper;
    private final AttSchedulingQueryService attSchedulingQueryService;

    /**
     * 校验离岗规则
     *
     * @param param 离岗单
     *
     * @return 离岗规则
     */
    public AttLeavePostRulePO leavePostCheck(AttLeavePostFromDTO param) {
        AttLeavePostRulePO result = new AttLeavePostRulePO();
        param.businessCheck();
        // 获取离岗规则
        AttGroupPO groupPO = attGroupRepository.getGroupByStaffId(param.getApplicantId());
        if (groupPO != null && groupPO.getLeavePostRuleId() != null) {
            AttLeavePostRulePO postRulePO = attLeavePostRuleMapper.selectById(groupPO.getLeavePostRuleId());
            if (postRulePO != null) {
                result = postRulePO;
            }
        }
        // 校验规则
        if (result.getId() != null) {
            // 提前发起
            if (Boolean.TRUE.equals(result.getBeforeApply()) && LocalDateTime.now().isAfter(param.getPostStartTime())) {
                throw new ServiceException("必须提前发起离岗申请");
            }
            // 最小申请时间
            if (result.getMinAskUnit() != null && result.getMinAskUnit() > AttConstants.zeroInt &&
                result.getMinAskUnit() > param.getPostNumber()) {
                throw new ServiceException("离岗时长不得低于" + result.getMinAskUnit() + "小时");
            }
        }
        return result;
    }

    /**
     * 离岗时间计算
     *
     * @param param 请假入参
     *
     * @return 请假时间
     */
    public String calcLeavePostDuration(AttLeavePostDurationParam param) {

        if (param == null || param.getStaffId() == null || param.getPostStartTime() == null || param.getPostEndTime() == null) {
            throw new ServiceException("请填写完整参数！");
        }

        if (param.getPostEndTime().isBefore(param.getPostStartTime())) {
            throw new ServiceException("开始时间必须小于结束时间！");
        }
        // 获取离岗规则
        AttLeavePostRulePO postRulePO = null;
        AttGroupPO groupPO = attGroupRepository.getGroupByStaffId(param.getStaffId());
        if (groupPO != null && groupPO.getLeavePostRuleId() != null) {
            postRulePO = attLeavePostRuleMapper.selectById(groupPO.getLeavePostRuleId());
        }

        if (postRulePO == null) {
            log.error("离岗规则不存在！{}", JsonUtils.serialize(param));
        }
        long duration = DateUtils.betweenMs(param.getPostStartTime(), param.getPostEndTime());
        // 按工作日计算
        if (postRulePO != null && AttDurationCalcWayEnum.WORKING_DAY.name().equals(postRulePO.getCalculationWay())) {
            duration = attSchedulingQueryService.getStaffWorkTime(param.getStaffId(), new Period(param.getPostStartTime(), param.getPostEndTime()));
        }

        return AttHolidayDomain.longToUnit(duration, AttConstants.HOUR, AttConstants.dayOfHour, false);
    }

}
