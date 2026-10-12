package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.apps.linkage.metadata.enumeration.BusinessTypeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.DateUnitEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.RuleTypeEnum;
import com.guhecloud.cube.biz.linkage.client.RuleClient;
import com.guhecloud.cube.biz.linkage.client.param.RuleBasicModifyParam;
import com.guhecloud.cube.biz.linkage.client.param.RuleModifyParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dao.LineLossAlarmRuleMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dao.LineLossConnectionMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossAlarmRule;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossAlarmRuleLine;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossAlarmRulePageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossAlarmRuleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossAlarmRuleLineService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossAlarmRuleService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossAlarmRulePageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossAlarmRuleVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 线损报警规则服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossAlarmRuleServiceImpl extends ServiceImpl<LineLossAlarmRuleMapper, LineLossAlarmRule> implements LineLossAlarmRuleService {
    private final LineLossAlarmRuleLineService lineLossAlarmRuleLineService;
    private final LineLossConnectionMapper lineLossConnectionMapper;
    private final RuleClient ruleClient;

    /**
     * 分页查询报警规则列表
     *
     * @param param 查询参数
     *
     * @return 分页列表
     */
    @Override
    public Pagination<LineLossAlarmRulePageVO> pageList(LineLossAlarmRulePageParam param) {
        var page = this.page(PagingUtils.convert(param), Wrappers.<LineLossAlarmRule>lambdaQuery()
                                     .like(StringUtils.isNotBlank(param.getName()), LineLossAlarmRule::getName, param.getName())
                                     .eq(param.getAlarmLevelId() != null, LineLossAlarmRule::getAlarmLevelId, param.getAlarmLevelId())
                            );
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        return PagingUtils.convert(page, page.getRecords().stream().map(this::convertToPageVO).collect(Collectors.toList()));
    }

    @Override
    public LineLossAlarmRuleVO detail(Long id) {
        var vo = new LineLossAlarmRuleVO();
        // 获取线损储存的规则信息
        var rule = this.getById(id);
        vo.covertLineLossBasicVO(rule);
        // 获取关联线路
        var lineList = lineLossAlarmRuleLineService.list(Wrappers.<LineLossAlarmRuleLine>lambdaQuery()
                                                                 .eq(LineLossAlarmRuleLine::getAlarmRuleId, id));
        var lineIdList = Optional.ofNullable(lineList).orElse(Collections.emptyList()).stream().map(LineLossAlarmRuleLine::getLineId)
                .collect(Collectors.toList());
        vo.getLossAlarmCondition().setLineIdList(lineIdList);
        if (CollectionUtils.isNotEmpty(lineIdList)) {
            vo.getLossAlarmCondition().setConnectionList(lineLossConnectionMapper.selectByIds(lineIdList));
        }
        // 获取联动的规则信息
        var linkageRule = ruleClient.getRule(vo.getTriggeredRuleId());
        if (linkageRule != null) {
            var linkageBasic = linkageRule.getRuleBasicVO();
            vo.setAlarmLevelName(linkageBasic.getTriggerLevelName());
            vo.setEmergencyEventTypeName(linkageBasic.getTriggerTypeName());
            vo.setEmergencyActions(linkageRule.getLinkageActionVOS());
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addRule(LineLossAlarmRuleParam param) {
        // 保存到联动规则
        var ruleModifyParam = this.getCovertLinkageRuleParam(param);
        long linkageId = ruleClient.save(ruleModifyParam);
        // 保存规则
        var po = param.getRulePo();
        po.setId(linkageId);
        po.setTriggeredRuleId(linkageId);
        this.save(po);
        // 保存关联线路
        this.saveLineLossAlarmRuleLine(po.getId(), param.getLossAlarmCondition().getLineIdList());
        return po.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRule(LineLossAlarmRuleParam param) {
        // 更新联动的规则
        var ruleModifyParam = this.getCovertLinkageRuleParam(param);
        ruleClient.update(param.getTriggeredRuleId(), ruleModifyParam);
        // 更新规则
        var po = param.getRulePo();
        this.saveOrUpdate(po);
        // 删除关联线路
        lineLossAlarmRuleLineService.remove(Wrappers.<LineLossAlarmRuleLine>lambdaQuery()
                                                    .eq(LineLossAlarmRuleLine::getAlarmRuleId, po.getId()));
        // 重新添加关联线路
        this.saveLineLossAlarmRuleLine(po.getId(), param.getLossAlarmCondition().getLineIdList());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRule(Long id) {
        var po = this.getById(id);
        if (po == null) {
            return true;
        }
        ruleClient.remove(po.getTriggeredRuleId());
        lineLossAlarmRuleLineService.remove(Wrappers.<LineLossAlarmRuleLine>lambdaQuery().eq(LineLossAlarmRuleLine::getAlarmRuleId, id));
        this.removeById(id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableRule(Long id, Boolean enable) {
        Assert.isParameterError(id == null, "规则 ID 不能为空");
        // 查询当前规则的启用状态
        var rule = this.getById(id);
        Assert.isNotFound(rule, "规则不存在");
        var currentAccount = SecurityContextHolder.getPrincipal().getAccount();
        var updateWrapper = Wrappers.<LineLossAlarmRule>lambdaUpdate()
                .eq(LineLossAlarmRule::getId, id)
                .set(LineLossAlarmRule::getEnabled, Boolean.TRUE.equals(enable))
                .set(LineLossAlarmRule::getUpdateTime, LocalDateTime.now())
                .set(LineLossAlarmRule::getUpdateBy, currentAccount);
        return this.update(updateWrapper);
    }

    // 添加规则的关联线路
    private void saveLineLossAlarmRuleLine(Long lineLossRuleId, List<Long> lineIdList) {
        if (lineIdList == null || CollectionUtils.isEmpty(lineIdList)) {
            return;
        }
        var lineList = new ArrayList<LineLossAlarmRuleLine>();
        lineIdList.forEach(lineId -> {
            var lineLossAlarmRuleLine = new LineLossAlarmRuleLine();
            lineLossAlarmRuleLine.setId(IdWorker.getId());
            lineLossAlarmRuleLine.setAlarmRuleId(lineLossRuleId);
            lineLossAlarmRuleLine.setLineId(lineId);
            lineList.add(lineLossAlarmRuleLine);
        });
        if (CollectionUtils.isNotEmpty(lineList)) {
            lineLossAlarmRuleLineService.saveBatch(lineList);
        }
    }

    // 提取转换联动的规则参数
    private RuleModifyParam getCovertLinkageRuleParam(LineLossAlarmRuleParam param) {
        var ruleModifyParam = new RuleModifyParam();
        ruleModifyParam.setBasicModify(this.getLinkageBasic(param));
        ruleModifyParam.setLinkageEmergencyActionModifies(param.getEmergencyActions());
        ruleModifyParam.setRuleConfig(JsonUtils.serialize(param));
        return ruleModifyParam;
    }

    // 提取转换联动的规则基础信息
    private RuleBasicModifyParam getLinkageBasic(LineLossAlarmRuleParam param) {
        var basic = new RuleBasicModifyParam();
        basic.setCode(String.valueOf(param.getId()));
        basic.setName(param.getName());
        basic.setApplicationCode("LINE_LOSS_MANAGEMENT");
        basic.setApplicationName("线损管理");
        basic.setType(RuleTypeEnum.ALARM_RULE);
        basic.setBusinessType(BusinessTypeEnum.LINE_LOSS_ALARM);
        basic.setTriggerLevelId(param.getAlarmLevelId());
        basic.setTriggerLevelName(param.getAlarmLevelName());
        basic.setTriggerTypeId(param.getEmergencyEventTypeId());
        basic.setTriggerTypeName(param.getEmergencyEventTypeName());
        basic.setEnabled(param.getEnabled());
        // 这里不需要去重，但是报警必须要这个字段所以默认3秒去重
        basic.setReiterated(false);
        basic.setRepeatTime(param.getRepeatTime());
        basic.setRepeatTimeUnit(DateUnitEnum.SECOND);
        return basic;
    }

    // 转换为分页 VO
    private LineLossAlarmRulePageVO convertToPageVO(LineLossAlarmRule rule) {
        var vo = new LineLossAlarmRulePageVO();
        vo.setId(rule.getId());
        vo.setName(rule.getName());
        vo.setAlarmLevelId(rule.getAlarmLevelId());
        vo.setAlarmLevelName(rule.getAlarmLevelName());
        vo.setEstimatedHandlerTime(rule.getEstimatedHandlerTime());
        vo.setEstimatedHandlerTimeName(rule.getEstimatedHandlerTimeName());
        vo.setEstimatedHandlerUnit(rule.getEstimatedHandlerUnit());
        vo.setRemark(rule.getRemark());
        vo.setEnabled(rule.getEnabled());
        vo.setCreateBy(rule.getCreateBy());
        vo.setCreateTime(rule.getCreateTime());
        return vo;
    }
}
