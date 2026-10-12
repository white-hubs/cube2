package com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.infrastructure.mapper.AttLeavePostRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.infrastructure.po.AttLeavePostRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.interfaces.param.AttLeavePostRuleParam;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 离岗规则操作服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttLeavePostRuleCommandService extends ServiceImpl<AttLeavePostRuleMapper, AttLeavePostRulePO> {
    private final AttLeavePostRuleLogCommandService attLeavePostRuleLogCommandService;

    /**
     * 新增离岗规则
     *
     * @param param 规则
     *
     * @return 规则id
     */
    public Long leavePostRuleAdd(AttLeavePostRuleParam param) {
        AttLeavePostRulePO postRulePO = new AttLeavePostRulePO();
        BeanUtils.copyProperties(param, postRulePO);
        Long id = IdWorker.getId();
        postRulePO.setId(id);
        this.save(postRulePO);
        // 添加操作日志
        attLeavePostRuleLogCommandService.addLog(postRulePO);
        return id;
    }

    /**
     * 编辑离岗规则
     *
     * @param param 规则
     *
     * @return 规则id
     */
    public Long leavePostRuleEdit(AttLeavePostRuleParam param) {
        AttLeavePostRulePO oldRulePO = this.getById(param.getId());
        if (oldRulePO == null) {
            throw new ServiceException("规则不存在");
        }
        this.update(Wrappers.<AttLeavePostRulePO>lambdaUpdate()
                            .eq(AttLeavePostRulePO::getId, param.getId())
                            .set(AttLeavePostRulePO::getName, param.getName())
                            .set(AttLeavePostRulePO::getBeforeApply, param.getBeforeApply())
                            .set(AttLeavePostRulePO::getCalculationWay, param.getCalculationWay())
                            .set(AttLeavePostRulePO::getMinAskUnit, param.getMinAskUnit())
                            .set(AttLeavePostRulePO::getUpdateTime, LocalDateTime.now())
                   );
        AttLeavePostRulePO newPO = new AttLeavePostRulePO();
        BeanUtils.copyProperties(oldRulePO, newPO);
        newPO.setName(param.getName());
        newPO.setBeforeApply(param.getBeforeApply());
        newPO.setCalculationWay(param.getCalculationWay());
        newPO.setMinAskUnit(param.getMinAskUnit());
        // 操作日志
        attLeavePostRuleLogCommandService.updateLog(newPO, oldRulePO);
        return param.getId();
    }

    /**
     * 删除离岗规则
     *
     * @param id 主键id
     *
     * @return 规则id
     */
    public Long leavePostRuleRemove(Long id) {
        AttLeavePostRulePO rulePO = this.getById(id);
        if (rulePO == null) {
            throw new ServiceException("规则不存在");
        }
        this.removeById(id);
        attLeavePostRuleLogCommandService.deleteLog(rulePO);
        return id;
    }
}
