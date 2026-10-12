package com.guhecloud.cube.srv.gen.adapter.access.control.server.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.dao.TypeManageMapper;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.entity.TypeManage;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.enumeration.EventResultEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik.CardModel;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik.HikPage;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik.HikPage2;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik.HikResponse;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik.KSDJEventTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik.KSDJRecordModel;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik.OrgModel;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik.TKEventTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik.TKRecordModel;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik.VisitingRecordModel;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessDevicesParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessPointsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorControlParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.KSDJRecordParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.TKRecordParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.VisitingRecordParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.service.HikAccessControlService;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AccessPointVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AccessRecordVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AuthDoorStatusVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.CardVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.ClientHttpVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorControlResultVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorDevicesVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.OrgVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.VisitingRecordVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 海康门禁对接文档：
 * https://open.hikvision.com/docs/docId?productId=5c67f1e2f05948198c909700&version=%2Ff95e951cefc54578b523d1738f65f0a1&tagPath=API%E5%88%97%E8%A1%A8-%E4%B8%80%E5%8D%A1%E9%80%9A%E5%BA%94%E7%94%A8%E6%9C%8D%E5%8A%A1-%E9%97%A8%E7%A6%81%E7%AE%A1%E7%90%86
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("DoubleBraceInitialization")
public class HikAccessControlServiceImpl implements HikAccessControlService {
    //设置OpenAPI接口的上下文
    private final String ARTEMIS_PATH = "/artemis";

    private final String clientHttps = "https://";

    private final SubsystemRedisUtils subsystemRedisUtils;
    private final TypeManageMapper typeManageMapper;

