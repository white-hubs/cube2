package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.HandlerService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.TaskTargetRelationService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectPlanQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectTaskQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTaskQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.DetailRateResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskRateResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.utils.DateUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectTaskQueryServiceImpl extends ServiceImpl<InspectTaskMapper, InspectTaskPO> implements InspectTaskQueryService {

    private final InspectQuery inspectQuery;

    private final InspectRecordQueryService recordQueryService;
    private final InspectTaskMapper taskMapper;

    private final TaskTargetRelationService taskTargetRelationService;
    private final InspectPlanQueryService inspectPlanQueryService;
    private final HandlerService handlerService;

    /**
     * 任务详情
     *
     * @param id
     *
     * @return
     */
    @Override
    public InspectTaskVO getVOById(Long id) {
        InspectTaskPO taskPO = this.getById(id);
        Assert.isNotFound(taskPO, "任务不存在！");

        InspectTaskVO taskResult = ObjectUtils.copying(taskPO, InspectTaskVO.class);
        taskResult.setReceiveNames(inspectQuery.extendUserNames(taskResult.getTenant(), taskResult.getReceiveIds()));
        taskResult.setReceiveDeptNames(inspectQuery.extendDeptNames(taskResult.getTenant(), taskResult.getReceiveDeptIds()));
        taskResult.setReceivePostNames(inspectQuery.extendPostNames(taskResult.getTenant(), taskResult.getReceivePostIds()));
        taskResult.setReceiveRoleNames(inspectQuery.extendPostNames(taskResult.getTenant(), taskResult.getReceiveRoleIds()));
        // 有关联被稽查任务id就是稽查任务
        taskResult.setIsCheck(taskPO.getCheckTaskId() != 0);
        // 计划名称
        taskResult.setPlanName(inspectPlanQueryService.getById(taskPO.getPlanId()).getName());
        // 计划对象集合
        taskResult.setTypeTargets(taskTargetRelationService.getTargetInfoByTaskId(taskPO.getId()));

        return taskResult;
    }

    /**
     * 任务详情-对象标准列表
     *
     * @param taskId
     * @param targetId
     *
     * @return
     */
    @Override
    public List<StandardResult> getDeailStandardList(Long taskId, Long targetId) {
        Assert.isParameterError(taskId == null, "任务不能为空(id)");
        Assert.isParameterError(targetId == null, "对象不能为空(id)");
        InspectTaskPO taskPO = this.getById(taskId);
        Assert.isNotFound(taskPO, "任务不存在！");
        // 任务列表只显示主任务所以跟计划的对象(标准)是一样的
        return inspectPlanQueryService.getDeailStandardList(taskPO.getPlanId(), targetId);
    }

    /**
     * 巡检任务-详情-执行进度
     *
     * @param taskId
     *
     * @return
     */
    @Override
    public DetailRateResult getDetailRateResult(Long taskId) {
        DetailRateResult result = new DetailRateResult();

        // 获取任务信息判断是否有子任务
        InspectTaskPO parentTask = this.getById(taskId);
        /*
         * 处理任务进度和巡检记录的集合列表
         */
        List<TaskRateResult> taskRateResults = new ArrayList<>();
        List<InspectRecordVO> recordTaskResultList = new ArrayList<>();
        if (!parentTask.getIsSplit()) {
            taskRateResults.add(handlerService.handleTaskRateResult(parentTask));
            recordTaskResultList.addAll(handlerService.getTaskRecordList(parentTask.getPlanId(), parentTask.getId()));
        } else {
            List<InspectRecordVO> sonRecordTaskResultList = new ArrayList<>();
            // 获取子任务列表
            List<InspectTaskPO> taskPOList = this.lambdaQuery().eq(true, InspectTaskPO::getParentId, taskId).list();
            taskPOList.parallelStream().forEach(po -> {
                taskRateResults.add(handlerService.handleTaskRateResult(po));
                sonRecordTaskResultList.addAll(handlerService.getTaskRecordList(po.getPlanId(), po.getId()));
            });
            recordTaskResultList.addAll(sonRecordTaskResultList);
        }

        // 设置返回对象的任务进度列表
        result.setTaskRateResults(taskRateResults);
        result.setRecordTaskResultList(recordTaskResultList);

        return result;
    }

    /**
     * 任务分页列表
     *
     * @param param
     *
     * @return
     */
    @Override
    public Pagination<InspectTaskVO> getPage(InspectTaskQueryParam param) {
        Period period = DateUtil.simpleDateTypeHandle(param.getDateType(), param.getPeriod());
        Page<InspectTaskPO> page = this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(InspectTaskPO::getName, param.getKeyword())
                    )
                .eq(param.getStatus() != null, InspectTaskPO::getStatus, param.getStatus())
                .eq(StringUtils.isNotEmpty(param.getTypeId()), InspectTaskPO::getTypeId, param.getTypeId())
                .eq(InspectTaskPO::getIsSplit, false)
                .le(InspectTaskPO::getValidBeginTime, LocalDateTime.now())
                .ne(InspectTaskPO::getStatus, InspectTaskStatusEnum.SPLIT)
                .between(period !=
                         null, InspectTaskPO::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .orderByDesc(InspectTaskPO::getCreateTime)
                .page(PagingUtils.convert(param));

        List<InspectTaskVO> collect = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList())
                .stream().map(po -> {
                    InspectTaskVO vo = ObjectUtils.copying(po, InspectTaskVO.class);
                    // 如果没有关联被稽查的任务id就说明不是稽查任务是普通任务
                    vo.setIsCheck(po.getCheckTaskId() != 0);
                    vo.setReceiveNames(inspectQuery.extendUserNames(vo.getTenant(), vo.getReceiveIds()));
                    vo.setReceiveDeptNames(inspectQuery.extendDeptNames(vo.getTenant(), vo.getReceiveDeptIds()));
                    vo.setReceivePostNames(inspectQuery.extendPostNames(vo.getTenant(), vo.getReceivePostIds()));
                    vo.setReceiveRoleNames(inspectQuery.extendPostNames(vo.getTenant(), vo.getReceiveRoleIds()));
                    // 设置任务完成率
                    vo.setRecordCompletionRate(this.getTaskRecordRate(vo.getId(), vo.getTargetNum()));
                    return vo;
                }).collect(Collectors.toList());

        return PagingUtils.convert(page, collect);
    }

    @Override
    public Pagination<InspectTaskVO> getPageByTarget(String targetCategory, Long targetId, InspectTaskQueryParam param) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Period period = DateUtil.simpleDateTypeHandle(param.getDateType(), param.getPeriod());

        Page<InspectTaskVO> pageByTarget = taskMapper.getPageByTarget(PagingUtils.convert(param), Wrappers.query()
                                                                              .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                                                                                      .like("a.name", param.getKeyword())
                                                                                  )
                                                                              .eq("a.is_split", 0)
                                                                              .and(ObjectUtils.isNotNull(userId), q -> q.like("a.receive_ids", userId)
                                                                                      .or().like("a.assist_ids", userId))
                                                                              .eq("b.target_category", targetCategory)
                                                                              .eq("b.target_id", targetId)
                                                                              .and(q -> q.eq(param.getStatus() != null, "a.status", param.getStatus()).or()
                                                                                      .eq(param.getStatus().equals(InspectTaskStatusEnum.EXECUTING), "a.status", TaskStatusEnum.ACCEPT))
                                                                              .eq(Boolean.TRUE.equals(param.getTemp()) && param.getStatus() != null, "a.status", param.getStatus())
                                                                              .between(period !=
                                                                                       null, "a.create_time", ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                              .eq(StringUtils.isNotBlank(param.getRecordStatus()), "b.status", param.getRecordStatus())
                                                                              .orderByDesc("a.create_time")
                                                                     );

        Optional.ofNullable(pageByTarget.getRecords()).orElse(Collections.emptyList()).forEach(vo -> {

            vo.setReceiveNames(inspectQuery.extendUserNames(vo.getTenant(), vo.getReceiveIds()));
            vo.setReceiveDeptNames(inspectQuery.extendDeptNames(vo.getTenant(), vo.getReceiveDeptIds()));
            vo.setReceivePostNames(inspectQuery.extendPostNames(vo.getTenant(), vo.getReceivePostIds()));
            vo.setReceiveRoleNames(inspectQuery.extendPostNames(vo.getTenant(), vo.getReceiveRoleIds()));
            this.extendRecordCompletionRate(vo);
        });

        return PagingUtils.convert(pageByTarget);
    }

    @Override
    public Pagination<InspectTaskVO> getMyTask(InspectTaskQueryParam param) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        param.setUserId(userId);
        return this.getPage(param);
    }

    private void extendRecordCompletionRate(InspectTaskVO vo) {
        long count = recordQueryService.lambdaQuery()
                .eq(InspectRecordPO::getTaskId, vo.getId())
                .eq(InspectRecordPO::getStatus, InspectRecordStatusEnum.OVER)
                .count();

        double d = (count * 1D / vo.getTargetNum()) * 100;
        vo.setRecordCompletionRate(BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP).doubleValue());

    }

    /**
     * 获取任务的记录完成率
     */
    private Double getTaskRecordRate(Long taskId, Integer targetNum) {
        if (targetNum == null || targetNum == 0) {
            return 0D;
        }
        long count = this.getTaskRecordNum(taskId, null);
        double d = (count * 1D / targetNum) * 100;
        return BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 获取任务 巡检对象数
     *
     * @param taskId     任务id 必填
     * @param resultEnum 结果(正常异常) 可选
     *
     * @return
     */
    public Long getTaskRecordNum(Long taskId, InspectRecordResultEnum resultEnum) {

        return recordQueryService.lambdaQuery()
                .eq(InspectRecordPO::getTaskId, taskId)
                .eq(resultEnum != null, InspectRecordPO::getResult, resultEnum)
                .eq(InspectRecordPO::getStatus, InspectRecordStatusEnum.OVER)
                .count();
    }
}
