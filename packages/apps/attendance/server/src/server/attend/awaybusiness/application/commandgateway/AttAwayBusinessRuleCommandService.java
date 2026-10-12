package com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.domain.AttAwayBusinessRuleCommandRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.infrastructure.mapper.AttAwayBusinessRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.infrastructure.po.AttAwayBusinessRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.interfaces.param.AttAwayBusinessRuleParam;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 出差规则操作服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAwayBusinessRuleCommandService extends ServiceImpl<AttAwayBusinessRuleMapper, AttAwayBusinessRulePO> {
    private final AttAwayBusinessRuleCommandRepository attAwayBusinessRuleCommandRepository;

    /**
     * 出差规则新增
     *
     * @param param 规则
     *
     * @return id
     */
    public Long awayBusinessRuleAdd(AttAwayBusinessRuleParam param) {
        Long id = IdWorker.getId();
        AttAwayBusinessRulePO rulePO = new AttAwayBusinessRulePO();
        BeanUtils.copyProperties(param, rulePO);
        rulePO.setId(id);
        attAwayBusinessRuleCommandRepository.awayBusinessRuleAdd(rulePO);
        this.save(rulePO);
        return id;
    }

    /**
     * 出差规则新增
     *
     * @param param 规则
     *
     * @return id
     */
    public Long awayBusinessRuleEdit(AttAwayBusinessRuleParam param) {
        Long id = IdWorker.getId();
        AttAwayBusinessRulePO rulePO = new AttAwayBusinessRulePO();
        BeanUtils.copyProperties(param, rulePO);
        rulePO.setId(id);
        AttAwayBusinessRulePO oldPO = this.baseMapper.selectById(param.getId());
        attAwayBusinessRuleCommandRepository.awayBusinessRuleEdit(oldPO, rulePO);
        this.update(null, Wrappers.<AttAwayBusinessRulePO>lambdaUpdate()
                            .eq(AttAwayBusinessRulePO::getId, param.getId())
                            .set(AttAwayBusinessRulePO::getBeforeApply, param.getBeforeApply())
                            .set(AttAwayBusinessRulePO::getName, param.getName())
                            .set(AttAwayBusinessRulePO::getMinAskUnit, param.getMinAskUnit())
                            .set(AttAwayBusinessRulePO::getCalculationWay, param.getCalculationWay())
                   );
        return id;
    }

    /**
     * 删除
     *
     * @param id 主键
     *
     * @return id
     */
    @LogRecord(success = "删除考勤组:{{#groupPO.name}}", subType = "ATTENDANCE_SECTION", subTypeName = "考勤组", operationType = "DELETE",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#groupPO.id}}"
    )
    public Long awayBusinessRuleRemove(Long id) {
        AttAwayBusinessRulePO awayBusinessRulePO = this.baseMapper.selectById(id);
        attAwayBusinessRuleCommandRepository.awayBusinessRuleRemove(awayBusinessRulePO);
        this.removeById(id);
        return id;
    }
}
