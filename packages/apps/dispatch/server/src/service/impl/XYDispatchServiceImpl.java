package com.guhecloud.cube.biz.dispatch.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.dispatch.server.dto.XYResponseDTO;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchConfig;
import com.guhecloud.cube.biz.dispatch.server.enumeration.XYCallResultEnum;
import com.guhecloud.cube.biz.dispatch.server.enumeration.XYCallTypeEnum;
import com.guhecloud.cube.biz.dispatch.server.enumeration.XYDeviceTypeEnum;
import com.guhecloud.cube.biz.dispatch.server.enumeration.XYGroupTypeEnum;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchCallQueryModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchDeviceModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchFileQueryModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchGPSQueryModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchGroupModel;
import com.guhecloud.cube.biz.dispatch.server.model.extend.DispatchDeviceExtendModel;
import com.guhecloud.cube.biz.dispatch.server.model.extend.DispatchGroupExtendModel;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchConfigService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceGpsHistoryService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchService;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchCallRecordVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchCommonTypeVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceStatusVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchFileVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGPSVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchNodeVO;
import com.guhecloud.cube.biz.dispatch.server.vo.extend.DispatchNodeExtendVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.Base64Utils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 描述：讯一设备的调度实现
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
@Slf4j
public class XYDispatchServiceImpl implements DispatchService {
    private static final String APIKEY = "N2E3YTBiYjU4YWUxNjM4YzkwOWQ1ZjBjYWYwYzRjZTQ=";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DispatchConfigService dispatchConfigService;
    @Autowired
    private DispatchDeviceGpsHistoryService dispatchDeviceGpsHistoryService;

    private final String ADD_GROUP_URI = "/group/add";//添加组
    private final String UPDATE_GROUP_URI = "/group/modify";
    private final String DEL_GROUP_URI = "/api/userManage/doDelGroup";//删除组
    private final String ADD_DEVICE_URI = "/employee/add";//添加设备
    private final String UPDATE_DEVICE_URI = "/employee/modify";//修改设备
    private final String DEL_DEVICE_URI = "/doDelUserByEID";//删除设备
    //查询通话记录
    private final String CALL_PAGE_URI = "/GetCallRecordByConditions";
    private final String FILE_RECORD_LIST_URI = "/selectCallRecordByConditions";
    private final String FILE_VIDEO_LIST_URI = "/selectCallRecordByConditions2";
    private final String GPS_LIST_URI = "/api/gisDetail/page";
    private final String DEVICE_STATUS_URI = "/GetAllOnlineUser2";
    private final String ALL_GROUP_URI = "/GetGroupPage";//获取所有组
    private final String ALL_DEVICE_URI = "/GetEmployeePage";//获取所有设备或用户
    private final String ALL_DEVICE_IN_GROUP_URI = "/getGroupMember";//获取所有设备或用户
    private final String DEVICE_JOIN_GROUP_URI = "/api/userManage/doAddGroupMember";
    private final String DEVICE_LEAVE_GROUP_URI = "/api/userManage/batchRemoveGroupMembers";

