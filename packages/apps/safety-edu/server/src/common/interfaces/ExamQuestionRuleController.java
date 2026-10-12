package com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces;

import com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.ExamQuestionRuleCommandService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command.ChangeExamQuestionRuleCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command.IncreaseExamQuestionRuleCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.ExamQuestionRuleQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.query.ExamQuestionRuleQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleOptionVO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 试卷考题规则
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/exam-question-rule")
public class ExamQuestionRuleController {

    private final ExamQuestionRuleCommandService examQuestionRuleCommandService;

    private final ExamQuestionRuleQueryService examQuestionRuleQueryService;

    /**
     * 新增试卷考题规则
     */
    @PostMapping("/create")
    public Boolean createExamQuestionRule(@Valid @RequestBody IncreaseExamQuestionRuleCommand command) {
        return this.examQuestionRuleCommandService.createExamQuestionRule(command);
    }

    /**
     * 修改试卷考题规则
     */
    @PutMapping("/change")
    public Boolean changeExamQuestionRule(@Valid @RequestBody ChangeExamQuestionRuleCommand command) {
        return this.examQuestionRuleCommandService.changeExamQuestionRule(command);
    }

    /**
     * 删除试卷考题规则
     */
    @DeleteMapping("/remove/{id}")
    public Boolean removeExamQuestionRule(@PathVariable("id") Long id) {
        return this.examQuestionRuleCommandService.removeExamQuestionRule(id);
    }

    /**
     * 试卷考题规则下拉列表（仅 id、名称）
     */
    @GetMapping("/option-list")
    public List<ExamQuestionRuleOptionVO> getExamQuestionRuleOptionList() {
        return this.examQuestionRuleQueryService.getExamQuestionRuleOptionList();
    }

    /**
     * 试卷考题规则列表
     */
    @GetMapping("/list")
    public Pagination<ExamQuestionRuleVO> getExamQuestionRuleList(@Valid ExamQuestionRuleQuery query) {
        return this.examQuestionRuleQueryService.getExamQuestionRuleList(query);
    }

    /**
     * 试卷考题规则详情
     */
    @GetMapping("/detail/{id}")
    public ExamQuestionRuleVO getExamQuestionRuleDetail(@PathVariable("id") Long id) {
        return this.examQuestionRuleQueryService.getExamQuestionRuleDetail(id);
    }
}
