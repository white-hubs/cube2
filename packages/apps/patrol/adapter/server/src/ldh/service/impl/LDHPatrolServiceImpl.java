package com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.service.impl;

import com.guhecloud.cube.srv.gen.adapter.patrol.server.enumeration.PatrolResultEnum;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl.AlarmResultSetHandler;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl.DeviceResultSetHandler;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl.LineResultSetHandler;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl.PlanResultSetHandler;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl.PointResultSetHandler;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl.RecordResultSetHandler;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.jdbchandler.impl.RouteResultSetHandler;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.model.DBConfig;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.service.PatrolService;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.utils.JdbcUtil;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.guhecloud.subsystem.requester.sdk.param.patrol.PatrolParam;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolAlarmVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolDeviceVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolLineVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolPlanVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolPointVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolRouteVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LDHPatrolServiceImpl implements PatrolService {
    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public List<PatrolPlanVO> getPatrolPlan(PatrolParam param) {
        log.info("兰德华-巡更计划查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        DBConfig dbConfig = this.getLDHDBInfo(basicConfig);
        String sql = "select * from GT_PLAN where create_time >= ? and create_time <= ?";
        var now = LocalDateTime.now();
        try {
            if (param.getStartTime() == null || param.getEndTime() == null) {
                param.setStartTime(DateUtils.offsetMonth(now, -120));
                param.setEndTime(DateUtils.endOfDay(now));
            }
            List<PatrolPlanVO> planList = JdbcUtil.executeQuery(dbConfig, sql, new Object[]{
                    param.getStartTime(), param.getEndTime()
            }, new PlanResultSetHandler());
            return planList;
        } catch (Exception e) {
            log.error("兰德华-巡更计划查询异常：", e);
            throw new ServiceException("兰德华-巡更计划查询异常", e);
        }
    }

    public List<PatrolPlanVO> getPatrolPlanEvaluation(PatrolParam param) {
        //与项目经理讨论后 暂时不查对面的巡检计划，巡检计划改为查询考核表
        log.info("兰德华-巡检考核表查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        DBConfig dbConfig = this.getLDHDBInfo(basicConfig);
        String sql = "select a.*,b.plan_name,b.line_id from GT_PLAN_EVALUATION a left join GT_PLAN b on a.plan_id = b.id where a.create_time >= ? and a.create_time <= ?";
        var now = LocalDateTime.now();
        try {
            if (param.getStartTime() == null || param.getEndTime() == null) {
                param.setStartTime(DateUtils.offsetMonth(now, -120));
                param.setEndTime(DateUtils.endOfDay(now));
            }
            List<PatrolPlanVO> planList = JdbcUtil.executeQuery(dbConfig, sql, new Object[]{
                    param.getStartTime(), param.getEndTime()
            }, new PlanResultSetHandler());
            return planList;
        } catch (Exception e) {
            log.error("兰德华-巡检考核表查询异常：", e);
            throw new ServiceException("兰德华-巡检考核表查询异常", e);
        }
    }

    @Override
    public List<PatrolLineVO> getPatrolLine(PatrolParam param) {
        log.info("兰德华-巡更线路查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        DBConfig dbConfig = this.getLDHDBInfo(basicConfig);
        String sql = "select * from GT_LINE";
        try {
            List<PatrolLineVO> list = JdbcUtil.executeQuery(dbConfig, sql, null, new LineResultSetHandler());
            return list;
        } catch (Exception e) {
            log.error("兰德华-巡更线路查询异常：", e);
            throw new ServiceException("兰德华-巡更线路查询异常", e);
        }
    }

    @Override
    public List<PatrolPointVO> getPatrolPoint(PatrolParam param) {
        log.info("兰德华-巡更点查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        DBConfig dbConfig = this.getLDHDBInfo(basicConfig);
        String sql = "select * from GT_CHECKPOINT";
        try {
            List<PatrolPointVO> list = JdbcUtil.executeQuery(dbConfig, sql, null, new PointResultSetHandler());
            return list;
        } catch (Exception e) {
            log.error("兰德华-巡更点查询异常：", e);
            throw new ServiceException("兰德华-巡更点查询异常", e);
        }
    }

    @Override
    public List<PatrolRouteVO> getPatrolRoute(PatrolParam param) {
        log.info("兰德华-巡更路线（线路和巡检点的关联）查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        DBConfig dbConfig = this.getLDHDBInfo(basicConfig);
        String sql = "select * from GT_LINE_EXTD";
        try {
            List<PatrolRouteVO> list = JdbcUtil.executeQuery(dbConfig, sql, null, new RouteResultSetHandler());
            return extendRoute(list);
        } catch (Exception e) {
            log.error("兰德华-巡更路线（线路和巡检点的关联）查询异常：", e);
            throw new ServiceException("兰德华-巡更路线（线路和巡检点的关联）查询异常", e);
        }
    }

    private List<PatrolRouteVO> extendRoute(List<PatrolRouteVO> list) {
        if (!CollectionUtils.isEmpty(list)) {
            List<PatrolLineVO> patrolLine = this.getPatrolLine(null);
            Map<String, String> lineMap = patrolLine.stream().collect(Collectors.toMap(PatrolLineVO::getId, l -> l.getName()));
            List<PatrolPointVO> patrolPoint = this.getPatrolPoint(null);
            Map<String, String> pointMap = patrolPoint.stream().collect(Collectors.toMap(PatrolPointVO::getId, p -> p.getName()));
            list.stream().forEach(r -> {
                r.setLineName(lineMap.get(r.getId()));
                r.setPointName(pointMap.get(r.getId()));
            });
        }
        return list;
    }

    @Override
    public List<PatrolAlarmVO> getPatrolAlarm(PatrolParam param) {
        log.info("兰德华-巡更报警查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        DBConfig dbConfig = this.getLDHDBInfo(basicConfig);
        String sql = "select * from GT_ALARM where alarm_Time >= ? and alarm_time <= ?";
        var now = LocalDateTime.now();
        try {
            if (param.getStartTime() == null || param.getEndTime() == null) {
                param.setStartTime(DateUtils.offsetDay(now, -7));
                param.setEndTime(DateUtils.endOfDay(now));
            }
            List<PatrolAlarmVO> list = JdbcUtil.executeQuery(dbConfig, sql, new Object[]{
                    param.getStartTime(), param.getEndTime()
            }, new AlarmResultSetHandler());
            return list;
        } catch (Exception e) {
            log.error("兰德华-巡更报警查询异常：", e);
            throw new ServiceException("兰德华-巡更报警查询异常", e);
        }
    }

    @Override
    public List<PatrolDeviceVO> getPatrolDevice(PatrolParam param) {
        log.info("兰德华-巡更设备查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        DBConfig dbConfig = this.getLDHDBInfo(basicConfig);
        String sql = "select * from GT_DEVICE";
        try {
            List<PatrolDeviceVO> list = JdbcUtil.executeQuery(dbConfig, sql, null, new DeviceResultSetHandler());
            return list;
        } catch (Exception e) {
            log.error("兰德华-巡更设备查询异常：", e);
            throw new ServiceException("兰德华-巡更设备查询异常", e);
        }
    }

    @Override
    public List<PatrolRecordVO> getPatrolRecord(PatrolParam param) {
        log.info("兰德华-巡更记录查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        DBConfig dbConfig = this.getLDHDBInfo(basicConfig);
        String sql = "select * from GT_PLAN_EVALUATION where create_time >= ? and create_time <= ?";
        var now = LocalDateTime.now();
        try {
            if (param.getStartTime() == null || param.getEndTime() == null) {
                param.setStartTime(DateUtils.offsetDay(now, -7));
                param.setEndTime(DateUtils.endOfDay(now));
            }
            List<PatrolRecordVO> list = JdbcUtil.executeQuery(dbConfig, sql, new Object[]{
                    param.getStartTime(), param.getEndTime()
            }, new RecordResultSetHandler());
            return extendRecord(list);
        } catch (Exception e) {
            log.error("兰德华-巡更记录查询异常：", e);
            throw new ServiceException("兰德华-巡更记录查询异常", e);
        }
    }

    private List<PatrolRecordVO> extendRecord(List<PatrolRecordVO> list) {
        if (!CollectionUtils.isEmpty(list)) {
            //查询线路
            List<PatrolLineVO> patrolLine = this.getPatrolLine(null);
            Map<String, String> lineMap = patrolLine.stream().collect(Collectors.toMap(PatrolLineVO::getName, l -> l.getId()));
            List<PatrolPointVO> patrolPoint = this.getPatrolPoint(null);
            Map<String, String> pointMap = patrolPoint.stream().collect(Collectors.toMap(PatrolPointVO::getName, p -> p.getId()));
            list.forEach(r -> {
                r.setLineId(lineMap.get(r.getLineName()));
                r.setPointId(pointMap.get(r.getPointName()));
                r.setStatus(PatrolResultEnum.NORMAL.name());//todo 这里根据实际结果处理巡检结果的状态
            });
        }
        return list;
    }

    /**
     * 获取基础配置
     *
     * @return
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key = SubsystemTypeConstants.PATROL_SERVER + ":" + "ldh" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    /**
     * 获取兰德华数据库配置
     *
     * @param config
     *
     * @return
     */
    private DBConfig getLDHDBInfo(SubsystemAdapterConfig config) {
        return JsonUtils.deserialize(config.getManufacturerJson(), DBConfig.class);
    }
}
