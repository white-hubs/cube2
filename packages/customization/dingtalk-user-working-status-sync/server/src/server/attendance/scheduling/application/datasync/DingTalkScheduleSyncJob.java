package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.application.datasync;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.DingTalkApiService;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.config.DingTalkConfig;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.dto.response.DingTalkScheduleListByUsersResponse;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.entity.DingTalkSchedule;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.enumeration.CheckTypeEnum;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.valueobj.DingTalkUserRef;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.DingTalkScheduleRepository;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.DingTalkUserRefRepository;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DingTalkScheduleSyncJob {

    private final DingTalkApiService dingTalkApiService;

    private final UserClient userClient;

    private final DingTalkScheduleRepository dingTalkScheduleRepository;

    private final DingTalkUserRefRepository dingTalkUserRefRepository;

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            // 核心线程数
            4,
            //maximumPoolSize: 池中允许的最大的线程数
            20,
            //keepAliveTime: 当线程数大于核心线程数的时候，线程在最大多长时间没有接到新任务就会终止释放， 最终线程池维持在 corePoolSize 大小
            50,
            //时间单位
            TimeUnit.SECONDS,
            //workQueue: 阻塞队列，用来存储等待执行的任务，如果当前对线程的需求超过了 corePoolSize 大小，就会放在这里等待空闲线程执行。
            new LinkedBlockingQueue<>(101),
            //threadFactory: 创建线程的工厂，比如指定线程名等
            new ThreadFactoryBuilder().setNameFormat("tenant-ding-talk-sync-pool-%d").build(),
            //拒绝策略，如果线程满了，线程池就会拒绝,被拒绝的任务的处理程序，该处理程序以静默方式丢弃
            new ThreadPoolExecutor.DiscardPolicy()
    );

    // 最大重试次数
    private static final int MAX_RETRY_TIMES = 3;
    // 初始重试延迟(毫秒)
    private static final long INITIAL_RETRY_DELAY = 5000;
    // 重试延迟倍数
    private static final double RETRY_DELAY_MULTIPLIER = 2.0;

    public DingTalkScheduleSyncJob(DingTalkApiService dingTalkApiService, UserClient userClient,
                                   DingTalkScheduleRepository dingTalkScheduleRepository,
                                   DingTalkUserRefRepository dingTalkUserRefRepository
                                  ) {
        this.dingTalkApiService = dingTalkApiService;
        this.userClient = userClient;
        this.dingTalkScheduleRepository = dingTalkScheduleRepository;
        this.dingTalkUserRefRepository = dingTalkUserRefRepository;
    }

    /**
     * 每天晚上11点同步第二天的排班信息
     */
    @JobAnnotation(name = "dingTalkScheduleSync", cron = "0 50 23 * * ?", desc = "钉钉排班信息同步任务")
    public void syncAllTenantsNextDaySchedule(String param) {
        log.info("开始执行多租户钉钉排班同步任务...");
        // 获取所有租户的配置列表
        var allDingTalkConfig = DingTalkConfig.getAllDingTalkConfig();
        // 为每个租户创建同步任务
        var futures = allDingTalkConfig.stream()
                .map(config -> CompletableFuture.runAsync(() -> {
                    try {
                        syncNextDaySchedule(config);
                    } finally {
                        SessionContextHolder.clearSessionContext();
                    }
                }, executor))
                .toList();
        // 等待所有租户同步完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .exceptionally(ex -> {
                    log.error("多租户排班同步任务执行异常", ex);
                    return null;
                })
                .join();
        log.info("多租户钉钉排班同步任务执行完成，共处理{}个租户", allDingTalkConfig.size());
    }

    public void syncNextDaySchedule(DingTalkConfig dingTalkConfig) {
        SessionContextHolder.requestTenant(dingTalkConfig.getTenant());
        log.info("开始执行钉钉排班同步任务...");
        int retryCount = 0;
        boolean success = false;
        long retryDelay = INITIAL_RETRY_DELAY;
        while (retryCount < MAX_RETRY_TIMES && !success) {
            try {
                if (retryCount > 0) {
                    log.warn("准备第{}次重试，等待{}毫秒后执行...", retryCount, retryDelay);
                    Thread.sleep(retryDelay);
                    retryDelay = (long) (retryDelay * RETRY_DELAY_MULTIPLIER);
                }
                //获取用户信息
                var dingTalkUsers = getDingTalkUsers();
                if (CollectionUtils.isEmpty(dingTalkUsers)) {
                    log.info("获取钉钉的用户信息为空");
                    break;
                }
                // 获取当前日期和明天日期
                var now = LocalDateTime.now();
                var tomorrow = now.plusDays(1);
                // 设置查询时间为明天的00:00:00到23:59:59
                var beginTime = LocalDateTime.of(tomorrow.toLocalDate(), LocalTime.MIN);
                var endTime = LocalDateTime.of(tomorrow.toLocalDate(), LocalTime.MAX);
                log.info("准备同步排班数据，时间范围: {} 至 {}", DateUtils.format(beginTime), DateUtils.format(endTime));
                processSchedule(dingTalkUsers, beginTime, endTime);
                success = true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("排班同步任务被中断", e);
                break;
            } catch (Exception e) {
                retryCount++;
                log.error("钉钉排班同步任务第{}次执行失败", retryCount, e);
            }
        }
        if (!success) {
            log.error("钉钉排班同步任务重试{}次后仍然失败", MAX_RETRY_TIMES);
        }
        SessionContextHolder.clearSessionContext();
    }

    public void processSchedule(List<DingTalkUserRef> dingTalkUsers,
                                LocalDateTime beginTime,
                                LocalDateTime endTime
                               ) {
        //获取排班信息
        var scheduleList = getScheduleListByUsersInBatches(dingTalkUsers.stream().map(DingTalkUserRef::getDingTalkUserId)
                                                                   .collect(Collectors.toSet()), beginTime, endTime);
        // 处理排班数据
        processScheduleData(scheduleList);
        log.info("钉钉排班同步任务执行成功，共同步{}条排班记录", scheduleList.size());
    }

    public List<DingTalkUserRef> getDingTalkUsers() {
        var userList = userClient.list(null);
        if (CollectionUtils.isEmpty(userList)) {
            return CollectionUtils.emptyList();
        }
        var userIds = userList.stream().map(UserDTO::getId).collect(Collectors.toSet());
        var dingTalkUserRefs = dingTalkUserRefRepository.getListBySystemUserIds(userIds);
        var hasDingTalkUserIds = dingTalkUserRefs.stream().map(DingTalkUserRef::getSystemUserId).collect(Collectors.toSet());
        var notIncludeUsers = userList.stream().filter(user -> !hasDingTalkUserIds.contains(user.getId())).toList();
        //查询不包含用户在钉钉里面的用户id
        // 4. 批量查询并创建新的关联
        if (!CollectionUtils.isEmpty(notIncludeUsers)) {
            var newAssociations = createAssociationsForUnlinkedUsers(notIncludeUsers);
            dingTalkUserRefs.addAll(newAssociations);
        }
        return dingTalkUserRefs;
    }

    /**
     * 为未关联的用户创建钉钉关联
     */
    private List<DingTalkUserRef> createAssociationsForUnlinkedUsers(List<UserDTO> unassociatedUsers) {
        // 按手机号分组，避免重复查询
        var usersByMobile = unassociatedUsers.stream()
                .filter(user -> StringUtils.isNotBlank(user.getMobile()))
                .collect(Collectors.toMap(
                        UserDTO::getMobile,
                        Function.identity(),
                        (existing, replacement) -> existing)); // 如果有重复手机号，保留第一个

        if (usersByMobile.isEmpty()) {
            return CollectionUtils.emptyList();
        }
        // 批量查询钉钉用户ID
        var dingTalkUserIds = batchGetDingTalkUserIds(usersByMobile.keySet());
        // 创建新的关联记录
        var newAssociations = new ArrayList<DingTalkUserRef>();
        dingTalkUserIds.forEach((mobile, dingTalkUserId) -> {
            var user = usersByMobile.get(mobile);
            if (user != null) {
                var ref = new DingTalkUserRef();
                ref.setSystemUserId(user.getId());
                ref.setDingTalkUserId(dingTalkUserId);
                newAssociations.add(ref);
            }
        });
        // 批量保存新关联
        if (!newAssociations.isEmpty()) {
            dingTalkUserRefRepository.saveBatch(newAssociations);
        }
        return newAssociations;
    }

    /**
     * 批量通过手机号查询钉钉用户ID
     */
    private Map<String, String> batchGetDingTalkUserIds(Set<String> mobiles) {
        var result = new HashMap<String, String>();
        mobiles.forEach(mobile -> {
            try {
                var response = dingTalkApiService.getUserIdByMobile(mobile);
                if (response != null && StringUtils.isNotBlank(response.getUserid())) {
                    result.put(mobile, response.getUserid());
                }
            } catch (Exception e) {
                log.error("通过手机号{}查询钉钉用户ID失败", mobile, e);
            }
        });
        return result;
    }

    /**
     * 分批查询排班信息(处理50个用户ID的限制)
     *
     * @param userIds   用户ID集合
     * @param beginTime 开始时间
     * @param endTime   结束时间
     *
     * @return 排班信息列表
     */
    public List<DingTalkScheduleListByUsersResponse> getScheduleListByUsersInBatches(Set<String> userIds,
                                                                                     LocalDateTime beginTime,
                                                                                     LocalDateTime endTime
                                                                                    ) {
        if (CollectionUtils.isEmpty(userIds)) {
            return CollectionUtils.emptyList();
        }
        var userIdList = new ArrayList<>(userIds);
        // 分批查询
        var allResults = new ArrayList<DingTalkScheduleListByUsersResponse>();
        // 钉钉API每次最多查询50个用户
        for (var users : ListUtils.partition(userIdList, 50)) {
            try {
                var batchUserSet = new HashSet<>(users);
                var batchResults = dingTalkApiService.getListByUsers(batchUserSet, beginTime, endTime);
                allResults.addAll(batchResults);
                // 避免触发钉钉API限流
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("排班查询被中断", e);
                break;
            } catch (Exception e) {
                log.error("排班批次查询失败: 用户id集合:{}", users, e);
            }
        }
        return allResults;
    }

    /**
     * 处理排班数据
     */
    private void processScheduleData(List<DingTalkScheduleListByUsersResponse> scheduleList) {
        var dingTalkSchedules = scheduleList.stream().map(response ->
                                                                  DingTalkSchedule.builder()
                                                                          .checkType(StringUtils.isBlank(response.getCheckType()) ? null
                                                                                                                                  : CheckTypeEnum.valueOf(response.getCheckType()))
                                                                          .planCheckTime(response.getPlanCheckTime())
                                                                          .groupId(response.getGroupId())
                                                                          .userid(response.getUserid())
                                                                          .approveId(response.getApproveId())
                                                                          .workDate(response.getWorkDate())
                                                                          .dingTalkScheduleId(response.getId())
                                                                          .shiftVersion(response.getShiftVersion())
                                                                          .shiftId(response.getShiftId())
                                                                          .build()).toList();
        this.dingTalkScheduleRepository.saveBatch(dingTalkSchedules);
    }
}
