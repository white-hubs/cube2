package com.guhecloud.cube.srv.gen.safety.education.server.common.converts;

import com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command.ChangeExamQuestionRuleCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command.IncreaseExamQuestionRuleCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ExamQuestionRulePO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleItemVO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleOptionVO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleVO;

import java.util.List;

/**
 * 试卷考题规则 PO / Command / VO 转换
 */
public class ExamQuestionRuleCovert {

    private ExamQuestionRuleCovert() {
    }

    public static ExamQuestionRuleOptionVO toOptionVo(ExamQuestionRulePO po) {
        ExamQuestionRuleOptionVO vo = new ExamQuestionRuleOptionVO();
        vo.setId(po.getId());
        vo.setRuleName(po.getRuleName());
        return vo;
    }

    public static ExamQuestionRuleVO toVo(ExamQuestionRulePO po, List<ExamQuestionRuleItemVO> items) {
        ExamQuestionRuleVO vo = new ExamQuestionRuleVO();
        vo.setId(po.getId());
        vo.setRuleName(po.getRuleName());
        vo.setTotalScore(po.getTotalScore());
        vo.setPassScore(po.getPassScore());
        vo.setQuestionNum(po.getQuestionNum());
        vo.setDescription(po.getDescription());
        vo.setUpdateTime(po.getUpdateTime());
        vo.setItems(items);
        return vo;
    }

    public static ExamQuestionRulePO newPoFromIncrease(IncreaseExamQuestionRuleCommand command, int totalScore, int questionNum) {
        ExamQuestionRulePO po = new ExamQuestionRulePO();
        po.setRuleName(command.getRuleName());
        po.setPassScore(command.getPassScore());
        po.setDescription(command.getDescription());
        po.setTotalScore(totalScore);
        po.setQuestionNum(questionNum);
        return po;
    }

    public static void applyChange(ChangeExamQuestionRuleCommand command, ExamQuestionRulePO po, int totalScore, int questionNum) {
        po.setRuleName(command.getRuleName());
        po.setPassScore(command.getPassScore());
        po.setDescription(command.getDescription());
        po.setTotalScore(totalScore);
        po.setQuestionNum(questionNum);
    }
}
