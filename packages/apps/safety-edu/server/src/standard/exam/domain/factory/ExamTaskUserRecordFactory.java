package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.factory;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreaseExamTaskUserRecordCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreaseExamTaskUserRecordQuestionCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreasePublicExamTaskUserRecordCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.ExamPaper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.UserExamTaskRecord;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperQuestionCategoryInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperQuestionInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamQuestionType;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskUserInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ThemeInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamTaskPaperMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamTaskUserMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户考试记录构造工厂
 *
 * @author lanchun
 * @date 2023/4/20 14:26
 */
public class ExamTaskUserRecordFactory {

    private ExamTaskUserRecordFactory() {

    }

    public static UserExamTaskRecord newUserExamTaskRecord(final IncreaseExamTaskUserRecordCommand increaseExamTaskUserRecordCommand) {
        //考试试卷信息
        BuildUserExamPaperInfo buildUserExamPaperInfo = new BuildUserExamPaperInfo(increaseExamTaskUserRecordCommand).invoke();
        Double userScore = buildUserExamPaperInfo.getUserScore();
        Boolean passed = buildUserExamPaperInfo.getPassed();
        ExamPaper examPaper = buildUserExamPaperInfo.getExamPaper();
        //用户信息
        ExamTaskUserMapper examTaskUserMapper = ApplicationContextHolder.getBean(ExamTaskUserMapper.class);
        ExamTaskUserPO examTaskUserPO = examTaskUserMapper.selectOne(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                             .eq(ExamTaskUserPO::getExamTaskId, increaseExamTaskUserRecordCommand.getExamTaskId())
                                                                             .eq(ExamTaskUserPO::getUserId, SecurityContextHolder
                                                                                     .getPrincipal().getId()));
        ExamTaskUserInfo examTaskUserInfo = new ExamTaskUserInfo(
                examTaskUserPO.getUserId(),
                examTaskUserPO.getUserIdentify(),
                examTaskUserPO.getUserName(),
                examTaskUserPO.getUserPhone(),
                examTaskUserPO.getUserDepartmentId(),
                examTaskUserPO.getUserDepartmentName(),
                examTaskUserPO.getUserPostId(),
                examTaskUserPO.getUserPostName());
        return getUserExamTaskRecord(userScore, passed, examPaper, examTaskUserInfo, increaseExamTaskUserRecordCommand.getExamTaskId(), increaseExamTaskUserRecordCommand.getExamDuration());
    }

    public static UserExamTaskRecord newPublicUserExamTaskRecord(
            final IncreasePublicExamTaskUserRecordCommand increaseExamTaskUserRecordCommand
                                                                ) {
        //构建试卷信息
        BuildUserExamPaperInfo buildUserExamPaperInfo = new BuildUserExamPaperInfo(increaseExamTaskUserRecordCommand).invoke();
        Double userScore = buildUserExamPaperInfo.getUserScore();
        Boolean passed = buildUserExamPaperInfo.getPassed();
        ExamPaper examPaper = buildUserExamPaperInfo.getExamPaper();
        //获取任务信息
        ExamTaskService examTaskService = ApplicationContextHolder.getBean(ExamTaskService.class);
        ExamTaskPO examTaskPO = examTaskService.getById(increaseExamTaskUserRecordCommand.getExamTaskId());
        //用户信息
        ExamTaskUserInfo examTaskUserInfo = new ExamTaskUserInfo(
                null,
                increaseExamTaskUserRecordCommand.getUserInfo().getUserIdentify(),
                increaseExamTaskUserRecordCommand.getUserInfo().getUserName(),
                increaseExamTaskUserRecordCommand.getUserInfo().getUserPhone(),
                null,
                increaseExamTaskUserRecordCommand.getUserInfo().getUserDepartmentName(),
                null,
                increaseExamTaskUserRecordCommand.getUserInfo().getUserPostName());
        UserExamTaskRecord userExamTaskRecord = getUserExamTaskRecord(userScore, passed, examPaper, examTaskUserInfo, increaseExamTaskUserRecordCommand.getExamTaskId(), increaseExamTaskUserRecordCommand.getExamDuration());
        userExamTaskRecord.setTenant(examTaskPO.getTenant());
        return userExamTaskRecord;
    }

    public static UserExamTaskRecord getUserExamTaskRecord(Double userScore, Boolean passed, ExamPaper examPaper,
                                                           ExamTaskUserInfo examTaskUserInfo, Long examTaskId, Integer examDuration
                                                          ) {
        var examTaskSubmittedDate = LocalDateTime.now();
        var examTime = DateUtils.offset(examTaskSubmittedDate, ChronoUnit.SECONDS, -examDuration);
        UserExamTaskRecord userExamTaskRecord = new UserExamTaskRecord(
                IdWorker.getId(),
                examTaskId,
                passed,
                userScore.floatValue(),
                examTime,
                examTaskSubmittedDate,
                examDuration
        );
        //设置考卷信息
        userExamTaskRecord.settingExamPaperInfo(examPaper);
        //设置用户信息
        userExamTaskRecord.settingExamTaskUserInfo(examTaskUserInfo);
        return userExamTaskRecord;
    }

