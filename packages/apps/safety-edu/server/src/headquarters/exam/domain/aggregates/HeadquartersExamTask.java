package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.aggregates;

import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.ChangeHeadquartersExamTaskMarketCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.valueobj.HeadquartersExamTaskMarket;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamTaskInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamTaskPaper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskChargeUserInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskDateInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskPublishedInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskRetakeInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskStatus;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskTimedInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskType;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ThemeInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.utils.ExamTaskPaperConvertUtils;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 总部考试任务
 *
 * @author lanchun
 * @date 2024/4/9 17:12
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HeadquartersExamTask {

    private Long id;

    /**
     * 任务基础信息
     */
    private ExamTaskInfo examTaskInfo;

    /**
     * 主题信息
     */
    private ThemeInfo themeInfo;

    /**
     * 考试时间信息
     */
    private ExamTaskDateInfo examTaskDateInfo;

    /**
     * 负责人信息
     */
    private ExamTaskChargeUserInfo examTaskChargeUserInfo;

    /**
     * 试卷信息
     */
    private List<ExamTaskPaper> examPapers;

    /**
     * 限时信息
     */
    private ExamTaskTimedInfo examTaskTimedInfo;

    /**
     * 复考信息
     */
    private ExamTaskRetakeInfo examTaskRetakeInfo;

    /**
     * 考试类型
     */
    private ExamTaskType examTaskType;

    /**
     * 考试状态
     */
    private ExamTaskStatus examTaskStatus;

    /**
     * 发布信息
     */
    private ExamTaskPublishedInfo examTaskPublishedInfo;

    /**
     * 考试人员总数
     */
    private Integer examUserNumber;

    /**
     * 考试市场信息
     */
    private List<HeadquartersExamTaskMarket> examTaskMarkets;

    public HeadquartersExamTask(Long id,
                                ExamTaskInfo examTaskInfo,
                                ThemeInfo themeInfo,
                                ExamTaskChargeUserInfo examTaskChargeUserInfo,
                                ExamTaskType examTaskType,
                                ExamTaskStatus examTaskStatus
                               ) {
        this.id = id;
        this.examTaskInfo = examTaskInfo;
        this.themeInfo = themeInfo;
        this.examTaskChargeUserInfo = examTaskChargeUserInfo;
        this.examTaskType = examTaskType;
        this.examTaskStatus = examTaskStatus;
    }

    /**
     * 任务限时设置
     */
    public void settingExamTaskTimedInfo(Boolean timed, Integer examDuration, String examDurationUnit) {
        this.examTaskTimedInfo = new ExamTaskTimedInfo(timed, examDuration, examDurationUnit);
    }

    /**
     * 任务复考设置
     */
    public void settingExamTaskRetakeInfo(Boolean retake, Integer retakeNumber) {
        this.examTaskRetakeInfo = new ExamTaskRetakeInfo(retake, retakeNumber);
    }

    /**
     * 考试人数设置
     *
     * @param examUserNumber
     */
    public void settingExamTaskUserNum(Integer examUserNumber) {
        this.examUserNumber = examUserNumber;
    }

    /**
     * 设置时间信息
     *
     * @param startTime
     * @param endTime
     */
    public void settingExamTaskDateInfo(LocalDateTime startTime, LocalDateTime endTime) {
        this.examTaskDateInfo = new ExamTaskDateInfo(startTime, endTime);
    }

    /**
     * 任务考试人员设置
     *
     * @param examTaskMarkets
     */
    public void settingExamTaskMarketInfos(List<HeadquartersExamTaskMarket> examTaskMarkets) {
        this.examTaskMarkets = examTaskMarkets;
    }

    /**
     * 发布时间设置
     */
    public void settingExamTaskPublishedInfo(LocalDateTime publishedTime) {
        this.examTaskPublishedInfo = new ExamTaskPublishedInfo(publishedTime);
    }

    /**
     * 重置发布时间
     */
    public void resetExamTaskPublishedInfo() {
        this.examTaskPublishedInfo = null;
    }

    /**
     * 修改任务基本信息
     *
     * @param name
     * @param remark
     */
    public void changeExamTaskInfo(String name, String remark) {
        this.examTaskInfo = new ExamTaskInfo(name, remark);
    }

    /**
     * 修改主题信息
     */
    public void changeThemeInfo(Long id) {
        this.themeInfo = new ThemeInfo(id, null);
    }

    /**
     * 试卷信息设置
     *
     * @param examPapers
     */
    public void settingExamPaper(List<ExamTaskPaper> examPapers) {
        this.examPapers = examPapers;
    }

    /**
     * 修改试卷信息
     */
    public void changeExamPaper(Set<Long> examPaperIds) {
        //试卷信息
        ExamPaperQueryService examPaperQueryService = ApplicationContextHolder.getBean(ExamPaperQueryService.class);
        List<ExamPaperVO> examPapersIfExist = examPaperQueryService.getExamPapersIfExist(examPaperIds);
        if (!CollectionUtils.isEmpty(examPapersIfExist)) {
            this.examPapers = ExamTaskPaperConvertUtils.examPaperVOConvertExamTaskPaper(examPapersIfExist);
        }
    }

    /**
     * 修改试卷类型
     */
    public void changeExamTaskType(ExamTaskTypeEnum examTaskType) {
        this.examTaskType = new ExamTaskType(examTaskType, examTaskType.getDescription());
    }

    /**
     * 修改负责人
     *
     * @param userId
     */
    public void changeExamTaskChargeUserInfo(Long userId) {
        this.examTaskChargeUserInfo = new ExamTaskChargeUserInfo(userId, null);
    }

    /**
     * 修改考试用户
     *
     * @param changeHeadquartersExamTaskMarketCommands
     */
    public void changeExamTaskMarketInfos(List<ChangeHeadquartersExamTaskMarketCommand> changeHeadquartersExamTaskMarketCommands) {
        List<HeadquartersExamTaskMarket> examTaskMarkets = new ArrayList<>(changeHeadquartersExamTaskMarketCommands.size());
        changeHeadquartersExamTaskMarketCommands.forEach(changeExamTaskUserCommand ->
                                                                 examTaskMarkets.add(new HeadquartersExamTaskMarket(
                                                                                             changeExamTaskUserCommand.getMarketCode(),
                                                                                             changeExamTaskUserCommand.getMarketTenantCode(),
                                                                                             changeExamTaskUserCommand.getMarketName()
                                                                                     )
                                                                                    ));
        this.examTaskMarkets = examTaskMarkets;
    }

    /**
     * 修改考试状态
     *
     * @param examTaskStatus
     */
    public void changeExamTaskStatus(ExamTaskStatusEnum examTaskStatus) {
        this.examTaskStatus = new ExamTaskStatus(examTaskStatus, examTaskStatus.getDescription());
    }
}
