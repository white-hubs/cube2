package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.impl.WorkOvertimeRuleCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo.SubmitWorkOvertimeRuleOperationVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitWorkOvertimeRuleCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice.WorkOverTimeRuleOperationService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.WorkOvertimeRuleDetailMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.WorkOvertimeRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeCompensateMethodEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.WorkOvertimeRuleDetailPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.WorkOvertimeRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator.WorkOvertimeRuleValidator;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/11
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkOvertimeRuleCommandServiceImpl extends ServiceImpl<WorkOvertimeRuleMapper, WorkOvertimeRulePO> implements WorkOvertimeRuleCommandService {

    private final WorkOvertimeRuleValidator workOvertimeRuleValidator;
    private final WorkOvertimeRuleDetailMapper detailMapper;
    private final WorkOverTimeRuleOperationService workOverTimeRuleOperationService;

    @Override
    public Long saveOrUpdate(SubmitWorkOvertimeRuleCommand command) {
        Long id = command.getId();
        workOvertimeRuleValidator.verifyIsExistByName(id, command.getName());
        if (null == id) {
            WorkOvertimeRulePO po = ObjectUtils.copying(command, WorkOvertimeRulePO.class);
            workOverTimeRuleOperationService.insertOperation(command);
            this.save(po);
            id = po.getId();
        } else {
            WorkOvertimeRulePO saved = this.getById(id);
            if (saved != null) {
                this.saveToOperationLog(command);
                ObjectUtils.copying(command, saved);
                this.updateById(saved);
            }
        }
        this.updateDetail(command, id);
        return id;
    }

    /**
     * @Author wql
     * @Description 保存加班规则操作日志
     * @Date 14:53 2023/6/20
     **/
    private void saveToOperationLog(SubmitWorkOvertimeRuleCommand newPO) {
        SubmitWorkOvertimeRuleCommand oldPO = new SubmitWorkOvertimeRuleCommand();
        WorkOvertimeRulePO byId = this.getById(newPO.getId());
        ObjectUtils.copying(byId, oldPO);
        List<WorkOvertimeRuleDetailPO> ruleDetailPOS = detailMapper.selectList(Wrappers.<WorkOvertimeRuleDetailPO>lambdaQuery()
                                                                                       .eq(WorkOvertimeRuleDetailPO::getRuleId, byId.getId()));
        if (ObjectUtils.isNotEmpty(ruleDetailPOS)) {
            ArrayList<SubmitWorkOvertimeRuleCommand.Detail> oldDetails = new ArrayList<>();
            List<SubmitWorkOvertimeRuleCommand.Detail> newDetails = newPO.getRuleDetails();
            for (SubmitWorkOvertimeRuleCommand.Detail newDetail : newDetails) {
                String type = newDetail.getType();
                for (WorkOvertimeRuleDetailPO detailPO : ruleDetailPOS) {
                    if (StringUtils.equals(type, detailPO.getType())) {
                        SubmitWorkOvertimeRuleCommand.Detail detail = new SubmitWorkOvertimeRuleCommand.Detail();
                        ObjectUtils.copying(detailPO, detail);
                        oldDetails.add(detail);
                    }
                }
            }
            oldPO.setRuleDetails(oldDetails);
        }
        SubmitWorkOvertimeRuleOperationVO oldVO = coverToOperationLogVO(oldPO);
        SubmitWorkOvertimeRuleOperationVO newVO = coverToOperationLogVO(newPO);
        workOverTimeRuleOperationService.updateOvertimeRule(oldVO, newVO);
    }

    /**
     * @Author wql
     * @Description 扁平化操作记录
     * @Date 14:53 2023/6/20
     **/
    private SubmitWorkOvertimeRuleOperationVO coverToOperationLogVO(SubmitWorkOvertimeRuleCommand po) {
        SubmitWorkOvertimeRuleOperationVO vo = new SubmitWorkOvertimeRuleOperationVO();
        ObjectUtils.copying(po, vo);
        List<SubmitWorkOvertimeRuleCommand.Detail> details = po.getRuleDetails();
        if (ObjectUtils.isNotEmpty(details)) {
            for (SubmitWorkOvertimeRuleCommand.Detail detail : details) {
                if (WorkOvertimeTypeEnum.WORK.name().equals(detail.getType())) {
                    vo.setWorkday_work_condition(detail.getWorkCondition());
                    vo.setWorkday_units(detail.getUnits());
                    vo.setWorkday_afterTime(detail.getAfterTime());
                    vo.setWorkday_method(detail.getMethod());
                    vo.setWorkday_compensationMethod(detail.getCompensationMethod());
                    vo.setWorkday_compensate(detail.getCompensate());
                    vo.setWorkday_proportion(detail.getProportion());
                } else if (WorkOvertimeTypeEnum.REST.name().equals(detail.getType())) {
                    vo.setDayOff_work_condition(detail.getWorkCondition());
                    vo.setDayOff_units(detail.getUnits());
                    vo.setDayOff_method(detail.getMethod());
                    vo.setDayOff_afterTime(detail.getAfterTime());
                    vo.setDayOff_compensationMethod(detail.getCompensationMethod());
                    vo.setDayOff_compensate(detail.getCompensate());
                    vo.setDayOff_proportion(detail.getProportion());
                } else if (WorkOvertimeTypeEnum.FESTIVAL.name().equals(detail.getType())) {
                    vo.setHoliday_work_condition(detail.getWorkCondition());
                    vo.setHoliday_units(detail.getUnits());
                    vo.setHoliday_method(detail.getMethod());
                    vo.setHoliday_afterTime(detail.getAfterTime());
                    vo.setHoliday_compensationMethod(detail.getCompensationMethod());
                    vo.setHoliday_compensate(detail.getCompensate());
                    vo.setHoliday_proportion(detail.getProportion());
                }
            }
        }
        return vo;
    }

    @Override
    public void delete(Long id) {
        workOverTimeRuleOperationService.deleteOperation(this.getById(id));
        // 删除规则
        DatabaseContextHolder.softDelete(this, id);
    }

    private void updateDetail(SubmitWorkOvertimeRuleCommand command, Long ruleId) {
        // 全部删除
        detailMapper.delete(Wrappers.<WorkOvertimeRuleDetailPO>lambdaQuery().eq(WorkOvertimeRuleDetailPO::getRuleId, ruleId));
        // 重新添加
        List<SubmitWorkOvertimeRuleCommand.Detail> ruleDetails = command.getRuleDetails();
        if (CollectionUtils.isNotEmpty(ruleDetails)) {
            ruleDetails.forEach(detail -> {
                WorkOvertimeRuleDetailPO detailPO = ObjectUtils.copying(detail, WorkOvertimeRuleDetailPO.class);
                if (WorkOvertimeCompensateMethodEnum.SYSTEM.name().equals(detailPO.getCompensationMethod())) {
                    Assert.isNotAcceptable(StringUtils.isEmpty(detailPO.getCompensate()), "当加班补偿为系统自动计算时必须选择补偿方式");
                } else {
                    detailPO.setCompensate("");
                }
                detailPO.setRuleId(ruleId);
                detailMapper.insert(detailPO);
            });
        }
    }

}
