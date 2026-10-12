package com.guhecloud.cube.srv.gen.safety.education.server.common.converts;

import com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command.ExamQuestionRuleItemCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ExamQuestionRuleItemPO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleItemVO;

/**
 * 试卷考题规则项 PO / Command / VO 转换
 */
public class ExamQuestionRuleItemCovert {

    private ExamQuestionRuleItemCovert() {
    }

    public static ExamQuestionRuleItemVO toVo(ExamQuestionRuleItemPO po) {
        ExamQuestionRuleItemVO vo = new ExamQuestionRuleItemVO();
        vo.setId(po.getId());
        vo.setRuleId(po.getRuleId());
        vo.setThemeKeywords(po.getThemeKeywords());
        vo.setExamQuestionType(po.getExamQuestionType());
        vo.setExtractCount(po.getExtractCount());
        vo.setPerScore(po.getPerScore());
        return vo;
    }

    public static ExamQuestionRuleItemPO newPo(Long ruleId, ExamQuestionRuleItemCommand command) {
        ExamQuestionRuleItemPO po = new ExamQuestionRuleItemPO();
        po.setRuleId(ruleId);
        po.setThemeKeywords(command.getThemeKeywords());
        po.setExamQuestionType(command.getExamQuestionType());
        po.setExtractCount(command.getExtractCount());
        po.setPerScore(command.getPerScore());
        return po;
    }
}
