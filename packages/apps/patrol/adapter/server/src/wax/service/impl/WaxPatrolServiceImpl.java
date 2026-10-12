package com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.enumeration.PatrolResultEnum;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.model.ThirdPlatformConfig;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.service.PatrolService;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model.WaxAlarm;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model.WaxDevice;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model.WaxLine;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model.WaxPage;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model.WaxPlan;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model.WaxPoint;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model.WaxRecord;
import com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model.WaxResponse;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.SecureUtils;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WaxPatrolServiceImpl implements PatrolService {
    private final SubsystemRedisUtils subsystemRedisUtils;
    private final RestTemplate restTemplate;
    private final String PLAN_URL = "/plans";//巡检计划
    private final String POINT_URL = "/checkpoints";
    private final String DEVICE_URL = "/devices";
    private final String RECORD_URL = "/planChecks";
    private final String ALARM_URL = "/dangers";
    private final String LINE_URL = "/lines";

    @Override
    public List<PatrolPlanVO> getPatrolPlan(PatrolParam param) {
        log.info("我爱巡-巡更计划查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        ThirdPlatformConfig config = this.getThirdPlatformConfig(basicConfig);
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
            String url = basicConfig.getServiceAddr() + PLAN_URL;

            UriComponentsBuilder uriBuilder = this.getUrlWithAuthParam(url, config)
                    .queryParam("pageNum", 1)
                    .queryParam("pageSize", 1000);

            ResponseEntity<WaxResponse> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, WaxResponse.class);
            if (response.getStatusCodeValue() == 200) {
                WaxResponse body = response.getBody();
                if (body.getCode() == 200) {
                    WaxPage<WaxPlan> page = JsonUtils.deserialize(body.getData(), new TypeReference<WaxPage<WaxPlan>>() {});
                    if (!CollectionUtils.isEmpty(page.getRows())) {
                        return page.getRows().stream().map(row -> {
                            PatrolPlanVO plan = new PatrolPlanVO();
                            plan.setId(row.getId().toString());
                            plan.setPlanName(row.getName());
                            //                            plan.setPlanType();//计划类型（有序无序）
                            plan.setPlanMode(String.valueOf(row.getPlanType()));//单次 循环 需要根据实际数据转换
                            plan.setStartTime(DateUtils.parse(row.getStartDate()));
                            plan.setEndTime(DateUtils.parse(row.getEndDate()));
                            plan.setLineId(row.getLineId().toString());
                            plan.setUserIds(row.getPatrolmanId().toString());
                            plan.setCreateTime(LocalDateTime.now());
                            return plan;
                        }).collect(Collectors.toList());
                    }
                } else {
                    log.error("我爱巡-巡更计划查询失败：", JsonUtils.serialize(body));
                }
            }
            return null;
        } catch (Exception e) {
            log.error("我爱巡-巡更计划查询异常：", e);
            throw new ServiceException("我爱巡-巡更计划查询异常", e);
        }
    }

    @Override
    public List<PatrolLineVO> getPatrolLine(PatrolParam param) {
        log.info("我爱巡-巡更线路查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        ThirdPlatformConfig config = this.getThirdPlatformConfig(basicConfig);
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
            String url = basicConfig.getServiceAddr() + LINE_URL;

            UriComponentsBuilder uriBuilder = this.getUrlWithAuthParam(url, config);

            ResponseEntity<WaxResponse> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, WaxResponse.class);
            if (response.getStatusCodeValue() == 200) {
                WaxResponse body = response.getBody();
                if (body.getCode() == 200) {
                    List<WaxLine> lineList = JsonUtils.deserialize(body.getData(), new TypeReference<List<WaxLine>>() {});
                    if (!CollectionUtils.isEmpty(lineList)) {
                        return lineList.stream().map(row -> {
                            PatrolLineVO line = new PatrolLineVO();
                            line.setId(row.getId().toString());
                            line.setName(row.getName());
                            return line;
                        }).collect(Collectors.toList());
                    }
                } else {
                    log.error("我爱巡-巡更计划查询失败：", JsonUtils.serialize(body));
                }
            }
            return null;
        } catch (Exception e) {
            log.error("我爱巡-巡更线路查询异常：", e);
            throw new ServiceException("我爱巡-巡更线路查询异常", e);
        }
    }

    @Override
    public List<PatrolRouteVO> getPatrolRoute(PatrolParam param) {
        log.info("我爱巡-巡更路线（线路和巡检点的关联）查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        ThirdPlatformConfig config = this.getThirdPlatformConfig(basicConfig);
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
            String url = basicConfig.getServiceAddr() + LINE_URL;

            UriComponentsBuilder uriBuilder = this.getUrlWithAuthParam(url, config);

            ResponseEntity<WaxResponse> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, WaxResponse.class);
            if (response.getStatusCodeValue() == 200) {
                WaxResponse body = response.getBody();
                if (body.getCode() == 200) {
                    List<WaxLine> lineList = JsonUtils.deserialize(body.getData(), new TypeReference<List<WaxLine>>() {});
                    if (!CollectionUtils.isEmpty(lineList)) {
                        return lineList.stream().map(row ->
                                                             row.getCheckpointIds().stream().map(cpId -> {
                                                                 PatrolRouteVO route = new PatrolRouteVO();
                                                                 route.setId(String.valueOf(IdWorker.getId()));
                                                                 route.setLineId(row.getId().toString());
                                                                 route.setLineName(row.getName());
                                                                 route.setPointId(cpId);
                                                                 return route;
                                                             }).collect(Collectors.toList())
                                                    ).flatMap(List::stream).collect(Collectors.toList());
                    }
                } else {
                    log.error("我爱巡-巡更计划查询失败：", JsonUtils.serialize(body));
                }
            }
            return null;
        } catch (Exception e) {
            log.error("我爱巡-巡更路线（线路和巡检点的关联）查询异常：", e);
            throw new ServiceException("我爱巡-巡更路线（线路和巡检点的关联）查询异常", e);
        }
    }

    @Override
    public List<PatrolPointVO> getPatrolPoint(PatrolParam param) {
        log.info("我爱巡-巡更点查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        ThirdPlatformConfig config = this.getThirdPlatformConfig(basicConfig);
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
            String url = basicConfig.getServiceAddr() + POINT_URL;

            UriComponentsBuilder uriBuilder = this.getUrlWithAuthParam(url, config)
                    .queryParam("pageNum", 1)
                    .queryParam("pageSize", 2000);

            ResponseEntity<WaxResponse> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, WaxResponse.class);
            if (response.getStatusCodeValue() == 200) {
                WaxResponse body = response.getBody();
                if (body.getCode() == 200) {
                    WaxPage<WaxPoint> page = JsonUtils.deserialize(body.getData(), new TypeReference<WaxPage<WaxPoint>>() {});
                    if (!CollectionUtils.isEmpty(page.getRows())) {
                        return page.getRows().stream().map(row -> {
                            PatrolPointVO point = new PatrolPointVO();
                            point.setId(row.getId().toString());
                            point.setName(row.getName());
                            point.setCode(row.getCard());
                            point.setRemark(row.getAreaName());
                            return point;
                        }).collect(Collectors.toList());
                    }
                }
            }
            return null;
        } catch (Exception e) {
            log.error("我爱巡-巡更点查询异常：", e);
            throw new ServiceException("我爱巡-巡更点查询异常", e);
        }
    }

    @Override
    public List<PatrolAlarmVO> getPatrolAlarm(PatrolParam param) {
        log.info("我爱巡-巡更报警查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        ThirdPlatformConfig config = this.getThirdPlatformConfig(basicConfig);
        var now = LocalDateTime.now();
        try {
            if (param.getStartTime() == null || param.getEndTime() == null) {
                param.setStartTime(DateUtils.offsetDay(now, -7));
                param.setEndTime(DateUtils.endOfDay(now));
            }
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
            String url = basicConfig.getServiceAddr() + ALARM_URL;

            UriComponentsBuilder uriBuilder = this.getUrlWithAuthParam(url, config)
                    .queryParam("pageNum", 1)
                    .queryParam("pageSize", 2000)
                    .queryParam("startTime", DateUtils.toTimestamp(param.getStartTime()))
                    .queryParam("endTime", DateUtils.toTimestamp(param.getEndTime()));

            ResponseEntity<WaxResponse> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, WaxResponse.class);
            if (response.getStatusCodeValue() == 200) {
                WaxResponse body = response.getBody();
                if (body.getCode() == 200) {
                    WaxPage<WaxAlarm> page = JsonUtils.deserialize(body.getData(), new TypeReference<WaxPage<WaxAlarm>>() {});
                    if (!CollectionUtils.isEmpty(page.getRows())) {
                        return page.getRows().stream().map(row -> {
                            PatrolAlarmVO alarm = new PatrolAlarmVO();
                            alarm.setId(row.getId());
                            alarm.setAlarmContent(row.getAreaName() + "发生异常");
                            alarm.setAlarmPoint(row.getAreaName());
                            alarm.setAlarmTime(DateUtils.format(DateUtils.parse(row.getCreateTime()), "yyyy-MM-dd hh:mm:ss"));
                            alarm.setAlarmType("异常");
                            alarm.setAlarmResult(row.getProc() ? "已处理" : "未处理");
                            return alarm;
                        }).collect(Collectors.toList());
                    }
                }
            }
            return null;
        } catch (Exception e) {
            log.error("我爱巡-巡更报警查询异常：", e);
            throw new ServiceException("我爱巡-巡更报警查询异常", e);
        }
    }

    @Override
    public List<PatrolDeviceVO> getPatrolDevice(PatrolParam param) {
        log.info("我爱巡-巡更设备查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        ThirdPlatformConfig config = this.getThirdPlatformConfig(basicConfig);
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
            String url = basicConfig.getServiceAddr() + DEVICE_URL;

            UriComponentsBuilder uriBuilder = this.getUrlWithAuthParam(url, config);

            ResponseEntity<WaxResponse> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, WaxResponse.class);
            if (response.getStatusCodeValue() == 200) {
                WaxResponse body = response.getBody();
                if (body.getCode() == 200) {
                    List<WaxDevice> deviceList = JsonUtils.deserialize(body.getData(), new TypeReference<List<WaxDevice>>() {});
                    if (!CollectionUtils.isEmpty(deviceList)) {
                        return deviceList.stream().map(row -> {
                            PatrolDeviceVO device = new PatrolDeviceVO();
                            device.setId(row.getId().toString());
                            device.setName(row.getName());
                            device.setCode(row.getCode());
                            device.setUser(row.getPatrolmanId());
                            return device;
                        }).collect(Collectors.toList());
                    }
                }
            }
            return null;
        } catch (Exception e) {
            log.error("我爱巡-巡更设备查询异常：", e);
            throw new ServiceException("我爱巡-巡更设备查询异常", e);
        }
    }

    @Override
    public List<PatrolRecordVO> getPatrolRecord(PatrolParam param) {
        log.info("我爱巡-巡更记录查询");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        ThirdPlatformConfig config = this.getThirdPlatformConfig(basicConfig);
        var now = LocalDateTime.now();
        try {
            if (param.getStartTime() == null || param.getEndTime() == null) {
                param.setStartTime(DateUtils.offsetDay(now, -7));
                param.setEndTime(DateUtils.endOfDay(now));
            }
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
            String url = basicConfig.getServiceAddr() + RECORD_URL;

            UriComponentsBuilder uriBuilder = this.getUrlWithAuthParam(url, config)
                    .queryParam("pageNum", 1)
                    .queryParam("pageSize", 2000)
                    .queryParam("startTime", DateUtils.toTimestamp(param.getStartTime()))
                    .queryParam("endTime", DateUtils.toTimestamp(param.getEndTime()));

            ResponseEntity<WaxResponse> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, WaxResponse.class);
            if (response.getStatusCodeValue() == 200) {
                WaxResponse body = response.getBody();
                if (body.getCode() == 200) {
                    WaxPage<WaxRecord> page = JsonUtils.deserialize(body.getData(), new TypeReference<WaxPage<WaxRecord>>() {});
                    if (!CollectionUtils.isEmpty(page.getRows())) {
                        return page.getRows().stream().map(row -> {
                            PatrolRecordVO record = new PatrolRecordVO();
                            record.setId(String.valueOf(IdWorker.getId()));
                            record.setStartTime(DateUtils.parse(row.getStartTime()));
                            record.setEndTime(DateUtils.parse(row.getEndTime()));
                            record.setLineId(row.getLineId());
                            record.setLineName(row.getLineName());
                            record.setPointId(row.getCheckpointId());
                            record.setPointName(row.getCheckpointName());
                            record.setLineOrder(row.getOrderNum());
                            record.setPlanId(row.getPlanId());
                            record.setUserId(row.getPatrolmanId());
                            record.setUserName(row.getPatrolmanName());
                            record.setNormal(true);//这个字段主要是标识 是否有报警；无报警则正常 有报警则异常
                            record.setStatus(row.getArriveType() >= 0 ? PatrolResultEnum.NORMAL.name() : PatrolResultEnum.EXP_MISS.name());
                            record.setPatrolTime(DateUtils.parse(row.getArriveTime()));
                            record.setCreateTime(record.getPatrolTime());
                            return record;
                        }).collect(Collectors.toList());
                    }
                }
            }
            return null;
        } catch (Exception e) {
            log.error("我爱巡-巡更记录查询异常：", e);
            throw new ServiceException("我爱巡-巡更记录查询异常", e);
        }
    }

    /**
     * 获取基础配置
     *
     * @return
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key = SubsystemTypeConstants.PATROL_SERVER + ":" + "wax" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    /**
     * 获取我爱巡平台配置
     *
     * @param config
     *
     * @return
     */
    private ThirdPlatformConfig getThirdPlatformConfig(SubsystemAdapterConfig config) {
        return JsonUtils.deserialize(config.getManufacturerJson(), ThirdPlatformConfig.class);
    }

    /**
     * 获取url以及拼接认证参数
     *
     * @param url
     * @param config
     *
     * @return
     */
    private UriComponentsBuilder getUrlWithAuthParam(String url, ThirdPlatformConfig config) {
        String appKey = config.getAppKey();
        String appSecret = config.getAppSecret();
        Long timestamp = System.currentTimeMillis();
        //Sign 签名：MD5(APP_KEY+APP_SECRET+timestamp)
        String sign = SecureUtils.md5(appKey + appSecret + timestamp).toLowerCase();
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("appKey", appKey)
                .queryParam("timestamp", timestamp)
                .queryParam("sign", sign);
    }
}
