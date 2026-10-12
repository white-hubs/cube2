package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.ExamPaperCommandService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.ChangeExamPaperCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.DeleteExamPaperCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreaseExamPaperCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.ExamPaperQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Collections;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamPaperController {

    private final ExamPaperCommandService examPaperCommandService;

    private final ExamPaperQueryService examPaperQueryService;

    /**
     * 试卷新增
     *
     * @param increaseExamPaperCommand
     */
    @PostMapping("/exam-paper")
    public void increaseExamPaper(@Valid @RequestBody IncreaseExamPaperCommand increaseExamPaperCommand) {
        this.examPaperCommandService.increaseExamPaper(increaseExamPaperCommand);
    }

    /**
     * 试卷编辑
     *
     * @param id
     * @param changeExamPaperCommand
     */
    @PutMapping("/exam-paper/{id}")
    public void changeExamPaper(@PathVariable("id") Long id, @Valid @RequestBody ChangeExamPaperCommand changeExamPaperCommand) {
        changeExamPaperCommand.setId(id);
        this.examPaperCommandService.changeExamPaper(changeExamPaperCommand);
    }

    /**
     * 试卷详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/exam-paper/{id}")
    public ExamPaperVO getExamPaper(@PathVariable("id") Long id) {
        return this.examPaperQueryService.getExamPaper(id);
    }

    /**
     * 试卷分页列表
     *
     * @param examPaperQuery
     *
     * @return
     */
    @GetMapping("/exam-paper/page")
    public Pagination<ExamPaperVO> getExamPaperPage(@Valid ExamPaperQuery examPaperQuery) {
        return this.examPaperQueryService.getExamPaperPage(examPaperQuery);
    }

    /**
     * 试卷删除
     *
     * @param id
     */
    @DeleteMapping("/exam-paper/{id}")
    public void deleteBatch(@PathVariable("id") Long id) {
        DeleteExamPaperCommand deleteExamPaperCommand = new DeleteExamPaperCommand(Collections.singleton(id));
        this.examPaperCommandService.deleteExamPaper(deleteExamPaperCommand);
    }
}
