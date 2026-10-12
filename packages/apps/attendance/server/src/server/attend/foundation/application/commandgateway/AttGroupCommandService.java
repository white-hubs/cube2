package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway.AttClockInLedgerApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockRulesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttModuleConfigCheck;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.querygateway.AttGroupQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.vo.AttGroupFunctionVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupAwayBusinessRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupCardRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupClassResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupHolidayRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupLeavePostRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupOvertimeRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupStaffResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingRuleQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttSchedulingRuleDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.SupplementCardRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.SupplementCardRulePO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttGroupCommandService {

    private final AttGroupRepository attGroupRepository;
    private final AttGroupMapper attGroupMapper;
    private final AttClockInLedgerApplication attClockInLedgerApplication;
    private final AttGroupQueryService attGroupQueryService;
    private final SupplementCardRuleMapper supplementCardRuleMapper;
    private final AttSchedulingRuleQueryService attSchedulingRuleQueryService;
    private final AttClockRulesMapper clockRulesMapper;
    private final UserClient userClient;
    private final AttModuleConfigCheck attModuleConfigCheck;

    /**
     * 新增考勤组
     *
     * @param param 考勤组
     *
     * @return id
     */
    public String addAttGroup(AttGroupParam param) {
        String result = "";
        // 校验
        param.validBusiness();
        // 校验是否有全部人的考勤组
        List<AttGroupPO> allRngesPOList = attGroupMapper.selectList(Wrappers.<AttGroupPO>lambdaQuery()
                                                                            .eq(AttGroupPO::getRanges, AttGroupRangesEnum.ALL.name()));
        if (!(allRngesPOList == null || allRngesPOList.isEmpty())) {
            throw new ServiceException("已有全部人考勤组，无法再创建考勤组");
        }
        // 如果时全部人考勤组校验是否有考勤组
        if (AttGroupRangesEnum.ALL.name().equals(param.getRanges())) {
            List<AttGroupPO> list = attGroupMapper.selectList(Wrappers.lambdaQuery());
            if (list != null && !list.isEmpty()) {
                throw new ServiceException("已有考勤组,无法创建全部人考勤组");
            }
        }
        /* 设置ouaa最新信息(手机号加密处理后前端给的手机号有星号需要自己再去ouaa获取) */
        this.setOuaaUserInfo(param);
        param.setId(IdWorker.getId());
        // 校验人员
        if (AttGroupRangesEnum.PART.name().equals(param.getRanges())) {
            // 校验考勤组人员是否已存在
            attGroupQueryService.checkGroupStaffExist(param.getId(), param.getGroupStaffList());
        }
        if (attModuleConfigCheck.checkModuleHave(AttConstants.repairClockCode)) {
            // 校验补卡规则
            this.checkSupplementCardRule(param.getCardRuleIds());
        }
        // 插入
        AttGroupFunctionVO newPO = this.getNewAttGroupFunctionVO(param);
        attGroupRepository.insertAttGroup(param, newPO);
        attClockInLedgerApplication.addClockInLedger(param.getGroupStaffList());
        return result;
    }

    /**
     * 编辑考勤组
     *
     * @param param 考勤组
     *
     * @return id
     */
    public String editAttGroup(AttGroupParam param) {
        // 校验
        param.validBusiness();
        AttGroupPO groupPO = attGroupMapper.selectById(param.getId());
        if (!groupPO.getRanges().equals(param.getRanges())) {
            throw new ServiceException("不能修改考勤组范围");
        }
        /* 设置ouaa最新信息(手机号加密处理后前端给的手机号有星号需要自己再去ouaa获取) */
        this.setOuaaUserInfo(param);
        // 校验人员
        if (AttGroupRangesEnum.PART.name().equals(param.getRanges())) {
            attGroupQueryService.checkGroupStaffExist(param.getId(), param.getGroupStaffList());
        }
        if (attModuleConfigCheck.checkModuleHave(AttConstants.repairClockCode)) {
            // 校验补卡规则
            this.checkSupplementCardRule(param.getCardRuleIds());
        }
        AttGroupFunctionVO oldPO = this.getOldAttGroupFunctionVO(param.getId());
        AttGroupFunctionVO newPO = this.getNewAttGroupFunctionVO(param);
        attGroupRepository.updateAttGroup(oldPO, newPO, param);
        attClockInLedgerApplication.addClockInLedger(param.getGroupStaffList());
        return "result";
    }

    /**
     * 删除考勤组
     *
     * @param id 主键
     *
     * @return id
     */
    public Long removeAttGroup(Long id) {
        AttGroupPO groupPO = attGroupMapper.selectById(id);
        attGroupRepository.deleteAttGroup(groupPO);
        return id;
    }

    /**
     * 校验补卡规则
     *
     * @param ids 补卡规则ids
     *
     * @return 是否合规
     */
    public void checkSupplementCardRule(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            List<Long> idList = Arrays.stream(ids.split(",")).map(Long::valueOf).collect(Collectors.toList());
            List<SupplementCardRulePO> cardRulePOList = supplementCardRuleMapper.selectByIds(idList);
            Assert.isParameterError(CollectionUtils.isEmpty(cardRulePOList), "规则不存在！");
            Map<String, String> map = new HashMap<>(3);
            cardRulePOList.forEach(ex -> {
                Arrays.asList(ex.getType().split(",")).forEach(type -> {
                    Assert.isConflict(map.containsKey(type), "每种补卡类型只能有一个！");
                    map.put(type, type);
                });
            });
        }
    }

    private AttGroupFunctionVO getOldAttGroupFunctionVO(Long id) {
        AttGroupDetailResult groupPo = attGroupQueryService.infoAttGroup(id);
        AttGroupFunctionVO old = new AttGroupFunctionVO();
        old.setName(groupPo.getName());
        old.setRanges(groupPo.getRanges());
        // 设置加班规则
        old.setOvertimeRuleName(groupPo.getOvertimeRule().getName());
        // 设置离岗规则
        old.setLeavePostRuleName(groupPo.getLeavePostRule().getName());
        // 设置出差规则
        old.setAwayBusinessRuleName(groupPo.getAwayBusinessRule().getName());
        // 设置补卡规则
        StringBuffer cardRuleName = new StringBuffer();
        for (AttGroupCardRuleResult ex : groupPo.getCardRuleList()) {
            if (ObjectUtils.isNotEmpty(cardRuleName)) {
                cardRuleName.append(",");
            }
            cardRuleName.append(ex.getName());
        }
        old.setCardRuleName(cardRuleName.toString());
        // 设置假期规则
        StringBuffer holidayRuleName = new StringBuffer();
        for (AttGroupHolidayRuleResult ex : groupPo.getHolidayRuleList()) {
            if (ObjectUtils.isNotEmpty(holidayRuleName)) {
                holidayRuleName.append(",");
            }
            holidayRuleName.append(ex.getName());
        }
        old.setHolidayRuleName(holidayRuleName.toString());
        if (attModuleConfigCheck.checkModuleHave(AttConstants.arrangeCode)) {
            // 排班规则
            AttSchedulingRuleDetailResult schedulingRuleDetailResult = attSchedulingRuleQueryService.infoSchedulingRule(groupPo.getSchedulingRuleId());
            old.setSchedulingRuleName(schedulingRuleDetailResult.getName());
        }
        // 考勤班次
        StringBuffer groupClassName = new StringBuffer();
        for (AttGroupClassResult ex : groupPo.getGroupClassList()) {
            if (ObjectUtils.isNotEmpty(groupClassName)) {
                groupClassName.append(",");
            }
            groupClassName.append(ex.getName());
        }
        old.setGroupClassName(groupClassName.toString());
        // 适用人员
        StringBuffer groupStaffName = new StringBuffer();
        for (AttGroupStaffResult ex : groupPo.getGroupStaffList()) {
            if (ObjectUtils.isNotEmpty(groupStaffName)) {
                groupStaffName.append(",");
            }
            groupStaffName.append(ex.getName());
        }
        AttClockRulePO clockRulePO = clockRulesMapper.selectById(groupPo.getClockRuleId());
        if (clockRulePO != null) {
            old.setClockRuleName(clockRulePO.getName());
        }
        old.setGroupStaffList(groupStaffName.toString());
        return old;
    }

    private AttGroupFunctionVO getNewAttGroupFunctionVO(AttGroupParam param) {
        AttGroupFunctionVO groupPo = ObjectUtils.copying(param, AttGroupFunctionVO.class);
        // 设置补卡规则
        List<AttGroupCardRuleResult> cardRuleList = attGroupRepository.getGroupCardRuleList(groupPo.getCardRuleIds());
        // 设置加班规则
        AttGroupOvertimeRuleResult overtimeRuleResult = attGroupRepository.getGroupOvretimeRuleList(groupPo.getOvertimeRuleId());
        // 设置假期规则
        List<AttGroupHolidayRuleResult> holidayRuleResults = attGroupRepository.getGroupHolidayRuleList(groupPo.getHolidayRuleIds());
        // 设置离岗规则
        AttGroupLeavePostRuleResult leavePostRuleResult = attGroupRepository.getGroupLeavePostRule(groupPo.getLeavePostRuleId());
        // 设置出差规则
        AttGroupAwayBusinessRuleResult awayBusinessRuleResult = attGroupRepository.getGroupAwayBusinessRule(groupPo.getAwayBusinessRuleId());
        if (attModuleConfigCheck.checkModuleHave(AttConstants.arrangeCode)) {
            // 排班规则
            AttSchedulingRuleDetailResult schedulingRuleDetailResult = attSchedulingRuleQueryService.infoSchedulingRule(groupPo.getSchedulingRuleId());
            groupPo.setSchedulingRuleName(schedulingRuleDetailResult.getName());
        }
        // 考勤班次
        List<AttGroupClassResult> groupClassResultList = attGroupQueryService.extClassList(groupPo.getGroupClassIds());
        // 打卡规则
        AttClockRulePO clockRulePO = clockRulesMapper.selectById(groupPo.getClockRuleId());
        // 获取考勤组人员
        if (AttGroupRangesEnum.PART.name().equals(groupPo.getRanges())) {
            StringBuffer groupStaffName = new StringBuffer();
            List<AttGroupStaffParam> groupStaffPOList = param.getGroupStaffList();
            for (AttGroupStaffParam ex : groupStaffPOList) {
                if (ObjectUtils.isNotEmpty(groupStaffName)) {
                    groupStaffName.append(",");
                }
                groupStaffName.append(ex.getName());
            }
            groupPo.setGroupStaffList(groupStaffName.toString());
        }
        StringBuffer cardRuleName = new StringBuffer();
        for (AttGroupCardRuleResult ex : cardRuleList) {
            if (ObjectUtils.isNotEmpty(cardRuleName)) {
                cardRuleName.append(",");
            }
            cardRuleName.append(ex.getName());
        }
        StringBuffer holidayRuleName = new StringBuffer();
        for (AttGroupHolidayRuleResult ex : holidayRuleResults) {
            if (ObjectUtils.isNotEmpty(holidayRuleName)) {
                holidayRuleName.append(",");
            }
            holidayRuleName.append(ex.getName());
        }
        StringBuffer groupClassName = new StringBuffer();
        for (AttGroupClassResult ex : groupClassResultList) {
            if (ObjectUtils.isNotEmpty(groupClassName)) {
                groupClassName.append(",");
            }
            groupClassName.append(ex.getName());
        }
        groupPo.setCardRuleName(cardRuleName.toString());
        groupPo.setHolidayRuleName(holidayRuleName.toString());
        groupPo.setOvertimeRuleName(overtimeRuleResult.getName());
        groupPo.setLeavePostRuleName(leavePostRuleResult.getName());
        groupPo.setAwayBusinessRuleName(awayBusinessRuleResult.getName());
        groupPo.setGroupClassName(groupClassName.toString());
        if (clockRulePO != null) {
            groupPo.setClockRuleName(clockRulePO.getName());
        }
        return groupPo;
    }

    private void setOuaaUserInfo(AttGroupParam param) {
        if (param.getGroupStaffList() != null && !param.getGroupStaffList().isEmpty()) {
            List<AttGroupStaffParam> staffParamList = param.getGroupStaffList();
            UserQuery query = new UserQuery();
            query.setUserIds(staffParamList.stream().map(AttGroupStaffParam::getStaffId).collect(Collectors.toSet()));
            List<UserDTO> userDTOList = userClient.list(query);
            Map<Long, UserDTO> userDTOMap = new HashMap<>();
            if (userDTOList != null && !userDTOList.isEmpty()) {
                for (UserDTO userDTO : userDTOList) {
                    userDTOMap.put(userDTO.getId(), userDTO);
                }
            }
            param.getGroupStaffList().forEach(s -> {
                UserDTO u = userDTOMap.get(s.getStaffId());
                if (u != null) {
                    s.setName(u.getName());
                    s.setMobile(u.getMobile());
                    if (u.getOrganization() != null) {
                        s.setDeptId(u.getOrganization().getId());
                        s.setDeptName(u.getOrganization().getName());
                    }
                }
            });
        }
    }
}
