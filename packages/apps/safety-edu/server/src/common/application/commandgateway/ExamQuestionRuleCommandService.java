package com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ExamQuestionRuleItemService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ExamQuestionRuleService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command.ChangeExamQuestionRuleCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command.ExamQuestionRuleItemCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command.IncreaseExamQuestionRuleCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.common.converts.ExamQuestionRuleCovert;
import com.guhecloud.cube.srv.gen.safety.education.server.common.converts.ExamQuestionRuleItemCovert;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ExamQuestionRuleItemPO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ExamQuestionRulePO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamQuestionRuleCommandService {

    private final ExamQuestionRuleService examQuestionRuleService;

    private final ExamQuestionRuleItemService examQuestionRuleItemService;

    /**
     * 新增试卷考题规则
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean createExamQuestionRule(IncreaseExamQuestionRuleCommand command) {
        int totalScore = calculateTotalScore(command.getItems());
        int questionNum = calculateQuestionNum(command.getItems());
        validateRule(command.getPassScore(), totalScore);

        ExamQuestionRulePO rulePO = ExamQuestionRuleCovert.newPoFromIncrease(command, totalScore, questionNum);
        this.examQuestionRuleService.save(rulePO);
        saveItems(rulePO.getId(), command.getItems());
        return true;
    }

    /**
     * 修改试卷考题规则
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeExamQuestionRule(ChangeExamQuestionRuleCommand command) {
        ExamQuestionRulePO rulePO = this.examQuestionRuleService.getById(command.getId());
        Assert.isNotFound(rulePO, "试卷考题规则不存在");

        int totalScore = calculateTotalScore(command.getItems());
        int questionNum = calculateQuestionNum(command.getItems());
        validateRule(command.getPassScore(), totalScore);

        clearItems(command.getId());
        ExamQuestionRuleCovert.applyChange(command, rulePO, totalScore, questionNum);
        this.examQuestionRuleService.updateById(rulePO);
        saveItems(rulePO.getId(), command.getItems());
        return true;
    }

    /**
     * 删除试卷考题规则
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeExamQuestionRule(Long id) {
        ExamQuestionRulePO rulePO = this.examQuestionRuleService.getById(id);
        Assert.isNotFound(rulePO, "试卷考题规则不存在");
        clearItems(id);
        DatabaseContextHolder.softDelete(ExamQuestionRulePO.class, id);
        return true;
    }

    private void saveItems(Long ruleId, List<ExamQuestionRuleItemCommand> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        List<ExamQuestionRuleItemPO> itemPOS = items.stream().map(cmd -> ExamQuestionRuleItemCovert.newPo(ruleId, cmd)).toList();
        this.examQuestionRuleItemService.saveBatch(itemPOS);
    }

    private void clearItems(Long ruleId) {
        List<ExamQuestionRuleItemPO> itemPOS = this.examQuestionRuleItemService.list(Wrappers
                .<ExamQuestionRuleItemPO>lambdaQuery()
                .eq(ExamQuestionRuleItemPO::getRuleId, ruleId));
        if (!CollectionUtils.isEmpty(itemPOS)) {
            DatabaseContextHolder.softDelete(ExamQuestionRuleItemPO.class, itemPOS);
        }
    }

    private int calculateTotalScore(List<ExamQuestionRuleItemCommand> items) {
        return items.stream().mapToInt(detail -> detail.getExtractCount() * detail.getPerScore()).sum();
    }

    private int calculateQuestionNum(List<ExamQuestionRuleItemCommand> items) {
        return items.stream().mapToInt(ExamQuestionRuleItemCommand::getExtractCount).sum();
    }

    private void validateRule(Integer passScore, int totalScore) {
        Assert.isNotAcceptable(passScore < 0, "及格分数不能小于0");
        Assert.isNotAcceptable(passScore > totalScore, "及格分数不能大于试卷总分");
    }
}
