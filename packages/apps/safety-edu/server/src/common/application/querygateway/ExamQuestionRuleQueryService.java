package com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ExamQuestionRuleItemService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ExamQuestionRuleService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.query.ExamQuestionRuleQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.common.converts.ExamQuestionRuleCovert;
import com.guhecloud.cube.srv.gen.safety.education.server.common.converts.ExamQuestionRuleItemCovert;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ExamQuestionRuleItemPO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ExamQuestionRulePO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleItemVO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleOptionVO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamQuestionRuleQueryService {

    private final ExamQuestionRuleService examQuestionRuleService;

    private final ExamQuestionRuleItemService examQuestionRuleItemService;

    /**
     * 按主键批量查询考题规则（用于随机组卷）
     */
    public List<ExamQuestionRuleVO> listExamQuestionRulesByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<ExamQuestionRulePO> rulePOS = this.examQuestionRuleService.list(Wrappers.<ExamQuestionRulePO>lambdaQuery()
                .in(ExamQuestionRulePO::getId, ids));
        if (CollectionUtils.isEmpty(rulePOS)) {
            return Collections.emptyList();
        }
        List<Long> ruleIdList = rulePOS.stream().map(ExamQuestionRulePO::getId).collect(Collectors.toList());
        List<ExamQuestionRuleItemPO> itemPOS = this.examQuestionRuleItemService.list(Wrappers
                .<ExamQuestionRuleItemPO>lambdaQuery()
                .in(ExamQuestionRuleItemPO::getRuleId, ruleIdList)
                .orderByAsc(ExamQuestionRuleItemPO::getId));
        Map<Long, List<ExamQuestionRuleItemPO>> itemMap = itemPOS.stream()
                .collect(Collectors.groupingBy(ExamQuestionRuleItemPO::getRuleId));
        return rulePOS.stream().map(po -> toVo(po, itemMap.get(po.getId()))).collect(Collectors.toList());
    }

    /**
     * 试卷考题规则下拉列表（仅 id、名称）
     */
    public List<ExamQuestionRuleOptionVO> getExamQuestionRuleOptionList() {
        List<ExamQuestionRulePO> rulePOS = this.examQuestionRuleService.list(Wrappers.<ExamQuestionRulePO>lambdaQuery()
                                                                                     .select(ExamQuestionRulePO::getId, ExamQuestionRulePO::getRuleName)
                                                                                     .orderByDesc(ExamQuestionRulePO::getCreateTime));
        if (CollectionUtils.isEmpty(rulePOS)) {
            return Collections.emptyList();
        }
        return rulePOS.stream().map(ExamQuestionRuleCovert::toOptionVo).collect(Collectors.toList());
    }

    /**
     * 试卷考题规则列表
     */
    public Pagination<ExamQuestionRuleVO> getExamQuestionRuleList(ExamQuestionRuleQuery query) {
        Page<ExamQuestionRulePO> page = this.examQuestionRuleService.page(PagingUtils.convert(query), Wrappers
                .<ExamQuestionRulePO>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getKeyword()), ExamQuestionRulePO::getRuleName, query.getKeyword())
                .orderByDesc(ExamQuestionRulePO::getCreateTime));
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        return toPaginationVo(page);
    }

    /**
     * 试卷考题规则详情
     */
    public ExamQuestionRuleVO getExamQuestionRuleDetail(Long id) {
        ExamQuestionRulePO rulePO = this.examQuestionRuleService.getById(id);
        Assert.isNotFound(rulePO, "试卷考题规则不存在");
        return toDetailVo(rulePO);
    }

    private Pagination<ExamQuestionRuleVO> toPaginationVo(Page<ExamQuestionRulePO> page) {
        List<Long> ruleIds = page.getRecords().stream().map(ExamQuestionRulePO::getId).collect(Collectors.toList());
        List<ExamQuestionRuleItemPO> itemPOS = this.examQuestionRuleItemService.list(Wrappers
                                                                                             .<ExamQuestionRuleItemPO>lambdaQuery()
                                                                                             .in(ExamQuestionRuleItemPO::getRuleId, ruleIds)
                                                                                             .orderByAsc(ExamQuestionRuleItemPO::getId));
        Map<Long, List<ExamQuestionRuleItemPO>> itemMap = CollectionUtils.isEmpty(itemPOS) ? Collections.emptyMap() :
                                                          itemPOS.stream()
                                                                  .collect(Collectors.groupingBy(ExamQuestionRuleItemPO::getRuleId));
        List<ExamQuestionRuleVO> vos = page.getRecords().stream()
                .map(rulePO -> toVo(rulePO, itemMap.get(rulePO.getId())))
                .collect(Collectors.toList());
        return PagingUtils.convert(page, vos);
    }

    private ExamQuestionRuleVO toDetailVo(ExamQuestionRulePO rulePO) {
        List<ExamQuestionRuleItemPO> itemPOS = this.examQuestionRuleItemService.list(Wrappers
                                                                                             .<ExamQuestionRuleItemPO>lambdaQuery()
                                                                                             .eq(ExamQuestionRuleItemPO::getRuleId, rulePO.getId())
                                                                                             .orderByAsc(ExamQuestionRuleItemPO::getId));
        return toVo(rulePO, itemPOS);
    }

    private ExamQuestionRuleVO toVo(ExamQuestionRulePO rulePO, List<ExamQuestionRuleItemPO> itemPOS) {
        List<ExamQuestionRuleItemVO> itemVOS = CollectionUtils.isEmpty(itemPOS) ? Collections.emptyList() :
                                               itemPOS.stream().map(ExamQuestionRuleItemCovert::toVo).collect(Collectors.toList());
        return ExamQuestionRuleCovert.toVo(rulePO, itemVOS);
    }
}
