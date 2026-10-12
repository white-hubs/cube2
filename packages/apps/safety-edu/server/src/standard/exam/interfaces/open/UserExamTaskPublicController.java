package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.open;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserRecordService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.UserExamTaskRecordCommandService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreasePublicExamTaskUserRecordCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamTaskPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamTaskQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.open.ExamTaskOpenQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserRecordPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.UserExamTaskRecordSubmittedVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.UserExamTaskRecordVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.UserExamTaskVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 用户考试任务接口管理
 *
 * @author lanchun
 * @date 2024/4/10 18:02
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserExamTaskPublicController {

    private final UserExamTaskRecordCommandService userExamTaskRecordCommandService;

    private final ExamTaskQueryService examTaskQueryService;

    private final ExamTaskUserRecordService examTaskUserRecordService;

    private final ExamTaskPaperQueryService examTaskPaperQueryService;

    /**
     * 用户考试任务分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/public/user-exam-task/page")
    public Pagination<UserExamTaskVO> getUserExamTaskPage(@Valid ExamTaskOpenQuery query) {
        return this.examTaskQueryService.getPublicUserExamTaskPage(query);
    }

    /**
     * 用户考试任务考试记录列表
     *
     * @param examTaskId
     *
     * @return
     */
    @GetMapping("/public/user-exam-task/{examTaskId}/records")
    public List<UserExamTaskRecordVO> getUserExamTaskRecords(@PathVariable("examTaskId") Long examTaskId, @RequestParam String userIdentify
                                                            ) {
        List<ExamTaskUserRecordPO> examTaskUserRecordPOS = this.examTaskUserRecordService.list(Wrappers.<ExamTaskUserRecordPO>lambdaQuery()
                                                                                                       .eq(ExamTaskUserRecordPO::getExamTaskId, examTaskId)
                                                                                                       .eq(ExamTaskUserRecordPO::getUserIdentify, userIdentify)
                                                                                                       .orderByDesc(ExamTaskUserRecordPO::getCreateTime));
        return ObjectUtils.copying(examTaskUserRecordPOS, UserExamTaskRecordVO.class);
    }

    /**
     * 用户获取待考试试卷
     *
     * @author lanchun
     * @date 2024/9/10 14:17
     */
    @GetMapping("/public/user-exam-task/{examTaskId}/paper")
    public ExamPaperVO userGetExamTaskPaper(@PathVariable("examTaskId") Long examTaskId, @RequestParam String userIdentify) {
        return this.examTaskPaperQueryService.userGetExamTaskPaper(examTaskId, userIdentify);
    }

    /**
     * 用户考试任务考试记录提交
     *
     * @param increaseExamTaskUserRecordCommand
     */
    @PostMapping("/public/user-exam-task/record/submitted")
    public UserExamTaskRecordSubmittedVO increaseUserExamTaskRecord(
            @Valid @RequestBody IncreasePublicExamTaskUserRecordCommand increaseExamTaskUserRecordCommand
                                                                   ) {
        return this.userExamTaskRecordCommandService.increasePublicExamTaskUserRecord(increaseExamTaskUserRecordCommand);
    }

    /**
     * 用户考试任务试卷记录信息
     *
     * @return
     */
    @GetMapping("/public/user-exam-task/record/{userExamTaskRecordId}/paper")
    public ExamPaperVO getUserExamTaskPaperRecord(@PathVariable("userExamTaskRecordId") Long userExamTaskRecordId) {
        return this.examTaskUserRecordService.getUserTaskRecordPaperInfo(userExamTaskRecordId);
    }
}
