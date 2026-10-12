package com.guhecloud.cube.srv.gen.adapter.access.control.server.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.enumeration.EventResultEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.DaHuaApi;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.config.DaHuaConfig;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.enums.DaHuaDoorEventEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.enums.DaHuaOpenResultEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo.DaHuaChannelVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo.DaHuaDoorEventInfoVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo.DaHuaPageVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo.DaHuaResponse;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessDevicesParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessPointsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorControlParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.service.DaHuaAccessService;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AccessPointVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AuthDoorStatusVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorControlResultVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorDevicesVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 大华门禁对接文档：
 * <a href="https://open-icc.dahuatech.com/#/home?url=%3Fnav%3Dwiki%2Fevo-accesscontrol%2Fevent.html&version=enterprisebase/5.0.15&blank=true">...</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DaHuaAccessServiceImpl implements DaHuaAccessService {

    /**
     * 智能物联综合管理平台iConnection Center V5.0.14 及以上版本
     * 门禁管理子系统 V1.1.4 及以上版本
     * 门禁记录分页
     */
    private static final String GET_DOOR_PAGE = "/evo-apigw/evo-accesscontrol/1.0.0/card/accessControl/swingCardRecord/bycondition/combined";
    /**
     * 智能物联综合管理平台iConnection Center V5.0.6 及以上版本
     * 门禁管理子系统 V1.0.0 及以上版本
     * 远程控制门禁开门
     */
    private static final String OPEN_DOOR = "/evo-apigw/evo-accesscontrol/1.0.0/card/accessControl/channelControl/openDoor";
    /**
     * 智能物联综合管理平台iConnection Center V5.0.6 及以上版本
     * 门禁管理子系统 V1.0.0 及以上版本
     * 远程控制门禁关门
     */
    private static final String CLOSE_DOOR = "/evo-apigw/evo-accesscontrol/1.0.0/card/accessControl/channelControl/closeDoor";
    /**
     * 智能物联综合管理平台iConnection Center V5.0.6 及以上版本
     * 门禁管理子系统 V1.0.0 及以上版本
     * 设置常开模式
     */
    private static final String STAY_OPEN = "/evo-apigw/evo-accesscontrol/1.0.0/card/accessControl/channelControl/stayOpen";
    /**
     * 智能物联综合管理平台iConnection Center V5.0.6 及以上版本
     * 门禁管理子系统 V1.0.0 及以上版本
     * 设置常闭模式
     */
    private static final String STAY_CLOSE = "/evo-apigw/evo-accesscontrol/1.0.0/card/accessControl/channelControl/stayClose";
    /**
     * 智能物联综合管理平台iConnection Center V5.0.6 及以上版本
     * 门禁管理子系统 V1.0.0 及以上版本
     * 远程配置门禁为普通模式
     */
    private static final String DOOR_NORMAL = "/evo-apigw/evo-accesscontrol/1.0.0/card/accessControl/channelControl/normal";

    /**
     * 获取门禁状态
     */
    private static final String DOOR_STATUS = "/evo-apigw/evo-accesscontrol/1.0.0/card/accessControl/channelControl/channels";

    private final SubsystemRedisUtils subsystemRedisUtils;
    private final DaHuaApi daHuaApi;

    @Override
    public Page<AccessPointVo> getDoorPoints(AccessPointsParam accessPointsParam) {
        return null;
    }

    @Override
    public Page<DoorDevicesVo> getDoorDevices(AccessDevicesParam accessDevicesParam) {
        return null;
    }

    @Override
    public Page<DoorEventsVo> getDoorEvents(DoorEventsParam doorEventsParam) throws Exception {
        Page<DoorEventsVo> page = new Page<>();
        log.info("大华门禁记录采集，参数为：{}", JsonUtils.serialize(doorEventsParam));
        //获取配置
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        //刷新配置
        DaHuaConfig daHuaConfig = this.getAccessConfig(subAdapterRefreshConfig);
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum", doorEventsParam.getPageNo());
        params.put("pageSize", doorEventsParam.getPageSize());
        //查询开始时间，格式：yyyy-MM-dd HH:mm:ss，不送默认从当前时间点减30天
        params.put("startSwingTime", doorEventsParam.getStartTime());
        //查询结束时间，格式：yyyy-MM-dd HH:mm:ss ，不送默认是当前时间点
        params.put("endSwingTime", doorEventsParam.getEndTime());
        log.info("大华门禁记录采集参数：{}", params);
        DaHuaResponse result = daHuaApi.daHuaAuthPostApi(daHuaConfig.getHost() + GET_DOOR_PAGE, new HashMap<>(), params, daHuaConfig);
        log.info("大华门禁记录采集，返回结果:{}", result);
        if (!Objects.isNull(result) && !Objects.isNull(result.getData())) {
            String jsonString = JSON.toJSONString(result.getData());
            DaHuaPageVO daHuaPageVO = JSON.parseObject(jsonString, DaHuaPageVO.class);
            page.setTotal(daHuaPageVO.getTotalPage());
            page.setRecords(daHuaDoorEventConvert(daHuaPageVO.getPageData()));
        }
        return page;
    }

    @Override
    public List<DoorControlResultVo> doorControl(DoorControlParam param) {
        List<DoorControlResultVo> resultVos = new ArrayList<>();
        log.info("大华门禁控制，参数为：{}", JsonUtils.serialize(param));
        //获取配置
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        //刷新配置
        DaHuaConfig daHuaConfig = this.getAccessConfig(subAdapterRefreshConfig);
        Map<String, Object> params = new HashMap<>();
        params.put("channelCodeList", param.getDoorIndexCodes());
        Integer controlType = param.getControlType();
        String url = daHuaConfig.getHost();
        switch (controlType) {
            case 1:
                //开启
                url = url + OPEN_DOOR;
                break;
            case 2:
                //关闭
                url = url + CLOSE_DOOR;
                break;
            case 0:
                //常开
                url = url + STAY_OPEN;
                break;
            case 3:
                //常闭
                url = url + STAY_CLOSE;
                break;
            case 5:
                //正常模式
                url = url + DOOR_NORMAL;
                break;
            default:
                break;
        }
        try {
            DaHuaResponse result = daHuaApi.daHuaAuthPostApi(url, new HashMap<>(), params, daHuaConfig);
            log.info("大华门禁控制，返回结果:{}", result);
            param.getDoorIndexCodes().forEach(item -> {
                DoorControlResultVo vo = new DoorControlResultVo();
                vo.setDoorIndexCode(item);
                vo.setControlResultDesc(result.getErrMsg());
                vo.setControlResultCode(result.getCode());
                resultVos.add(vo);
            });

        } catch (JsonProcessingException e) {
            log.error("大华门禁控制 序列化失败");
            throw new ServiceException("大华门禁控制 序列化失败:{}", e);
        }

        return resultVos;
    }

    @Override
    public List<AuthDoorStatusVo> getDoorStates(List<String> ids) {
        //获取配置
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        //刷新配置
        DaHuaConfig daHuaConfig = this.getAccessConfig(subAdapterRefreshConfig);
        Map<String, Object> params = new HashMap<>();
        params.put("channelCodes", ids);
        params.put("allowSmartLock", false);
        try {
            DaHuaResponse result = daHuaApi.daHuaAuthPostApi(daHuaConfig.getHost() + DOOR_STATUS, new HashMap<>(), params, daHuaConfig);
            log.info("大华门禁状态，返回结果:{}", result);
            if (!Objects.isNull(result) && !Objects.isNull(result.getData())) {
                String jsonString = JSON.toJSONString(result.getData());
                List<DaHuaChannelVO> authDoorStatusVos = JSON.parseObject(jsonString, new TypeReference<List<DaHuaChannelVO>>() {
                });

                return authDoorStatusVos.stream()
                        .map(t -> {
                            AuthDoorStatusVo vo = new AuthDoorStatusVo();
                            vo.setDoorIndexCode(t.getChannelCode());
                            vo.setDoorState(daHuaToGhy(t.getStatus(), t.getWorkMode()));
                            return vo;
                        }).collect(Collectors.toList());
            }
        } catch (JsonProcessingException e) {
            log.error("大华门禁状态 序列化失败");
            throw new ServiceException("大华门禁状态 序列化失败:{}", e);
        }
        return Collections.emptyList();
    }

    /**
     * 大华转古河云
     *
     * @param doorState
     *
     * @return
     */
    public Integer daHuaToGhy(Integer doorState, Integer workMode) {
        switch (workMode) {
            //常关
            case 1:
                return 3;
            //常开
            case 2:
                return 0;
            default:
                return doorState;
        }

    }

    /**
     * 获取门禁基础配置
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key = SubsystemTypeConstants.DOOR_SERVER + ":" + "daHua" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    /**
     * 获取门禁授权配置
     */
    private DaHuaConfig getAccessConfig(SubsystemAdapterConfig config) {
        DaHuaConfig daHuaConfig = JsonUtils.deserialize(config.getManufacturerJson(), DaHuaConfig.class);
        if (daHuaConfig != null) {
            daHuaConfig.setTenant(config.getTenant());
        }
        return daHuaConfig;
    }

    private List<DoorEventsVo> daHuaDoorEventConvert(List<DaHuaDoorEventInfoVO> dataList) {
        if (!CollectionUtils.isEmpty(dataList)) {
            return dataList.stream().map(r -> {
                DaHuaDoorEventEnum typeManage = DaHuaDoorEventEnum.getByCode(r.getOpenType());

                return DoorEventsVo.builder()
                        .eventId(Objects.isNull(r.getId()) ? "" : r.getId().toString())
                        .doorIndexCode(r.getChannelCode())
                        .doorName(r.getChannelName())
                        .eventType(typeManage == null ? "" : typeManage.getName())
                        .personId(Objects.isNull(r.getPersonId()) ? "" : r.getPersonId().toString())
                        .personName(r.getPersonName())
                        .inAndOutType(r.getEnterOrExit())
                        .eventName(typeManage == null ? "" : typeManage.getName())
                        .eventTime(r.getSwingTime())
                        .eventWay(typeManage == null ? "" : typeManage.getName())
                        .eventResult(DaHuaOpenResultEnum.SUCCESS.getCode().equals(r.getOpenResult()) ?
                                     EventResultEnum.DO_PASS.name() : EventResultEnum.DO_REFUSE.name())
                        .cardNo(r.getCardNumber())
                        .build();
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
