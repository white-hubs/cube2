package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttHolidayGrantWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.repository.AttHolidayRuleRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param.AttHolidayRuleParam;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 假期规则查询服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttHolidayRuleCommandService extends ServiceImpl<AttHolidayRuleMapper, AttHolidayRulePO> {

    private final AttHolidayRuleMapper attHolidayRuleMapper;
    private final AttHolidayRuleRepository attHolidayRuleRepository;

    /**
     * 新增假期规则
     *
     * @param param 假期规则
     */
    public void addHolidayRule(AttHolidayRuleParam param) {
        // 如果是加班自动计入余额校验是否已有自动计入的假期
        if (StringUtils.isNotBlank(param.getGrantWay()) && AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(param.getGrantWay())) {
            List<AttHolidayRulePO> holidayRulePOList = attHolidayRuleMapper.selectList(Wrappers.emptyWrapper());
            if (holidayRulePOList != null && !holidayRulePOList.isEmpty()) {
                boolean workOut = holidayRulePOList.stream()
                        .anyMatch(po -> AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(po.getGrantWay()));
                if (workOut) {
                    throw new ServiceException("已有加班计入余额的假期规则！");
                }
            }
        }

        AttHolidayRulePO po = param.convertPO();
        po.setCreateTime(LocalDateTime.now());
        attHolidayRuleRepository.addHolidayRule(po);
    }

    /**
     * 假期规则编辑
     *
     * @param param 假期规则
     */
    public void editHolidayRule(AttHolidayRuleParam param) {
        AttHolidayRulePO oldPO = attHolidayRuleMapper.selectById(param.getId());
        if (oldPO == null) {
            throw new ServiceException("规则不存在！");
        }
        AttHolidayRulePO nowPO = param.convertPO();
        if (param.getNotLimit()) {
            nowPO.setGrantDate(oldPO.getGrantDate());
            nowPO.setGrantWay(oldPO.getGrantWay());
            nowPO.setQuota(oldPO.getQuota());
            nowPO.setValidity(oldPO.getValidity());
            nowPO.setFixedUnit(oldPO.getFixedUnit());
        }
        attHolidayRuleRepository.editHolidayRule(oldPO, nowPO, param.convertPO());
    }

    /**
     * 假期规则删除
     *
     * @param id id
     */
    public void removeHolidayRule(Long id) {
        log.info("删除假期规则：{}", id);
        AttHolidayRulePO rulePO = attHolidayRuleMapper.selectById(id);
        if (rulePO != null) {
            attHolidayRuleRepository.removeHolidayRule(rulePO);
        }
    }
}