    @Override
    public Page<AccessPointVo> getDoorPoints(AccessPointsParam accessPointsParam) {
        String contentType = "application/json";
        Map<String, String> path = this.getAuth("/api/resource/v2/door/search");
        //构建请求参数
        JSONObject jsonBody = new JSONObject();
        ArrayList<Object> expressions = new ArrayList<>();
        if (StringUtils.isNotEmpty(accessPointsParam.getName())) {
            HashMap<String, Object> nameParam = new HashMap<>();
            nameParam.put("key", accessPointsParam.getName());
            nameParam.put("operator", 6);
            ArrayList<String> values = new ArrayList<>();
            values.add(accessPointsParam.getName());
            nameParam.put("values", values);
            expressions.add(nameParam);
        }
        jsonBody.put("expressions", expressions);
        jsonBody.put("pageNo", accessPointsParam.getPageNo());
        jsonBody.put("pageSize", accessPointsParam.getPageSize());
        jsonBody.put("orderBy", "createTime");
        jsonBody.put("orderType", "desc");
        String body = jsonBody.toJSONString();

        log.info("path:{}body:{}", path, body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);// post请求application/json类型参数
        log.info("result:{}", result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String data = jsonObject.get("data").toString();
        JSONObject jsonObject1 = JSONObject.parseObject(data);
        Integer total = (Integer) jsonObject1.get("total");
        JSONArray jsonArray = jsonObject1.getJSONArray("list");
        List<AccessPointVo> accessPointVos = jsonArray.toJavaList(AccessPointVo.class);
        Page<AccessPointVo> page = new Page<>();
        page.setTotal(total);
        page.setRecords(accessPointVos);
        return page;
    }

    @Override
    public Page<DoorDevicesVo> getDoorDevices(AccessDevicesParam accessDevicesParam) {
        String contentType = "application/json";
        Map<String, String> path = this.getAuth("/api/resource/v2/acsDevice/search");
        //构建请求参数
        JSONObject jsonBody = new JSONObject();
        ArrayList<Object> expressions = new ArrayList<>();
        if (StringUtils.isNotEmpty(accessDevicesParam.getName())) {
            HashMap<String, Object> nameParam = new HashMap<>();
            nameParam.put("key", accessDevicesParam.getName());
            nameParam.put("operator", 6);
            ArrayList<String> values = new ArrayList<>();
            values.add(accessDevicesParam.getName());
            nameParam.put("values", values);
            expressions.add(nameParam);
        }
        jsonBody.put("expressions", expressions);
        jsonBody.put("pageNo", accessDevicesParam.getPageNo());
        jsonBody.put("pageSize", accessDevicesParam.getPageSize());
        jsonBody.put("orderBy", "createTime");
        jsonBody.put("orderType", "desc");
        String body = jsonBody.toJSONString();

        log.info("path:{}body:{}", path, body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);// post请求application/json类型参数
        log.info("result:{}", result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String data = jsonObject.get("data").toString();
        JSONObject jsonObject1 = JSONObject.parseObject(data);
        Integer total = (Integer) jsonObject1.get("total");
        JSONArray jsonArray = jsonObject1.getJSONArray("list");
        List<DoorDevicesVo> doorDevicesVos = jsonArray.toJavaList(DoorDevicesVo.class);
        Page<DoorDevicesVo> page = new Page<>();
        page.setTotal(total);
        page.setRecords(doorDevicesVos);
        return page;
    }

    @Override
    public Page<DoorEventsVo> getDoorEvents(DoorEventsParam doorEventsParam) throws Exception {
        log.info("海康门禁记录采集，参数为：{}", JsonUtils.serialize(doorEventsParam));
        String contentType = "application/json";
        Map<String, String> path = this.getAuth("/api/acs/v2/door/events");
        //构建请求参数
        JSONObject jsonBody = new JSONObject();
        if (StringUtils.isNotEmpty(doorEventsParam.getDoorName())) {
            jsonBody.put("doorName", doorEventsParam.getDoorName());
        }

        if (StringUtils.isNotEmpty(doorEventsParam.getEndTime()) && StringUtils.isNotEmpty(doorEventsParam.getStartTime())) {
            //处理时间格式 ISO8601 例如：北京时间2004年5月3日下午5点30分8秒，可以写成2004-05-03T17:30:08+08:00
            String[] startAndEnd = formatDateToISO8601(doorEventsParam.getStartTime(), doorEventsParam.getEndTime());
            jsonBody.put("startTime", startAndEnd[0]);
            jsonBody.put("endTime", startAndEnd[1]);
        }

        if (CollectionUtils.isNotEmpty(doorEventsParam.getEventTypes())) {
            jsonBody.put("eventTypes", doorEventsParam.getEventTypes());
        }

        if (CollectionUtils.isNotEmpty(doorEventsParam.getDoorIndexCodes())) {
            jsonBody.put("doorIndexCodes", doorEventsParam.getDoorIndexCodes());
        }

        jsonBody.put("pageNo", doorEventsParam.getPageNo());
        jsonBody.put("pageSize", doorEventsParam.getPageSize());
        jsonBody.put("sort", "eventTime");
        jsonBody.put("order", "desc");
        String body = jsonBody.toJSONString();

        log.info("海康门禁记录采集参数：{}", body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);// post请求application/json类型参数
        log.info("海康门禁记录采集，返回结果:{}", result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String data = jsonObject.get("data").toString();
        JSONObject jsonObject1 = JSONObject.parseObject(data);
        Integer total = (Integer) jsonObject1.get("total");
        JSONArray jsonArray = jsonObject1.getJSONArray("list");
        List<DoorEventsVo> doorEventsVos = jsonArray.toJavaList(DoorEventsVo.class);
        Page<DoorEventsVo> page = new Page<>();
        page.setTotal(total);
        page.setRecords(hikDoorEventConvert(doorEventsVos));
        return page;
    }

    @Override
    public List<DoorControlResultVo> doorControl(DoorControlParam param) {
        //替换古河云到海康的状态code
        if (ObjectUtils.isNotEmpty(param.getControlType())) {
            param.setControlType(ghyToHik(param.getControlType()));
        }
        String contentType = "application/json";
        Map<String, String> path = this.getAuth("/api/acs/v1/door/doControl");
        //构建请求参数
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("doorIndexCodes", param.getDoorIndexCodes());
        jsonBody.put("controlType", param.getControlType());
        String body = jsonBody.toJSONString();
        log.info("path:{}body:{}", path, body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);// post请求application/json类型参数
        log.info("result:{}", result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray data = jsonObject.getJSONArray("data");
        return ObjectUtils.copying(data, DoorControlResultVo.class);
    }

    @Override
    public List<AuthDoorStatusVo> getDoorStates(List<String> ids) {
        log.info("海康门禁状态采集，参数为：{}", JsonUtils.serialize(ids));
        String contentType = "application/json";
        Map<String, String> path = this.getAuth("/api/acs/v1/door/states");
        //构建请求参数

        /**
         * STEP5：组装请求参数
         */
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("doorIndexCodes", ids);
        String body = jsonBody.toJSONString();

        log.info("path:{}body:{}", path, body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);// post请求application/json类型参数
        log.info("result:{}", result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray authDoorList = data.getJSONArray("authDoorList");
        List<AuthDoorStatusVo> copying = ObjectUtils.copying(authDoorList, AuthDoorStatusVo.class);
        //替换海康到古河云的状态code
        copying.forEach(copye -> {
            if (ObjectUtils.isNotEmpty(copye.getDoorState())) {
                copye.setDoorState(hikToGhy(copye.getDoorState()));
            }
        });
        return copying;
    }

    /**
     * @Description 平台访问
     * @Author lrj
     */
    private void login() {
        String key = SubsystemTypeConstants.DOOR_SERVER + ":" + "hik" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        ClientHttpVo clientHttpVo = JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), ClientHttpVo.class);
        /**
         * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
         */
        ArtemisConfig.host = clientHttpVo.getHost(); // 平台的ip端口
        ArtemisConfig.appKey = clientHttpVo.getAppKey();  // 密钥appkey
        ArtemisConfig.appSecret = clientHttpVo.getAppSecret();// 密钥appSecret
    }

    /**
     * 格式化海康查询须要的时间格式
     *
     * @param start
     * @param end
     *
     * @return
     */
    private String[] formatDateToISO8601(String start, String end) {
        //如要表示北京时间2004年5月3日下午5点30分8秒，可以写成2004-05-03T17:30:08+08:00
        String[] startDate = start.split(" ");
        String finalStart = startDate[0] + "T" + startDate[1] + "+08:00";
        String[] endDate = end.split(" ");
        String finalEnd = endDate[0] + "T" + endDate[1] + "+08:00";
        log.info("海康门禁，处理时间格式为：{},{}", finalStart, finalEnd);
        return new String[]{ finalStart, finalEnd };
    }

    //将 datetime 字符串 转换成 iso8601的格式
    //iso8601示例：2021-01-27T23:00:00+08:00
    private String convertLocalDateForIso8601(LocalDateTime date) {
        try {
            return DateUtils.format(date, "yyyy-MM-dd'T'HH:mm:ssXXX");
        } catch (Exception e) {
            log.error("将 datetime 日期 转换成 iso8601的字符串格式失败", e);
        }
        return null;
    }

    /**
     * 将iso8601格式转换成本地时间
     *
     * @param iso8601Time
     *
     * @return
     */
    private LocalDateTime convertIso8601ToLocalTime(String iso8601Time) {
        if (StringUtils.isNotEmpty(iso8601Time)) {
            try {
                // 解析ISO 8601格式的时间字符串为Date对象
                return DateUtils.parse(iso8601Time, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            } catch (Exception e) {
                log.error("将 datetime 字符串 转换成 iso8601的日期格式失败", e);
            }
        }
        return null;
    }

    private List<DoorEventsVo> hikDoorEventConvert(List<DoorEventsVo> dataList) {
        if (!CollectionUtils.isEmpty(dataList)) {
            List<TypeManage> typeManages = typeManageMapper.selectList(null);
            Map<String, TypeManage> eventTypeMap = typeManages.stream()
                    .collect(Collectors.toMap(typeManage -> typeManage.getTypeCode().toString(), typeManage -> typeManage, (o, n) -> n));

            return dataList.stream().map(r -> {
                TypeManage typeManage = eventTypeMap.get(r.getEventType());

                return DoorEventsVo.builder()
                        .eventId(r.getEventId())
                        .doorIndexCode(r.getDoorIndexCode())
                        .doorName(r.getDoorName())
                        .eventType(typeManage == null ? "" : typeManage.getName())
                        .personId(r.getPersonId())
                        .personName(r.getPersonName())
                        .inAndOutType(r.getInAndOutType())
                        .eventName(typeManage == null ? "" : typeManage.getName())
                        .eventTime(r.getEventTime())
                        .eventWay(typeManage == null ? "" : typeManage.getName())
                        .eventResult(typeManage == null ? EventResultEnum.DO_REFUSE.name() : typeManage.getTypeResult())
                        .cardNo(r.getCardNo())
                        .build();
            }).collect(Collectors.toList());
        }
        return dataList;
    }

    /**
     * 获取认证信息
     *
     * @return
     */

    private Map<String, String> getAuth(String apiPath) {
        //初始化认证信息
        login();
        //构建jdk请求参数
        final String previewURLsApi = ARTEMIS_PATH + apiPath;
        Map<String, String> path = new HashMap<>(2) {
            {
                put(clientHttps, previewURLsApi);//根据现场环境部署确认是http还是https
            }
        };
        return path;
    }

    @Override
    public List<CardVO> getDoorCard() {
        log.info("门禁一卡通: 卡信息采集开始");
        Map<String, String> path = this.getAuth("/api/irds/v1/card/advance/cardList");
        //构建请求参数
        JSONObject bodyParam = new JSONObject();
        bodyParam.put("pageNo", 1);
        bodyParam.put("pageSize", 1000);
        String body = bodyParam.toJSONString();

        log.info("path:{}body:{}", path, body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        log.info("result:{}", result);
        HikResponse<HikPage<CardModel>> response = JsonUtils.deserialize(result, new TypeReference<HikResponse<HikPage<CardModel>>>() {});
        if ("0".equals(response.getCode())) {
            List<CardModel> list = response.getData().getList();
            return list.stream().map(card -> {
                CardVO vo = new CardVO();
                vo.setId(card.getCardId());
                vo.setCardNo(card.getCardNo());
                vo.setPersonId(card.getPersonId());
                vo.setPersonName(card.getPersonName());
                vo.setOrgId(card.getOrgId());
                vo.setUseStatus(CardModel.UseStatusEnum.getNameByValue(card.getUseStatus()));
                vo.setCardType(card.getCardType());
                vo.setCardTypeName(card.getCardTypeName());
                vo.setStartDate(convertIso8601ToLocalTime(card.getStartDate()));
                vo.setEndDate(convertIso8601ToLocalTime(card.getEndDate()));
                vo.setLossDate(convertIso8601ToLocalTime(card.getLossDate()));
                vo.setUnlossDate(convertIso8601ToLocalTime(card.getUnlossDate()));
                vo.setCreateTime(convertIso8601ToLocalTime(card.getCreateTime()));
                vo.setUpdateTime(convertIso8601ToLocalTime(card.getUpdateTime()));
                vo.setIsBindFace(card.getIsBindFace());
                vo.setIsBindFinger(card.getIsBindFinger());
                vo.setRemark(card.getDescription());
                return vo;
            }).collect(Collectors.toList());
        } else {
            log.error("门禁一卡通: 卡信息采集失败:{}", response.getMsg());
        }
        log.info("门禁一卡通: 卡信息采集结束");
        return Collections.emptyList();
    }

    @Override
    public List<OrgVO> getOrg() {
        log.info("门禁一卡通: 组织信息采集开始");
        Map<String, String> path = this.getAuth("/api/resource/v2/org/advance/orgList");
        //构建请求参数
        JSONObject bodyParam = new JSONObject();
        bodyParam.put("pageNo", 1);
        bodyParam.put("pageSize", 1000);
        String body = bodyParam.toJSONString();

        log.info("path:{}body:{}", path, body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        log.info("result:{}", result);
        HikResponse<HikPage<OrgModel>> response = JsonUtils.deserialize(result, new TypeReference<HikResponse<HikPage<OrgModel>>>() {});
        if ("0".equals(response.getCode())) {
            List<OrgModel> list = response.getData().getList();
            return list.stream().map(org -> {
                OrgVO vo = new OrgVO();
                vo.setId(org.getOrgIndexCode());
                vo.setOrgCode(org.getOrganizationCode());
                vo.setParentId(org.getParentOrgIndexCode());
                vo.setOrgName(org.getOrgName());
                vo.setOrgPath(org.getOrgPath());
                return vo;
            }).collect(Collectors.toList());
        } else {
            log.error("门禁一卡通: 组织信息采集失败:{}", response.getMsg());
        }
        log.info("门禁一卡通: 组织信息采集结束");
        return Collections.emptyList();
    }

    @Override
    public List<VisitingRecordVO> getVisitingRecord(VisitingRecordParam param) {
        log.info("门禁一卡通: 来访信息采集开始");
        Map<String, String> path = this.getAuth("/api/visitor/v2/visiting/records");
        //构建请求参数
        JSONObject bodyParam = new JSONObject();
        bodyParam.put("pageNo", 1);
        bodyParam.put("pageSize", 1000);
        bodyParam.put("visitStartTimeBegin", convertLocalDateForIso8601(param.getStartTime()));
        bodyParam.put("visitStartTimeEnd", convertLocalDateForIso8601(param.getEndTime()));
        String body = bodyParam.toJSONString();

        log.info("path:{}body:{}", path, body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        log.info("result:{}", result);
        HikResponse<HikPage<VisitingRecordModel>> response = JsonUtils.deserialize(result, new TypeReference<HikResponse<HikPage<VisitingRecordModel>>>() {});
        if ("0".equals(response.getCode())) {
            List<VisitingRecordModel> list = response.getData().getList();
            return list.stream().map(r -> {
                                         VisitingRecordVO.VisitingStatus visitingStatus = VisitingRecordVO.VisitingStatus.getStatusByValue(r.getVisitorStatus());
                                         return VisitingRecordVO.builder()
                                                 .id(r.getOrderId())
                                                 .receptionistName(r.getReceptionistName())
                                                 .cardNo(r.getCardNo())
                                                 .phoneNo(r.getPhoneNo())
                                                 .plateNo(r.getPlateNo())
                                                 .visitStartTime(convertIso8601ToLocalTime(r.getVisitStartTime()))
                                                 .visitEndTime(convertIso8601ToLocalTime(r.getVisitEndTime()))
                                                 .visitorName(r.getVisitorName())
                                                 .visitPurpose(r.getVisitPurpose())
                                                 .visitorStatus(visitingStatus != null ? visitingStatus.getCode() : "")
                                                 .visitorStatusName(visitingStatus != null ? visitingStatus.getDisplayName() : "")
                                                 .build();
                                     }
                                    ).collect(Collectors.toList());
        } else {
            log.error("门禁一卡通: 来访信息采集失败:{}", response.getMsg());
        }
        log.info("门禁一卡通: 来访信息采集结束");
        return Collections.emptyList();
    }

    @Override
    public List<AccessRecordVO> getTKRecord(TKRecordParam param) {
        log.info("门禁一卡通: 梯控信息采集开始");
        Map<String, String> path = this.getAuth("/api/ecs/v1/access_events/search");
        //构建请求参数
        JSONObject bodyParam = new JSONObject();
        bodyParam.put("pageNo", 1);
        bodyParam.put("pageSize", 1000);
        bodyParam.put("startTime", convertLocalDateForIso8601(param.getStartTime()));
        bodyParam.put("endTime", convertLocalDateForIso8601(param.getEndTime()));
        String body = bodyParam.toJSONString();

        log.info("path:{}body:{}", path, body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        log.info("result:{}", result);
        HikResponse<HikPage2<TKRecordModel>> response = JsonUtils.deserialize(result, new TypeReference<HikResponse<HikPage2<TKRecordModel>>>() {});
        if ("0".equals(response.getCode())) {
            List<TKRecordModel> list = response.getData().getRows();
            if (!CollectionUtils.isEmpty(list)) {
                return list.stream().map(event ->
                                                 AccessRecordVO.builder()
                                                         .id(event.getEventId())
                                                         .eventName(event.getEventName())
                                                         .eventType(event.getEventType())
                                                         .eventTypeName(TKEventTypeEnum.getDisplayNameByValue(event.getEventType()))
                                                         .eventTime(convertIso8601ToLocalTime(event.getEventTime()))
                                                         .cardNo(event.getCardNo())
                                                         .personId(event.getPersonId())
                                                         .personName(event.getPersonName())
                                                         .orgId(event.getOrgId())
                                                         .orgName(event.getOrgName())
                                                         .deviceCode(event.getDeviceCode())
                                                         .deviceName(event.getDeviceName())
                                                         .alias(event.getLiftName())
                                                         .accessType("TK")
                                                         .build()
                                        ).collect(Collectors.toList());
            }
        } else {
            log.error("门禁一卡通: 梯控信息采集失败:{}", response.getMsg());
        }
        log.info("门禁一卡通: 梯控信息采集结束");
        return Collections.emptyList();
    }

    @Override
    public List<AccessRecordVO> getKSDJRecord(KSDJRecordParam param) {
        log.info("门禁一卡通: 可视对讲信息采集开始");
        Map<String, String> path = this.getAuth("/api/vis/v1/accessEvent/events");
        //构建请求参数
        JSONObject bodyParam = new JSONObject();
        bodyParam.put("pageNo", 1);
        bodyParam.put("pageSize", 1000);
        bodyParam.put("startTime", convertLocalDateForIso8601(param.getStartTime()));
        bodyParam.put("endTime", convertLocalDateForIso8601(param.getEndTime()));
        String body = bodyParam.toJSONString();

        log.info("path:{}body:{}", path, body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        log.info("result:{}", result);
        HikResponse<HikPage<KSDJRecordModel>> response = JsonUtils.deserialize(result, new TypeReference<HikResponse<HikPage<KSDJRecordModel>>>() {});
        if ("0".equals(response.getCode())) {
            List<KSDJRecordModel> list = response.getData().getList();
            if (!CollectionUtils.isEmpty(list)) {
                return list.stream().map(event ->
                                                 AccessRecordVO.builder()
                                                         .id(event.getEventId())
                                                         .eventName(event.getEventName())
                                                         .eventType(event.getEventType())
                                                         .eventTypeName(KSDJEventTypeEnum.getDisplayNameByValue(event.getEventType()))
                                                         .eventTime(convertIso8601ToLocalTime(event.getEventTime()))
                                                         .cardNo(event.getCardNo())
                                                         .personId(event.getPersonId())
                                                         .personName(event.getPersonName())
                                                         .orgId(event.getOrgIndexCode())
                                                         .orgName(event.getOrgName())
                                                         .deviceCode(event.getDeviceIndexCode())
                                                         .deviceName(event.getDeviceName())
                                                         .accessType("KSDJ")
                                                         .build()
                                        ).collect(Collectors.toList());
            }
        } else {
            log.error("门禁一卡通: 可视对讲信息采集失败:{}", response.getMsg());
        }
        log.info("门禁一卡通: 可视对讲信息采集结束");
        return Collections.emptyList();
    }

    /**
     * 古河云转海康
     *
     * @return
     */
    public Integer ghyToHik(Integer doorState) {
        switch (doorState) {
            //常开
            case 0:
                return 0;
            //开启
            case 1:
                return 2;
            //关闭
            case 2:
                return 1;
            //常关
            case 3:
                return 3;
            default:
                return doorState;
        }
    }

    /**
     * 海康转古河云
     *
     * @return
     */
    public Integer hikToGhy(Integer doorState) {
        switch (doorState) {
            //常开
            case 0:
                return 0;
            //关闭
            case 1:
                return 2;
            //开启
            case 2:
                return 1;
            //常关
            case 3:
                return 3;
            default:
                return doorState;
        }
    }
}
