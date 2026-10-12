package com.guhecloud.cube.srv.gen.appraisals.server.rule.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.appraisals.server.common.util.AppraisalDepartmentUtils;
import com.guhecloud.cube.srv.gen.appraisals.server.common.util.AppraisalRuleValidator;
import com.guhecloud.cube.srv.gen.appraisals.server.integration.FormFlowGroupIntegrationService;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dao.FormAppraisalRuleContentMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dao.FormAppraisalRuleItemMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dao.FormAppraisalRuleMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dto.ContentCommand;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dto.ItemCommand;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRule;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleContent;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleItem;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.param.FormAppraisalRuleCommand;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.service.FormAppraisalRuleService;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 考核规则服务实现类
 * 提供考核规则的增删改操作实现
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormAppraisalRuleServiceImpl extends ServiceImpl<FormAppraisalRuleMapper, FormAppraisalRule> implements FormAppraisalRuleService {
    private final FormAppraisalRuleMapper ruleMapper;
    private final FormAppraisalRuleContentMapper contentMapper;
    private final FormAppraisalRuleItemMapper itemMapper;
    private final FormFlowGroupIntegrationService formFlowGroupIntegrationService;

    /**
     * 保存考核规则（包含规则内容）
     *
     * @param command 考核规则保存命令
     *
     * @return 新创建的规则 ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(FormAppraisalRuleCommand command) {
        // 验证规则数据的合法性
        AppraisalRuleValidator.validate(command);
        formFlowGroupIntegrationService.assertLeafGroup(command.getFormGroupId());
        // 生成唯一的规则 ID
        long ruleId = IdWorker.getId();
        // 构建规则实体对象
        FormAppraisalRule rule = FormAppraisalRule.builder()
                .id(ruleId)
                .name(command.getName())
                .inspectionItem(command.getInspectionItem())
                .periodType(command.getPeriodType())
                .totalScore(command.getTotalScore())
                .spotCheckScore(command.getSpotCheckScore())
                .inspectionScore(command.getInspectionScore())
                .responsibleDepartmentIds(AppraisalDepartmentUtils.joinDepartmentIds(command.getResponsibleDepartmentIds()))
                .formGroupId(command.getFormGroupId())
                .enabled(command.getEnabled())
                .ruleVersion(1)  // 初始版本号为 1
                .build();
        // 插入规则到数据库
        ruleMapper.insert(rule);
        // 保存规则的内容树（内容和项）
        saveTree(ruleId, command.getContents());
        // 返回新创建的规则 ID
        return ruleId;
    }

    /**
     * 更新考核规则（包含规则内容）
     * 更新时会递增版本号并清空原有内容重新插入
     *
     * @param id      规则 ID
     * @param command 考核规则更新命令
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, FormAppraisalRuleCommand command) {
        // 验证规则数据的合法性
        AppraisalRuleValidator.validate(command);
        formFlowGroupIntegrationService.assertLeafGroup(command.getFormGroupId());
        // 查询现有的规则
        FormAppraisalRule existing = ruleMapper.selectById(id);
        Assert.isNotFound(existing == null, "规则不存在");
        // 更新规则基本信息
        existing.setName(command.getName());
        existing.setInspectionItem(command.getInspectionItem());
        existing.setPeriodType(command.getPeriodType());
        existing.setTotalScore(command.getTotalScore());
        existing.setSpotCheckScore(command.getSpotCheckScore());
        existing.setInspectionScore(command.getInspectionScore());
        existing.setResponsibleDepartmentIds(AppraisalDepartmentUtils.joinDepartmentIds(command.getResponsibleDepartmentIds()));
        existing.setFormGroupId(command.getFormGroupId());
        existing.setEnabled(command.getEnabled());
        // 递增版本号：如果原版本号为 null 则设为 1，否则加 1
        existing.setRuleVersion(existing.getRuleVersion() == null ? 1 : existing.getRuleVersion() + 1);
        // 更新规则到数据库
        ruleMapper.updateById(existing);
        // 删除原有的规则内容（级联删除）
        contentMapper.delete(Wrappers.<FormAppraisalRuleContent>lambdaQuery().eq(FormAppraisalRuleContent::getRuleId, id));
        // 删除原有的规则项（级联删除）
        itemMapper.delete(Wrappers.<FormAppraisalRuleItem>lambdaQuery().eq(FormAppraisalRuleItem::getRuleId, id));
        // 重新保存新的规则内容树
        saveTree(id, command.getContents());
    }

    /**
     * 启用或禁用考核规则
     *
     * @param id     规则 ID
     * @param enable 启用状态 true-启用 false-禁用
     */
    @Override
    public void enabled(Long id, Boolean enable) {
        // 查询现有的规则
        FormAppraisalRule existing = ruleMapper.selectById(id);
        Assert.isNotFound(existing == null, "规则不存在");
        // 更新启用状态
        existing.setEnabled(enable);
        // 更新规则到数据库
        ruleMapper.updateById(existing);
    }

    /**
     * 删除考核规则（级联删除规则内容和规则项）
     *
     * @param id 规则 ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long id) {
        // 先删除规则项（子表数据）
        itemMapper.delete(Wrappers.<FormAppraisalRuleItem>lambdaQuery().eq(FormAppraisalRuleItem::getRuleId, id));
        // 再删除规则内容（子表数据）
        contentMapper.delete(Wrappers.<FormAppraisalRuleContent>lambdaQuery().eq(FormAppraisalRuleContent::getRuleId, id));
        // 最后删除规则主表数据
        ruleMapper.deleteById(id);
    }

    /**
     * 保存规则内容树（内容和项）
     * 遍历内容列表，为每个内容创建记录并关联其下的规则项
     *
     * @param ruleId   规则 ID
     * @param contents 规则内容列表
     */
    private void saveTree(Long ruleId, List<ContentCommand> contents) {
        // 遍历每个内容项
        for (ContentCommand contentCommand : contents) {
            // 生成内容 ID
            long contentId = IdWorker.getId();
            // 构建规则内容实体
            FormAppraisalRuleContent content = FormAppraisalRuleContent.builder()
                    .id(contentId)
                    .ruleId(ruleId)
                    .contentType(contentCommand.getContentType())
                    .sourceType(contentCommand.getSourceType())
                    .sourceRefId(contentCommand.getSourceRefId())
                    .sourceRefCode(contentCommand.getSourceRefCode())
                    .sourceRefName(contentCommand.getSourceRefName())
                    .contentDesc(contentCommand.getContentDesc())
                    .standardDesc(contentCommand.getStandardDesc())
                    .sort(contentCommand.getSort())
                    .build();
            // 插入规则内容到数据库
            contentMapper.insert(content);
            // 遍历该内容项下的所有规则项
            for (ItemCommand itemCommand : contentCommand.getItems()) {
                // 构建规则项实体
                FormAppraisalRuleItem item = FormAppraisalRuleItem.builder()
                        .id(IdWorker.getId())
                        .ruleId(ruleId)
                        .contentId(contentId)  // 关联到父级内容
                        .itemName(itemCommand.getItemName())
                        .fieldKey(itemCommand.getFieldKey())
                        .calcType(itemCommand.getCalcType())
                        // 将区间配置对象列表转换为 JSON 字符串存储
                        .rangeConfigJson(JSON.toJSONString(itemCommand.getRangeConfig()))
                        .sort(itemCommand.getSort())
                        .build();
                // 插入规则项到数据库
                itemMapper.insert(item);
            }
        }
    }
}
