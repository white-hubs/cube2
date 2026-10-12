package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.ChangeExamPaperCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.DeleteExamPaperCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreaseExamPaperCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator.ChangeExamPaperValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator.DeleteExamPaperValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.ExamPaper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.factory.ExamPaperFactory;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.ExamPaperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamPaperCommandService {

    private final ExamPaperRepository examPaperRepository;

    private final ChangeExamPaperValidator changeExamPaperValidator;

    private final DeleteExamPaperValidator deleteExamPaperValidator;

    /**
     * 考卷新增
     *
     * @param increaseExamPaperCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void increaseExamPaper(IncreaseExamPaperCommand increaseExamPaperCommand) {
        ExamPaper examPaper = ExamPaperFactory.newExamPaper(increaseExamPaperCommand);
        this.examPaperRepository.save(examPaper);
    }

    /**
     * 考卷编辑
     *
     * @param changeExamPaperCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeExamPaper(ChangeExamPaperCommand changeExamPaperCommand) {
        //编辑校验
        this.changeExamPaperValidator.verify(changeExamPaperCommand);
        //试卷实体信息
        ExamPaper examPaper = this.examPaperRepository.get(changeExamPaperCommand.getId()).get();
        //修改基础信息
        examPaper.changeExamPaperInfo(changeExamPaperCommand.getName(), changeExamPaperCommand.getScore(), changeExamPaperCommand.getPassingGradeScore(), changeExamPaperCommand.getRemark());
        //修改大题信息
        examPaper.settingExamPaperQuestionCategoryInfos(changeExamPaperCommand.getQuestionCategories());
        this.examPaperRepository.save(examPaper);
    }

    /**
     * 考卷删除
     *
     * @param deleteExamPaperCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteExamPaper(DeleteExamPaperCommand deleteExamPaperCommand) {
        //删除校验
        this.deleteExamPaperValidator.verify(deleteExamPaperCommand);
        this.examPaperRepository.deleteBatchByIds(deleteExamPaperCommand.getExamPaperIds());
    }
}
