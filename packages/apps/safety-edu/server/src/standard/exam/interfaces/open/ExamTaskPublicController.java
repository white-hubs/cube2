package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.open;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.ExamTaskCommandService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.PublishedExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.WithdrawExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamTaskPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamTaskQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.ExamTaskQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamTaskVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 考试任务管理接口
 *
 * @author lanchun
 * @date 2024/4/10 17:48
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamTaskPublicController {

    private final ExamTaskCommandService examTaskCommandService;

    private final ExamTaskQueryService examTaskQueryService;

    private final ExamTaskPaperQueryService examTaskPaperQueryService;

    /**
     * 考试任务详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/public/exam-task/{id}")
    public ExamTaskVO getExamTask(@PathVariable("id") Long id,
                                  @RequestParam(value = "userIdentify", required = false) String userIdentify
                                 ) {
        return this.examTaskQueryService.getPublicExamTask(id, userIdentify);
    }

    /**
     * 考试任务分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/public/exam-task/page")
    public Pagination<ExamTaskVO> getExamTaskPage(@Valid ExamTaskQuery query) {
        return this.examTaskQueryService.getPublicExamTaskPage(query);
    }

    /**
     * 考试任务发布
     *
     * @param id
     */
    @PutMapping("/public/exam-task/published/{id}")
    public void examTaskPublished(@PathVariable("id") Long id) {
        this.examTaskCommandService.publishedPublicExamTask(new PublishedExamTaskCommand(id));
    }

    /**
     * 考试任务撤回
     *
     * @param id
     */
    @PutMapping("/public/exam-task/withdraw/{id}")
    public void examTaskWithdraw(@PathVariable("id") Long id) {
        this.examTaskCommandService.withdrawExamTask(new WithdrawExamTaskCommand(id));
    }

    /**
     * 获取考试试卷详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/public/exam-task/{id}/paper/{examPaperId}")
    public ExamPaperVO getExamTaskPaper(@PathVariable("id") Long id, @PathVariable("examPaperId") Long examPaperId) {
        return this.examTaskPaperQueryService.getExamTaskPaperVO(id, examPaperId);
    }

}
