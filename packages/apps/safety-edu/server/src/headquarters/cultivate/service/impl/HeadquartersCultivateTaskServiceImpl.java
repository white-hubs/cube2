package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.dao.HeadquartersCultivateTaskMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.dao.HeadquartersCultivateTaskMarketMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTaskCoursewareLibrary;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTaskMarket;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.message.event.HeadquartersCultivateTaskPublishedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.message.event.HeadquartersCultivateTaskRevokedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.message.model.HeadquartersCultivateTaskMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateCoursewarParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateTaskMarketParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateTaskParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateTaskQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.HeadquartersCultivateTaskCoursewarLibraryService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.HeadquartersCultivateTaskMarketService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.HeadquartersCultivateTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateCoursewarVO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateTaskDetailVO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateTaskMarketVO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainModeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainTargetEnum;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 培训任务服务层
 *
 * @author lanchun
 * @date 2024/4/10 14:59
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersCultivateTaskServiceImpl extends ServiceImpl<HeadquartersCultivateTaskMapper, HeadquartersCultivateTask> implements HeadquartersCultivateTaskService {
    private final HeadquartersCultivateTaskMarketService headquartersCultivateTaskMarketService;
    private final HeadquartersCultivateTaskMarketMapper headquartersCultivateTaskMarketMapper;
    private final HeadquartersExamTaskService headquartersExamTaskService;
    private final HeadquartersCultivateTaskCoursewarLibraryService coursewarLibraryService;
    private final ThemeService themeService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(HeadquartersCultivateTaskParam param) {
        HeadquartersCultivateTask headquartersCultivateTask = ObjectUtils.copying(param, HeadquartersCultivateTask.class);
        //插入主题名称
        ThemePO theme = themeService.getById(param.getThemeId());
        headquartersCultivateTask.setThemeName(theme != null ? theme.getName() : "");
        headquartersCultivateTask.setCultivateTaskStatus(TrainStatusEnum.WAIT_PUBLISH.name());
        this.baseMapper.insert(headquartersCultivateTask);
        //保存培训市场
        this.saveCultivateMarkets(headquartersCultivateTask.getId(), param.getCultivateTaskMarkets());
        //保存培训课件
        this.saveCourseware(headquartersCultivateTask.getId(), param.getCultivateCoursewares());
        return headquartersCultivateTask.getId();
    }

    //保存培训市场
    private void saveCultivateMarkets(Long id, List<HeadquartersCultivateTaskMarketParam> cultivateMarkets) {
        //删除之前的培训市场
        headquartersCultivateTaskMarketService.remove(Wrappers.<HeadquartersCultivateTaskMarket>lambdaQuery()
                                                              .eq(HeadquartersCultivateTaskMarket::getHeadquartersCultivateTaskId, id));
        //保存现有的市场
        if (!CollectionUtils.isEmpty(cultivateMarkets)) {
            cultivateMarkets.forEach(u -> {
                //初始化市场
                u.setHeadquartersCultivateTaskId(id);
                headquartersCultivateTaskMarketService.add(u);
            });
        }
    }

    //保存培训课件
    private void saveCourseware(Long id, List<HeadquartersCultivateCoursewarParam> coursewars) {
        //删除之前的课件
        coursewarLibraryService.remove(Wrappers.<HeadquartersCultivateTaskCoursewareLibrary>lambdaQuery()
                                               .eq(HeadquartersCultivateTaskCoursewareLibrary::getHeadquartersCultivateTaskId, id));
        //保存课件
        if (!CollectionUtils.isEmpty(coursewars)) {
            coursewars.forEach(c -> {
                c.setHeadquartersCultivateTaskId(id);
                coursewarLibraryService.add(c);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, HeadquartersCultivateTaskParam param) {
        //校验培训是否可以修改：培训开始以后则不能修改
        HeadquartersCultivateTask headquartersCultivateTask = this.baseMapper.selectById(id);
        Assert.isNotFound(headquartersCultivateTask, "未查询到相关信息");
        Assert.isNotAcceptable(!TrainStatusEnum.WAIT_PUBLISH.name()
                .equals(headquartersCultivateTask.getCultivateTaskStatus()), "请撤回后再编辑");

        ObjectUtils.copying(param, headquartersCultivateTask);
        //插入主题名称
        ThemePO theme = themeService.getById(param.getThemeId());
        headquartersCultivateTask.setThemeName(theme != null ? theme.getName() : "");
        this.baseMapper.updateById(headquartersCultivateTask);
        //保存培训人员
        this.saveCultivateMarkets(headquartersCultivateTask.getId(), param.getCultivateTaskMarkets());
        //保存培训课件
        this.saveCourseware(headquartersCultivateTask.getId(), param.getCultivateCoursewares());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        HeadquartersCultivateTask headquartersCultivateTask = this.baseMapper.selectById(id);
        Assert.isNotAcceptable(!TrainStatusEnum.WAIT_PUBLISH.name().equals(headquartersCultivateTask.getCultivateTaskStatus()) &&
                               !TrainStatusEnum.OVER.name().equals(headquartersCultivateTask.getCultivateTaskStatus()), "请撤回后再删除");
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public HeadquartersCultivateTaskDetailVO getById(Long id) {
        HeadquartersCultivateTask headquartersCultivateTask = this.baseMapper.selectById(id);
        Assert.isNotFound(headquartersCultivateTask, "未查询到数据");
        HeadquartersCultivateTaskDetailVO taskVO = ObjectUtils.copying(headquartersCultivateTask, HeadquartersCultivateTaskDetailVO.class);
        //枚举转换
        this.convertEnum(taskVO);
        //扩展培训市场和课件信息
        this.extendMarketAndCoursewar(taskVO);
        return taskVO;
    }

    private void extendMarketAndCoursewar(HeadquartersCultivateTaskDetailVO taskVO) {
        //考试信息
        if (taskVO.getExamTaskId() != null) {
            HeadquartersExamTaskPO examTaskPO = headquartersExamTaskService.getById(taskVO.getExamTaskId());
            taskVO.setExamTaskName(examTaskPO != null ? examTaskPO.getName() : "");
        }
        //查询培训市场
        List<HeadquartersCultivateTaskMarketVO> marketVOS = headquartersCultivateTaskMarketService.getCultivateMarketsByCultivateId(taskVO.getId());
        taskVO.setCultivateTaskMarkets(marketVOS);
        //查询课件
        List<HeadquartersCultivateCoursewarVO> coursewars = coursewarLibraryService.getCoursewarsByCultivateId(taskVO.getId());
        taskVO.setCultivateCoursewares(coursewars);
    }

    @Override
    public Pagination<HeadquartersCultivateTaskVO> getPage(HeadquartersCultivateTaskQueryParam param) {
        Period period = param.getPeriod();
        //请自行添加查询条件
        Page<HeadquartersCultivateTask> page = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getCultivateMode()), HeadquartersCultivateTask::getCultivateMode, param.getCultivateMode())
                .eq(StringUtils.isNotEmpty(param.getCultivateTarget()), HeadquartersCultivateTask::getCultivateTarget, param.getCultivateTarget())
                .eq(StringUtils.isNotEmpty(param.getCultivateTaskStatus()), HeadquartersCultivateTask::getCultivateTaskStatus, param.getCultivateTaskStatus())
                .ge(period != null &&
                    period.getBegin() != null, HeadquartersCultivateTask::getCultivateEndTime, ObjectUtils.ifNull(period, Period::getBegin))
                .le(period != null &&
                    period.getEnd() != null, HeadquartersCultivateTask::getCultivateStartTime, ObjectUtils.ifNull(period, Period::getEnd))
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q.like(HeadquartersCultivateTask::getName, param.getKeyword())
                        .or().like(HeadquartersCultivateTask::getSpeaker, param.getKeyword())
                        .or().like(HeadquartersCultivateTask::getChargeUserName, param.getKeyword()))
                .eq(param.getThemeId() != null, HeadquartersCultivateTask::getThemeId, param.getThemeId())
                .orderByDesc(HeadquartersCultivateTask::getCultivateStartTime)
                .page(PagingUtils.convert(param));
        Pagination<HeadquartersCultivateTaskVO> pagination = PagingUtils.convert(page, HeadquartersCultivateTaskVO.class);

        //将数据字典进行转换
        if (!CollectionUtils.isEmpty(pagination.getRecords())) {
            pagination.getRecords().forEach(this::convertEnum);
        }
        return pagination;
    }

    private void convertEnum(HeadquartersCultivateTaskVO data) {
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
        HeadquartersCultivateTask headquartersCultivateTask = this.baseMapper.selectById(id);
        Assert.isNotFound(headquartersCultivateTask, "未查询到相关信息");
        headquartersCultivateTask.setCultivateTaskStatus(status);
        if (TrainStatusEnum.PUBLISHED.name().equals(status)) {//发布
            HeadquartersCultivateTaskMessage message = getHeadquartersCultivateTaskMessage(id, headquartersCultivateTask);
            applicationEventPublisher.publishEvent(new HeadquartersCultivateTaskPublishedEvent(this, message));
        }
        //撤回操作
        if (TrainStatusEnum.REVOKED.name().equals(status)) {
            //进行中的任务 无法撤销
            Assert.isConflict(TrainStatusEnum.TRAINING.name()
                                      .equals(headquartersCultivateTask.getCultivateTaskStatus()), "培训正在进行，无法撤销");
            Assert.isNotAcceptable(TrainStatusEnum.OVER.name()
                                           .equals(headquartersCultivateTask.getCultivateTaskStatus()), "培训已经结束，无法撤销");
            headquartersCultivateTask.setCultivateTaskStatus(TrainStatusEnum.WAIT_PUBLISH.name());
            applicationEventPublisher.publishEvent(new HeadquartersCultivateTaskRevokedEvent(this, getHeadquartersCultivateTaskMessage(id, headquartersCultivateTask)));
        }
        this.baseMapper.updateById(headquartersCultivateTask);
    }

    public HeadquartersCultivateTaskMessage getHeadquartersCultivateTaskMessage(Long id,
                                                                                HeadquartersCultivateTask headquartersCultivateTask
                                                                               ) {
        //发布推送消息
        HeadquartersCultivateTaskMessage message = new HeadquartersCultivateTaskMessage();
        message.setTask(headquartersCultivateTask);
        //查询市场
        List<HeadquartersCultivateTaskMarket> markets = this.headquartersCultivateTaskMarketMapper.selectList(Wrappers
                                                                                                                      .<HeadquartersCultivateTaskMarket>lambdaQuery()
                                                                                                                      .eq(HeadquartersCultivateTaskMarket::getHeadquartersCultivateTaskId, id));
        message.setMarkets(markets);
        return message;
    }
}