package com.guhecloud.cube.customization.xialing.server.drone.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.customization.xialing.server.drone.client.XianingDroneApiClient;
import com.guhecloud.cube.customization.xialing.server.drone.dao.DroneResultMapper;
import com.guhecloud.cube.customization.xialing.server.drone.dao.DroneRouteMapper;
import com.guhecloud.cube.customization.xialing.server.drone.dao.DroneSyncBatchMapper;
import com.guhecloud.cube.customization.xialing.server.drone.dao.DroneTaskMapper;
import com.guhecloud.cube.customization.xialing.server.drone.dao.DroneTaskRecordMapper;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneResult;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneRoute;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneSyncBatch;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneTask;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneTaskRecord;
import com.guhecloud.cube.customization.xialing.server.drone.service.DroneService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneResultQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneRouteCommandParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneRouteQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTaskCreateParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTaskFinishParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTaskQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTaskRecordQueryParam;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneResultVO;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneRouteVO;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneTaskRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneTaskVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 霞凝无人机服务实现
 * <p>
 * 对外提供查询与操作接口，对内负责将第三方平台数据同步到本地表。
 * 同步策略采用“按领域全量拉取 + 本地upsert + 缺失标记删除”的方式，
 * 并通过 {@link DroneSyncBatch} 记录每次同步批次的执行情况。
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {
    private static final int SYNC_PAGE_SIZE = 100;
    private static final List<Integer> TASK_TYPES = Arrays.asList(1, 3, 4, 5);
    private static final List<Integer> RESULT_FILE_TYPES = Arrays.asList(1, 2, 3);
    private static final List<Integer> RESULT_P_TYPES = Arrays.asList(0, 1);

    private final XianingDroneApiClient xianingDroneApiClient;
    private final DroneRouteMapper droneRouteMapper;
    private final DroneTaskMapper droneTaskMapper;
    private final DroneTaskRecordMapper droneTaskRecordMapper;
    private final DroneResultMapper droneResultMapper;
    private final DroneSyncBatchMapper droneSyncBatchMapper;

    @Override
    public Pagination<DroneRouteVO> pageRoutes(DroneRouteQueryParam param) {
        param = param == null ? new DroneRouteQueryParam() : param;
        // 基础字段过滤由 platform 统一处理，这里只保留业务筛选条件。
        var wrapper = Wrappers.<DroneRoute>lambdaQuery()
                .orderByDesc(DroneRoute::getUpdateTime);
        if (StringUtils.hasText(param.getTrackName())) {
            wrapper.like(DroneRoute::getTrackName, param.getTrackName());
        }
        if (StringUtils.hasText(param.getNestSn())) {
            wrapper.eq(DroneRoute::getNestSn, param.getNestSn());
        }
        if (param.getTypes() != null && !param.getTypes().isEmpty()) {
            wrapper.in(DroneRoute::getTrackType, param.getTypes());
        }
        // 查询并映射为VO输出。
        Page<DroneRoute> result = droneRouteMapper.selectPage(PagingUtils.convert(param), wrapper);
        List<DroneRouteVO> records = new ArrayList<>();
        for (DroneRoute route : result.getRecords()) {
            records.add(toRouteVO(route));
        }
        return new Pagination<>(result.getTotal(), result.getPages(), records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRoute(DroneRouteCommandParam param) {
        // 先调用第三方创建，再回刷本地航迹表。
        Long routeId = xianingDroneApiClient.createRoute(param);
        syncRoutes();
        return routeId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRoute(DroneRouteCommandParam param) {
        // 编辑第三方数据后，重新同步本地快照。
        Boolean result = xianingDroneApiClient.updateRoute(param);
        syncRoutes();
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRoute(Long routeId) {
        // 删除第三方数据后，同步本地删除状态。
        Boolean result = xianingDroneApiClient.deleteRoute(routeId);
        syncRoutes();
        return result;
    }

    @Override
    public Pagination<DroneTaskVO> pageTasks(DroneTaskQueryParam param) {
        param = param == null ? new DroneTaskQueryParam() : param;
        // 构建任务分页查询条件。
        var wrapper = Wrappers.<DroneTask>lambdaQuery()
                .orderByDesc(DroneTask::getExecutionTime)
                .orderByDesc(DroneTask::getUpdateTime);
        if (param.getType() != null) {
            wrapper.eq(DroneTask::getTaskType, param.getType());
        }
        if (StringUtils.hasText(param.getNestSn())) {
            wrapper.eq(DroneTask::getNestSn, param.getNestSn());
        }
        if (param.getTrackId() != null) {
            wrapper.eq(DroneTask::getThirdRouteId, param.getTrackId());
        }
        if (StringUtils.hasText(param.getTaskName())) {
            wrapper.like(DroneTask::getTaskName, param.getTaskName());
        }
        // 执行分页查询并转换为VO。
        Page<DroneTask> result = droneTaskMapper.selectPage(PagingUtils.convert(param), wrapper);
        List<DroneTaskVO> records = new ArrayList<>();
        for (DroneTask task : result.getRecords()) {
            records.add(toTaskVO(task));
        }
        return new Pagination<>(result.getTotal(), result.getPages(), records);
    }

    @Override
    public List<DroneTaskVO> listAbnormalTasks() {
        // 查询天气冲突或结果状态异常的任务。
        List<DroneTask> rows = droneTaskMapper.selectList(Wrappers.<DroneTask>lambdaQuery()
                                                                  .and(w -> w.eq(DroneTask::getWeatherConflict, true)
                                                                          .or()
                                                                          .in(DroneTask::getResultStatus, Arrays.asList(-1, 3, 6, 10)))
                                                                  .orderByDesc(DroneTask::getExecutionTime)
                                                                  .orderByDesc(DroneTask::getUpdateTime));
        List<DroneTaskVO> list = new ArrayList<>();
        for (DroneTask row : rows) {
            list.add(toTaskVO(row));
        }
        return list;
    }

    @Override
    public Pagination<DroneTaskRecordVO> pageTaskRecords(DroneTaskRecordQueryParam param) {
        param = param == null ? new DroneTaskRecordQueryParam() : param;
        var period = param.getExecutionTime();
        var wrapper = Wrappers.<DroneTaskRecord>lambdaQuery()
                .between(period !=
                         null, DroneTaskRecord::getExecutionTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .eq(param.getScheduleStatus() != null, DroneTaskRecord::getScheduleStatus, param.getScheduleStatus())
                .orderByDesc(DroneTaskRecord::getExecutionTime)
                .orderByDesc(DroneTaskRecord::getUpdateTime);
        if (param.getTaskId() != null) {
            // 仅按第三方任务ID过滤。
            wrapper.eq(DroneTaskRecord::getThirdTaskId, param.getTaskId());
        }
        Page<DroneTaskRecord> result = droneTaskRecordMapper.selectPage(PagingUtils.convert(param), wrapper);
        List<DroneTaskRecordVO> records = new ArrayList<>();
        for (DroneTaskRecord row : result.getRecords()) {
            records.add(toTaskRecordVO(row));
        }
        return new Pagination<>(result.getTotal(), result.getPages(), records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DroneTaskRecordVO> createTask(DroneTaskCreateParam param) {
        // 任务创建后，需要同步任务与任务记录两张表。
        List<DroneTaskRecordVO> records = xianingDroneApiClient.createTask(param);
        syncTasks();
        syncTaskRecords();
        return records;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteTask(Long taskId) {
        // 删除任务后，同步任务与任务记录。
        Boolean result = xianingDroneApiClient.deleteTask(taskId);
        syncTasks();
        syncTaskRecords();
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean finishTask(DroneTaskFinishParam param) {
        // 结束任务会影响任务状态与执行记录。
        Boolean result = xianingDroneApiClient.finishTask(param);
        syncTasks();
        syncTaskRecords();
        return result;
    }

    @Override
    public Pagination<DroneResultVO> pageResults(DroneResultQueryParam param) {
        param = param == null ? new DroneResultQueryParam() : param;
        // 构建成果分页查询。
        var wrapper = Wrappers.<DroneResult>lambdaQuery()
                .orderByDesc(DroneResult::getThirdCreateTime)
                .orderByDesc(DroneResult::getUpdateTime);
        if (param.getScheduledId() != null) {
            wrapper.eq(DroneResult::getThirdScheduleId, param.getScheduledId());
        }
        if (param.getFileType() != null) {
            wrapper.eq(DroneResult::getFileType, param.getFileType());
        }
        if (param.getChannelType() != null) {
            wrapper.eq(DroneResult::getChannelType, param.getChannelType());
        }
        if (param.getPType() != null) {
            wrapper.eq(DroneResult::getPhotoType, param.getPType());
        }
        // 查询成果并转换为VO。
        Page<DroneResult> result = droneResultMapper.selectPage(PagingUtils.convert(param), wrapper);
        List<DroneResultVO> records = new ArrayList<>();
        for (DroneResult row : result.getRecords()) {
            records.add(toResultVO(row));
        }
        return new Pagination<>(result.getTotal(), result.getPages(), records);
    }

    @Override
    public void downloadResultFile(String fileKey, HttpServletResponse response) throws IOException {
        xianingDroneApiClient.downloadResultFile(fileKey, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fullSyncCurrentTenant() {
        // 按业务依赖顺序同步：先航迹和任务，再任务记录，最后成果数据。
        syncRoutes();
        syncTasks();
        syncTaskRecords();
        syncResults();
    }

    /**
     * 同步航迹数据。
     * <p>
     * 记录批次信息，拉取远端全量航迹并执行本地upsert，失败时写入错误信息。
     * </p>
     */
    private void syncRoutes() {
        Integer tenant = currentTenant();
        // 创建同步批次记录，方便追踪本次执行。
        DroneSyncBatch batch = createBatch("route", tenant);
        LocalDateTime syncTime = LocalDateTime.now();
        try {
            // 拉取全量并执行本地upsert。
            List<DroneRouteVO> remoteRows = fetchAllRoutes();
            batch.setTotalCount(remoteRows.size());
            upsertRoutes(remoteRows, tenant, syncTime);
            batch.setSuccessCount(remoteRows.size());
            batch.setFailCount(0);
            batch.setSyncStatus(1);
        } catch (Exception e) {
            // 失败时记录错误信息，不抛出以避免影响其他域同步。
            log.error("霞凝无人机航迹全量同步失败 tenant={}", tenant, e);
            batch.setSyncStatus(2);
            batch.setFailCount(batch.getTotalCount());
            batch.setErrorMessage(limitError(e));
        } finally {
            // 无论成功失败都要结束批次。
            finishBatch(batch);
        }
    }

    /**
     * 同步任务数据。
     */
    private void syncTasks() {
        Integer tenant = currentTenant();
        DroneSyncBatch batch = createBatch("task", tenant);
        LocalDateTime syncTime = LocalDateTime.now();
        try {
            // 全量抓取任务并落库。
            List<DroneTaskVO> remoteRows = fetchAllTasks();
            batch.setTotalCount(remoteRows.size());
            upsertTasks(remoteRows, tenant, syncTime);
            batch.setSuccessCount(remoteRows.size());
            batch.setFailCount(0);
            batch.setSyncStatus(1);
        } catch (Exception e) {
            log.error("霞凝无人机任务全量同步失败 tenant={}", tenant, e);
            batch.setSyncStatus(2);
            batch.setFailCount(batch.getTotalCount());
            batch.setErrorMessage(limitError(e));
        } finally {
            finishBatch(batch);
        }
    }

    /**
     * 同步任务记录数据。
     * <p>
     * 先从本地任务表获取第三方任务ID，再按任务维度分页拉取执行记录。
     * </p>
     */
    private void syncTaskRecords() {
        Integer tenant = currentTenant();
        DroneSyncBatch batch = createBatch("taskRecord", tenant);
        LocalDateTime syncTime = LocalDateTime.now();
        try {
            // 先取本地任务ID集合，再按任务逐页拉任务记录。
            List<DroneTask> tasks = droneTaskMapper.selectList(Wrappers.<DroneTask>lambdaQuery()
                                                                       .eq(DroneTask::getTenant, tenant)
                                                                       .eq(DroneTask::getDeleted, false)
                                                                       .select(DroneTask::getThirdTaskId));
            List<DroneTaskRecordVO> remoteRows = fetchAllTaskRecords(tasks);
            batch.setTotalCount(remoteRows.size());
            upsertTaskRecords(remoteRows, tenant, syncTime);
            batch.setSuccessCount(remoteRows.size());
            batch.setFailCount(0);
            batch.setSyncStatus(1);
        } catch (Exception e) {
            log.error("霞凝无人机任务记录全量同步失败 tenant={}", tenant, e);
            batch.setSyncStatus(2);
            batch.setFailCount(batch.getTotalCount());
            batch.setErrorMessage(limitError(e));
        } finally {
            finishBatch(batch);
        }
    }

    /**
     * 同步成果数据。
     * <p>
     * 先从本地任务记录提取调度ID，再按调度ID、文件类型、照片类型组合拉取成果。
     * </p>
     */
    private void syncResults() {
        Integer tenant = currentTenant();
        DroneSyncBatch batch = createBatch("result", tenant);
        LocalDateTime syncTime = LocalDateTime.now();
        try {
            // 成果查询依赖调度ID，先从任务记录表提取调度ID集合。
            List<DroneTaskRecord> records = droneTaskRecordMapper.selectList(Wrappers.<DroneTaskRecord>lambdaQuery()
                                                                                     .eq(DroneTaskRecord::getTenant, tenant)
                                                                                     .eq(DroneTaskRecord::getDeleted, false)
                                                                                     .select(DroneTaskRecord::getThirdScheduleId));
            List<DroneResultVO> remoteRows = fetchAllResults(records);
            batch.setTotalCount(remoteRows.size());
            upsertResults(remoteRows, tenant, syncTime);
            batch.setSuccessCount(remoteRows.size());
            batch.setFailCount(0);
            batch.setSyncStatus(1);
        } catch (Exception e) {
            log.error("霞凝无人机成果全量同步失败 tenant={}", tenant, e);
            batch.setSyncStatus(2);
            batch.setFailCount(batch.getTotalCount());
            batch.setErrorMessage(limitError(e));
        } finally {
            finishBatch(batch);
        }
    }

    /**
     * 拉取远端全部航迹（分页）。
     */
    private List<DroneRouteVO> fetchAllRoutes() {
        List<DroneRouteVO> result = new ArrayList<>();
        int pageNum = 1;
        while (true) {
            // 第三方接口要求显式传分页和筛选字段。
            DroneRouteQueryParam query = new DroneRouteQueryParam();
            query.setTrackName("");
            query.setNestSn("");
            query.setTypes(new ArrayList<>());
            query.setPageNumber(pageNum);
            query.setPageSize(SYNC_PAGE_SIZE);
            Pagination<DroneRouteVO> page = xianingDroneApiClient.pageRoutes(query);
            if (page.getRecords().isEmpty()) {
                // 空页即结束。
                break;
            }
            result.addAll(page.getRecords());
            if (page.getRecords().size() < SYNC_PAGE_SIZE) {
                // 最后一页不足pageSize时结束循环。
                break;
            }
            pageNum++;
        }
        return result;
    }

    /**
     * 拉取远端任务并去重合并。
     * <p>
     * 先按任务类型查询常规任务，再用异常任务接口补充并覆盖关键状态。
     * </p>
     */
    private List<DroneTaskVO> fetchAllTasks() {
        // 以第三方任务ID为key去重。
        Map<Long, DroneTaskVO> taskMap = new LinkedHashMap<>();
        for (Integer taskType : TASK_TYPES) {
            // 按任务类型分别拉取，补齐接口能力范围。
            DroneTaskQueryParam query = new DroneTaskQueryParam();
            query.setType(taskType);
            query.setPageNumber(1);
            query.setPageSize(SYNC_PAGE_SIZE);
            Pagination<DroneTaskVO> page = xianingDroneApiClient.pageTasks(query);
            for (DroneTaskVO item : page.getRecords()) {
                if (item.getTaskId() != null) {
                    taskMap.put(item.getTaskId(), item);
                }
            }
        }
        // 异常任务接口提供额外状态字段，合并到已有结果中。
        List<DroneTaskVO> abnormalRows = xianingDroneApiClient.listAbnormalTasks();
        for (DroneTaskVO abnormal : abnormalRows) {
            if (abnormal.getTaskId() == null) {
                continue;
            }
            DroneTaskVO existing = taskMap.get(abnormal.getTaskId());
            if (existing == null) {
                taskMap.put(abnormal.getTaskId(), abnormal);
            } else {
                // 已存在则仅覆盖关键信息，保留主查询返回的其余字段。
                mergeTask(existing, abnormal);
            }
        }
        return new ArrayList<>(taskMap.values());
    }

    /**
     * 根据本地任务列表拉取全部任务记录（分页）。
     */
    private List<DroneTaskRecordVO> fetchAllTaskRecords(List<DroneTask> tasks) {
        List<DroneTaskRecordVO> result = new ArrayList<>();
        for (DroneTask task : tasks) {
            if (task.getThirdTaskId() == null) {
                continue;
            }
            // 每个任务独立分页拉取执行记录。
            int pageNum = 1;
            while (true) {
                DroneTaskRecordQueryParam query = new DroneTaskRecordQueryParam();
                query.setTaskId(task.getThirdTaskId());
                query.setPageNumber(pageNum);
                query.setPageSize(SYNC_PAGE_SIZE);
                Pagination<DroneTaskRecordVO> page = xianingDroneApiClient.pageTaskRecords(query);
                if (page.getRecords().isEmpty()) {
                    break;
                }
                result.addAll(page.getRecords());
                if (page.getRecords().size() < SYNC_PAGE_SIZE) {
                    break;
                }
                pageNum++;
            }
        }
        return result;
    }

    /**
     * 根据调度ID拉取全部成果并按成果ID去重。
     */
    private List<DroneResultVO> fetchAllResults(List<DroneTaskRecord> records) {
        // 成果按ID去重，避免不同查询组合下重复数据。
        Map<Long, DroneResultVO> resultMap = new LinkedHashMap<>();
        Set<Long> scheduleIds = new LinkedHashSet<>();
        for (DroneTaskRecord record : records) {
            if (record.getThirdScheduleId() != null) {
                scheduleIds.add(record.getThirdScheduleId());
            }
        }
        for (Long scheduleId : scheduleIds) {
            // 按文件类型和照片类型组合遍历查询。
            for (Integer fileType : RESULT_FILE_TYPES) {
                for (Integer pType : RESULT_P_TYPES) {
                    int pageNum = 1;
                    while (true) {
                        DroneResultQueryParam query = new DroneResultQueryParam();
                        query.setScheduledId(scheduleId);
                        query.setFileType(fileType);
                        query.setPType(pType);
                        query.setChannelType(-1);
                        query.setPageNumber(pageNum);
                        query.setPageSize(SYNC_PAGE_SIZE);
                        Pagination<DroneResultVO> page = xianingDroneApiClient.pageResults(query);
                        if (page.getRecords().isEmpty()) {
                            break;
                        }
                        for (DroneResultVO item : page.getRecords()) {
                            if (item.getId() != null) {
                                resultMap.put(item.getId(), item);
                            }
                        }
                        if (page.getRecords().size() < SYNC_PAGE_SIZE) {
                            break;
                        }
                        pageNum++;
                    }
                }
            }
        }
        return new ArrayList<>(resultMap.values());
    }

    /**
     * 航迹upsert与软删除。
     */
    private void upsertRoutes(List<DroneRouteVO> remoteRows, Integer tenant, LocalDateTime syncTime) {
        // 加载本地未删除航迹，构建 thirdRouteId -> 实体 映射。
        List<DroneRoute> activeRows = droneRouteMapper.selectList(Wrappers.<DroneRoute>lambdaQuery()
                                                                          .eq(DroneRoute::getTenant, tenant)
                                                                          .eq(DroneRoute::getDeleted, false));
        Map<Long, DroneRoute> existingMap = new LinkedHashMap<>();
        for (DroneRoute row : activeRows) {
            if (row.getThirdRouteId() != null) {
                existingMap.put(row.getThirdRouteId(), row);
            }
        }
        Set<Long> remoteIds = new LinkedHashSet<>();
        for (DroneRouteVO remote : remoteRows) {
            if (remote.getId() == null) {
                continue;
            }
            remoteIds.add(remote.getId());
            DroneRoute entity = existingMap.get(remote.getId());
            if (entity == null) {
                // 远端存在、本地不存在：插入新记录。
                entity = new DroneRoute();
                entity.setId(IdWorker.getId());
                entity.setTenant(tenant);
                entity.setDeleted(false);
            }
            // 同步第三方快照字段。
            entity.setThirdRouteId(remote.getId());
            entity.setTrackName(remote.getTrackName());
            entity.setProjectName(remote.getProjectName());
            entity.setNestSn(remote.getNestSn());
            entity.setNestName(remote.getNestName());
            entity.setFileUrl(remote.getFileUrl());
            entity.setWebFileUrl(remote.getWebFileUrl());
            entity.setFileMd5(remote.getFileMd5());
            entity.setFileName(remote.getFileName());
            entity.setTrackStatus(remote.getStatus());
            entity.setTrackType(remote.getType());
            entity.setTrackModel(remote.getModel());
            entity.setCreateType(remote.getCreateType());
            entity.setAltitudeMode(remote.getAltitudeMode());
            entity.setTrackLength(remote.getTrackLength());
            entity.setWaypointCount(remote.getWaypointCount());
            entity.setEstimatedTime(remote.getEstimatedTime());
            entity.setPhotoTotal(remote.getPhotoTotal());
            entity.setHeight(remote.getHeight());
            entity.setSpeed(remote.getSpeed());
            entity.setPitchAngle(remote.getPitchAngle());
            entity.setThirdCreateTime(remote.getCreateTime());
            entity.setThirdUpdateTime(remote.getUpdateTime());
            entity.setRawJson(JsonUtils.serialize(remote));
            entity.setLastSyncTime(syncTime);
            if (existingMap.containsKey(remote.getId())) {
                // 远端存在、本地存在：更新。
                droneRouteMapper.updateById(entity);
            } else {
                droneRouteMapper.insert(entity);
            }
        }
        for (DroneRoute active : activeRows) {
            if (active.getThirdRouteId() != null && !remoteIds.contains(active.getThirdRouteId())) {
                // 本地存在、远端不存在：软删除。
                active.setDeleted(true);
                active.setLastSyncTime(syncTime);
                droneRouteMapper.updateById(active);
            }
        }
    }

    /**
     * 任务upsert与软删除。
     */
    private void upsertTasks(List<DroneTaskVO> remoteRows, Integer tenant, LocalDateTime syncTime) {
        // 构建本地任务映射，后续按thirdTaskId进行upsert。
        List<DroneTask> activeRows = droneTaskMapper.selectList(Wrappers.<DroneTask>lambdaQuery()
                                                                        .eq(DroneTask::getTenant, tenant)
                                                                        .eq(DroneTask::getDeleted, false));
        Map<Long, DroneTask> existingMap = new LinkedHashMap<>();
        for (DroneTask row : activeRows) {
            if (row.getThirdTaskId() != null) {
                existingMap.put(row.getThirdTaskId(), row);
            }
        }
        Set<Long> remoteIds = new LinkedHashSet<>();
        for (DroneTaskVO remote : remoteRows) {
            if (remote.getTaskId() == null) {
                continue;
            }
            remoteIds.add(remote.getTaskId());
            DroneTask entity = existingMap.get(remote.getTaskId());
            if (entity == null) {
                // 新任务插入。
                entity = new DroneTask();
                entity.setId(IdWorker.getId());
                entity.setTenant(tenant);
                entity.setDeleted(false);
            }
            // 刷新任务字段快照。
            entity.setThirdTaskId(remote.getTaskId());
            entity.setThirdRouteId(remote.getTrackId());
            entity.setTaskName(remote.getTaskName());
            entity.setTrackName(remote.getTrackName());
            entity.setNestSn(remote.getNestSn());
            entity.setNestName(remote.getNestName());
            entity.setFileUrl(remote.getFileUrl());
            entity.setTaskType(remote.getTaskType());
            entity.setTaskStatus(remote.getStatus());
            entity.setResultStatus(remote.getResultStatus());
            entity.setWeatherConflict(remote.getWeatherConflict());
            entity.setStartingPoint(remote.getStartingPoint());
            entity.setLastPoint(remote.getLastPoint());
            entity.setExecutionTime(toDateTime(remote.getExecutionTime()));
            entity.setStartTime(toDateTime(remote.getStartTime()));
            entity.setEndTime(toDateTime(remote.getEndTime()));
            entity.setRemark(remote.getRemark());
            entity.setRawJson(JsonUtils.serialize(remote));
            entity.setLastSyncTime(syncTime);
            if (existingMap.containsKey(remote.getTaskId())) {
                droneTaskMapper.updateById(entity);
            } else {
                droneTaskMapper.insert(entity);
            }
        }
        for (DroneTask active : activeRows) {
            if (active.getThirdTaskId() != null && !remoteIds.contains(active.getThirdTaskId())) {
                // 远端缺失则软删除本地记录。
                active.setDeleted(true);
                active.setLastSyncTime(syncTime);
                droneTaskMapper.updateById(active);
            }
        }
    }

    /**
     * 任务记录upsert与软删除。
     */
    private void upsertTaskRecords(List<DroneTaskRecordVO> remoteRows, Integer tenant, LocalDateTime syncTime) {
        // 任务记录以第三方调度ID作为主去重键。
        List<DroneTaskRecord> activeRows = droneTaskRecordMapper.selectList(Wrappers.<DroneTaskRecord>lambdaQuery()
                                                                                    .eq(DroneTaskRecord::getTenant, tenant)
                                                                                    .eq(DroneTaskRecord::getDeleted, false));
        Map<Long, DroneTaskRecord> existingMap = new LinkedHashMap<>();
        for (DroneTaskRecord row : activeRows) {
            if (row.getThirdScheduleId() != null) {
                existingMap.put(row.getThirdScheduleId(), row);
            }
        }
        Set<Long> remoteIds = new LinkedHashSet<>();
        for (DroneTaskRecordVO remote : remoteRows) {
            if (remote.getId() == null) {
                continue;
            }
            Long scheduleId = remote.getScheduledId() != null ? remote.getScheduledId() : remote.getId();
            remoteIds.add(scheduleId);
            DroneTaskRecord entity = existingMap.get(scheduleId);
            if (entity == null) {
                // 新调度记录插入。
                entity = new DroneTaskRecord();
                entity.setId(IdWorker.getId());
                entity.setTenant(tenant);
                entity.setDeleted(false);
            }
            // 更新任务记录字段快照。
            entity.setThirdRecordId(remote.getId());
            entity.setThirdTaskId(remote.getTaskId());
            entity.setThirdRouteId(remote.getTrackId());
            entity.setThirdScheduleId(scheduleId);
            entity.setSortieId(remote.getSortieId());
            entity.setTaskName(remote.getTaskName());
            entity.setTrackName(remote.getTrackName());
            entity.setNestSn(remote.getNestSn());
            entity.setNestName(remote.getNestName());
            entity.setTaskType(remote.getTaskType());
            entity.setScheduleStatus(remote.getStatus());
            entity.setResultStatus(remote.getResultStatus());
            entity.setStartingPoint(remote.getStartingPoint());
            entity.setLastPoint(remote.getLastPoint());
            entity.setSortieNum(remote.getSortieNum());
            entity.setSumPhotoTotal(remote.getSumPhotoTotal());
            entity.setFinishPhotoTotal(remote.getFinishPhotoTotal());
            entity.setExecutionTime(toDateTime(remote.getExecutionTime()));
            entity.setStartTime(toDateTime(remote.getStartTime()));
            entity.setEndTime(toDateTime(remote.getEndTime()));
            entity.setRemark(remote.getRemark());
            entity.setRawJson(JsonUtils.serialize(remote));
            entity.setLastSyncTime(syncTime);
            if (existingMap.containsKey(scheduleId)) {
                droneTaskRecordMapper.updateById(entity);
            } else {
                droneTaskRecordMapper.insert(entity);
            }
        }
        for (DroneTaskRecord active : activeRows) {
            if (active.getThirdScheduleId() != null && !remoteIds.contains(active.getThirdScheduleId())) {
                // 远端缺失时软删除。
                active.setDeleted(true);
                active.setLastSyncTime(syncTime);
                droneTaskRecordMapper.updateById(active);
            }
        }
    }

    /**
     * 成果upsert与软删除。
     */
    private void upsertResults(List<DroneResultVO> remoteRows, Integer tenant, LocalDateTime syncTime) {
        // 成果数据按第三方成果ID做upsert。
        List<DroneResult> activeRows = droneResultMapper.selectList(Wrappers.<DroneResult>lambdaQuery()
                                                                            .eq(DroneResult::getTenant, tenant)
                                                                            .eq(DroneResult::getDeleted, false));
        Map<Long, DroneResult> existingMap = new LinkedHashMap<>();
        for (DroneResult row : activeRows) {
            if (row.getThirdResultId() != null) {
                existingMap.put(row.getThirdResultId(), row);
            }
        }
        Set<Long> remoteIds = new LinkedHashSet<>();
        for (DroneResultVO remote : remoteRows) {
            if (remote.getId() == null) {
                continue;
            }
            remoteIds.add(remote.getId());
            DroneResult entity = existingMap.get(remote.getId());
            if (entity == null) {
                // 新成果插入。
                entity = new DroneResult();
                entity.setId(IdWorker.getId());
                entity.setTenant(tenant);
                entity.setDeleted(false);
            }
            // 刷新成果字段快照。
            entity.setThirdResultId(remote.getId());
            entity.setThirdTaskId(parseLong(remote.getTaskId()));
            entity.setThirdScheduleId(parseLong(remote.getScheduledId()));
            entity.setTrackName(remote.getTrackName());
            entity.setTaskName(remote.getTaskName());
            entity.setNestSn(remote.getNestSn());
            entity.setDroneSn(remote.getDroneSn());
            entity.setFileKey(remote.getFileKey());
            entity.setThumbKey(remote.getThumbKey());
            entity.setFileName(remote.getFileName());
            entity.setFileMd5(remote.getFileMd5());
            entity.setAnalysisStatus(remote.getAnalysisStatus());
            entity.setFileSize(remote.getFileSize());
            entity.setUploadType(remote.getUploadType());
            entity.setCoordValue(remote.getCoordValue());
            entity.setFileType(remote.getFileType());
            entity.setPodType(remote.getPodType());
            entity.setChannelType(remote.getChannelType());
            entity.setLongitude(toBigDecimal(remote.getLongitude()));
            entity.setLatitude(toBigDecimal(remote.getLatitude()));
            entity.setAltitude(toBigDecimal(remote.getAltitude()));
            entity.setJudgeStatus(remote.getJudgeStatus());
            entity.setResultType(remote.getType());
            entity.setPhotoType(remote.getPtype());
            entity.setThirdCreateTime(toDateTime(remote.getCreateTime()));
            entity.setRawJson(JsonUtils.serialize(remote));
            entity.setLastSyncTime(syncTime);
            if (existingMap.containsKey(remote.getId())) {
                droneResultMapper.updateById(entity);
            } else {
                droneResultMapper.insert(entity);
            }
        }
        for (DroneResult active : activeRows) {
            if (active.getThirdResultId() != null && !remoteIds.contains(active.getThirdResultId())) {
                // 本地冗余记录做软删除。
                active.setDeleted(true);
                active.setLastSyncTime(syncTime);
                droneResultMapper.updateById(active);
            }
        }
    }

    /**
     * 创建同步批次记录，初始状态为“进行中”。
     */
    private DroneSyncBatch createBatch(String domain, Integer tenant) {
        // 统一初始化批次统计字段。
        DroneSyncBatch batch = new DroneSyncBatch();
        batch.setId(IdWorker.getId());
        batch.setTenant(tenant);
        batch.setDeleted(false);
        batch.setSyncDomain(domain);
        batch.setSyncStatus(0);
        batch.setTotalCount(0);
        batch.setSuccessCount(0);
        batch.setFailCount(0);
        batch.setBatchStartTime(LocalDateTime.now());
        droneSyncBatchMapper.insert(batch);
        return batch;
    }

    /**
     * 结束同步批次，写入完成时间。
     */
    private void finishBatch(DroneSyncBatch batch) {
        // 标记批次完成时间并回写。
        batch.setBatchFinishTime(LocalDateTime.now());
        droneSyncBatchMapper.updateById(batch);
    }

    /**
     * 用异常任务信息补充常规任务返回中缺失或过期的状态字段。
     */
    private void mergeTask(DroneTaskVO target, DroneTaskVO source) {
        // 仅覆盖非空字段，避免把已有值覆盖为null。
        if (source.getWeatherConflict() != null) {
            target.setWeatherConflict(source.getWeatherConflict());
        }
        if (source.getResultStatus() != null) {
            target.setResultStatus(source.getResultStatus());
        }
        if (source.getNestSn() != null) {
            target.setNestSn(source.getNestSn());
        }
        if (source.getNestName() != null) {
            target.setNestName(source.getNestName());
        }
        if (source.getStartTime() != null) {
            target.setStartTime(source.getStartTime());
        }
        if (source.getEndTime() != null) {
            target.setEndTime(source.getEndTime());
        }
    }

    /**
     * 实体转航迹VO。
     */
    private DroneRouteVO toRouteVO(DroneRoute route) {
        // 数据库实体字段映射到SDK返回VO。
        DroneRouteVO vo = new DroneRouteVO();
        vo.setId(route.getThirdRouteId());
        vo.setFileUrl(route.getFileUrl());
        vo.setWebFileUrl(route.getWebFileUrl());
        vo.setFileMd5(route.getFileMd5());
        vo.setFileName(route.getFileName());
        vo.setTrackName(route.getTrackName());
        vo.setProjectName(route.getProjectName());
        vo.setStatus(route.getTrackStatus());
        vo.setTrackLength(route.getTrackLength());
        vo.setWaypointCount(route.getWaypointCount());
        vo.setEstimatedTime(route.getEstimatedTime());
        vo.setType(route.getTrackType());
        vo.setModel(route.getTrackModel());
        vo.setCreateType(route.getCreateType());
        vo.setPhotoTotal(route.getPhotoTotal());
        vo.setAltitudeMode(route.getAltitudeMode());
        vo.setNestSn(route.getNestSn());
        vo.setNestName(route.getNestName());
        vo.setHeight(route.getHeight());
        vo.setSpeed(route.getSpeed());
        vo.setPitchAngle(route.getPitchAngle());
        vo.setCreateTime(route.getThirdCreateTime());
        vo.setUpdateTime(route.getThirdUpdateTime());
        return vo;
    }

    /**
     * 实体转任务VO。
     */
    private DroneTaskVO toTaskVO(DroneTask task) {
        // 时间字段统一转为epoch毫秒。
        DroneTaskVO vo = new DroneTaskVO();
        vo.setId(task.getId());
        vo.setTaskId(task.getThirdTaskId());
        vo.setTaskName(task.getTaskName());
        vo.setTaskType(task.getTaskType());
        vo.setStatus(task.getTaskStatus());
        vo.setRemark(task.getRemark());
        vo.setTrackId(task.getThirdRouteId());
        vo.setTrackName(task.getTrackName());
        vo.setFileUrl(task.getFileUrl());
        vo.setNestSn(task.getNestSn());
        vo.setNestName(task.getNestName());
        vo.setStartTime(toEpochMilli(task.getStartTime()));
        vo.setEndTime(toEpochMilli(task.getEndTime()));
        vo.setResultStatus(task.getResultStatus());
        vo.setWeatherConflict(task.getWeatherConflict());
        vo.setStartingPoint(task.getStartingPoint());
        vo.setLastPoint(task.getLastPoint());
        vo.setExecutionTime(toEpochMilli(task.getExecutionTime()));
        return vo;
    }

    /**
     * 实体转任务记录VO。
     */
    private DroneTaskRecordVO toTaskRecordVO(DroneTaskRecord row) {
        // 输出任务记录给上层调用方。
        DroneTaskRecordVO vo = new DroneTaskRecordVO();
        vo.setId(row.getThirdRecordId());
        vo.setTaskId(row.getThirdTaskId());
        vo.setTrackId(row.getThirdRouteId());
        vo.setScheduledId(row.getThirdScheduleId());
        vo.setSortieId(row.getSortieId());
        vo.setTaskName(row.getTaskName());
        vo.setTrackName(row.getTrackName());
        vo.setNestSn(row.getNestSn());
        vo.setNestName(row.getNestName());
        vo.setTaskType(row.getTaskType());
        vo.setStatus(row.getScheduleStatus());
        vo.setResultStatus(row.getResultStatus());
        vo.setStartingPoint(row.getStartingPoint());
        vo.setLastPoint(row.getLastPoint());
        vo.setSortieNum(row.getSortieNum());
        vo.setSumPhotoTotal(row.getSumPhotoTotal());
        vo.setFinishPhotoTotal(row.getFinishPhotoTotal());
        vo.setExecutionTime(toEpochMilli(row.getExecutionTime()));
        vo.setStartTime(toEpochMilli(row.getStartTime()));
        vo.setEndTime(toEpochMilli(row.getEndTime()));
        vo.setRemark(row.getRemark());
        return vo;
    }

    /**
     * 实体转成果VO。
     */
    private DroneResultVO toResultVO(DroneResult row) {
        // 成果实体转输出VO，ID字段按接口约定转字符串。
        DroneResultVO vo = new DroneResultVO();
        vo.setId(row.getThirdResultId());
        vo.setTrackName(row.getTrackName());
        vo.setTaskName(row.getTaskName());
        vo.setNestSn(row.getNestSn());
        vo.setDroneSn(row.getDroneSn());
        vo.setTaskId(row.getThirdTaskId() == null ? null : String.valueOf(row.getThirdTaskId()));
        vo.setScheduledId(row.getThirdScheduleId() == null ? null : String.valueOf(row.getThirdScheduleId()));
        vo.setFileKey(row.getFileKey());
        vo.setThumbKey(row.getThumbKey());
        vo.setFileName(row.getFileName());
        vo.setFileMd5(row.getFileMd5());
        vo.setAnalysisStatus(row.getAnalysisStatus());
        vo.setFileSize(row.getFileSize());
        vo.setUploadType(row.getUploadType());
        vo.setCoordValue(row.getCoordValue());
        vo.setFileType(row.getFileType());
        vo.setPodType(row.getPodType());
        vo.setChannelType(row.getChannelType());
        vo.setLongitude(toDouble(row.getLongitude()));
        vo.setLatitude(toDouble(row.getLatitude()));
        vo.setAltitude(toDouble(row.getAltitude()));
        vo.setJudgeStatus(row.getJudgeStatus());
        vo.setType(row.getResultType());
        vo.setPtype(row.getPhotoType());
        vo.setCreateTime(toEpochMilli(row.getThirdCreateTime()));
        return vo;
    }

    /**
     * 读取当前会话租户ID。
     */
    private Integer currentTenant() {
        // 请求上下文中维护当前租户信息。
        return SessionContextHolder.getRequester().getTenant();
    }

    /**
     * epoch毫秒时间戳转本地日期时间。
     */
    private LocalDateTime toDateTime(Long millis) {
        if (millis == null) {
            return null;
        }
        // 以系统时区解释毫秒时间戳。
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    /**
     * 本地日期时间转epoch毫秒时间戳。
     */
    private Long toEpochMilli(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        // 本地时间按系统时区回转为毫秒时间戳。
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 字符串安全转Long。
     */
    private Long parseLong(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        // 仅在非空字符串时转换，避免NumberFormatException噪声。
        return Long.valueOf(value);
    }

    /**
     * Double转BigDecimal。
     */
    private BigDecimal toBigDecimal(Double value) {
        if (value == null) {
            return null;
        }
        // 使用valueOf避免浮点字符串化误差。
        return BigDecimal.valueOf(value);
    }

    /**
     * BigDecimal转Double。
     */
    private Double toDouble(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.doubleValue();
    }

    /**
     * 限制错误信息长度，避免超长文本写库。
     */
    private String limitError(Exception e) {
        String message = e.getMessage();
        if (!StringUtils.hasText(message)) {
            return e.getClass().getSimpleName();
        }
        // 错误信息过长时截断，避免字段超长。
        return message.length() > 1000 ? message.substring(0, 1000) : message;
    }
}