    /**
     * 获取访问token
     *
     * @param userName
     *
     * @return
     */
    private String getToken(String userName) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String key = "XY:DISPATCH:" + tenant + ":" + userName;
        String token = (String) this.redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(token)) {
            token = dispatchLogin(userName, userName);
            this.redisTemplate.opsForValue().set(key, token, 5, TimeUnit.MINUTES);
        }
        return token;
    }

    @Override
    public String dispatchLogin(String userName, String password) {
        try {
            return getAuthorization();
        } catch (Exception e) {
            log.error("讯一，获取token失败");
        }
        return "";
    }

    /**
     * 初始化调度参数
     *
     * @return
     */
    private XYResponseDTO.Config initDispathPlatformConfig() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        //        String key = "XY:DISPATCH:" + tenant;
        //        String paramMapStr = (String)this.redisTemplate.opsForValue().get(key);
        //        if(StringUtils.isEmpty(paramMapStr)){
        DispatchConfig config = dispatchConfigService.getOne(Wrappers.<DispatchConfig>lambdaQuery().eq(DispatchConfig::getTenant, tenant));
        Assert.isNotFound(config == null, "未配置调度台相关参数，接口调用失败！");
        //扩展配置处理
        String extendConfig = config.getExtendConfig();
        Map<String, String> extendMap = JsonUtils.deserialize(extendConfig, Map.class);
        XYResponseDTO.Config ptConfig = new XYResponseDTO.Config(config.getAccount(), config.getPassword(), config.getPlatformAddr(), extendMap.get("rootNode"), extendMap.get("fileUrl"));
        return ptConfig;
    }

    @Override
    public DispatchGroupModel addGroup(DispatchGroupModel param) {
        XYResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        if (StringUtils.isEmpty(param.getParentCode())) {
            param.setParentCode(config.getRootNode());
        }

        Map<String, Object> map = new HashMap<>();
        if (param instanceof DispatchGroupExtendModel) {
            log.info("讯一设备组参数：" + JsonUtils.serialize(param));
            DispatchGroupExtendModel extend = (DispatchGroupExtendModel) param;
            map.put("parentGroupNum", param.getParentCode());
            map.put("groupNum", param.getCode());
            map.put("groupName", param.getName());
            map.put("theType", param.getType());// 0 对讲组; 1非对讲组  说明：对讲组会默认添加到根组下；需要树形组结构，请填非对讲组
            map.put("isRecord", extend.getIsRecord());
            map.put("groupLevel", extend.getGroupLevel());
            map.put("seq", extend.getSort());
            map.put("intercomIdleTimeOut", Objects.isNull(extend.getIntercomIdleTimeOut()) ? 600 : extend.getIntercomIdleTimeOut());
            map.put("intercomPowerTimeCount", Objects.isNull(extend.getIntercomPowerTimeCount()) ? 60 : extend.getIntercomPowerTimeCount());
            map.put("queueMaxNum", Objects.isNull(extend.getQueueMaxNum()) ? 5 : extend.getQueueMaxNum());
            map.put("intercomStateSpace", Objects.isNull(extend.getIntercomStateSpace()) ? 5 : extend.getIntercomStateSpace());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("token", getToken(config.getAccount()));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<XYResponseDTO> response = restTemplate.postForEntity(
                config.getPlatformAddr() + ADD_GROUP_URI, request, XYResponseDTO.class);
        if (response.getBody().getCode() == 200) {
            return param;
        } else {
            throw new ServiceException(response.getBody().getMsg() + ":" + param.getCode());
        }
    }

    @Override
    public boolean updateGroup(DispatchGroupModel param) {
        XYResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        if (StringUtils.isEmpty(param.getParentCode())) {
            param.setParentCode(config.getRootNode());
        }

        Map<String, Object> map = new HashMap<>();
        if (param instanceof DispatchGroupExtendModel) {
            DispatchGroupExtendModel extend = (DispatchGroupExtendModel) param;
            map.put("parentGroupNum", param.getParentCode());
            map.put("groupNum", param.getCode());
            map.put("groupName", param.getName());
            map.put("theType", param.getType());// 0 对讲组; 1非对讲组  说明：对讲组会默认添加到根组下；需要树形组结构，请填非对讲组
            map.put("isRecord", extend.getIsRecord());
            map.put("groupLevel", extend.getGroupLevel());
            map.put("seq", extend.getSort());
            map.put("intercomIdleTimeOut", Objects.isNull(extend.getIntercomIdleTimeOut()) ? 600 : extend.getIntercomIdleTimeOut());
            map.put("intercomPowerTimeCount", Objects.isNull(extend.getIntercomPowerTimeCount()) ? 60 : extend.getIntercomPowerTimeCount());
            map.put("queueMaxNum", Objects.isNull(extend.getQueueMaxNum()) ? 5 : extend.getQueueMaxNum());
            map.put("intercomStateSpace", Objects.isNull(extend.getIntercomStateSpace()) ? 5 : extend.getIntercomStateSpace());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("token", getToken(config.getAccount()));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<XYResponseDTO> response = restTemplate.postForEntity(
                config.getPlatformAddr() + UPDATE_GROUP_URI, request, XYResponseDTO.class);
        if (response.getBody().getCode() == 200) {
            return true;
        } else {
            throw new ServiceException(response.getBody().getMsg() + ":" + param.getCode());
        }
    }

    /**
     * 如果组被第三方平台删除，会造成业务平台无法删除组的问题；所以在删除组的实现方法内处理掉【无法找到组】异常
     *
     * @param code
     *
     * @return
     */
    @Override
    public boolean delGroup(String code) {
        XYResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", getToken(config.getAccount()));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
        String url = config.getPlatformAddr() + DEL_GROUP_URI;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("groupNum", code);

        ResponseEntity<XYResponseDTO> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, XYResponseDTO.class);
        if (response.getBody().getState() == 200) {
            return true;
        } else {
            throw new ServiceException(response.getBody().getDescribe());
        }
    }

    @Override
    public DispatchDeviceModel addDevice(DispatchDeviceModel param) {
        XYResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("token", getToken(config.getAccount()));
        if (StringUtils.isEmpty(param.getParentCode())) {
            param.setParentCode(config.getRootNode());
        }
        Map<String, Object> map = new HashMap<>();
        if (param instanceof DispatchDeviceExtendModel) {
            DispatchDeviceExtendModel extend = (DispatchDeviceExtendModel) param;
            map.put("employeeLevel", extend.getDeviceLevel());
            map.put("isRecord", extend.getIsRecord());
            map.put("isVideo", extend.getIsVideo());
            map.put("remark", extend.getRemark());
        }
        map.put("groupNum", param.getParentCode());
        map.put("employeeId", param.getCode());
        map.put("password", Base64Utils.decode(param.getPassword()));//base64解码
        map.put("type", param.getType());
        map.put("name", param.getName());
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<XYResponseDTO> response = restTemplate.postForEntity(
                config.getPlatformAddr() + ADD_DEVICE_URI, request, XYResponseDTO.class);
        if (response.getBody().getCode() == 200) {
            return param;
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    @Override
    public boolean updateDevice(DispatchDeviceModel param) {
        XYResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("token", getToken(config.getAccount()));
        Map<String, Object> map = new HashMap<>();
        if (param instanceof DispatchDeviceExtendModel) {
            DispatchDeviceExtendModel extend = (DispatchDeviceExtendModel) param;
            map.put("employeeLevel", extend.getDeviceLevel());
            map.put("isRecord", extend.getIsRecord());
            map.put("isVideo", extend.getIsVideo());
            map.put("remark", extend.getRemark());
        }
        map.put("groupNum", param.getParentCode());
        map.put("employeeId", param.getCode());
        map.put("password", Base64Utils.decode(param.getPassword()));
        map.put("type", param.getType());
        map.put("name", param.getName());
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<XYResponseDTO> response = restTemplate.postForEntity(
                config.getPlatformAddr() + UPDATE_DEVICE_URI, request, XYResponseDTO.class);
        if (response.getBody().getCode() == 200) {
            return true;
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    @Override
    public boolean delDevice(String code) {
        XYResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", getToken(config.getAccount()));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
        String url = config.getPlatformAddr() + DEL_DEVICE_URI.replace("{number}", code);
        ResponseEntity<XYResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, request, XYResponseDTO.class);
        if (response.getBody().getCode() == 200) {
            return true;
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    @Override
    public boolean addDeviceToGroup(String groupCode, List<String> codes) {
        XYResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("token", getToken(config.getAccount()));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);

        String url = config.getPlatformAddr() + DEVICE_JOIN_GROUP_URI;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("groupNum", groupCode)
                .queryParam("employeelist", StringUtils.join(",", codes));

        ResponseEntity<XYResponseDTO> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, XYResponseDTO.class);
        if (response.getBody().getState() == 200) {
            return true;
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    @Override
    public boolean delDeviceFromGroup(String groupCode, List<String> codes) {
        XYResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("token", getToken(config.getAccount()));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
        String url = config.getPlatformAddr() + DEVICE_LEAVE_GROUP_URI;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("groupNum", groupCode)
                .queryParam("employeeIds", StringUtils.join(",", codes));

        ResponseEntity<XYResponseDTO> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, XYResponseDTO.class);
        if (response.getStatusCodeValue() == 200 && response.getBody().getCode() == 200) {
            return true;
        } else {
            throw new ServiceException(response.getBody().getDescribe());
        }
    }

    @Override
    public Pagination<DispatchCallRecordVO> callPage(DispatchCallQueryModel param) {
        XYResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", getToken(config.getAccount()));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);

        String url = config.getPlatformAddr() + CALL_PAGE_URI;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("curPage", param.getPageNumber())
                .queryParam("pageRows", param.getPageSize())
                .queryParam("caller", param.getCallerNumber())
                .queryParam("called", param.getCalleeNumber())
                .queryParam("callType", param.getCallType())
                .queryParam("callResult", param.getCallResult())
                .queryParam("startTime", param.getStartTime())
                .queryParam("releaseTime", param.getEndTime());

        ResponseEntity<XYResponseDTO.CallPageResponse> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, XYResponseDTO.CallPageResponse.class);
        if (response.getBody().getState() == 200) {
            List<XYResponseDTO.CallRecord> records = response.getBody().getData();
            return new Pagination(response.getBody().getTotalCount(), response.getBody().getPages(), buildDispatchCallRecord(records));
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    /**
     * 清洗数据 统一格式
     *
     * @param records
     *
     * @return
     */
    private List<DispatchCallRecordVO> buildDispatchCallRecord(List<XYResponseDTO.CallRecord> records) {
        return records.stream().map(r ->
                                            DispatchCallRecordVO.builder()
                                                    .callId(r.getCid())
                                                    .callerNumber(r.getCaller())
                                                    .callerName(r.getCallerName())
                                                    .calleeNumber(r.getCalled())
                                                    .calleeName(r.getCalledName())
                                                    .callType(r.getCallType())
                                                    .callTypeName(XYCallTypeEnum.valueOf("CT_" + r.getCallType()).getDesc())
                                                    .startTime(r.getCreateTime())
                                                    .endTime(r.getReleaseTime())
                                                    .callResult(XYCallResultEnum.valueOf("CR_" + r.getCallResult()).getDesc())
                                                    .releaseReason(r.getReleaseReassion())
                                                    .build()
                                   ).collect(Collectors.toList());
    }

    @Override
    public List<DispatchGPSVO> gpsHistory(DispatchGPSQueryModel param) {
        return dispatchDeviceGpsHistoryService.gpsHistory(param).getRecords();
    }

    @Override
    public Pagination<DispatchGPSVO> gpsHistoryPage(DispatchGPSQueryModel param) {
        Page<DispatchGPSVO> dispatchGPSVOPage = dispatchDeviceGpsHistoryService.gpsHistory(param);
        return PagingUtils.convert(dispatchGPSVOPage);
    }

    @Override
    public Pagination<DispatchFileVO> communicationFilePage(DispatchFileQueryModel param) {
        XYResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", getToken(config.getAccount()));
        log.info("讯一通讯文件查询Token：" + getToken(config.getAccount()));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
        String realAddr = "voice".equals(param.getFileType()) ? FILE_RECORD_LIST_URI : FILE_VIDEO_LIST_URI;
        String url = config.getPlatformAddr() + realAddr;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("curPage", param.getPageNumber())
                .queryParam("pageRows", param.getPageSize())
                .queryParam("number", param.getCode())
                .queryParam("callType", param.getCallType());

        String finalUrl = uriBuilder.toUriString() + "&beginTime=" + param.getStartTime() + "&endTime=" + param.getEndTime();
        log.info("讯一通讯文件查询地址：" + finalUrl);
        //录音文件
        ResponseEntity<XYResponseDTO.FileListResponse> response = restTemplate.postForEntity(finalUrl, request, XYResponseDTO.FileListResponse.class);
        log.info("讯一通讯文件查询返回：" + JsonUtils.serialize(response));

        if ((response.getBody().getState() != null && response.getBody().getState() == 200)
            || (response.getBody().getCode() != null && response.getBody().getCode() == 200)) {
            List<XYResponseDTO.FileRecord> files = response.getBody().getData().getData();
            Long total = response.getBody().getData().getTotalCount();
            Long pages = response.getBody().getData().getPages();
            if (CollectionUtils.isEmpty(files)) {
                return Pagination.empty();
            } else {
                return new Pagination(total, pages, buildCommunicationFile(files, config, param.getFileType()));
            }
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    /**
     * 构建文件信息
     *
     * @param dataList
     *
     * @return
     */
    private List<DispatchFileVO> buildCommunicationFile(List<XYResponseDTO.FileRecord> dataList, XYResponseDTO.Config config,
                                                        String fileType
                                                       ) {
        return dataList.stream().map(d -> DispatchFileVO.builder()
                .type(fileType)
                .name(d.getRecordName())
                .suffix(d.getRecordName().substring(d.getRecordName().lastIndexOf(".") + 1))
                .caller(d.getCaller())
                .callerName(d.getCallerName())
                .callee(d.getCalled())
                .calleeName(d.getCalledName())
                .callType(d.getCallType() != null ? XYCallTypeEnum.valueOf("CT_" + d.getCallType()).getDesc() : null)
                .fileCreateTime(d.getCreateTime())
                .fileEndTime(d.getReleaseTime())
                .fileTimeLength(d.getTimeLength())
                .url(config.getFileUrl() + "/" + d.getSubPath() + "/" + d.getRecordName())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<DispatchDeviceStatusVO> deviceStatus(List<String> codes) {
        XYResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", getToken(config.getAccount()));

        HttpEntity<String> request = new HttpEntity<>(null, headers);

        String url = config.getPlatformAddr() + DEVICE_STATUS_URI;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, String.class);

        if (response.getStatusCode().value() == 200 && response.getBody() != null) {
            XYResponseDTO.DeviceStatusResponse deviceStatusResponse = JsonUtils.deserialize(response.getBody(), XYResponseDTO.DeviceStatusResponse.class);
            if (!CollectionUtils.isEmpty(deviceStatusResponse.getOnlines())) {
                return deviceStatusResponse.getOnlines().stream().map(d -> {
                    //目前APP已直接转换成高德经纬度后上报目前不需要再次转换
                    return DispatchDeviceStatusVO.builder()
                            .code(d.getEmployeeID())
                            .lng(Double.valueOf(d.getLongitude()))//经度
                            .lat(Double.valueOf(d.getLatitude()))//纬度
                            .direction((com.guhecloud.platform.base.util.StringUtils.isBlank(d.getDirect()) || "N".equals(d.getDirect()))
                                       ? null
                                       : new BigDecimal(d.getDirect()).intValue())
                            .speed((com.guhecloud.platform.base.util.StringUtils.isBlank(d.getSpeed()) || "N".equals(d.getSpeed())) ? null
                                                                                                                                    : Float.valueOf(d.getSpeed()))
                            .uploadTime(
                                    StringUtils.isEmpty(d.getTime()) ? LocalDateTime.now() : DateUtils.parse(d.getTime(), "yyyyMMddHHmmss"))
                            .onlineStatus(true)
                            .videoStatus(true)
                            .build();
                }).collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<DispatchNodeVO> allSubNode(String code) {
        return null;
    }

    /**
     * 根据普天调度对接文档  type = 1的为组
     * subType 0 固定组    1 临时组
     *
     * @param code
     *
     * @return
     */
    @Override
    public List<DispatchNodeVO> allGroup(String code) {
        XYResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", getToken(config.getAccount()));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
        String url = config.getPlatformAddr() + ALL_GROUP_URI;

        ResponseEntity<XYResponseDTO.NodeResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, XYResponseDTO.NodeResponse.class);

        List<XYResponseDTO.Node> datas = response.getBody().getList();
        if (!CollectionUtils.isEmpty(datas)) {
            return datas.stream().map(d ->
                                              DispatchNodeExtendVO.builder()
                                                      .parentCodes(Collections.singletonList(d.getParentGroupNum()))
                                                      .code(d.getGroupNum())
                                                      .name(d.getGroupName())
                                                      .type(d.getType())
                                                      .subType(d.getSubType())
                                                      .groupLevel(d.getGroupLevel())
                                                      .sort(d.getSeq())
                                                      .isRecord(d.getISRecord() != null && d.getISRecord() == 1)
                                                      .isVideo(d.getIsVideo() != null && d.getIsVideo() == 1)
                                                      .intercomIdleTimeOut(d.getIntercomIdleTimeOut())
                                                      .intercomPowerTimeCount(d.getIntercomPowerTimeCount())
                                                      .intercomStateSpace(d.getIntercomStateSpace())
                                                      .queueMaxNum(d.getQueueMaxNum())
                                                      .build()
                                     ).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }

    }

    /**
     * 根据普天调度对接文档  type = 2的为用户
     * subType
     * 0=网络话机
     * 1=外线手机
     * 2=外线固话
     * 10=调度台
     * 31=手持终端
     * 30=车载终端
     * 50=智能终端
     * 51=卫星电话
     * 60=摄像头用户
     * 80=卡片机
     * 70=gps设备
     * 90=无屏机设备
     *
     * @param code
     *
     * @return
     */
    @Override
    public List<DispatchNodeVO> allDevice(String code) {
        XYResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", getToken(config.getAccount()));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
        //所有的设备 没有组信息
        String deviceUrl = config.getPlatformAddr() + ALL_DEVICE_URI;
        UriComponentsBuilder deviceUrlBuilder = UriComponentsBuilder.fromHttpUrl(deviceUrl)
                .queryParam("curPage", 1)
                .queryParam("pageRows", 1000);

        ResponseEntity<XYResponseDTO.DeviceResponse> deviceResponse = restTemplate.exchange(deviceUrlBuilder.toUriString(), HttpMethod.GET, request, XYResponseDTO.DeviceResponse.class);
        List<XYResponseDTO.Device> devices = deviceResponse.getBody().getPage().getDataList();
        //所有组里面的设备
        String deviceInGroupUrl = config.getPlatformAddr() + ALL_DEVICE_IN_GROUP_URI;
        UriComponentsBuilder deviceInGroupUrlBuilder = UriComponentsBuilder.fromHttpUrl(deviceInGroupUrl)
                .queryParam("curPage", 1)
                .queryParam("pageRows", 1000);

        ResponseEntity<XYResponseDTO.DeviceInGroupResponse> deviceInGroupResponse = restTemplate.exchange(deviceInGroupUrlBuilder.toUriString(), HttpMethod.GET, request, XYResponseDTO.DeviceInGroupResponse.class);
        List<XYResponseDTO.DeviceInGroup> deviceInGroup = deviceInGroupResponse.getBody().getPage().getDataList();
        //设备与组映射
        Map<String, List<String>> deviceInGroupMap = deviceInGroup.stream()
                .collect(Collectors.groupingBy(XYResponseDTO.DeviceInGroup::getEmployeeID,
                                               Collectors.mapping(device -> device.getGroupNum(), Collectors.toList())));

        if (!CollectionUtils.isEmpty(devices)) {
            return devices.stream().map(d ->
                                                DispatchNodeExtendVO.builder()
                                                        .parentCodes(deviceInGroupMap.get(d.getEmployeeId()))
                                                        .code(d.getEmployeeId())
                                                        .name(d.getName())
                                                        .type(d.getType())
                                                        .deviceLevel(d.getEmployeeLevel())
                                                        .isRecord(d.getIsRecord() != null && d.getIsRecord() == 1)
                                                        .isVideo(d.getIsVideo() != null && d.getIsVideo() == 1)
                                                        .password(Base64Utils.encode(d.getPassword()))
                                                        .remark(d.getRemark())
                                                        .build()
                                       ).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public String getDeviceTypeName(String type) {
        return XYDeviceTypeEnum.valueOf("DT_" + type).getDesc();
    }

    @Override
    public String getGroupTypeName(String type) {
        return XYGroupTypeEnum.valueOf("GT_" + type).getDesc();
    }

    @Override
    public List<DispatchCommonTypeVO> getAllDeviceType() {
        return Arrays.stream(XYDeviceTypeEnum.values())
                .map(t -> DispatchCommonTypeVO.builder()
                        .code(t.name().replace("DT_", ""))
                        .name(t.getDesc())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<DispatchCommonTypeVO> getAllGroupType() {
        return Arrays.stream(XYGroupTypeEnum.values())
                .map(t -> DispatchCommonTypeVO.builder()
                        .code(t.name().replace("GT_", ""))
                        .name(t.getDesc())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<DispatchCommonTypeVO> getCallType() {
        return Arrays.stream(XYCallTypeEnum.values())
                .map(t -> DispatchCommonTypeVO.builder()
                        .code(t.name().replace("CT_", ""))
                        .name(t.getDesc())
                        .build())
                .collect(Collectors.toList());
    }

    //获取接口 Authorization，有效期 5 分钟 Authorization：(随机串:时间戳:token)
    public String getAuthorization() throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String noncestr = UUID.randomUUID().toString();
        String token = timestamp + "\n" + noncestr + "\n" + APIKEY + "\n";
        token = encryptMD5("==" + encryptMD5(token) + "==");
        token = Base64Utils.encode(token);
        return noncestr + ":" + timestamp + ":" + token;
    }

    public String encryptMD5(String str) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(str.getBytes("utf8"));
        byte[] b = md5.digest();
        StringBuilder strMD5 = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            if ((b[i] & 0xFF) < 16) {
                strMD5.append("0");
            }
            strMD5.append(Long.toHexString(b[i] & 0xFF));
        }
        return strMD5.toString();
    }

}