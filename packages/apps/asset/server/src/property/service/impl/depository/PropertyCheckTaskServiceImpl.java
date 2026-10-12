package com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.depository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.property.management.client.property.param.property.CheckTaskRelationParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckPlanPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckTaskParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckTaskPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedExcelVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.enumeration.FileSceneCodeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.impl.UserClientService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.constant.CheckTypeConstant;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.assembly.PropertyCheckTaskAdminMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyCheckTaskMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyCheckTaskRelationMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyDetailedMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.assembly.PropertyCheckTaskAdmin;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckPlan;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckTask;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckTaskRelation;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyDetailed;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.CheckTaskStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.MessageModuleTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertyStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyCheckTaskRelationService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyCheckTaskService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyRepairReportService;
import com.guhecloud.cube.srv.gen.property.management.server.property.util.MessageUtil;
import com.guhecloud.cube.srv.gen.property.management.server.property.util.PropertyTaskCenterUtil;
import com.guhecloud.cube.srv.gen.property.management.server.property.util.PropertyTaskTodoUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 盘点任务服务实现类
 *
 * @author : lijie
 * @date : 2023-3-20
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyCheckTaskServiceImpl extends ServiceImpl<PropertyCheckTaskMapper, PropertyCheckTask> implements PropertyCheckTaskService {
    private final PropertyCheckTaskRelationService taskRelationService;
    private final PropertyCheckTaskAdminMapper adminMapper;
    private final UserClientService userClientService;
    private final PropertyCheckTaskRelationMapper taskRelationMapper;
    private final PropertyDetailedMapper propertyDetailedMapper;
    private final PropertyCheckTaskMapper checkTaskMapper;
    private final FileClient fileClient;
    private final AreaClient areaClient;
    private final PropertyTaskTodoUtil propertyTaskTodoUtil;
    private final PropertyTaskCenterUtil propertyTaskCenterUtil;
    private final MessageUtil messageUtil;
    private final UserClient userClient;
    private final DataDictionaryClient dictionaryClient;
    private final PropertyRepairReportService propertyRepairReportService;
    private final FileOperateClient fileOperateClient;

    /**
     * 盘点计划-盘点明细
     *
     * @param param 盘点计划查询条件
     *
     * @return 盘点资产明细
     */
    @Override
    public Pagination<PropertyDetailedVO> queryByPlanIdPage(PropertyCheckPlanPageParam param) {
        if (ObjectUtils.isEmpty(param.getId())) {
            throw new RuntimeException("盘点计划id不能为空");
        }
        Page<PropertyDetailedVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.queryByPlanIdPage(page, param.getId());
        Pagination<PropertyDetailedVO> pagination = new Pagination<>(page.getTotal(), page.getPages(), page.getRecords());
        for (PropertyDetailedVO ex : pagination.getRecords()) {
            ex.setCreateByName(userClientService.getCreateByName(ex.getCreateBy(), ex.getCreatorId(), ex.getCreatorType()));
            ex.setReceiveUserName(userClientService.getUser(ex.getReceiveUserId()));
            ex.setCheckUserName(userClientService.getUser(ex.getCheckUserId()));
            if (ObjectUtils.isNotEmpty(ex.getUsageDate())) {
                // 计算剩余使用期限
                long usageTime = DateUtils.compare(ex.getUsageDate(), LocalDateTime.now()) < 1 ? 0
                                                                                               : DateUtils.betweenMonth(LocalDateTime.now(), ex.getUsageDate(), true);
                if (usageTime < 1) {
                    usageTime = 0L;
                }
                ex.setUsageTime(usageTime);
                ex.setUsage(DateUtils.compare(ex.getUsageDate(), LocalDateTime.now()) < 1);
            }
            if (ObjectUtils.isNotEmpty(ex.getAbnormalType())) {
                ex.setAbnormalTypeName(dictionaryClient.getDataDictionaryById(ex.getAbnormalType()).getName());
            }
        }
        return pagination;
    }

    @Override
    public List<PropertyDetailedExcelVO> queryByPlanIdPage(String id) {
        List<PropertyDetailedExcelVO> data = this.baseMapper.queryByPlanIdExcel(id);
        for (PropertyDetailedExcelVO ex : data) {
            ex.setCheckStatus(CheckTaskStatusEnum.getCheckTaskStatusEnum(ex.getCheckStatus()));
            ex.setReceiveUserName(userClientService.getUser(ex.getReceiveUserName()));
            ex.setCreateByName(userClientService.getCreateByName(ex.getCreateByName(), ex.getCreatorId(), ex.getCreatorType()));
            if (ObjectUtils.isNotEmpty(ex.getAbnormalType())) {
                ex.setAbnormalType(dictionaryClient.getDataDictionaryById(Long.parseLong(ex.getAbnormalType())).getName());
            }
        }
        return data;
    }

    /**
     * 盘点任务列表(app)
     *
     * @param param 盘点任务查询条件
     *
     * @return 盘点任务信息
     */
    @Override
    public Pagination<PropertyCheckTaskPageVO> queryTaskByUserIdPage(PropertyCheckPlanPageParam param) {
        // 获取当前用户信息
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Page<PropertyCheckTaskPageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        if (StringUtils.isNotEmpty(param.getPropertyId())) {
            List<PropertyCheckTaskRelation> propertyCheckTaskRelations = taskRelationMapper.selectList(new LambdaQueryWrapper<PropertyCheckTaskRelation>()
                                                                                                               .select(PropertyCheckTaskRelation::getTaskId)
                                                                                                               .in(PropertyCheckTaskRelation::getPropertyId, Arrays
                                                                                                                       .stream(param
                                                                                                                                       .getPropertyId()
                                                                                                                                       .split(","))
                                                                                                                       .collect(Collectors.toList())));
            if (CollectionUtils.isEmpty(propertyCheckTaskRelations)) {
                return new Pagination<>();
            }
            param.setTaskIdList(propertyCheckTaskRelations.stream().map(PropertyCheckTaskRelation::getTaskId).collect(Collectors.toList()));
        }
        page = this.baseMapper.queryTaskByUserIdPage(page, param, userId);
        Pagination<PropertyCheckTaskPageVO> pagination = new Pagination<>(page.getTotal(), page.getPages(), page.getRecords());
        extendProperCheckTaskPageVOSS(pagination.getRecords());
        return pagination;
    }

    private void extendProperCheckTaskPageVOSS(List<PropertyCheckTaskPageVO> records) {
        for (PropertyCheckTaskPageVO ex : records) {
            if (ObjectUtils.isEmpty(ex.getEstimateEndTime())) {
                ex.setEstimateEndTime(null);
            }
            if (ObjectUtils.isEmpty(ex.getRealityEndTime())) {
                ex.setRealityEndTime(null);
            }
            UserDTO user = userClient.getUser(ex.getCheckUserId());
            if (ObjectUtils.isNotEmpty(user)) {
                ex.setCheckUserName(user.getName());
                ex.setCheckDepName(Optional.ofNullable(user.getOrganization()).orElse(new UserDTO.Organization()).getName());
            }
            ex.setDutyUserName(userClientService.getUser(ex.getDutyUserId()));
            ex.setCheckWaitNumber(
                    ex.getCheckTotalNumber() - ex.getCheckCompleteNumber() - ex.getCheckLossNumber() - ex.getAbnormalNumber());
        }
    }

    @Override
    public List<PropertyCheckTaskPageVO> queryTaskByPlan(Long planId) {
        List<PropertyCheckTaskPageVO> data = this.baseMapper.queryTaskByPlan(planId);
        extendProperCheckTaskPageVOSS(data);
        return data;
    }

    @Override
    public Pagination<PropertyCheckTaskPageVO> queryTaskByPropertyId(PropertyCheckTaskParam param) {
        Page<PropertyCheckTaskPageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.queryTaskByPropertyId(page, param);
        extendProperCheckTaskPageVOSS(page.getRecords());
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords());
    }

    @Override
    public Pagination<PropertyDetailedVO> queryCheckTaskPage(Long pageNumber, Long pageSize, Long taskId, String taskStatus) {
        Page<PropertyDetailedVO> page = new Page<>(pageNumber, pageSize);
        page = baseMapper.queryAllByTaskIdPage(page, taskId, taskStatus);
        Pagination<PropertyDetailedVO> pagination = new Pagination<>(page.getTotal(), page.getPages(), page.getRecords());
        Boolean chooseType = baseMapper.getChooseType(taskId);
        for (PropertyDetailedVO ex : pagination.getRecords()) {
            if (ObjectUtils.isNotEmpty(ex.getImgsStr())) {
                List<Long> fileIdds = Arrays.stream(ex.getImgsStr().split(","))
                        .map(s -> Long.parseLong(s.trim()))
                        .collect(Collectors.toList());
                List<FileVO> file = fileClient.getByIds(fileIdds);
                if (ObjectUtils.isNotEmpty(file)) {
                    ex.setImgs(file);
                }
            }
            ex.setChooseType(chooseType);
            ex.setReceiveUserName(userClientService.getUser(ex.getReceiveUserId()));
        }
        return pagination;
    }

    @Override
    public Integer insertList(PropertyCheckPlan plan, List<PropertyDetailedPageVO> params) {
        log.info("生成盘点任务:{}", plan.getCheckType());
        List<PropertyCheckTaskRelation> taskRelationList;
        switch (plan.getCheckType()) {
            case CheckTypeConstant.PERSON:
                taskRelationList = this.checkByPerson(plan, params);
                break;
            case CheckTypeConstant.ORG:
                taskRelationList = this.checkByOrg(plan, params);
                break;
            case CheckTypeConstant.ADMIN:
                taskRelationList = this.checkByAdmin(plan, params);
                break;
            default:
                throw new ServiceException("未找到该盘点方式");
        }
        Assert.isNotAcceptable(ObjectUtils.isEmpty(taskRelationList), "该盘点计划未关联资产");
        // 批量添加盘点任务、资产关联数据
        taskRelationService.insertList(taskRelationList);
        // 更新任务资产数量
        List<PropertyCheckTask> taskList = baseMapper.selectList(new LambdaQueryWrapper<PropertyCheckTask>()
                                                                         .eq(PropertyCheckTask::getPlanId, plan.getId()));
        for (PropertyCheckTask ex : taskList) {
            Long number = taskRelationMapper.selectCount(new LambdaQueryWrapper<PropertyCheckTaskRelation>()
                                                                 .eq(PropertyCheckTaskRelation::getTaskId, ex.getId()));
            ex.setCheckTotalNumber(number);
            baseMapper.updateById(ex);
        }
        return taskRelationList.size();
    }

    @Override
    public Boolean submitProperty(CheckTaskRelationParam param) {
        PropertyCheckTaskRelation taskRelation = taskRelationMapper.selectOne(new LambdaQueryWrapper<PropertyCheckTaskRelation>()
                                                                                      .eq(PropertyCheckTaskRelation::getTaskId, param.getTaskId())
                                                                                      .eq(PropertyCheckTaskRelation::getPropertyId, param.getPropertyId())
                                                                                      .orderByDesc(PropertyCheckTaskRelation::getCreateTime));
        if (ObjectUtils.isEmpty(taskRelation)) {
            throw new ServiceException("无效扫码");
        }
        if (!CheckTaskStatusEnum.INCOMPLETE.name().equals(taskRelation.getCheckStatus())) {
            throw new ServiceException("该资产已被盘点");
        }
        taskRelation.setCheckStatus(param.getCheckStatus());
        taskRelation.setCheckTime(DateUtils.now());
        taskRelation.setAbnormalType(param.getAbnormalType());
        taskRelation.setAbnormalRemarks(param.getAbnormalRemarks());
        if (ObjectUtils.isNotEmpty(param.getAbnormalFiles())) {
            taskRelation.setAbnormalFile(StringUtils.join(",", param.getAbnormalFiles()));
        }
        boolean bool = taskRelationMapper.updateById(taskRelation) > 0;
        PropertyCheckTask task = checkTaskMapper.selectById(param.getTaskId());
        if (CheckTaskStatusEnum.COMPLETED.name().equals(param.getCheckStatus())) {
            task.setCheckCompleteNumber(task.getCheckCompleteNumber() + 1);
        } else if (CheckTaskStatusEnum.LOSS.name().equals(param.getCheckStatus())) {
            task.setCheckLossNumber(task.getCheckLossNumber() + 1);
        } else if (CheckTaskStatusEnum.ABNORMAL.name().equals(param.getCheckStatus())) {
            task.setAbnormalNumber(task.getAbnormalNumber() + 1);
        }
        checkTaskMapper.updateById(task);
        if (bool) {
            // 修改资产信息
            PropertyDetailed propertyDetailed = propertyDetailedMapper.selectById(param.getPropertyId());
            propertyDetailed.setReceiveDepName(param.getReceiveDepName());
            propertyDetailed.setReceiveDepId(param.getReceiveDepId());
            propertyDetailed.setReceiveUserId(param.getReceiveUserId());
            String propertyStatus = ObjectUtils.isNotEmpty(param.getReceiveDepId()) ? PropertyStatusEnum.MAKE_USE_OF.name()
                                                                                    : PropertyStatusEnum.LEAVE_UNUSED.name();
            if (ObjectUtils.isNotEmpty(propertyDetailed.getStatus()) &&
                PropertyStatusEnum.REPAIR_REPORT.name().equals(propertyDetailed.getStatus())) {
                propertyRepairReportService.updateRepairReport(propertyDetailed.getId(), propertyStatus);
            } else {
                propertyDetailed.setStatus(propertyStatus);
            }
            if (ObjectUtils.isNotEmpty(param.getAreaIds())) {
                propertyDetailed.setAreaId(param.getAreaIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
            }
            propertyDetailed.setStorageLocation(param.getStorageLocation());
            // 处理资产图片
            if (ObjectUtils.isNotEmpty(param.getImgs())) {
                propertyDetailed.setImgs(StringUtils.join(",", param.getImgs()));
                FileInduceParam fileInduceParam = new FileInduceParam();
                fileInduceParam.setSceneCode(FileSceneCodeEnum.PROPERTY_MANAGEMENT_PROPERTY.getCode());
                fileInduceParam.setFileIdList(param.getImgs());
                fileOperateClient.fileInduceSingleBody(fileInduceParam);
            } else {
                propertyDetailed.setImgs(null);
            }
            bool = propertyDetailedMapper.updateById(propertyDetailed) > 0;
        }
        return bool;
    }

    public String getAraeName(List<String> areaIds) {
        StringBuffer areaName = new StringBuffer();
        if (ObjectUtils.isNotEmpty(areaIds)) {
            for (String areaId : areaIds) {
                AreaDTO area = areaClient.getAreaById(Long.parseLong(areaId));
                if (ObjectUtils.isNotEmpty(areaName)) {
                    areaName.append("/");
                }
                areaName.append(area.getName());
            }
        }
        return areaName.toString();
    }

    /**
     * 根据部门生成盘点任务
     *
     * @param plan   盘点计划信息
     * @param params 盘点资产信息
     */
    private List<PropertyCheckTaskRelation> checkByOrg(PropertyCheckPlan plan, List<PropertyDetailedPageVO> params) {
        log.info("根据部门生成盘点任务");
        Map<Long, PropertyCheckTask> map = new HashMap<>();
        List<PropertyCheckTaskRelation> relationList = new ArrayList<>();
        params.forEach(ex -> {
            if (ObjectUtils.isNotEmpty(ex.getReceiveDepId())) {
                PropertyCheckTaskAdmin taskAdmin = adminMapper.selectOne(new LambdaQueryWrapper<PropertyCheckTaskAdmin>()
                                                                                 .eq(PropertyCheckTaskAdmin::getOrgId, ex.getReceiveDepId()));
                if (ObjectUtils.isEmpty(ex.getReceiveDepName())) {
                    ex.setReceiveDepName("有部门");
                }
                Assert.isNotAcceptable(ObjectUtils.isEmpty(taskAdmin), ex.getReceiveDepName() + "未配置资产管理员");
                PropertyCheckTask task = map.get(ex.getReceiveDepId());
                if (ObjectUtils.isNotEmpty(task)) {
                    task.setCheckTotalNumber(task.getCheckTotalNumber() + 1);
                    this.lambdaUpdate().eq(PropertyCheckTask::getId, task.getId())
                            .set(PropertyCheckTask::getCheckTotalNumber, task.getCheckTotalNumber())
                            .update();
                    map.put(ex.getReceiveDepId(), task);
                } else {
                    // 新增盘点任务
                    task = this.getTask(plan, 1L, taskAdmin.getAdminId());
                    map.put(ex.getReceiveDepId(), task);
                }
                // 盘点任务关联资产
                relationList.add(this.getTaskRelation(task.getId(), ex.getId(), plan.getId()));
            }
        });
        Assert.isNotAcceptable(ObjectUtils.isEmpty(relationList), "所有部门下无资产");
        return relationList;
    }

    /**
     * 根据管理员生成盘点任务
     *
     * @param plan   盘点计划信息
     * @param params 盘点资产信息
     *
     * @return
     */
    private List<PropertyCheckTaskRelation> checkByAdmin(PropertyCheckPlan plan, List<PropertyDetailedPageVO> params) {
        log.info("根据管理员生成盘点任务");
        // 资产、任务关联数据
        List<PropertyCheckTaskRelation> relationList = new ArrayList<>();
        // 已经生成的任务
        Map<Long, Long> map = new HashMap<>();
        // 盘点任务信息
        Map<Long, PropertyCheckTask> taskNum = new HashMap<>();
        params.forEach(ex -> {
            Assert.isNotAcceptable(ObjectUtils.isEmpty(ex.getAdminId()), ex.getName() + "未配置资产管理员");
            if (ObjectUtils.isNotEmpty(ex.getAdminId())) {
                PropertyCheckTask task = new PropertyCheckTask();
                // 一个人多个资产，只需要生成一个任务，需要先判断是否已经生成任务了
                Long taskId = map.get(ex.getAdminId());
                if (ObjectUtils.isNotEmpty(taskId)) {
                    // 已经生成任务，则增加盘点的资产数量
                    task = taskNum.get(taskId);
                    task.setCheckTotalNumber(task.getCheckTotalNumber() + 1);
                } else {
                    // 没有生成任务则新增盘点任务,资产总数默认为1
                    task = this.getTask(plan, 1L, ex.getAdminId());
                    // 新增记录,一个人多个资产，只需要生成一个任务
                    map.put(ex.getAdminId(), task.getId());
                }
                taskNum.put(task.getId(), task);
                relationList.add(this.getTaskRelation(task.getId(), ex.getId(), plan.getId()));
            }
        });
        log.info("生成盘点任务完成，开始更新资产总数量");
        taskNum.values().forEach(ex -> {
            this.baseMapper.updateById(ex);
        });
        return relationList;
    }

    /**
     * 负责人盘点
     *
     * @param plan   盘点计划信息
     * @param params 资产信息
     *
     * @return 盘点任务、资产关联数据
     */
    private List<PropertyCheckTaskRelation> checkByPerson(PropertyCheckPlan plan, List<PropertyDetailedPageVO> params) {
        log.info("根据负责人生成盘点任务");
        PropertyCheckTask task = this.getTask(plan, (long) params.size(), plan.getDutyUserId());
        List<PropertyCheckTaskRelation> relationList = new ArrayList<>();
        params.forEach(ex -> {
            relationList.add(this.getTaskRelation(task.getId(), ex.getId(), plan.getId()));
        });
        return relationList;
    }

    /**
     * 生成任务数据
     *
     * @param plan             盘点计划信息
     * @param checkTotalNumber 盘点资产总数
     * @param checkUserId      盘点人
     *
     * @return 盘点任务信息
     */
    private PropertyCheckTask getTask(PropertyCheckPlan plan, Long checkTotalNumber, Long checkUserId) {
        PropertyCheckTask task = new PropertyCheckTask();
        task.setPlanId(plan.getId());
        task.setPlanName(plan.getTaskName());
        task.setStatus(TaskStatusEnum.UNDONE.name());
        task.setCheckTotalNumber(checkTotalNumber);
        task.setCheckCompleteNumber(0);
        task.setCheckWinNumber(0);
        task.setCheckLossNumber(0);
        task.setCheckUserId(checkUserId);
        task.setEstimateEndTime(plan.getEstimateEndTime());
        int i = this.baseMapper.insert(task);
        if (i < 1) {
            throw new ServiceException("生成任务失败");
        }
        Map<String, String> map = new HashMap<>();
        map.put("taskName", plan.getTaskName());
        messageUtil.messageSend(MessageModuleTypeEnum.INVENTORY_TASK.name(), map, task.getCheckUserId());
        propertyTaskTodoUtil.todoInsert(task, "PROPERTY_TASK", "资产盘点");
        propertyTaskCenterUtil.workInsert(task, "PROPERTY_TASK", "资产盘点");
        return task;
    }

    /**
     * 生成任务和资产关系表数据
     *
     * @param taskId     盘点任务id
     * @param propertyId 资产id
     * @param planId     盘点计划id
     *
     * @return 盘点任务、资产关联数据
     */
    private PropertyCheckTaskRelation getTaskRelation(Long taskId, Long propertyId, Long planId) {
        PropertyCheckTaskRelation relation = new PropertyCheckTaskRelation();
        relation.setTaskId(taskId);
        relation.setPropertyId(propertyId);
        relation.setPlanId(planId);
        relation.setCheckStatus(CheckTaskStatusEnum.INCOMPLETE.name());
        return relation;
    }
}
