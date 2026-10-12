package com.guhecloud.cube.biz.dispatch.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.dispatch.server.dto.PTResponseDTO;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchConfig;
import com.guhecloud.cube.biz.dispatch.server.enumeration.PTDeviceTypeEnum;
import com.guhecloud.cube.biz.dispatch.server.enumeration.PTGroupTypeEnum;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchCallQueryModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchDeviceModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchFileQueryModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchGPSQueryModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchGroupModel;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchConfigService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchService;
import com.guhecloud.cube.biz.dispatch.server.util.CoordinateTransUtil;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchCallRecordVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchCommonTypeVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceStatusVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchFileVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGPSVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchNodeVO;
import com.guhecloud.cube.biz.dispatch.server.vo.extend.DispatchNodeExtendVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 描述：普天设备的调度实现
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
@Slf4j
public class PTDispatchServiceImpl implements DispatchService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DispatchConfigService dispatchConfigService;

    private final String LOGIN_URI = "/ps/data/login";
    private final String ADD_GROUP_URI = "/ps/data/groupOperAdd";
    private final String DEL_GROUP_URI = "/ps/data/groupOperDel/{number}";
    private final String ADD_DEVICE_URI = "/ps/data/userOperAdd";
    private final String UPDATE_DEVICE_URI = "/ps/data/userOperMod/{number}";
    private final String DEL_DEVICE_URI = "/ps/data/userOperDel/{number}";
    private final String CALL_PAGE_URI = "/ps/call/page?size={size}&current={current}&number={number}&startTime={startTime}&endTime={endTime}";
    private final String FILE_LIST_URI = "/ps/file/list?number={number}&startTime={startTime}&endTime={endTime}";
    private final String GPS_LIST_URI = "/ucp/map/getMapHistoryQueryApi";
    private final String DEVICE_STATUS_URI = "/ps/data/userStatusInfoList";
    private final String ALL_SUB_NODE_URI = "/ps/data/nodeSubDataList/{nodeNumber}";
    private final String DEVICE_JOIN_GROUP_URI = "/ps/data/groupOperJoin/{number}";
    private final String DEVICE_LEAVE_GROUP_URI = "/ps/data/groupOperLeave/{number}";

    /**
     * 获取访问token
     *
     * @param userName
     *
     * @return
     */
    private String getToken(String userName) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String key = "PT:DISPATCH:" + tenant + ":" + userName;
        String token = (String) this.redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(token)) {
            token = dispatchLogin(userName, userName);
            this.redisTemplate.opsForValue().set(key, token, 120, TimeUnit.MINUTES);
        }
        return token;
    }

    /**
     * 初始化调度参数
     *
     * @return
     */
    private PTResponseDTO.Config initDispathPlatformConfig() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String key = "PT:DISPATCH:" + tenant;
        String paramMapStr = (String) this.redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(paramMapStr)) {
            DispatchConfig config = dispatchConfigService.getOne(Wrappers.<DispatchConfig>lambdaQuery()
                                                                         .eq(DispatchConfig::getTenant, tenant));
            Assert.isNotFound(config == null, "未配置调度台相关参数，接口调用失败！");
            //扩展配置处理
            String extendConfig = config.getExtendConfig();
            Map<String, String> extendMap = JsonUtils.deserialize(extendConfig, Map.class);
            PTResponseDTO.Config ptConfig = new PTResponseDTO.Config(config.getAccount(), config.getPassword(), config.getPlatformAddr(), extendMap.get("rootNode"));
            this.redisTemplate.opsForValue().set(key, JsonUtils.serialize(ptConfig), 120, TimeUnit.MINUTES);
            return ptConfig;
        } else {
            return JsonUtils.deserialize(paramMapStr, PTResponseDTO.Config.class);
        }
    }

    @Override
    public String dispatchLogin(String userName, String password) {
        //        例子: 使用帐号1001, 帐号密码为1001登录
        //        PTYT2021-12-02 00:00:0010011001
        //        生成的md5(32位小写)为e736d2cdd71857f3d2863ba92ccbd25d
        PTResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        String timeStamp = DateUtils.format(LocalDateTime.now());
        String encrypt = stringToMD5("PTYT" + timeStamp + userName + password);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("number", userName);
        map.add("timeStamp", timeStamp);
        map.add("encrypt", encrypt);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<PTResponseDTO.LoginResponse> response = restTemplate.postForEntity(
                config.getPlatformAddr() + LOGIN_URI, request, PTResponseDTO.LoginResponse.class);
        return response.getBody().getData().getToken();
    }

    @Override
    public DispatchGroupModel addGroup(DispatchGroupModel param) {
        PTResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("token", getToken(config.getAccount()));
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(param.getParentCode())) {
            param.setParentCode(config.getRootNode());
        }
        map.put("parentNumber", param.getParentCode());
        map.put("number", param.getCode());
        map.put("name", param.getName());
        map.put("type", param.getType());
        if (param.getType() == 1) {
            map.put("groupValidTime", param.getValidTime());//临时组  设置过期时间
        }
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<PTResponseDTO> response = restTemplate.postForEntity(
                config.getPlatformAddr() + ADD_GROUP_URI, request, PTResponseDTO.class);
        if (response.getBody().getCode() == 0) {
            return param;
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
        PTResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", getToken(config.getAccount()));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
        String url = config.getPlatformAddr() + DEL_GROUP_URI.replace("{number}", code);
        ResponseEntity<PTResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, request, PTResponseDTO.class);
        if (response.getBody().getCode() == 0 || "操作失败,组不存在".equals(response.getBody().getMsg())) {
            return true;
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    @Override
    public DispatchDeviceModel addDevice(DispatchDeviceModel param) {
        PTResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("token", getToken(config.getAccount()));
        if (StringUtils.isEmpty(param.getParentCode())) {
            param.setParentCode(config.getRootNode());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("parentNumber", param.getParentCode());
        map.put("number", param.getCode());
        map.put("name", param.getName());
        map.put("type", param.getType());
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<PTResponseDTO> response = restTemplate.postForEntity(
                config.getPlatformAddr() + ADD_DEVICE_URI, request, PTResponseDTO.class);
        if (response.getBody().getCode() == 0) {
            return param;
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    @Override
    public boolean updateDevice(DispatchDeviceModel param) {
        PTResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("token", getToken(config.getAccount()));
        Map<String, Object> map = new HashMap<>();
        map.put("name", param.getName());
        map.put("type", param.getType());
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<PTResponseDTO> response = restTemplate.postForEntity(
                config.getPlatformAddr() + UPDATE_DEVICE_URI, request, PTResponseDTO.class);
        if (response.getBody().getCode() == 0) {
            return true;
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    @Override
    public boolean delDevice(String code) {
        PTResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", getToken(config.getAccount()));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
        String url = config.getPlatformAddr() + DEL_DEVICE_URI.replace("{number}", code);
        ResponseEntity<PTResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, request, PTResponseDTO.class);
        if (response.getBody().getCode() == 0) {
            return true;
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    @Override
    public boolean addDeviceToGroup(String groupCode, List<String> codes) {
        PTResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("token", getToken(config.getAccount()));
        Map<String, Object> map = new HashMap<>();
        map.put("userList", codes);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        String url = config.getPlatformAddr() + DEVICE_JOIN_GROUP_URI.replace("{number}", groupCode);
        ResponseEntity<PTResponseDTO> response = restTemplate.postForEntity(url, request, PTResponseDTO.class);
        if (response.getBody().getCode() == 0) {
            return true;
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    @Override
    public boolean delDeviceFromGroup(String groupCode, List<String> codes) {
        PTResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("token", getToken(config.getAccount()));
        Map<String, Object> map = new HashMap<>();
        map.put("userList", codes);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        String url = config.getPlatformAddr() + DEVICE_LEAVE_GROUP_URI.replace("{number}", groupCode);
        ResponseEntity<PTResponseDTO> response = restTemplate.postForEntity(url, request, PTResponseDTO.class);
        if (response.getBody().getCode() == 0) {
            return true;
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    @Override
    public Pagination<DispatchCallRecordVO> callPage(DispatchCallQueryModel param) {
        PTResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", getToken(config.getAccount()));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
        Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("current", param.getPageNumber());
        urlParams.put("size", param.getPageSize());
        urlParams.put("number", param.getCode());
        urlParams.put("startTime", param.getStartTime());
        urlParams.put("endTime", param.getEndTime());
        String url = config.getPlatformAddr() + CALL_PAGE_URI;
        ResponseEntity<PTResponseDTO.CallPageResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, PTResponseDTO.CallPageResponse.class, urlParams);
        if (response.getBody().getCode() == 0) {
            PTResponseDTO.CallPage page = response.getBody().data;
            List<DispatchCallRecordVO> records = ObjectUtils.copying(page.getRecords(), DispatchCallRecordVO.class);
            return new Pagination(page.getTotal(), page.getPages(), records);
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    @Override
    public List<DispatchGPSVO> gpsHistory(DispatchGPSQueryModel param) {
        PTResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("token", getToken(config.getAccount()));
        Map<String, Object> map = new HashMap<>();
        map.put("number", param.getCode());
        map.put("time_begin", param.getStartTime());
        map.put("time_end", param.getEndTime());
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<PTResponseDTO.GPSResponse> response = restTemplate.postForEntity(
                config.getPlatformAddr() + GPS_LIST_URI, request, PTResponseDTO.GPSResponse.class);
        if (response.getBody().getErrorCode() == 0) {
            List<PTResponseDTO.GPSData> gpsDataList = JsonUtils.deserialize(response.getBody()
                                                                                    .getResultContent(), new TypeReference<List<PTResponseDTO.GPSData>>() {
            });
            if (!CollectionUtils.isEmpty(gpsDataList)) {
                List<DispatchGPSVO> dispatchGPSVOS = gpsDataList.stream().map(gps -> {
                    //转换经纬度 高德使用的是gcj02 我国颁布的坐标系
                    double[] doubles = CoordinateTransUtil.wgs84togcj02(gps.getLongiTude(), gps.getLatiTude());
                    return DispatchGPSVO.builder()
                            .code(gps.getDn())
                            .uploadTime(DateUtils.parse(gps.getStarttime()))
                            .lng(doubles[0])
                            .lat(doubles[1])
                            .direction(gps.getDirection())
                            .height(gps.getHeight())
                            .speed(gps.getSpeed())
                            .build();
                }).toList();
                List<DispatchGPSVO> dispatchGPSResponseVOS = new ArrayList<>();
                for (int i = 0; i < dispatchGPSVOS.size(); i++) {
                    if (i > 0) {
                        //过滤掉连续的重复坐标数据
                        if (dispatchGPSVOS.get(i).getLat() == dispatchGPSVOS.get(i - 1).getLat()
                            && dispatchGPSVOS.get(i).getLng() == dispatchGPSVOS.get(i - 1).getLng()) {
                            continue;
                        }
                    }
                    if (dispatchGPSVOS.get(i).getLat() > 1 && dispatchGPSVOS.get(i).getLng() > 1) {
                        dispatchGPSResponseVOS.add(dispatchGPSVOS.get(i));
                    }
                }
                return dispatchGPSResponseVOS;
            } else {
                return Collections.emptyList();
            }
        } else {
            throw new ServiceException(response.getBody().getErrorDesc());
        }
    }

    @Override
    public Pagination<DispatchGPSVO> gpsHistoryPage(DispatchGPSQueryModel param) {
        return Pagination.empty();
    }

    @Override
    public List<DispatchFileVO> communicationFile(DispatchFileQueryModel param) {
        PTResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", getToken(config.getAccount()));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
        Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("number", param.getCode());
        urlParams.put("startTime", param.getStartTime());
        urlParams.put("endTime", param.getEndTime());
        String url = config.getPlatformAddr() + FILE_LIST_URI;
        ResponseEntity<PTResponseDTO.FileListResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, PTResponseDTO.FileListResponse.class, urlParams);
        if (response.getBody().getCode() == 0) {
            List<PTResponseDTO.FileRecord> files = response.getBody().getData();
            if (CollectionUtils.isEmpty(files)) {
                return Collections.emptyList();
            } else {
                return files.stream().map(f ->
                                                  DispatchFileVO.builder()
                                                          .name(f.getDataFilename())
                                                          .time(f.getDataTime())
                                                          .size(f.getDataSize())
                                                          .length(f.getDataLength())
                                                          .type(f.getDataType())
                                                          .url(f.getUrl())
                                                          .suffix(f.getDataFilename().split("[.]")[1])
                                                          .uploadUserName(f.getName())
                                                          .build()
                                         ).collect(Collectors.toList());
            }
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    @Override
    public List<DispatchDeviceStatusVO> deviceStatus(List<String> codes) {
        PTResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("token", getToken(config.getAccount()));
        //查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("userList", codes.toArray());
        HttpEntity<String> request = new HttpEntity<>(JsonUtils.serialize(params), headers);

        String url = config.getPlatformAddr() + DEVICE_STATUS_URI;
        ResponseEntity<PTResponseDTO.DeviceStatusResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, PTResponseDTO.DeviceStatusResponse.class);
        if (response.getBody().getCode() == 0) {
            List<PTResponseDTO.DeviceStatus> datas = response.getBody().getData();
            if (!CollectionUtils.isEmpty(datas)) {
                return datas.stream().map(s -> {
                            double[] doubles = CoordinateTransUtil.wgs84togcj02(s.getLongitude(), s.getLatitude());
                            return DispatchDeviceStatusVO.builder()
                                    .code(s.getNumber())
                                    .lng(doubles[0])//经度
                                    .lat(doubles[1])//纬度
                                    .direction(s.getDirection())
                                    .speed(s.getSpeed())
                                    .uploadTime(DateUtils.parse(s.getTime()))
                                    .onlineStatus(s.isOnlineStatus())
                                    .videoStatus(s.isVideoStatus())
                                    .build();
                        })
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    @Override
    public List<DispatchNodeVO> allSubNode(String code) {
        PTResponseDTO.Config config = this.initDispathPlatformConfig();//初始化参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", getToken(config.getAccount()));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
        String url = config.getPlatformAddr() + ALL_SUB_NODE_URI.replace("{nodeNumber}", code);
        ResponseEntity<PTResponseDTO.NodeResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, PTResponseDTO.NodeResponse.class);
        if (response.getBody().getCode() == 0) {
            List<PTResponseDTO.Node> datas = response.getBody().getData();
            if (!CollectionUtils.isEmpty(datas)) {
                return datas.stream().map(d ->
                                                  DispatchNodeExtendVO.builder()
                                                          .code(d.getNumber())
                                                          .name(d.getName())
                                                          .type(d.getType())
                                                          .subType(d.getSubType())
                                                          .parentCodes(d.getParentList())//可能有多个父级，因为一个终端可能在多个组下
                                                          .childCodes(d.getChildList())
                                                          .build()
                                         ).collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } else {
            throw new ServiceException(response.getBody().getMsg());
        }
    }

    /**
     * 根据普天调度对接文档  type = 0的为节点
     * subType 0 实节点    1 虚拟节点
     *
     * @param code
     *
     * @return
     */
    @Override
    public List<DispatchNodeVO> allNode(String code) {
        List<DispatchNodeVO> nodes = this.allSubNode(code);
        //过滤出 所有的终端（用户）
        if (!CollectionUtils.isEmpty(nodes)) {
            return nodes.stream().filter(n -> n.getType().equals("1")).collect(Collectors.toList());
        }
        return Collections.emptyList();
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
        List<DispatchNodeVO> nodes = this.allSubNode(code);
        //过滤出 所有的终端（用户）
        if (!CollectionUtils.isEmpty(nodes)) {
            return nodes.stream().filter(n -> n.getType().equals("1")).collect(Collectors.toList());
        }
        return Collections.emptyList();
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
        List<DispatchNodeVO> nodes = this.allSubNode(code);
        //过滤出 所有的终端（用户）
        if (!CollectionUtils.isEmpty(nodes)) {
            return nodes.stream().filter(n -> n.getType().equals("2")).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public String getDeviceTypeName(String type) {
        return PTDeviceTypeEnum.valueOf("DT_" + type).getDesc();
    }

    @Override
    public String getGroupTypeName(String type) {
        return PTGroupTypeEnum.valueOf("GT_" + type).getDesc();
    }

    @Override
    public List<DispatchCommonTypeVO> getAllDeviceType() {
        return Arrays.stream(PTDeviceTypeEnum.values())
                .map(t -> DispatchCommonTypeVO.builder()
                        .code(t.name().replace("DT_", ""))
                        .name(t.getDesc())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<DispatchCommonTypeVO> getAllGroupType() {
        return Arrays.stream(PTGroupTypeEnum.values())
                .map(t -> DispatchCommonTypeVO.builder()
                        .code(t.name().replace("GT_", ""))
                        .name(t.getDesc())
                        .build())
                .collect(Collectors.toList());
    }

    //md5加密 32位小写
    public String stringToMD5(String plainText) {
        byte[] mdBytes = null;
        try {
            mdBytes = MessageDigest.getInstance("MD5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不存在！");
        }
        String mdCode = new BigInteger(1, mdBytes).toString(16);

        if (mdCode.length() < 32) {
            int a = 32 - mdCode.length();
            for (int i = 0; i < a; i++) {
                mdCode = "0" + mdCode;
            }
        }
        return mdCode;            // 默认返回32位小写
    }

}