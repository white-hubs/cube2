package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.dao.CultivateTaskMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.dao.CultivateTaskUserMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskCoursewareLibrary;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskUser;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainModeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainSignEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainTargetEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.event.CultivateTaskPublishedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.event.UserCultivateSignEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.model.CultivateSignMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.model.CultivateTaskMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateCoursewarParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateSignPublicParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskPublicQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskUserParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskCoursewarLibraryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.util.ExcelUtils;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateCommonStatVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateCoursewarVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateMyTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskDetailVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskPageVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskStatVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskUserVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskWithStatVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateUserStatVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamTaskQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.ExamTaskQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamTaskMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskStatisticsPageVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.query.ExamTaskStatisticsPageQuery;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 描述：培训任务服务层
 *
 * @author Appet
 * @date 2023-04-18 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CultivateTaskServiceImpl extends ServiceImpl<CultivateTaskMapper, CultivateTask> implements CultivateTaskService {
    private final CultivateTaskUserService cultivateTaskUserService;
    private final CultivateTaskUserMapper cultivateTaskUserMapper;
    private final CultivateTaskMapper cultivateTaskMapper;
    private final ExamTaskMapper examTaskMapper;
    private final CultivateTaskCoursewarLibraryService coursewarLibraryService;
    private final ThemeService themeService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserClient userClient;
    private final ExamTaskQueryService examTaskQueryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(CultivateTaskParam param) {
        CultivateTask cultivateTask = ObjectUtils.copying(param, CultivateTask.class);
        //插入主题名称
        ThemePO theme = themeService.getById(param.getThemeId());
        cultivateTask.setThemeName(theme != null ? theme.getName() : param.getThemeName());
        cultivateTask.setCultivateTaskStatus(TrainStatusEnum.WAIT_PUBLISH.name());
        cultivateTask.setMarketTenant(
                Objects.nonNull(param.getMarketTenant()) ? param.getMarketTenant() : SessionContextHolder.getRequester().getTenant());
        //插入来源信息
        if (Objects.nonNull(param.getCultivateTaskSource())) {
            cultivateTask.setSourceCode(param.getCultivateTaskSource().getSourceCode());
            cultivateTask.setSourceName(param.getCultivateTaskSource().getSourceName());
            cultivateTask.setSourceKey(param.getCultivateTaskSource().getSourceKey());
        } else {
            cultivateTask.setSourceCode("LOCAL_CREATE");
            cultivateTask.setSourceName("自建");
        }
        this.baseMapper.insert(cultivateTask);
        //保存培训人员
        this.saveCultivateUser(cultivateTask.getId(), param.getCultivateUsers());
        //保存培训课件
        this.saveCourseware(cultivateTask.getId(), param.getCultivateCoursewares());
        return cultivateTask.getId();
    }

    //保存培训人员
    private void saveCultivateUser(Long id, List<CultivateTaskUserParam> cultivateUsers) {
        //删除之前的培训人员
        cultivateTaskUserService.remove(Wrappers.<CultivateTaskUser>lambdaQuery()
                                                .eq(CultivateTaskUser::getCultivateTaskId, id));
        if (CollectionUtils.isEmpty(cultivateUsers)) {
            return;
        }
        //保存现有的人员
        cultivateUsers.forEach(u -> {
            //初始化人员信息
            if (StringUtils.isEmpty(u.getUserName())) {
                UserDTO user = userClient.getUser(u.getUserId());
                if (user != null) {
                    u.setUserName(user.getName());
                    u.setUserPhone(user.getMobile());
                    //处理部门
                    if (user.getOrganization() != null) {
                        u.setUserDepartmentId(user.getOrganization().getId());
                        u.setUserDepartmentName(user.getOrganization().getName());
                    }
                    //职位处理
                    if (!CollectionUtils.isEmpty(user.getPositions())) {
                        u.setUserPostId(user.getPositions().get(0).getId());
                        u.setUserPostName(user.getPositions().get(0).getName());
                    }
                }
            }
            u.setUserIdentify(u.getUserId().toString());
            u.setCultivateTaskId(id);
            cultivateTaskUserService.add(u);
        });
    }

    //保存培训课件
    private void saveCourseware(Long id, List<CultivateCoursewarParam> coursewars) {
        //删除之前的课件
        coursewarLibraryService.remove(Wrappers.<CultivateTaskCoursewareLibrary>lambdaQuery()
                                               .eq(CultivateTaskCoursewareLibrary::getCultivateTaskId, id));
        //保存课件
        if (!CollectionUtils.isEmpty(coursewars)) {
            coursewars.forEach(c -> {
                c.setCultivateTaskId(id);
                coursewarLibraryService.add(c);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, CultivateTaskParam param) {
        //校验培训是否可以修改：培训开始以后则不能修改
        CultivateTask cultivateTask = this.baseMapper.selectById(id);
        Assert.isNotFound(cultivateTask, "未查询到相关信息");
        Assert.isNotAcceptable(!TrainStatusEnum.WAIT_PUBLISH.name().equals(cultivateTask.getCultivateTaskStatus()), "请撤回后再编辑");

        ObjectUtils.copying(param, cultivateTask);
        //插入主题名称
        ThemePO theme = themeService.getById(param.getThemeId());
        cultivateTask.setThemeName(theme != null ? theme.getName() : param.getThemeName());
        this.baseMapper.updateById(cultivateTask);
        //保存培训人员
        this.saveCultivateUser(cultivateTask.getId(), param.getCultivateUsers());
        //保存培训课件
        this.saveCourseware(cultivateTask.getId(), param.getCultivateCoursewares());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        CultivateTask cultivateTask = this.baseMapper.selectById(id);
        Assert.isNotAcceptable(!TrainStatusEnum.WAIT_PUBLISH.name().equals(cultivateTask.getCultivateTaskStatus())
                               && !TrainStatusEnum.OVER.name().equals(cultivateTask.getCultivateTaskStatus()), "请撤回后再删除");
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public CultivateTaskDetailVO getById(Long id) {
        CultivateTask cultivateTask = this.baseMapper.selectById(id);
        if (cultivateTask == null) {//这里是兼容查询，以便代办查询任务详情时，传递培训人员记录的ID过来
            CultivateTaskUser taskUser = cultivateTaskUserMapper.selectById(id);
            if (taskUser != null) {
                cultivateTask = this.baseMapper.selectById(taskUser.getCultivateTaskId());
            }
        }
        Assert.isNotFound(cultivateTask, "未查询到数据");
        CultivateTaskDetailVO taskVO = ObjectUtils.copying(cultivateTask, CultivateTaskDetailVO.class);
        //枚举转换
        this.convertEnum(taskVO);
        //扩展培训人员和课件信息
        this.extendUserAndCoursewar(taskVO);

        return taskVO;
    }

    private void extendUserAndCoursewar(CultivateTaskDetailVO taskVO) {
        //考试信息
        if (taskVO.getExamTaskId() != null) {
            ExamTaskPO examTaskPO = examTaskMapper.selectById(taskVO.getExamTaskId());
            taskVO.setExamTaskName(examTaskPO != null ? examTaskPO.getName() : "");
            ExamTaskQuery examTaskQuery = new ExamTaskQuery();
            examTaskQuery.setId(taskVO.getExamTaskId());
            taskVO.setExamTaskVO(examTaskQueryService.getUserExamTask(examTaskQuery));
        }
        //查询培训人员
        List<CultivateTaskUserVO> users = cultivateTaskUserService.getCultivateUsersByCultivateId(taskVO.getId());
        taskVO.setCultivateUsers(users);
        //查询课件
        List<CultivateCoursewarVO> coursewars = coursewarLibraryService.getCoursewarsByCultivateId(taskVO.getId());
        taskVO.setCultivateCoursewares(coursewars);
    }

    @Override
    @DataPermissionSwitch(dataModel = "safetyEducation:cultivateTask")
    public Pagination<CultivateTaskVO> getPage(CultivateTaskQueryParam param) {
        Period period = param.getPeriod();
        //请自行添加查询条件
        Page<CultivateTask> page = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getCultivateMode()), CultivateTask::getCultivateMode, param.getCultivateMode())
                .eq(StringUtils.isNotEmpty(param.getCultivateTarget()), CultivateTask::getCultivateTarget, param.getCultivateTarget())
                .eq(StringUtils.isNotEmpty(param.getCultivateTaskStatus()), CultivateTask::getCultivateTaskStatus, param.getCultivateTaskStatus())
                .ge(period != null &&
                    period.getBegin() != null, CultivateTask::getCultivateEndTime, ObjectUtils.ifNull(period, Period::getBegin))
                .le(period != null &&
                    period.getEnd() != null, CultivateTask::getCultivateStartTime, ObjectUtils.ifNull(period, Period::getEnd))
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q.like(CultivateTask::getName, param.getKeyword())
                        .or().like(CultivateTask::getSpeaker, param.getKeyword())
                        .or().like(CultivateTask::getChargeUserName, param.getKeyword()))
                .eq(param.getThemeId() != null, CultivateTask::getThemeId, param.getThemeId())
                .eq(Objects.nonNull(param.getMarketTenant()), CultivateTask::getMarketTenant, param.getMarketTenant())
                .orderByDesc(CultivateTask::getCultivateStartTime)
                .page(PagingUtils.convert(param));
        Pagination<CultivateTaskVO> pagination = PagingUtils.convert(page, CultivateTaskVO.class);

        //将数据字典进行转换
        if (!CollectionUtils.isEmpty(pagination.getRecords())) {
            pagination.getRecords().stream().forEach(d -> convertEnum(d));
        }
        return pagination;
    }

    @Override
    public Pagination<CultivateMyTaskVO> getMyTaskPage(CultivateTaskQueryParam param) {
        param.setUserId(SecurityContextHolder.getPrincipal().getId());
        Page<CultivateMyTaskVO> page = cultivateTaskMapper.getMyTask(PagingUtils.convert(param), param);
        return PagingUtils.convert(page, page.getRecords());
    }

    @Override
    public Pagination<CultivateMyTaskVO> getPublicMyTaskPage(CultivateTaskPublicQueryParam param) {
        Page<CultivateMyTaskVO> page = cultivateTaskMapper.getPublicMyTaskPage(PagingUtils.convert(param), param);
        return PagingUtils.convert(page, page.getRecords());
    }

    @Override
    public Pagination<CultivateTaskWithStatVO> getPageExtendStat(CultivateTaskQueryParam param) {
        Pagination<CultivateTaskVO> page = this.getPage(param);
        return PagingUtils.convert(page, extendStat(page.getRecords()));
    }

    @Override
    public List<CultivateTaskPageVO> getCultivateStandardList(CultivateTaskQueryParam param) {
        List<CultivateTaskPageVO> cultivateStandardPage = this.cultivateTaskMapper.getCultivateStandardList(SessionContextHolder
                                                                                                                    .getRequester()
                                                                                                                    .getTenant(), param);
        //将数据字典进行转换
        if (!CollectionUtils.isEmpty(cultivateStandardPage)) {
            cultivateStandardPage.stream().forEach(d -> {
                //培训状态
                d.setCultivateTaskStatusName(TrainStatusEnum.valueOf(d.getCultivateTaskStatus()).getDesc());
                //培训方式
                d.setCultivateModeName(TrainModeEnum.valueOf(d.getCultivateMode()).getDesc());
                d.setCultivateTargetName(TrainTargetEnum.valueOf(d.getCultivateTarget()).getDesc());
            });
        }
        return cultivateStandardPage;
    }

    @Override
    public void exportCultivateExtendStat(CultivateTaskQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        Period period = param.getPeriod();
        //请自行添加查询条件
        List<CultivateTask> datas = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getCultivateMode()), CultivateTask::getCultivateMode, param.getCultivateMode())
                .eq(StringUtils.isNotEmpty(param.getCultivateTarget()), CultivateTask::getCultivateTarget, param.getCultivateTarget())
                .eq(StringUtils.isNotEmpty(param.getCultivateTaskStatus()), CultivateTask::getCultivateTaskStatus, param.getCultivateTaskStatus())
                .ge(period != null &&
                    period.getBegin() != null, CultivateTask::getCultivateEndTime, ObjectUtils.ifNull(period, Period::getBegin))
                .le(period != null &&
                    period.getEnd() != null, CultivateTask::getCultivateStartTime, ObjectUtils.ifNull(period, Period::getEnd))
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q.like(CultivateTask::getName, param.getKeyword())
                        .or().like(CultivateTask::getSpeaker, param.getKeyword())
                        .or().like(CultivateTask::getChargeUserName, param.getKeyword()))
                .orderByDesc(CultivateTask::getCultivateStartTime)
                .list();
        Assert.isNotFound(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");

        List<CultivateTaskVO> voList = ObjectUtils.copying(datas, CultivateTaskVO.class);
        //转换枚举
        if (!CollectionUtils.isEmpty(voList)) {
            voList.stream().forEach(d -> convertEnum(d));
        }
        //扩展统计信息
        List<CultivateTaskWithStatVO> cultivateTaskWithStatVOS = this.extendStat(voList);

        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("name", "任务名称");
        writer.addHeaderAlias("themeName", "培训主题");
        writer.addHeaderAlias("cultivateTargetName", "培训类型");
        writer.addHeaderAlias("cultivateModeName", "培训形式");
        writer.addHeaderAlias("speaker", "主讲人");
        writer.addHeaderAlias("chargeUserName", "负责人");
        writer.addHeaderAlias("cultivateStartTime", "培训开始时间");
        writer.addHeaderAlias("cultivateEndTime", "培训结束时间");
        writer.addHeaderAlias("cultivateAddress", "培训地点");
        writer.addHeaderAlias("cultivateContent", "培训内容");
        writer.addHeaderAlias("trainNum", "预计参加人数");
        writer.addHeaderAlias("signedNum", "实际参加人数");
        writer.addHeaderAlias("unsignedNum", "缺勤人数");
        writer.addHeaderAlias("belatedNum", "迟到人数");
        writer.addHeaderAlias("hasExam", "是否有考试");
        writer.addHeaderAlias("realityExamUserNumber", "完成考试人数");
        writer.addHeaderAlias("missExamUserNumber", "缺考人数");
        writer.addHeaderAlias("noPassedUserNumber", "不及格人数");
        writer.addHeaderAlias("examPaperScore", "试卷总分");
        writer.addHeaderAlias("averageScore", "平均分");
        writer.addHeaderAlias("passingRate", "及格率");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(cultivateTaskWithStatVOS, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "培训统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    //扩展培训统计和考试统计相关信息
    private List<CultivateTaskWithStatVO> extendStat(List<CultivateTaskVO> datas) {
        //扩展统计信息
        if (!CollectionUtils.isEmpty(datas)) {
            return datas.stream().map(t -> {
                CultivateTaskWithStatVO vo = ObjectUtils.copying(t, CultivateTaskWithStatVO.class);
                //查询培训统计信息
                CultivateTaskWithStatVO trainStat = cultivateTaskUserMapper.getTrainStat(vo.getId());
                vo.setTrainNum(trainStat.getTrainNum());
                vo.setSignedNum(trainStat.getSignedNum());
                vo.setUnsignedNum(trainStat.getTrainNum() - trainStat.getSignedNum());
                vo.setBelatedNum(trainStat.getBelatedNum());
                vo.setHasExam(vo.getExamTaskId() != null ? "是" : "否");
                //查询考试统计信息
                if (vo.getExamTaskId() != null) {
                    ExamTaskStatisticsPageVO examStat = this.getExamStat(vo.getExamTaskId());
                    vo.setExamPaperScore(examStat.getExamPaperScore());
                    vo.setExamUserNumber(examStat.getExamUserNumber());
                    vo.setRealityExamUserNumber(examStat.getRealityExamUserNumber());
                    vo.setMissExamUserNumber(examStat.getMissExamUserNumber());
                    vo.setNoPassedUserNumber(examStat.getNoPassedUserNumber());
                    vo.setAverageScore(examStat.getAverageScore());
                    vo.setPassingRate(examStat.getPassingRate());
                }
                return vo;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    //获取考试信息
    private ExamTaskStatisticsPageVO getExamStat(Long examId) {
        ExamTaskStatisticsPageQuery query = new ExamTaskStatisticsPageQuery();
        query.setExamTaskIds(Collections.unmodifiableSet(new HashSet<>(Arrays.asList(examId))));
        List<ExamTaskStatisticsPageVO> examTaskStat = examTaskMapper.selectExamTaskStatisticsList(SessionContextHolder.getRequester()
                                                                                                          .getTenant(), query);
        if (!CollectionUtils.isEmpty(examTaskStat)) {
            return examTaskStat.get(0);
        }
        return new ExamTaskStatisticsPageVO();
    }

    private void convertEnum(CultivateTaskVO data) {
        if (data != null) {
            //培训状态
            data.setCultivateTaskStatusName(TrainStatusEnum.valueOf(data.getCultivateTaskStatus()).getDesc());
            //培训方式
            data.setCultivateModeName(TrainModeEnum.valueOf(data.getCultivateMode()).getDesc());
            data.setCultivateTargetName(TrainTargetEnum.valueOf(data.getCultivateTarget()).getDesc());
        }
    }

    @Override
    public void updateStatus(Long id, String status) {
        CultivateTask cultivateTask = this.baseMapper.selectById(id);
        Assert.isNotFound(cultivateTask, "未查询到相关信息");
        cultivateTask.setCultivateTaskStatus(status);
        SessionContextHolder.requestTenant(cultivateTask.getTenant());
        if (TrainStatusEnum.PUBLISHED.name().equals(status)) {//发布
            //发布推送消息
            CultivateTaskMessage message = new CultivateTaskMessage();
            message.setTask(cultivateTask);
            //查询人员
            List<CultivateTaskUser> users = cultivateTaskUserMapper.selectList(Wrappers.<CultivateTaskUser>lambdaQuery()
                                                                                       .eq(CultivateTaskUser::getCultivateTaskId, id));
            Assert.isNotAcceptable(TrainTargetEnum.STAFF.name().equals(cultivateTask.getCultivateTarget()) &&
                                   CollectionUtils.isEmpty(users), "发布失败,未指定培训用户");
            message.setUsers(users);

            applicationEventPublisher.publishEvent(new CultivateTaskPublishedEvent(this, message));
        }
        //撤回操作
        if (TrainStatusEnum.REVOKED.name().equals(status)) {
            //进行中的任务 无法撤销
            Assert.isNotAcceptable(TrainStatusEnum.TRAINING.name()
                                           .equals(cultivateTask.getCultivateTaskStatus()), "培训正在进行，无法撤销");
            Assert.isNotAcceptable(TrainStatusEnum.OVER.name()
                                           .equals(cultivateTask.getCultivateTaskStatus()), "培训已经结束，无法撤销");
            cultivateTask.setCultivateTaskStatus(TrainStatusEnum.WAIT_PUBLISH.name());
        }
        this.baseMapper.updateById(cultivateTask);
    }

    @Override
    public void updatePublicStatus(Long id, String status) {
        CultivateTask cultivateTask = this.baseMapper.selectById(id);
        Assert.isNotFound(cultivateTask, "未查询到相关信息");
        cultivateTask.setCultivateTaskStatus(status);
        SessionContextHolder.requestTenant(cultivateTask.getTenant());
        if (TrainStatusEnum.PUBLISHED.name().equals(status)) {//发布
            //发布推送消息
            CultivateTaskMessage message = new CultivateTaskMessage();
            message.setTask(cultivateTask);
            //查询人员
            List<CultivateTaskUser> users = cultivateTaskUserMapper.selectList(Wrappers.<CultivateTaskUser>lambdaQuery()
                                                                                       .eq(CultivateTaskUser::getCultivateTaskId, id));
            message.setUsers(users);
            applicationEventPublisher.publishEvent(new CultivateTaskPublishedEvent(this, message));
        }
        //撤回操作
        if (TrainStatusEnum.REVOKED.name().equals(status)) {
            //进行中的任务 无法撤销
            Assert.isNotAcceptable(TrainStatusEnum.TRAINING.name()
                                           .equals(cultivateTask.getCultivateTaskStatus()), "培训正在进行，无法撤销");
            Assert.isNotAcceptable(TrainStatusEnum.OVER.name()
                                           .equals(cultivateTask.getCultivateTaskStatus()), "培训已经结束，无法撤销");
            cultivateTask.setCultivateTaskStatus(TrainStatusEnum.WAIT_PUBLISH.name());
        }
        this.baseMapper.updateById(cultivateTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cultivateSign(Long id) {
        CultivateTask cultivateTask = this.baseMapper.selectById(id);
        Assert.isNotFound(cultivateTask, "未查询到相关信息");
        Assert.isNotAcceptable(cultivateTask.getCultivateTaskStatus()
                                       .equals(TrainStatusEnum.WAIT_PUBLISH.name()), "培训还未发布，无法签到");
        Assert.isNotAcceptable(cultivateTask.getCultivateEndTime().isBefore(LocalDateTime.now()), "培训已经结束，无法签到");
        //查找人员
        CultivateTaskUser user = cultivateTaskUserService.getOne(Wrappers.<CultivateTaskUser>lambdaQuery()
                                                                         .eq(CultivateTaskUser::getCultivateTaskId, id)
                                                                         .eq(CultivateTaskUser::getUserName, SecurityContextHolder
                                                                                 .getPrincipal().getName()));
        Assert.isNotAcceptable(user == null, "你没有在培训人员中，无法签到");
        //修改用户签到信息
        updateUserSignInfo(cultivateTask, user);
        cultivateTaskUserService.updateById(user);
        //签到之后，更新代办状态
        CultivateSignMessage message = new CultivateSignMessage();
        message.setTask(cultivateTask);
        message.setUser(user);
        applicationEventPublisher.publishEvent(new UserCultivateSignEvent(this, message));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cultivatePublicSign(@NonNull Long id, @NonNull CultivateSignPublicParam cultivateSignPublicParam) {
        CultivateTask cultivateTask = this.baseMapper.selectById(id);
        Assert.isNotFound(cultivateTask, "未查询到相关信息");
        Assert.isNotAcceptable(cultivateTask.getCultivateTaskStatus()
                                       .equals(TrainStatusEnum.WAIT_PUBLISH.name()), "培训还未发布，无法签到");
        Assert.isNotAcceptable(cultivateTask.getCultivateEndTime().isBefore(LocalDateTime.now()), "培训已经结束，无法签到");
        SessionContextHolder.requestTenant(cultivateTask.getTenant());
        //查找人员
        CultivateTaskUser user = cultivateTaskUserService.getOne(Wrappers.<CultivateTaskUser>lambdaQuery()
                                                                         .eq(CultivateTaskUser::getUserIdentify, cultivateSignPublicParam.getUserIdentify())
                                                                         .eq(CultivateTaskUser::getCultivateTaskId, id));
        boolean isExecuteSave = false;
        if (Objects.isNull(user)) {
            isExecuteSave = true;
            user = new CultivateTaskUser();
            user.setId(IdWorker.getId());
            user.setCultivateTaskId(cultivateTask.getId());
            user.setUserIdentify(cultivateSignPublicParam.getUserIdentify());
            user.setUserName(cultivateSignPublicParam.getUserName());
            user.setUserPhone(cultivateSignPublicParam.getUserPhone());
            user.setUserDepartmentName(cultivateSignPublicParam.getUserDepartmentName());
            user.setUserPostName(cultivateSignPublicParam.getUserPostName());
            user.setTenant(cultivateTask.getTenant());
        }
        //修改用户签到信息
        updateUserSignInfo(cultivateTask, user);
        if (isExecuteSave) {
            cultivateTaskUserService.save(user);
        } else {
            cultivateTaskUserService.updateById(user);
        }
        //签到之后，更新代办状态
        CultivateSignMessage message = new CultivateSignMessage();
        message.setTask(cultivateTask);
        message.setUser(user);
        applicationEventPublisher.publishEvent(new UserCultivateSignEvent(this, message));
    }

    public void updateUserSignInfo(CultivateTask cultivateTask, CultivateTaskUser user) {
        var currentMilli = DateUtils.toTimestamp(LocalDateTime.now());
        user.setUserSignStatus(TrainSignEnum.SIGNED.name());//已签
        user.setUserSignTime(LocalDateTime.now());
        if (currentMilli > DateUtils.toTimestamp(cultivateTask.getCultivateStartTime())
            && !Boolean.TRUE.equals(cultivateTask.getLateStatus())) {
            user.setBelated(true);
        } else if (currentMilli <= DateUtils.toTimestamp(cultivateTask.getCultivateStartTime())) {
            user.setBelated(false);
        }
    }

    @Override
    public CultivateTaskStatVO getCultivateTaskStat(CultivateTaskQueryParam param) {
        Period period = this.initQueryParam(param.getPeriodType(), param.getPeriod());
        Period qoqPeriod = calQOQPeriod(period);
        CultivateTaskStatVO currentStat = this.baseMapper.getCultivateTaskStat(period);
        CultivateTaskStatVO qoqStat = this.baseMapper.getCultivateTaskStat(qoqPeriod);
        //计算环比
        buildCultivateTaskStatVO(currentStat, qoqStat);
        return currentStat;
    }

    @Override
    public CultivateTaskStatVO getCultivateTaskPermissionStat(CultivateTaskQueryParam param) {
        Period period = this.initQueryParam(param.getPeriodType(), param.getPeriod());
        Period qoqPeriod = calQOQPeriod(period);
        CultivateTaskStatVO currentStat = this.baseMapper.getCultivateTaskPermissionStat(period);
        CultivateTaskStatVO qoqStat = this.baseMapper.getCultivateTaskPermissionStat(qoqPeriod);
        buildCultivateTaskStatVO(currentStat, qoqStat);
        return currentStat;
    }

    private void buildCultivateTaskStatVO(CultivateTaskStatVO currentStat, CultivateTaskStatVO qoqStat) {
        //计算环比
        if (qoqStat != null && qoqStat.getCultivateNum() != null && qoqStat.getCultivateNum().intValue() != 0) {
            BigDecimal qoq = currentStat.getCultivateNum().subtract(qoqStat.getCultivateNum()).multiply(BigDecimal.valueOf(100))
                    .divide(qoqStat.getCultivateNum(), 2, BigDecimal.ROUND_HALF_UP);
            currentStat.setQoq(qoq);
        } else if (qoqStat != null &&
                   Objects.nonNull(qoqStat.getCultivateNum()) &&
                   qoqStat.getCultivateNum().intValue() == 0 &&
                   currentStat.getCultivateNum().intValue() != 0) {
            currentStat.setQoq(BigDecimal.valueOf(100));
        } else {
            currentStat.setQoq(BigDecimal.valueOf(0));
        }
    }

    @Override
    public List<CultivateCommonStatVO> getCultivateThemeStat(CultivateTaskQueryParam param) {
        Period period = this.initQueryParam(param.getPeriodType(), param.getPeriod());
        return this.baseMapper.getCultivateThemeStat(period);
    }

    @Override
    public List<CultivateCommonStatVO> getCultivateTrendStat(CultivateTaskQueryParam param) {
        Period period = this.initQueryParam(param.getPeriodType(), param.getPeriod());
        List<CultivateCommonStatVO> cultivateTrendStat = this.baseMapper.getCultivateTrendStat(period);
        if (!CollectionUtils.isEmpty(cultivateTrendStat)) {
            YearMonth startMonth = YearMonth.parse(DateUtils.format(period.getBegin(), "YYYY-MM"));
            YearMonth endMonth = YearMonth.parse(DateUtils.format(period.getEnd(), "YYYY-MM"));
            return IntStream.rangeClosed(0, endMonth.getMonthValue() - startMonth.getMonthValue() +
                                            (12 * (endMonth.getYear() - startMonth.getYear()))).mapToObj(startMonth::plusMonths).
                    map(m -> {
                        CultivateCommonStatVO stat = cultivateTrendStat.stream().filter(t -> t.getName().equals(m.toString())).findAny()
                                .orElse(null);
                        if (stat == null) {
                            return new CultivateCommonStatVO(m.toString(), 0);
                        } else {
                            return stat;
                        }
                    }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public CultivateUserStatVO getTrainUserStat(CultivateTaskQueryParam param) {
        Period period = this.initQueryParam(param.getPeriodType(), param.getPeriod());
        return this.baseMapper.getTrainUserStat(period);
    }

    @Override
    public List<CultivateCommonStatVO> getCultivateTargetStat(CultivateTaskQueryParam param) {
        Period period = this.initQueryParam(param.getPeriodType(), param.getPeriod());
        List<CultivateCommonStatVO> cultivateTargetStat = this.baseMapper.getCultivateTargetStat(period);
        if (!CollectionUtils.isEmpty(cultivateTargetStat)) {
            cultivateTargetStat.stream().forEach(s -> s.setName(TrainTargetEnum.valueOf(s.getName()).getDesc() + "培训"));
        }
        return cultivateTargetStat;
    }

    @Override
    public List<CultivateUserStatVO> getUserSignStatByTask(CultivateTaskQueryParam param) {
        Period period = this.initQueryParam(param.getPeriodType(), param.getPeriod());
        List<CultivateTask> list = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getCultivateMode()), CultivateTask::getCultivateMode, param.getCultivateMode())
                .eq(StringUtils.isNotEmpty(param.getCultivateTarget()), CultivateTask::getCultivateTarget, param.getCultivateTarget())
                .eq(StringUtils.isNotEmpty(param.getCultivateTaskStatus()), CultivateTask::getCultivateTaskStatus, param.getCultivateTaskStatus())
                .ge(period != null &&
                    period.getBegin() != null, CultivateTask::getCultivateEndTime, ObjectUtils.ifNull(period, Period::getBegin))
                .le(period != null &&
                    period.getEnd() != null, CultivateTask::getCultivateStartTime, ObjectUtils.ifNull(period, Period::getEnd))
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q.like(CultivateTask::getName, param.getKeyword())
                        .or().like(CultivateTask::getSpeaker, param.getKeyword())
                        .or().like(CultivateTask::getChargeUserName, param.getKeyword())).list();
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().map(d -> {
                CultivateTaskWithStatVO trainStat = cultivateTaskUserMapper.getTrainStat(d.getId());
                CultivateUserStatVO stat = ObjectUtils.copying(trainStat, CultivateUserStatVO.class);
                stat.setName(d.getName());
                return stat;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBySourceKeys(Set<Long> sourceKeys) {
        if (CollectionUtils.isEmpty(sourceKeys)) {
            return;
        }
        List<CultivateTask> cultivateTasks = this.baseMapper.selectList(Wrappers.<CultivateTask>lambdaQuery()
                                                                                .in(CultivateTask::getSourceKey, sourceKeys));
        if (CollectionUtils.isEmpty(cultivateTasks)) {
            DatabaseContextHolder.softDelete(CultivateTask.class, cultivateTasks);
        }
    }

    private Period initQueryParam(String timeType, Period period) {
        if (StringUtils.isNotEmpty(timeType)) {
            LocalDateTime startTime;
            LocalDateTime endTime;
            if ("TODAY".equals(timeType)) {//按小时分组
                startTime = DateUtils.beginOfDay(LocalDateTime.now());
                endTime = DateUtils.endOfDay(LocalDateTime.now());
            } else if ("THIS_MONTH".equals(timeType)) {
                startTime = DateUtils.beginOfMonth(LocalDateTime.now());
                endTime = DateUtils.endOfMonth(LocalDateTime.now());
            } else if ("THIS_YEAR".equals(timeType)) {
                startTime = DateUtils.beginOfYear(LocalDateTime.now());
                endTime = DateUtils.endOfYear(LocalDateTime.now());
            } else if ("QOQ_TODAY".equals(timeType)) {
                var yesterday = DateUtils.offsetDay(LocalDateTime.now(), -1);
                startTime = DateUtils.beginOfDay(yesterday);
                endTime = DateUtils.endOfDay(yesterday);
            } else if ("QOQ_MONTH".equals(timeType)) {
                var lastMonthDay = DateUtils.offsetMonth(LocalDateTime.now(), -1);
                startTime = DateUtils.beginOfMonth(lastMonthDay);
                endTime = DateUtils.endOfDay(lastMonthDay);
            } else if ("QOQ_YEAR".equals(timeType)) {
                var lastYearDay = DateUtils.offsetMonth(LocalDateTime.now(), -1);
                startTime = DateUtils.beginOfYear(lastYearDay);
                endTime = DateUtils.endOfDay(lastYearDay);
            } else if ("YOY_TODAY".equals(timeType)) {
                var yoyDay = DateUtils.offsetMonth(LocalDateTime.now(), -12);
                startTime = DateUtils.beginOfDay(yoyDay);
                endTime = DateUtils.endOfDay(yoyDay);
            } else if ("YOY_MONTH".equals(timeType)) {
                var yoyMonthDay = DateUtils.offsetMonth(LocalDateTime.now(), -12);
                startTime = DateUtils.beginOfMonth(yoyMonthDay);
                endTime = DateUtils.endOfDay(yoyMonthDay);
            } else if ("YOY_YEAR".equals(timeType)) {
                var lastYearDay = DateUtils.offsetMonth(LocalDateTime.now(), -12);
                startTime = DateUtils.beginOfYear(lastYearDay);
                endTime = DateUtils.endOfDay(lastYearDay);
            } else {
                return initQueryParam("THIS_MONTH", null);//默认本月数据
            }
            return Period.of(startTime, endTime);
        } else {
            return period;
        }
    }

    private Period calQOQPeriod(Period period) {
        var between = DateUtils.between(period.getBegin(), period.getEnd(), ChronoUnit.DAYS);
        var start = DateUtils.offsetDay(period.getBegin(), -(between + 1));
        var end = DateUtils.offsetDay(period.getEnd(), -(between + 1));
        return Period.of(start, end);
    }
}