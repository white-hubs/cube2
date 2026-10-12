package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.factory;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.IncreaseHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.aggregates.HeadquartersExamTask;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.valueobj.HeadquartersExamTaskMarket;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamTaskInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamTaskPaper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskChargeUserInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskStatus;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskType;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ThemeInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.utils.ExamTaskPaperConvertUtils;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;

import java.util.ArrayList;
import java.util.List;

public class HeadquartersExamTaskFactory {

    private HeadquartersExamTaskFactory() {

    }

    public static HeadquartersExamTask newExamTask(final IncreaseHeadquartersExamTaskCommand increaseExamTaskCommand) {
        //基础信息
        ExamTaskInfo examTaskInfo = new ExamTaskInfo(increaseExamTaskCommand.getName(), increaseExamTaskCommand.getRemark());
        //主题信息
        ThemeInfo themeInfo = new ThemeInfo(increaseExamTaskCommand.getThemeId(), null);
        //负责人信息
        ExamTaskChargeUserInfo examTaskChargeUserInfo = new ExamTaskChargeUserInfo(increaseExamTaskCommand.getChargeUserId(), null);
        //考试类型
        ExamTaskType examTaskType = new ExamTaskType(increaseExamTaskCommand.getExamTaskType(), increaseExamTaskCommand.getExamTaskType()
                .getDescription());
        //任务状态
        ExamTaskStatus examTaskStatus = new ExamTaskStatus(ExamTaskStatusEnum.UNPUBLISHED, ExamTaskStatusEnum.UNPUBLISHED.getDescription());
        //考试市场
        List<HeadquartersExamTaskMarket> examTaskMarkets = new ArrayList<>(increaseExamTaskCommand.getExamTaskMarkets().size());
        increaseExamTaskCommand.getExamTaskMarkets().forEach(increaseExamTaskUserCommand ->
                                                                     examTaskMarkets.add(new HeadquartersExamTaskMarket(
                                                                             increaseExamTaskUserCommand.getMarketCode(),
                                                                             increaseExamTaskUserCommand.getMarketTenantCode(),
                                                                             increaseExamTaskUserCommand.getMarketName()
                                                                     ))
                                                            );
        HeadquartersExamTask examTask = new HeadquartersExamTask(IdWorker.getId(), examTaskInfo, themeInfo, examTaskChargeUserInfo, examTaskType, examTaskStatus);
        //考试时间信息
        examTask.settingExamTaskDateInfo(increaseExamTaskCommand.getExamStartTime(), increaseExamTaskCommand.getExamEndTime());
        //考试试卷
        examTask.settingExamPaper(getExamTaskPapers(increaseExamTaskCommand));
        //复考信息
        examTask.settingExamTaskRetakeInfo(increaseExamTaskCommand.getRetake(), increaseExamTaskCommand.getRetakeNumber());
        //限时信息
        examTask.settingExamTaskTimedInfo(increaseExamTaskCommand.getTimed(), increaseExamTaskCommand.getExamDuration(), increaseExamTaskCommand.getExamDurationUnit());
        //考试人数
        examTask.settingExamTaskUserNum(increaseExamTaskCommand.getExamUserNumber());
        //考试市场
        examTask.settingExamTaskMarketInfos(examTaskMarkets);
        return examTask;
    }

    private static List<ExamTaskPaper> getExamTaskPapers(IncreaseHeadquartersExamTaskCommand increaseExamTaskCommand) {
        //试卷信息
        ExamPaperQueryService examPaperQueryService = ApplicationContextHolder.getBean(ExamPaperQueryService.class);
        List<ExamPaperVO> examPapersIfExist = examPaperQueryService.getExamPapersIfExist(increaseExamTaskCommand.getExamPaperIds());
        //考试试卷
        return ExamTaskPaperConvertUtils.examPaperVOConvertExamTaskPaper(examPapersIfExist);
    }
}
