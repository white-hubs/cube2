package com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl;

import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.util.CustomCellWriteUtil;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainFileSceneCodeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.param.StatisticsQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.common.service.MaintainUserClientService;
import com.guhecloud.cube.srv.gen.maintain.server.common.util.DatePeriodUtils;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolTaskLocationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTask;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTaskLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.AddTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.ExceptionTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PatrolResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PatrolTaskStateEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolLocationPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.StatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskResultParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolLocationService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskLocationService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.CommonUtil;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.DateUnitUtils;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.PatrolDataPermissionUtils;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolLocationVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationTopStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.PatrolRouteStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.PersonnelStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskLocationStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.ElectronLocationSafetyReportVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.ElectronTaskSafetyReportVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolTaskDataVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.TaskTimeQuantumVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl.AreaClientService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl.ExportExcelService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 巡更记录
 * @date 2023/5/29 14:47
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolTaskLocationServiceImpl extends ServiceImpl<PatrolTaskLocationMapper, PatrolTaskLocation> implements PatrolTaskLocationService {

    private final PatrolLocationService patrolLocationService;
    private final FileRefClient fileRefClient;
    private final PatrolTaskLocationMapper patrolTaskLocationMapper;
    private final FileClient fileClient;
    private final ExportExcelService exportExcelService;
    private final MaintainUserClientService maintainUserClientService;
    private final AreaClientService areaClientService;
    private final OrganizationClientService organizationClientService;
    private final UserClient userClient;
    private final PatrolDataPermissionUtils patrolDataPermissionUtils;
    private final PatrolTaskMapper patrolTaskMapper;

    /**
     * @Author wql
     * @Description 生成任务对应的巡更记录
     * @Date 14:42 2023/5/29
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTaskLocation(Long id, Long planId, List<Long> locationIds, TaskTimeQuantumVO taskTimeQuantumVO) {
        ArrayList<PatrolTaskLocation> list = new ArrayList<>();
        for (int i = 0; i < locationIds.size(); i++) {
            list.add(PatrolTaskLocation.builder().locationId(locationIds.get(i)).orderNum((long) i + 1).planId(planId)
                             .startDate(taskTimeQuantumVO.getStartDate()).endDate(taskTimeQuantumVO.getEndDate()).taskId(id)
                             .addType(AddTypeEnum.TASK.name()).build());
        }
        this.saveBatch(list);
    }

    /**
     * 生成任务对应的巡更记录
     *
     * @param id            任务id
     * @param executePeriod 任务时间段
     * @param planId        计划id
     * @param locationIds   路线id集合
     * @param dateTime      任务的开始日期
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTaskLocation(Long id, String belonger, List<String> executePeriod, Long planId, List<Long> locationIds,
                                 LocalDateTime dateTime
                                ) {
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
        }
        ArrayList<PatrolTaskLocation> list = new ArrayList<>();
        if (executePeriod == null || executePeriod.isEmpty()) {
            throw new ServiceException("任务时段不能为空！");
        }
        for (String period : executePeriod) {
            String[] periods = period.split("-");
            var start = DateUtils.parse(DateUtils.formatDate(dateTime) + " " + periods[0] + ":00");
            if (start.isBefore(dateTime)) {
                start = DateUtils.offsetDay(start, 1);
            }
            var end = DateUtils.parse(DateUtils.formatDate(dateTime) + " " + periods[1] + ":00");
            if (end.isBefore(dateTime)) {
                end = DateUtils.offsetDay(end, 1);
            }
            for (int i = 0; i < locationIds.size(); i++) {
                list.add(PatrolTaskLocation.builder().locationId(locationIds.get(i)).orderNum((long) i + 1).planId(planId).startDate(start)
                                 .endDate(end).taskId(id).addType(AddTypeEnum.TASK.name()).executePeriod(period).build());
            }
        }

        this.saveBatch(list);
    }

    /**
     * @Author wql
     * @Description 获取巡更任务的巡更记录
     * @Date 15:42 2023/5/29
     **/
    @Override
    public List<PatrolTaskDataVO> getTaskLocation(Long taskId, String executePeriod, String areaSequence, Boolean resultNull) {
        PatrolTask task = patrolTaskMapper.selectById(taskId);
        Assert.isNotFound(task, "任务不存在！");
        var areaLocationIds = new HashSet<Long>();
        // 区域sequence条件
        if (StringUtils.isNotBlank(areaSequence)) {
            if (areaSequence.endsWith("-")) {
                areaSequence = areaSequence.substring(0, areaSequence.length() - 1);
            }
            areaSequence = areaSequence.replaceAll("-", ",");
            patrolLocationService.lambdaQuery().eq(PatrolLocation::getAreaId, areaSequence).list()
                    .forEach(i -> areaLocationIds.add(i.getId()));
        }
        Long loginUserId = SecurityContextHolder.getPrincipal().getId();
        List<Long> superintendentUserIds = this.lambdaQuery().select(PatrolTaskLocation::getSuperintendentUserId)
                .eq(PatrolTaskLocation::getTaskId, taskId).list().stream().map(PatrolTaskLocation::getSuperintendentUserId)
                .toList();
        // 是否包含登陆人
        boolean isSuperintendent = superintendentUserIds.contains(loginUserId);
        // 是否是任务负责人
        boolean isResponsible = loginUserId.equals(task.getUserId()) ||
                                (task.getAssistIds() != null && task.getAssistIds().contains(String.valueOf(loginUserId)));
        // 是否需要权限查询(只有被分发了点位,且不是负责人才进行权限验证。因为详情没有数据权限,列表才有。能看到列表并且点击详情就待代表有权限查看)
        boolean permission = isSuperintendent && !isResponsible;
        List<PatrolTaskLocation> taskLocations = this.lambdaQuery().eq(PatrolTaskLocation::getTaskId, taskId)
                // 筛选未处理的
                .isNull(Boolean.TRUE.equals(resultNull), PatrolTaskLocation::getResult)
                .eq(StringUtils.isNotBlank(executePeriod), PatrolTaskLocation::getExecutePeriod, executePeriod)
                .in(CollectionUtils.isNotEmpty(areaLocationIds), PatrolTaskLocation::getLocationId, areaLocationIds)
                // 权限查询
                .eq(permission, PatrolTaskLocation::getSuperintendentUserId, loginUserId)
                .groupBy(PatrolTaskLocation::getLocationId)
                .orderByAsc(PatrolTaskLocation::getStartDate, PatrolTaskLocation::getOrderNum).list();
        List<PatrolTaskDataVO> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(taskLocations)) {
            return list;
        }
        Set<Long> locationIds = new HashSet<>();
        Set<Long> userIds = new HashSet<>();
        List<Long> ids = new ArrayList<>();
        for (var location : taskLocations) {
            locationIds.add(location.getLocationId());
            userIds.add(location.getUserId());
            userIds.add(location.getSuperintendentUserId());
            ids.add(location.getId());
        }
        PatrolLocationPageParam locationParam = new PatrolLocationPageParam();
        locationParam.setIdSet(locationIds);
        Map<Long, PatrolLocationVO> patrolLocationVOMap = Optional.ofNullable(patrolLocationService.queryLocationBatch(locationParam))
                .orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(PatrolLocationVO::getId, Function.identity()));
        Map<Long, UserDTO> userDTOMap = maintainUserClientService.getUserBatch(userIds);
        BusinessKeysParam param = new BusinessKeysParam();
        param.setBusinessKeys(ids);
        Map<Long, List<FileVO>> byBusinessKeys = fileClient.getByBusinessKeys(param);
        taskLocations.forEach(taskLocation -> {
            PatrolLocationVO locationVO = patrolLocationVOMap.getOrDefault(taskLocation.getLocationId(), new PatrolLocationVO());
            // 协同人权限
            boolean isAssistant = task.getUserId() != null && task.getAssistIds() != null &&
                                  task.getUserId().equals(taskLocation.getSuperintendentUserId()) &&
                                  task.getAssistIds().contains(String.valueOf(loginUserId));

            list.add(PatrolTaskDataVO.builder().id(taskLocation.getId()).locationId(taskLocation.getLocationId())
                             .superintendentUserId(taskLocation.getSuperintendentUserId())
                             .superintendentUserName(userDTOMap.getOrDefault(taskLocation.getSuperintendentUserId(), new UserDTO())
                                                             .getName())
                             .superintendent(Objects.equals(taskLocation.getSuperintendentUserId(), loginUserId) || isAssistant)
                             .locationName(locationVO.getPatrolLocationName()).arae(locationVO.getArea()).nfcCode(locationVO.getNfcCode())
                             .bluetoothCode(locationVO.getBluetoothCode()).patrolDate(taskLocation.getPatrolDate())
                             .patrolResult(taskLocation.getResult()).hiddenId(taskLocation.getHiddenId()).remark(taskLocation.getRemark())
                             .orderNum(taskLocation.getOrderNum()).startDate(taskLocation.getStartDate()).endDate(taskLocation.getEndDate())
                             .nfcLongitude(locationVO.getPatrolLongitude()).nfcDimension(locationVO.getPatrolDimension())
                             .userName(userDTOMap.getOrDefault(taskLocation.getUserId(), new UserDTO()).getName())
                             .files(byBusinessKeys.get(taskLocation.getId())).build());
        });
        return list;
    }

    /**
     * @Author wql
     * @Description 提交巡更结果
     * @Date 16:26 2023/5/29
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitResult(PatrolTaskResultParam param) {
        PatrolTaskLocation byId = this.getById(param.getId());
        Assert.isNotFound(byId, "该巡更记录不存在!");
        Assert.isNotAcceptable(DateUtils.compare(byId.getStartDate(), LocalDateTime.now()) >= 0, "该任务还未开始!");
        Assert.isNotAcceptable(DateUtils.compare(byId.getEndDate(), LocalDateTime.now()) <= 0, "该任务已结束!");
        Assert.isNotAcceptable(Objects.nonNull(byId.getResult()), "该巡更点已被巡更");
        var task = patrolTaskMapper.selectById(byId.getTaskId());
        Long loginUserId = SecurityContextHolder.getPrincipal().getId();
        var isSuperintendent = byId.getSuperintendentUserId() != null && loginUserId.equals(byId.getSuperintendentUserId());
        // 是负责人的点位，且登陆人是协同人。
        var isAssistIds = byId.getSuperintendentUserId() != null && task.getBelonger() != null && task.getAssistIds() != null &&
                          task.getUserId().equals(byId.getSuperintendentUserId()) &&
                          task.getAssistIds().contains(String.valueOf(loginUserId));
        // 不是负责人也不是协同人
        Assert.isNotAcceptable(!(isSuperintendent || isAssistIds), "该巡更点不属于自己！");
        byId.setResult(param.getResult());
        byId.setPatrolDate(LocalDateTime.now());
        byId.setPatrolOrder(countPatrolOrder(byId.getTaskId()));
        byId.setUserId(SecurityContextHolder.getPrincipal().getId());
        if (PatrolResultEnum.ABNORMAL.name().equals(param.getResult())) {
            byId.setExceptionType(ExceptionTypeEnum.PATROL_ABNORMAL.name());
        }
        byId.setRemark(param.getRemark());
        if (StringUtils.isNotBlank(param.getHiddenId())) {
            byId.setHiddenNum(true);
            byId.setHiddenId(param.getHiddenId());
        }
        this.updateById(byId);
        buildFiles(byId.getId(), param.getFileIds());
        return byId.getTaskId();
    }

    /**
     * @Author wql
     * @Description 计算实际巡更顺序
     * @Date 14:25 2023/10/27
     **/
    private Long countPatrolOrder(Long taskId) {
        Long count = this.lambdaQuery().eq(PatrolTaskLocation::getTaskId, taskId).isNotNull(PatrolTaskLocation::getPatrolDate).count();
        return count + 1;
    }

    /**
     * @Author wql
     * @Description 统计该任务还未巡更记录数
     * @Date 17:22 2023/5/29
     **/
    @Override
    public Long getNotInspectedNum(Long taskId) {
        return (long) this.getNotExecuteRecord(taskId, null).size();
    }

    private List<PatrolTaskLocation> getNotExecuteRecord(Long taskId, String executePeriod) {
        List<PatrolTaskLocation> list = this.lambdaQuery().eq(PatrolTaskLocation::getTaskId, taskId)
                .eq(StringUtils.isNotBlank(executePeriod), PatrolTaskLocation::getExecutePeriod, executePeriod)
                .and(q -> q.isNull(PatrolTaskLocation::getResult).or().isNull(PatrolTaskLocation::getPatrolDate)).list();
        return Optional.ofNullable(list).orElse(Collections.emptyList());
    }

    /**
     * @param taskId 任务id
     * @param hand   true表示只查手动填写的异常，过期自动异常不会查询。(有巡更时间的是手动填写
     *
     * @Author wql
     * @Description 查询结果异常的巡巡更录
     * @Date 17:26 2023/5/29
     **/
    @Override
    public Long countAbnormalResultRecord(Long taskId, Boolean hand) {
        return this.lambdaQuery().eq(PatrolTaskLocation::getTaskId, taskId).eq(PatrolTaskLocation::getResult, PatrolResultEnum.ABNORMAL)
                .isNotNull(Boolean.TRUE.equals(hand), PatrolTaskLocation::getPatrolDate).count();
    }

    /**
     * @Author wql
     * @Description 将剩余未巡更的记录改为超时异常
     * @Date 17:27 2023/5/29
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void overtimeRecord(Long userId, Long taskId) {
        List<PatrolTaskLocation> list = getNotExecuteRecord(taskId, null);
        list.forEach(record -> {
            record.setUserId(userId);
            record.setResult(PatrolResultEnum.ABNORMAL.name());
            record.setExceptionType(ExceptionTypeEnum.LEAK_DETECTION.name());
        });
        this.updateBatchById(list);
    }

    /**
     * @Author wql
     * @Description 更新未巡更记录的巡更人员
     * @Date 17:32 2023/5/29
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserId(Long userId, Long taskId) {
        List<PatrolTaskLocation> list = getNotExecuteRecord(taskId, null);
        list.forEach(record -> {
            record.setUserId(userId);
        });
        this.updateBatchById(list);
    }

    /**
     * @Author wql
     * @Description 巡更点统计
     * @Date 15:15 2023/5/30
     **/
    @Override
    public Pagination<LocationStatisticsVO> locationStatistics(StatisticParam param) {
        Page<PatrolTaskLocation> page = new Page<>(param.getPageNumber(), param.getPageSize());
        Page<LocationStatisticsVO> result = patrolTaskLocationMapper.locationStatistics(page, param);
        setLocationOtherInfo(result.getRecords(), param.getPeriod());
        return PagingUtils.convert(result);
    }

    @Override
    public List<LocationStatisticsVO> locationStatisticsTop5(StatisticParam param) {
        List<LocationStatisticsVO> locationStatistics = patrolTaskLocationMapper.getLocationStatistics(param);
        for (LocationStatisticsVO locationStatistic : locationStatistics) {
            locationStatistic.setPatrolRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(locationStatistic.getTotalNum(), locationStatistic.getRealNum()));
        }
        return locationStatistics;
    }

    @Override
    public List<LocationStatisticsVO> getPatrolExpStat(StatisticParam param) {
        List<LocationStatisticsVO> list = patrolTaskLocationMapper.getPatrolExpStat(param);
        if (param.getResultSize() != null && CollectionUtils.isNotEmpty(list) && list.size() >= param.getResultSize()) {
            list = list.subList(0, param.getResultSize());
        }
        return list;
    }

    /**
     * @Author wql
     * @Description 统计其他字段信息
     * @Date 17:01 2023/5/31
     **/
    private void setLocationOtherInfo(List<LocationStatisticsVO> list, Period period) {
        Optional.ofNullable(list).orElse(Collections.emptyList()).forEach(vo -> {
            //异常次数
            Long abnormal = this.lambdaQuery().between(ObjectUtils.isNotNull(period), PatrolTaskLocation::getStartDate, ObjectUtils
                            .ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                    .eq(PatrolTaskLocation::getLocationId, vo.getLocationId()).eq(PatrolTaskLocation::getResult, PatrolResultEnum.ABNORMAL)
                    .count();
            //漏巡数
            Long leak = this.lambdaQuery().between(ObjectUtils.isNotNull(period), PatrolTaskLocation::getStartDate, ObjectUtils
                            .ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                    .eq(PatrolTaskLocation::getLocationId, vo.getLocationId()).eq(PatrolTaskLocation::getResult, PatrolResultEnum.ABNORMAL)
                    .eq(PatrolTaskLocation::getExceptionType, ExceptionTypeEnum.LEAK_DETECTION).count();
            //实巡数
            Long realNum = this.lambdaQuery().between(ObjectUtils.isNotNull(period), PatrolTaskLocation::getStartDate, ObjectUtils
                            .ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                    .eq(PatrolTaskLocation::getLocationId, vo.getLocationId()).isNotNull(PatrolTaskLocation::getPatrolDate).count();
            //所属区域
            vo.setAreaName(areaClientService.getAreaNamesByIds(vo.getAreaId()));
            vo.setOmitNum(leak);
            vo.setAbnormalNum(abnormal);
            vo.setRealNum(realNum);
            vo.setPatrolRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(vo.getTotalNum(), vo.getRealNum()));
        });
    }

    /**
     * @Author wql
     * @Description 巡更点统计导出
     * @Date 15:15 2023/5/30
     **/
    @Override
    public ExcelWriter locationExport(StatisticParam param) {
        Period period = param.getPeriod();
        List<LocationStatisticsVO> result = patrolTaskLocationMapper.locationExport(param);
        setLocationOtherInfo(result, period);
        return exportExcelService.locationExport(result);
    }

    /**
     * @Author wql
     * @Description 巡更人员统计
     * @Date 15:20 2023/5/30
     **/
    @Override
    public Pagination<PersonnelStatisticsVO> personnelStatistics(StatisticParam param) {
        Page<PatrolTaskLocation> page = new Page<>(param.getPageNumber(), param.getPageSize());
        List<PatrolTask> patrolTasks = patrolDataPermissionUtils.setDataPermission(null, "total")
                .select(PatrolTask::getId).list();
        if (CollectionUtils.isEmpty(patrolTasks)) {
            return new Pagination<>();
        }
        param.setTaskIds(patrolTasks.stream().map(PatrolTask::getId).collect(Collectors.toList()));
        Page<PersonnelStatisticsVO> result = patrolTaskLocationMapper.personnelStatistics(page, param);
        setPersonnelOtherInfo(result.getRecords(), param.getPeriod());
        return PagingUtils.convert(result);
    }

    /**
     * @Author wql
     * @Description 设置人员统计其他信息
     * @Date 17:45 2023/5/31
     **/
    private void setPersonnelOtherInfo(List<PersonnelStatisticsVO> result, Period period) {
        Set<Long> userIds = result.stream().map(PersonnelStatisticsVO::getUserId).collect(Collectors.toSet());
        UserQuery userQuery = new UserQuery();
        userQuery.setUserIds(userIds);
        List<UserDTO> userList = userClient.list(userQuery);
        Map<Long, UserDTO> userMap = userList.stream().collect(Collectors.toMap(UserDTO::getId, value -> value));
        Optional.ofNullable(result).orElse(Collections.emptyList()).forEach(vo -> {
            UserDTO user = userMap.get(vo.getUserId());
            if (ObjectUtils.isEmpty(user)) {
                return;
            }
            vo.setUserName(user.getName());
            if (ObjectUtils.isNotEmpty(user.getOrganization())) {
                vo.setDepName(user.getOrganization().getName());
            }
            //异常任务数
            List<PatrolTaskLocation> list = this.lambdaQuery().eq(PatrolTaskLocation::getUserId, vo.getUserId())
                    .eq(PatrolTaskLocation::getResult, PatrolResultEnum.ABNORMAL)
                    .between(ObjectUtils.isNotNull(period), PatrolTaskLocation::getStartDate, ObjectUtils
                            .ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                    .select(PatrolTaskLocation::getTaskId).groupBy(PatrolTaskLocation::getTaskId).list();
            Integer count = ObjectUtils.isEmpty(list) ? 0 : list.size();

            //漏巡数
            Long leak = this.lambdaQuery().between(ObjectUtils.isNotNull(period), PatrolTaskLocation::getStartDate, ObjectUtils
                            .ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                    .eq(PatrolTaskLocation::getUserId, vo.getUserId()).eq(PatrolTaskLocation::getResult, PatrolResultEnum.ABNORMAL)
                    .eq(PatrolTaskLocation::getExceptionType, ExceptionTypeEnum.LEAK_DETECTION).count();
            //实巡数
            Long realNum = this.lambdaQuery().between(ObjectUtils.isNotNull(period), PatrolTaskLocation::getStartDate, ObjectUtils
                            .ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                    .eq(PatrolTaskLocation::getUserId, vo.getUserId()).isNotNull(PatrolTaskLocation::getPatrolDate).count();
            vo.setAbnormalTaskNum(count);
            vo.setOmitNum(leak);
            vo.setRealNum(realNum);
            vo.setPatrolRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(vo.getTotalNum(), vo.getRealNum()));
        });
    }

    /**
     * @Author wql
     * @Description 巡更人员统计导出
     * @Date 15:21 2023/5/30
     **/
    @Override
    public ExcelWriter personnelExport(StatisticParam param) {
        List<PatrolTask> patrolTasks = patrolDataPermissionUtils.setDataPermission(null, "total")
                .select(PatrolTask::getId).list();
        param.setTaskIds(patrolTasks.stream().map(PatrolTask::getId).collect(Collectors.toList()));
        List<PersonnelStatisticsVO> result = patrolTaskLocationMapper.personnelExport(param);
        setPersonnelOtherInfo(result, param.getPeriod());
        return exportExcelService.personnelExport(result);
    }

    @Override
    public Boolean updateHidden(List<String> hiddenDangerIds) {
        String hiddenDangerId = hiddenDangerIds.get(0);
        PatrolTaskLocation taskLocation = new PatrolTaskLocation();
        taskLocation.setHiddenIsDone(true);
        return this.lambdaUpdate().eq(PatrolTaskLocation::getHiddenId, hiddenDangerId).set(PatrolTaskLocation::getHiddenIsDone, true)
                .update();
    }

    /**
     * @Author wql
     * @Description 获取巡更点最新的巡更任务
     * @Date 9:00 2023/6/5
     **/
    @Override
    public PatrolTaskDataVO getNewestTask(Long locationId) {
        List<PatrolTask> tasks = patrolTaskLocationMapper.getNewestTask(locationId, PatrolTaskStateEnum.IN_EXECUTION.name());
        Assert.isNotAcceptable(ObjectUtils.isEmpty(tasks), "该巡更点没有执行中的巡更任务");
        Long userId = SecurityContextHolder.getPrincipal().getId();
        PatrolTaskLocation taskLocation = null;
        String patrolWay = "";
        for (PatrolTask task : tasks) {
            if (DateUtils.isIn(LocalDateTime.now(), task.getStartDate(), task.getEndDate())) {
                if (task.getInOrder()) {
                    //如果是顺序执行,需要找到当前执行点
                    List<PatrolTaskLocation> list = this.lambdaQuery().eq(PatrolTaskLocation::getTaskId, task.getId())
                            .isNull(PatrolTaskLocation::getPatrolDate).isNull(PatrolTaskLocation::getResult)
                            .orderByAsc(PatrolTaskLocation::getStartDate).orderByAsc(PatrolTaskLocation::getOrderNum).list();
                    if (ObjectUtils.isNotEmpty(list) && locationId.equals(list.get(0).getLocationId())) {
                        taskLocation = list.get(0);
                    }
                } else {
                    List<PatrolTaskLocation> list = this.lambdaQuery().eq(PatrolTaskLocation::getLocationId, locationId)
                            .isNull(PatrolTaskLocation::getPatrolDate).isNull(PatrolTaskLocation::getResult)
                            .eq(PatrolTaskLocation::getTaskId, task.getId()).list();
                    if (ObjectUtils.isNotEmpty(list)) {
                        taskLocation = list.get(0);
                    }
                }
                if (ObjectUtils.isNotNull(taskLocation) &&
                    (userId.equals(task.getUserId()) ||
                     (StringUtils.isNotBlank(task.getAssistIds()) && task.getAssistIds().contains(String.valueOf(userId))))) {
                    patrolWay = task.getPatrolWay();
                    break;
                } else {
                    taskLocation = null;
                }
            }
        }
        Assert.isNotAcceptable(ObjectUtils.isNull(taskLocation), "该巡更点没有可以执行的巡更任务");
        PatrolLocationVO locationVO = patrolLocationService.queryById(taskLocation.getLocationId());
        return PatrolTaskDataVO.builder().id(taskLocation.getId()).locationId(taskLocation.getLocationId())
                .locationName(locationVO.getPatrolLocationName()).arae(locationVO.getArea()).nfcCode(locationVO.getNfcCode())
                .patrolDate(taskLocation.getPatrolDate()).patrolResult(taskLocation.getResult()).hiddenId(taskLocation.getHiddenId())
                .remark(taskLocation.getRemark()).orderNum(taskLocation.getOrderNum()).startDate(taskLocation.getStartDate())
                .endDate(taskLocation.getEndDate()).patrolWay(patrolWay).files(fileClient.getByBusinessKey(locationId)).build();
    }

    @Override
    public List<LocationTopStatisticsVO> locationAbnormalTopStatisticsList(StatisticsQueryParam param) {
        List<LocationTopStatisticsVO> topStatistics = patrolTaskLocationMapper.locationAbnormalTopStatisticsList(param.getPeriod());
        Period previousPeriod = DatePeriodUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        List<LocationTopStatisticsVO> preTopStatistics = patrolTaskLocationMapper.locationAbnormalTopStatisticsList(previousPeriod);
        setTopChainRatio(topStatistics, preTopStatistics);
        return topStatistics;
    }

    @Override
    public List<LocationTopStatisticsVO> locationForgetTopStatisticsList(StatisticsQueryParam param) {
        List<LocationTopStatisticsVO> topStatistics = patrolTaskLocationMapper.locationForgetTopStatisticsList(param.getPeriod());
        Period previousPeriod = DatePeriodUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        List<LocationTopStatisticsVO> preTopStatistics = patrolTaskLocationMapper.locationForgetTopStatisticsList(previousPeriod);
        setTopChainRatio(topStatistics, preTopStatistics);
        return topStatistics;
    }

    @Override
    public TaskLocationStatisticsVO taskLocationAbnormalStatistics(StatisticsQueryParam param) {
        Long abnormalNum = this.lambdaQuery()
                .between(ObjectUtils.isNotNull(param.getPeriod()), PatrolTaskLocation::getStartDate, ObjectUtils
                        .ifNull(param.getPeriod(), Period::getBegin), ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                .eq(PatrolTaskLocation::getResult, PatrolResultEnum.ABNORMAL.name()).count();
        Long total = this.lambdaQuery().between(ObjectUtils.isNotNull(param.getPeriod()), PatrolTaskLocation::getStartDate, ObjectUtils
                .ifNull(param.getPeriod(), Period::getBegin), ObjectUtils.ifNull(param.getPeriod(), Period::getEnd)).count();
        return TaskLocationStatisticsVO.builder().locationNum(total).abnormalNum(abnormalNum)
                .proportion(MathUtils.calculateProportionPercentage(abnormalNum, total).floatValue()).build();
    }

    @Override
    public TaskLocationStatisticsVO taskLocationMissingStatistics(StatisticsQueryParam param) {
        Long leakNum = this.lambdaQuery().between(ObjectUtils.isNotNull(param.getPeriod()), PatrolTaskLocation::getStartDate, ObjectUtils
                        .ifNull(param.getPeriod(), Period::getBegin), ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                .eq(PatrolTaskLocation::getResult, PatrolResultEnum.ABNORMAL.name())
                .eq(PatrolTaskLocation::getExceptionType, ExceptionTypeEnum.LEAK_DETECTION.name()).count();
        Long total = this.lambdaQuery().between(ObjectUtils.isNotNull(param.getPeriod()), PatrolTaskLocation::getStartDate, ObjectUtils
                .ifNull(param.getPeriod(), Period::getBegin), ObjectUtils.ifNull(param.getPeriod(), Period::getEnd)).count();
        return TaskLocationStatisticsVO.builder().locationNum(total).abnormalNum(leakNum)
                .proportion(MathUtils.calculateProportionPercentage(leakNum, total).floatValue()).build();
    }

    @Override
    public ElectronTaskSafetyReportVO getElectronTaskForSafetyReport(Period period) {
        List<ElectronLocationSafetyReportVO> electronLocationSafetyReportVOS = patrolTaskLocationMapper
                .selectPatrolDataForSafetyReport(period);
        Long total = 0L;
        if (CollectionUtils.isNotEmpty(electronLocationSafetyReportVOS)) {
            total = electronLocationSafetyReportVOS.stream().map(ElectronLocationSafetyReportVO::getTotalNum).reduce(Long::sum).get();
            List<Long> userDepIdList = electronLocationSafetyReportVOS.stream().map(ElectronLocationSafetyReportVO::getUserDepId)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(userDepIdList)) {
                List<OrganizationDTO> orgInfoByIdList = organizationClientService.getOrgInfoByIdList(userDepIdList);
                Map<Long, String> deparmentMap = new HashMap<>(orgInfoByIdList.size());
                if (CollectionUtils.isNotEmpty(orgInfoByIdList)) {
                    deparmentMap = orgInfoByIdList.stream()
                            .collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName, (k1, k2) -> k1));
                }
                for (ElectronLocationSafetyReportVO safetyReportVO : electronLocationSafetyReportVOS) {
                    safetyReportVO.setUserDepName(deparmentMap.get(safetyReportVO.getUserDepId()));
                }
            }

        } else {
            electronLocationSafetyReportVOS = new ArrayList<>();
        }
        //环比
        Period month = DateUnitUtils.getPreviousPeriod("THIS_MONTH", period);
        Long ringNum = patrolTaskLocationMapper.countNum(month);
        if (Objects.isNull(ringNum)) {
            ringNum = 0L;
        }
        //同比
        Period samePeriod = DateUnitUtils.getSamePeriod(period);
        Long sameNum = patrolTaskLocationMapper.countNum(samePeriod);

        if (Objects.isNull(sameNum)) {
            sameNum = 0L;
        }
        return ElectronTaskSafetyReportVO.builder().num(total).ringNum(ringNum).sameNum(sameNum)
                .electronLocationSafetyReportVOS(electronLocationSafetyReportVOS).build();
    }

    @Override
    public List<Long> getTaskIdByLocationId(Long locationId) {
        if (locationId == null) {
            return new ArrayList<>();
        }
        List<PatrolTaskLocation> list = this.lambdaQuery().eq(PatrolTaskLocation::getLocationId, locationId)
                .select(PatrolTaskLocation::getTaskId).list();
        return Optional.ofNullable(list).orElse(new ArrayList<>()).stream().map(PatrolTaskLocation::getTaskId).collect(Collectors.toList());
    }

    @Override
    public Pagination<PatrolRouteStatisticsVO> patrolRouteStatistics(StatisticParam param) {
        Integer total = patrolTaskLocationMapper.patrolRouteStatisticsTotal(param);
        if (ObjectUtils.isEmpty(total) || total == 0) {
            return new Pagination<>();
        }
        Page<PatrolRouteStatisticsVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        List<PatrolRouteStatisticsVO> data = patrolTaskLocationMapper.patrolRouteStatistics(param);
        page.setTotal(total);
        return PagingUtils.convert(page, this.patrolRouteStatisticsExtend(data));
    }

    @Override
    public void patrolRouteStatisticsExport(StatisticParam param, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        List<PatrolRouteStatisticsVO> data = patrolTaskLocationMapper.patrolRouteStatistics(param);
        Assert.parameterIsNull(data, "没有数据,无法导出");
        try {
            EasyExcelFactory.write(response.getOutputStream()).excelType(ExcelTypeEnum.XLSX).registerWriteHandler(new CustomCellWriteUtil())
                    .head(PatrolRouteStatisticsVO.class).sheet("巡更路线统计").doWrite(this.patrolRouteStatisticsExtend(data));
        } catch (Exception e) {
            log.error("电子巡更路线统计导出失败", e);
            throw new ServiceException("文件下载失败");
        }
    }

    private void setTopChainRatio(List<LocationTopStatisticsVO> topStatistics, List<LocationTopStatisticsVO> preTopStatistics) {
        Map<Long, Integer> map = new HashMap<>();
        if (ObjectUtils.isNotEmpty(preTopStatistics)) {
            map = preTopStatistics.stream()
                    .collect(Collectors.toMap(LocationTopStatisticsVO::getLocationId, LocationTopStatisticsVO::getNum));
        }
        if (ObjectUtils.isNotEmpty(topStatistics)) {
            for (LocationTopStatisticsVO vo : topStatistics) {
                vo.setChainRatio(MathUtils.calculateRingRatio(vo.getNum(),
                                                              map.get(vo.getLocationId()) == null ? 0
                                                                                                  : map.get(vo.getLocationId()))
                                         .floatValue());
            }
        }
    }

    /**
     * @Author wql
     * @Description 生成图片
     * @Date 16:31 2023/5/29
     **/
    private void buildFiles(Long id, List<Long> fileIds) {
        //绑定图片前先删除历史图片
        fileRefClient.removeByBusinessKeyAndScene(id, MaintainFileSceneCodeEnum.ELECT_PATROL_SUBMIT.getCode());
        List<FileRefCreateParam> params = Optional.ofNullable(fileIds).orElse(Collections.emptyList()).stream().map(fileId -> {
            FileRefCreateParam param = new FileRefCreateParam();
            param.setSceneCode(MaintainFileSceneCodeEnum.ELECT_PATROL_SUBMIT.getCode());
            param.setFileId(fileId);
            param.setBusinessKey(id);
            return param;
        }).toList();

        if (CollectionUtils.isNotEmpty(fileIds)) {
            FileRefCreateParam param = new FileRefCreateParam();
            param.setFileIdList(fileIds);
            param.setSceneCode(MaintainFileSceneCodeEnum.ELECT_PATROL_SUBMIT.getCode());
            param.setBusinessKey(id);
            this.fileRefClient.refInduce(param);
        }
    }

    private List<PatrolRouteStatisticsVO> patrolRouteStatisticsExtend(List<PatrolRouteStatisticsVO> data) {
        data.forEach(ex -> {
            ex.setTotalNum(Optional.ofNullable(ex.getTaskLocations()).orElse(new ArrayList<>()).size());
            ex.getTaskLocations().forEach(taskLocation -> {
                if (ObjectUtils.isNotEmpty(taskLocation.getResult())) {
                    ex.setRealNum(ex.getRealNum() + 1);
                } else {
                    ex.setOmitNum(ex.getOmitNum() + 1);
                }
            });
            if (ex.getTotalNum().equals(ex.getRealNum())) {
                ex.setPatrolRatio("100%");
            } else if (ex.getRealNum() == 0) {
                ex.setPatrolRatio("0%");
            } else {
                BigDecimal bigDecimal = new BigDecimal(ex.getRealNum())
                        .divide(new BigDecimal(ex.getTotalNum()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
                ex.setPatrolRatio(bigDecimal + "%");
            }
        });
        return data;
    }
}