    private static class BuildUserExamPaperInfo {
        private IncreaseExamTaskUserRecordCommand increaseExamTaskUserRecordCommand;
        private Double userScore;
        private Boolean passed;
        private ExamPaper examPaper;

        public BuildUserExamPaperInfo(IncreaseExamTaskUserRecordCommand increaseExamTaskUserRecordCommand) {
            this.increaseExamTaskUserRecordCommand = increaseExamTaskUserRecordCommand;
        }

        public Double getUserScore() {
            return userScore;
        }

        public Boolean getPassed() {
            return passed;
        }

        public ExamPaper getExamPaper() {
            return examPaper;
        }

        public BuildUserExamPaperInfo invoke() {
            userScore = increaseExamTaskUserRecordCommand.getExamQuestions().stream()
                    .filter(increaseExamTaskUserRecordQuestionCommand ->
                                    Objects.equals(Boolean.TRUE, increaseExamTaskUserRecordQuestionCommand.getAnswerState())
                                    && Objects.equals(Boolean.TRUE, increaseExamTaskUserRecordQuestionCommand.getEditState()))
                    .mapToDouble(IncreaseExamTaskUserRecordQuestionCommand::getScore)
                    .sum();
            //试卷信息
            ExamTaskPaperMapper examTaskPaperMapper = ApplicationContextHolder.getBean(ExamTaskPaperMapper.class);
            ExamTaskPaperPO examTaskPaperPO = examTaskPaperMapper.selectOne(Wrappers.<ExamTaskPaperPO>lambdaQuery()
                                                                                    .eq(ExamTaskPaperPO::getExamTaskId, increaseExamTaskUserRecordCommand.getExamTaskId())
                                                                                    .eq(ExamTaskPaperPO::getExamPaperId, increaseExamTaskUserRecordCommand.getExamPaperId()));
            //是否通过考试
            passed = userScore >= examTaskPaperPO.getExamPaperPassingGradeScore();
            //试卷试题
            ExamPaperVO examPaperVO = JsonUtils.deserialize(examTaskPaperPO.getExamPaperContent(), ExamPaperVO.class);
            Map<Long, List<IncreaseExamTaskUserRecordQuestionCommand>> collect = increaseExamTaskUserRecordCommand.getExamQuestions()
                    .stream().collect(Collectors.groupingBy(IncreaseExamTaskUserRecordQuestionCommand::getExamPaperQuestionCategoryId));
            //试卷大题
            List<ExamPaperQuestionCategoryInfo> examPaperQuestionCategoryInfos = new ArrayList<>(collect.keySet().size());
            //大题下的小题
            examPaperVO.getQuestionCategories().stream().forEach(examPaperQuestionCategoryVO -> {
                List<IncreaseExamTaskUserRecordQuestionCommand> increaseExamTaskUserRecordQuestionCommands = collect.get(examPaperQuestionCategoryVO.getId());
                List<ExamPaperQuestionInfo> examPaperQuestionInfos = new ArrayList<>(increaseExamTaskUserRecordQuestionCommands.size());
                increaseExamTaskUserRecordQuestionCommands.stream().forEach(increaseExamTaskUserRecordQuestionCommand -> {
                    ExamPaperQuestionInfo examPaperQuestionInfo = new ExamPaperQuestionInfo(
                            increaseExamTaskUserRecordQuestionCommand.getId(),
                            increaseExamTaskUserRecordQuestionCommand.getName(),
                            new ThemeInfo(increaseExamTaskUserRecordQuestionCommand.getThemeId(), increaseExamTaskUserRecordQuestionCommand.getName()),
                            new ExamQuestionType(increaseExamTaskUserRecordQuestionCommand.getQuestionType(), increaseExamTaskUserRecordQuestionCommand
                                    .getQuestionType().getDescription()),
                            increaseExamTaskUserRecordQuestionCommand.getQuestionContent(),
                            increaseExamTaskUserRecordQuestionCommand.getScore(),
                            increaseExamTaskUserRecordQuestionCommand.getAnswerAnalysis(),
                            increaseExamTaskUserRecordQuestionCommand.getSort());
                    examPaperQuestionInfo.settingAnswerState(increaseExamTaskUserRecordQuestionCommand.getAnswerState());
                    examPaperQuestionInfo.settingEditState(increaseExamTaskUserRecordQuestionCommand.getEditState());
                    examPaperQuestionInfos.add(examPaperQuestionInfo);
                });
                examPaperQuestionCategoryInfos.add(new ExamPaperQuestionCategoryInfo(
                        examPaperQuestionCategoryVO.getId(),
                        examPaperQuestionCategoryVO.getTitle(),
                        examPaperQuestionCategoryVO.getSort(),
                        examPaperQuestionInfos
                ));
            });
            examPaper = new ExamPaper(
                    increaseExamTaskUserRecordCommand.getExamPaperId(),
                    new ExamPaperInfo(examTaskPaperPO.getExamPaperName(), examTaskPaperPO.getExamPaperScore(), examTaskPaperPO.getExamPaperPassingGradeScore(), examTaskPaperPO.getExamPaperRemark()),
                    examPaperQuestionCategoryInfos);
            return this;
        }
    }
}
