package com.guhecloud.cube.srv.gen.maintain.server.electron.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolAlarmMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolDeviceMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolLocationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolPlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolRouteMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolTaskLocationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolAlarm;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolDevice;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolPlan;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolRoute;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTaskLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.AddTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.DeleteStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.ExceptionTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PatrolResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PlanStatusEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.guhecloud.subsystem.requester.sdk.client.PatrolClient;
import com.guhecloud.subsystem.requester.sdk.param.patrol.PatrolParam;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolAlarmVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolDeviceVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolPlanVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolPointVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolRouteVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolDataIntegrateJob {
    private final PatrolClient patrolClient;
    private final PatrolDeviceMapper patrolDeviceMapper;
    private final PatrolLocationMapper patrolLocationMapper;
    private final PatrolRouteMapper patrolRouteMapper;
    private final PatrolPlanMapper patrolPlanMapper;
    private final PatrolAlarmMapper patrolAlarmMapper;
    private final PatrolTaskLocationMapper patrolTaskLocationMapper;
    private final SubsystemRedisUtils subsystemRedisUtils;

    /**
     * 巡更基础数据采集，
     * 包括：线路（line）、巡更点（point）、线路和点的关系(route)、设备（device）
     * tenant通过定时任务的param传递过来
     * 这些基础数据，本身数据量和增长量都不大，采用全量同步
     *
     * @param param
     *
     * @return
     */
    @JobAnnotation(name = "PatrolBasicDataCollect", cron = "0 0/10 * * * ?", desc = "巡更-三方巡更基础数据采集")
    public void patrolBasicDataCollect(String param) {
        SessionContextHolder.clearSessionContext();
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.PATROL_SERVER);
        subEndpointRefreshConfigs.forEach(config -> {
            SessionContextHolder.requestTenant(config.getTenant());
            this.syncThirdPatrolPoint();
            this.syncThirdPatrolDevice();
            this.syncThirdPatrolRoute();
            SessionContextHolder.clearSessionContext();
        });
    }

    /**
     * 巡更业务数据采集，
     * 包括：巡更计划、巡更记录、巡更报警
     * tenant通过定时任务的param传递过来
     * 这些业务数据随时间增长，同步的时候最好传入时间参数，不传时间的话，适配层的接口应该默认
     * xxljob中的param参数可以定义成这样的格式：tenant#startTime-endTime
     *
     * @param param
     *
     * @return
     */
    @JobAnnotation(name = "PatrolBizDataCollect", cron = "0 0/10 * * * ?", desc = "巡更-三方巡更业务数据采集")
    public void patrolBizDataCollect(String param) {
        SessionContextHolder.clearSessionContext();
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.PATROL_SERVER);
        subEndpointRefreshConfigs.forEach(config -> {
            SessionContextHolder.requestTenant(config.getTenant());
            //处理参数
            var requestParam = param.split("#");
            var tenant = Integer.valueOf(requestParam[0]);
            var patrolParam = new PatrolParam();
            if (requestParam.length > 1) {
                var queryTime = requestParam[1].split("-");
                patrolParam.setStartTime(DateUtils.beginOfDay(DateUtils.parse(queryTime[0])));
                patrolParam.setEndTime(DateUtils.endOfDay((DateUtils.parse(queryTime[1]))));
            }
            //设置tenant
            patrolParam.setTenant(tenant);
            SessionContextHolder.requestTenant(tenant);
            this.syncThirdPatrolPlan(patrolParam);
            this.syncThirdPatrolAlarm(patrolParam);
            this.syncThirdPatrolRecord(patrolParam);
            SessionContextHolder.requestTenant(null);
        });
    }

    /**
     * 同步第三方巡更记录
     *
     * @param patrolParam
     */
    private void syncThirdPatrolRecord(PatrolParam patrolParam) {
        try {
            //处理同步时间
            PatrolTaskLocation lastRecord = patrolTaskLocationMapper.selectOne(Wrappers.<PatrolTaskLocation>lambdaQuery()
                                                                                       .orderByDesc(PatrolTaskLocation::getPatrolDate)
                                                                                       .last("limit 0,1"));
            if (patrolParam.getStartTime() == null && patrolParam.getEndTime() == null) {
                patrolParam.setStartTime(lastRecord.getPatrolDate());
                patrolParam.setEndTime(LocalDateTime.now());
            }
            List<PatrolRecordVO> list = patrolClient.getPatrolRecord(patrolParam);
            if (CollectionUtils.isNotEmpty(list)) {
                //插入设备
                List<PatrolTaskLocation> collect = list.stream().map(r -> {
                    PatrolTaskLocation data = new PatrolTaskLocation();
                    data.setId(Long.valueOf(r.getId()));
                    data.setLocationId(Long.valueOf(r.getPointId()));
                    data.setOrderNum(r.getLineOrder());
                    data.setPlanId(Long.valueOf(r.getPlanId()));
                    data.setAddType(AddTypeEnum.PLAN.name());
                    //根据现场数据进行调试：取r.getStatus()值进行对比
                    String result =
                            r.getStatus().indexOf("NORMAL") >= 0 ? PatrolResultEnum.NORMAL.name() : PatrolResultEnum.ABNORMAL.name();
                    data.setResult(result);
                    //如果是正常，还要取r.getStatus()值进行对比 漏巡情况
                    String expCode = r.getStatus().indexOf("EXP_MISS") >= 0
                                     ? ExceptionTypeEnum.LEAK_DETECTION.name()
                                     : (r.getNormal() ? null : ExceptionTypeEnum.PATROL_ABNORMAL.name());
                    data.setExceptionType(expCode);
                    data.setPatrolOrder(r.getLineOrder());
                    //巡检记录表没有人员名称字段，建议增加userName字段
                    //                    data.setUserId(r.getUserId() != null ? Long.valueOf(r.getUserId()) : null);
                    data.setRemark(r.getUserName());
                    data.setPatrolDate(r.getPatrolTime());
                    data.setStartDate(r.getStartTime());
                    data.setEndDate(r.getEndTime());
                    data.setDeleted(false);
                    data.setTenant(patrolParam.getTenant());
                    EntityAuditor.populateAudit(data, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, r.getCreateTime());
                    return data;
                }).collect(Collectors.toList());
                patrolTaskLocationMapper.insertOrUpdateBatch(collect);
            }
        } catch (Exception e) {
            log.error("第三方电子巡更-巡更记录同步失败", e);
        }
    }

    /**
     * 同步第三方巡更报警
     *
     * @param patrolParam
     */
    private void syncThirdPatrolAlarm(PatrolParam patrolParam) {
        try {
            //处理查询时间 从上一次同步的最大报警时间开始同步
            PatrolAlarm lastAlarm = patrolAlarmMapper.selectOne(Wrappers.<PatrolAlarm>lambdaQuery()
                                                                        .orderByDesc(PatrolAlarm::getAlarmTime).last("limit 0,1"));
            if (patrolParam.getStartTime() == null && patrolParam.getEndTime() == null) {
                patrolParam.setStartTime(
                        (null == lastAlarm) ? DateUtils.offsetDay(LocalDateTime.now(), -7) : DateUtils.parse(lastAlarm.getAlarmTime()));
                patrolParam.setEndTime(LocalDateTime.now());
            }
            List<PatrolAlarmVO> list = patrolClient.getPatrolAlarm(patrolParam);
            if (CollectionUtils.isNotEmpty(list)) {
                //插入设备
                List<PatrolAlarm> collect = list.stream().map(a -> {
                    PatrolAlarm data = ObjectUtils.copying(a, PatrolAlarm.class);
                    data.setDeleted(false);
                    data.setTenant(patrolParam.getTenant());
                    EntityAuditor.populateAudit(data, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, LocalDateTime.now());
                    return data;
                }).collect(Collectors.toList());
                patrolAlarmMapper.insertOrUpdateBatch(collect);
            }
        } catch (Exception e) {
            log.error("第三方电子巡更-巡更报警同步失败", e);
        }
    }

    /**
     * 同步第三方巡更计划
     *
     * @param patrolParam
     */
    private void syncThirdPatrolPlan(PatrolParam patrolParam) {
        try {
            List<PatrolPlanVO> list = patrolClient.getPatrolPlan(patrolParam);
            if (CollectionUtils.isNotEmpty(list)) {
                //插入设备
                List<PatrolPlan> collect = list.stream().map(p -> {
                    PatrolPlan data = new PatrolPlan();
                    data.setId(Long.valueOf(p.getId()));
                    data.setPlanName(p.getPlanName());
                    data.setRouteId(StringUtils.isNotBlank(p.getLineId()) ? Long.valueOf(p.getLineId()) : null);
                    data.setStartData(p.getStartTime());
                    data.setEndData(p.getEndTime());
                    data.setStatus(PlanStatusEnum.ENABLE.getRemarks());
                    data.setDeleted(false);
                    data.setTenant(patrolParam.getTenant());
                    EntityAuditor.populateAudit(data, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, p.getCreateTime());
                    return data;
                }).collect(Collectors.toList());
                patrolPlanMapper.insertOrUpdateBatch(collect);
            }
        } catch (Exception e) {
            log.error("第三方电子巡更-巡更计划同步失败", e);
        }
    }

    /**
     * 同步第三方平台的巡更设备数据
     */
    private void syncThirdPatrolDevice() {
        try {
            List<PatrolDeviceVO> list = patrolClient.getPatrolDevice(new PatrolParam());
            if (CollectionUtils.isNotEmpty(list)) {
                //先删除现有的设备数据
                patrolDeviceMapper.delete(null);
                //插入设备
                list.forEach(d -> {
                    PatrolDevice data = ObjectUtils.copying(d, PatrolDevice.class);
                    data.setDeleted(false);
                    EntityAuditor.populateAudit(data, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, LocalDateTime.now());
                    patrolDeviceMapper.insert(data);
                });
            }
        } catch (Exception e) {
            log.error("第三方电子巡更-设备数据同步失败", e);
        }
    }

    /**
     * 同步第三方平台的巡更点
     */
    private void syncThirdPatrolPoint() {
        try {
            List<PatrolPointVO> list = patrolClient.getPatrolPoint(new PatrolParam());
            if (CollectionUtils.isNotEmpty(list)) {
                //先删除现有的巡更点
                patrolLocationMapper.delete(null);
                //插入巡更点
                list.stream().forEach(d -> {
                    PatrolLocation data = new PatrolLocation();
                    data.setId(Long.valueOf(d.getId()));
                    data.setPatrolLocationName(d.getName());
                    data.setPatrolLongitude(d.getLongitude());
                    data.setPatrolDimension(d.getLatitude());
                    data.setNfcCode(d.getCode());
                    data.setTenant(SessionContextHolder.getRequester().getTenant());
                    data.setDeleted(false);
                    data.setStatus(DeleteStatusEnum.NO_REMOVE.getRemarks());
                    EntityAuditor.populateAudit(data, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, LocalDateTime.now());
                    patrolLocationMapper.insert(data);
                });
            }
        } catch (Exception e) {
            log.error("第三方电子巡更-巡更点同步失败", e);
        }
    }

    /**
     * 同步第三方平台的巡更线路
     */
    private void syncThirdPatrolRoute() {
        try {
            List<PatrolRouteVO> list = patrolClient.getPatrolRoute(new PatrolParam());
            if (CollectionUtils.isNotEmpty(list)) {
                //先删除现有的路线
                patrolRouteMapper.delete(null);
                //插入巡更点
                list.forEach(d -> {
                    PatrolRoute data = new PatrolRoute();
                    data.setId(Long.valueOf(d.getId()));
                    data.setUuid(Long.valueOf(d.getLineId()));
                    data.setPatrolRouteName(d.getLineName());
                    data.setPatrolLocationId(Long.valueOf(d.getPointId()));
                    data.setRuleOrder(d.getOrder());
                    data.setTenant(SessionContextHolder.getRequester().getTenant());
                    data.setDeleted(false);
                    data.setStatus("NO_REMOVE");
                    EntityAuditor.populateAudit(data, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, LocalDateTime.now());
                    patrolRouteMapper.insert(data);
                });
            }
        } catch (Exception e) {
            log.error("第三方电子巡更-巡更线路同步失败", e);
        }
    }
}
