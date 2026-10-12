package com.guhecloud.cube.srv.gen.appraisals.server.rule.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.srv.gen.appraisals.server.common.util.AppraisalDepartmentUtils;
import com.guhecloud.cube.srv.gen.appraisals.server.integration.OrganizationIntegrationService;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dao.FormAppraisalRuleContentMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dao.FormAppraisalRuleItemMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dao.FormAppraisalRuleMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dto.RangeConfigDTO;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRule;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleContent;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleItem;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.param.FormAppraisalRulePageParam;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.service.FormAppraisalRuleQueryService;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.vo.FormAppraisalRuleContentVO;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.vo.FormAppraisalRuleDetailVO;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.vo.FormAppraisalRuleItemVO;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.vo.FormAppraisalRulePageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 考核规则查询服务实现类
 * 提供考核规则的查询操作实现
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormAppraisalRuleQueryServiceImpl implements FormAppraisalRuleQueryService {
    private final FormAppraisalRuleMapper ruleMapper;
    private final FormAppraisalRuleContentMapper contentMapper;
    private final FormAppraisalRuleItemMapper itemMapper;
    private final OrganizationIntegrationService organizationIntegrationService;

    /**
     * 根据 ID 获取考核规则详情（包含规则内容树）
     *
     * @param id 规则 ID
     *
     * @return 考核规则详情 VO
     */
    @Override
    public FormAppraisalRuleDetailVO getById(Long id) {
        // 查询规则基本信息
        FormAppraisalRule rule = ruleMapper.selectById(id);
        if (rule == null) {
            return null;
        }
        FormAppraisalRuleDetailVO detailVO = toDetailVO(rule);
        fillDepartmentInfo(detailVO, rule.getResponsibleDepartmentIds());
        // 构建并设置规则内容树（包含内容和项的层级结构）
        detailVO.setContents(buildContentTree(id));
        return detailVO;
    }

    /**
     * 分页获取考核规则列表
     *
     * @param param 分页查询参数
     *
     * @return 考核规则分页数据
     */
    @Override
    public Pagination<FormAppraisalRulePageVO> getPage(FormAppraisalRulePageParam param) {
        // 构建分页查询条件
        Page<FormAppraisalRule> page = ruleMapper.selectPage(PagingUtils.convert(param), Wrappers.<FormAppraisalRule>lambdaQuery()
                // 关键词模糊匹配（规则名称）
                .like(param.getKeyword() != null && !param.getKeyword().isBlank(), FormAppraisalRule::getName, param.getKeyword())
                // 周期类型精确匹配
                .eq(param.getPeriodType() != null, FormAppraisalRule::getPeriodType, param.getPeriodType())
                .apply(
                        param.getResponsibleDepartmentId() != null,
                        "find_in_set({0}, responsible_department_ids)",
                        param.getResponsibleDepartmentId()
                      )
                // 所属分组精确匹配
                .eq(param.getFormGroupId() != null, FormAppraisalRule::getFormGroupId, param.getFormGroupId())
                // 启用状态精确匹配
                .eq(param.getEnabled() != null, FormAppraisalRule::getEnabled, param.getEnabled())
                // 按更新时间降序排序
                .orderByDesc(FormAppraisalRule::getUpdateTime));
        // 转换实体为 VO 并设置责任部门名称
        Set<Long> allDepartmentIds = new LinkedHashSet<>();
        for (FormAppraisalRule rule : page.getRecords()) {
            allDepartmentIds.addAll(AppraisalDepartmentUtils.parseDepartmentIds(rule.getResponsibleDepartmentIds()));
        }
        Map<Long, OrganizationDTO> departmentMap = organizationIntegrationService.getDepartmentMapByIds(allDepartmentIds);
        List<FormAppraisalRulePageVO> records = page.getRecords().stream().map(rule -> {
            FormAppraisalRulePageVO vo = toPageVO(rule);
            fillDepartmentInfo(vo, rule.getResponsibleDepartmentIds(), departmentMap);
            return vo;
        }).toList();
        // 返回分页结果
        return PagingUtils.convert(page, records);
    }

    /**
     * 构建规则内容树（内容 - 项层级结构）
     *
     * @param ruleId 规则 ID
     *
     * @return 规则内容 VO 列表
     */
    private List<FormAppraisalRuleContentVO> buildContentTree(Long ruleId) {
        // 查询该规则下的所有内容项，按排序号升序排列
        List<FormAppraisalRuleContent> contents = contentMapper.selectList(Wrappers.<FormAppraisalRuleContent>lambdaQuery()
                                                                                   .eq(FormAppraisalRuleContent::getRuleId, ruleId)
                                                                                   .orderByAsc(FormAppraisalRuleContent::getSort));
        // 查询该规则下的所有规则项，按排序号升序排列
        List<FormAppraisalRuleItem> items = itemMapper.selectList(Wrappers.<FormAppraisalRuleItem>lambdaQuery()
                                                                          .eq(FormAppraisalRuleItem::getRuleId, ruleId)
                                                                          .orderByAsc(FormAppraisalRuleItem::getSort));
        // 存储最终结果
        List<FormAppraisalRuleContentVO> result = new ArrayList<>();
        // 遍历每个内容项，构建层级结构
        for (FormAppraisalRuleContent content : contents) {
            FormAppraisalRuleContentVO contentVO = toContentVO(content);
            // 筛选出属于当前内容项的规则项，并按排序号排序（null 值排在最后）
            List<FormAppraisalRuleItemVO> itemVOs = items.stream()
                    // 过滤：只保留当前内容项关联的规则项
                    .filter(item -> content.getId().equals(item.getContentId()))
                    // 排序：按 sort 字段升序，null 值排在最后
                    .sorted(Comparator.comparing(FormAppraisalRuleItem::getSort, Comparator.nullsLast(Integer::compareTo)))
                    // 转换为 VO 对象
                    .map(item -> {
                        FormAppraisalRuleItemVO itemVO = toItemVO(item);
                        // 解析区间配置 JSON 字符串为 DTO 对象列表
                        itemVO.setRangeConfig(JSON.parseArray(item.getRangeConfigJson(), RangeConfigDTO.class));
                        return itemVO;
                    }).toList();
            // 将规则项列表设置到内容 VO 中
            contentVO.setItems(itemVOs);
            // 添加到结果列表
            result.add(contentVO);
        }
        return result;
    }

    private FormAppraisalRuleDetailVO toDetailVO(FormAppraisalRule rule) {
        FormAppraisalRuleDetailVO vo = new FormAppraisalRuleDetailVO();
        vo.setId(rule.getId());
        vo.setName(rule.getName());
        vo.setInspectionItem(rule.getInspectionItem());
        vo.setPeriodType(rule.getPeriodType());
        vo.setTotalScore(rule.getTotalScore());
        vo.setSpotCheckScore(rule.getSpotCheckScore());
        vo.setInspectionScore(rule.getInspectionScore());
        vo.setFormGroupId(rule.getFormGroupId());
        vo.setEnabled(rule.getEnabled());
        vo.setRuleVersion(rule.getRuleVersion());
        return vo;
    }

    private FormAppraisalRulePageVO toPageVO(FormAppraisalRule rule) {
        FormAppraisalRulePageVO vo = new FormAppraisalRulePageVO();
        vo.setId(rule.getId());
        vo.setName(rule.getName());
        vo.setInspectionItem(rule.getInspectionItem());
        vo.setPeriodType(rule.getPeriodType());
        vo.setTotalScore(rule.getTotalScore());
        vo.setSpotCheckScore(rule.getSpotCheckScore());
        vo.setInspectionScore(rule.getInspectionScore());
        vo.setFormGroupId(rule.getFormGroupId());
        vo.setEnabled(rule.getEnabled());
        vo.setRuleVersion(rule.getRuleVersion());
        vo.setUpdateTime(rule.getUpdateTime());
        return vo;
    }

    private FormAppraisalRuleContentVO toContentVO(FormAppraisalRuleContent content) {
        FormAppraisalRuleContentVO vo = new FormAppraisalRuleContentVO();
        vo.setId(content.getId());
        vo.setContentType(content.getContentType());
        vo.setSourceType(content.getSourceType());
        vo.setSourceRefId(content.getSourceRefId());
        vo.setSourceRefCode(content.getSourceRefCode());
        vo.setSourceRefName(content.getSourceRefName());
        vo.setContentDesc(content.getContentDesc());
        vo.setStandardDesc(content.getStandardDesc());
        vo.setSort(content.getSort());
        return vo;
    }

    private FormAppraisalRuleItemVO toItemVO(FormAppraisalRuleItem item) {
        FormAppraisalRuleItemVO vo = new FormAppraisalRuleItemVO();
        vo.setId(item.getId());
        vo.setItemName(item.getItemName());
        vo.setFieldKey(item.getFieldKey());
        vo.setCalcType(item.getCalcType());
        vo.setSort(item.getSort());
        return vo;
    }

    private void fillDepartmentInfo(FormAppraisalRulePageVO vo, String responsibleDepartmentIds, Map<Long, OrganizationDTO> departmentMap) {
        List<Long> departmentIds = AppraisalDepartmentUtils.parseDepartmentIds(responsibleDepartmentIds);
        vo.setResponsibleDepartmentIds(departmentIds);
        vo.setResponsibleDepartmentNames(AppraisalDepartmentUtils.joinDepartmentNames(
                departmentIds.stream().map(id -> {
                    OrganizationDTO department = departmentMap.get(id);
                    return department == null ? "" : department.getName();
                }).toList()
                                                                                     ));
    }

    private void fillDepartmentInfo(FormAppraisalRuleDetailVO vo, String responsibleDepartmentIds) {
        List<Long> departmentIds = AppraisalDepartmentUtils.parseDepartmentIds(responsibleDepartmentIds);
        vo.setResponsibleDepartmentIds(departmentIds);
        vo.setResponsibleDepartmentNames(AppraisalDepartmentUtils.joinDepartmentNames(
                organizationIntegrationService.getDepartmentNames(departmentIds)
                                                                                     ));
    }
}
