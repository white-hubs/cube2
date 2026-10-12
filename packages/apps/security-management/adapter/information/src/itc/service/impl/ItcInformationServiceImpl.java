package com.guhecloud.cube.srv.gen.adapter.information.server.itc.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.adapter.information.server.itc.dto.ItcMaterialResponse;
import com.guhecloud.cube.srv.gen.adapter.information.server.itc.dto.ItcProgramPageResponse;
import com.guhecloud.cube.srv.gen.adapter.information.server.itc.dto.ItcResponse;
import com.guhecloud.cube.srv.gen.adapter.information.server.itc.dto.ItcTokenResponse;
import com.guhecloud.cube.srv.gen.adapter.information.server.itc.enums.ItcOnlineEnum;
import com.guhecloud.cube.srv.gen.adapter.information.server.itc.enums.ItcPlayTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.information.server.itc.enums.ItcPushTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.information.server.itc.qo.ItcRequestQo;
import com.guhecloud.cube.srv.gen.adapter.information.server.itc.qo.ItcSendProgramParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.itc.service.ItcInformationService;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.DeviceDTO;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.MaterialParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.ProgramScheduleParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.SendProgramToTerminalParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.MaterialVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.ProgramVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.SendProgramToTerminalVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.enums.YouZhiLedCronEnum;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItcInformationServiceImpl implements ItcInformationService {

    //账号登录接口
    private final String GET_APP_PASSWORD = "/ids/ocs/v2.php/core/getapppassword";

    //节⽬分⻚列表
    private final String RESOURCE_PAGE_LIST = "/ids/apps/diffusion/api/v1/resource_page_list";

    //节⽬详情
    private final String RESOURCE_DETAIL = "/ids/apps/diffusion/api/v1/resource_detail";

    //发送节⽬
    private final String STORE_PUSH = "/ids/apps/diffusion/api/v1/store_push";

    //素材详情
    private final String MATERIAL_DETAIL = "ids/apps/diffusion/api/v1/detail";

    private final SubsystemRedisUtils subsystemRedisUtils;

    /**
     * 节目列表(节目库)
     */
    @Override
    public List<ProgramVo> getProgramList(ProgramScheduleParam request) {
        log.info(">>>>>>>>>>>>itc获取节目列表(节目库)>>>>>>>>>");
        // 1. 获取基础参数
        ItcRequestQo itcRequestQo = this.getBasicConfig();

        // 2. 定义每页大小
        int pageSize = 1000;

        // 3. 尝试获取第一页的数据，以确定总页数
        ItcProgramPageResponse.DataObject firstPageData = this.getResourcePageList(itcRequestQo, 1, pageSize);
        if (firstPageData == null || firstPageData.getList() == null || firstPageData.getList().isEmpty()) {
            return Collections.emptyList(); // 如果第一页为空，直接返回空列表
        }

        // 4. 计算总页数
        int totalPages = (int) Math.ceil((double) firstPageData.getTotal() / pageSize);

        // 5. 使用Stream API批量获取所有页面的数据
        List<ItcProgramPageResponse.ItcProgramPageDTO> itcProgramPageDTOS = IntStream.rangeClosed(1, totalPages) // 创建从1到总页数的流
                .mapToObj(page -> getResourcePageList(itcRequestQo, page, pageSize)) // 获取每一页的数据
                .filter(Objects::nonNull) // 过滤掉可能的null值
                .flatMap(dataObject -> dataObject.getList()
                        .stream()) // 将每页的数据流扁平化为单个流
                .collect(Collectors.toList());// 收集结果
        return itcProgramPageDTOS.stream().map(itcProgramPageDTO -> ProgramVo.builder()
                .id(String.valueOf(itcProgramPageDTO.getId()))
                .name(itcProgramPageDTO.getTitle())
                .categoryName(
                        Objects.isNull(itcProgramPageDTO.getTitle())
                        ? ""
                        : itcProgramPageDTO
                                .getTitle())
                .build()).collect(Collectors.toList());
    }

    /**
     * 根据节目id集合批量获取节目对应的素材下载地址
     */
    @Override
    public List<ProgramVo> getMaterialsByProgramId(MaterialParam request) {
        log.info(">>>>>>>>>>>>itc获取素材下载地址>>>>>>>>>");
        //1.拼接参数
        ItcRequestQo itcRequestQo = this.getBasicConfig();
        return request.getProgramIds().stream().map(programId -> {
            ProgramVo programVo = new ProgramVo();
            ItcMaterialResponse.ItcProgramDTO ledResourceDetail = this.getResourceDetail(itcRequestQo, programId);
            if (Objects.nonNull(ledResourceDetail)) {
                programVo.setId(programId);
                programVo.setName(ledResourceDetail.getTitle());
                programVo.setCategoryName(Objects.isNull(ledResourceDetail.getGroup()) ? ""
                                                                                       : ledResourceDetail
                                                  .getGroup());
                //循环调用，获取素材列表（暂未提供根据素材id集合获取的API）
                if (StringUtils.isNotEmpty(ledResourceDetail.getMaterialIds())) {
                    programVo.setMaterials(Arrays.stream(ledResourceDetail.getMaterialIds().split(",")).map(materialId -> {
                        ItcMaterialResponse.ItcProgramDTO materialDetail = this.getMaterialDetail(itcRequestQo, materialId);
                        if (!Objects.isNull(materialDetail)) {
                            return MaterialVo.builder()
                                    .materialId(materialId)
                                    .materialName(materialDetail.getTitle())
                                    .address(materialDetail.getUrl())
                                    .build();
                        }
                        return null;
                    }).collect(Collectors.toList()));
                }
            }
            return programVo;
        }).collect(Collectors.toList());
    }

    /**
     * 发送节目到指定终端
     */
    @Override
    public List<SendProgramToTerminalVo> sendProgramToTerminal(SendProgramToTerminalParam param) {
        log.info(">>>>>>>>>>>>itc发送节目>>>>>>>>>");
        ItcRequestQo itcRequestQo = this.getBasicConfig();
        //1.封装节目的内容
        ItcSendProgramParam itcSendProgramParam = buildSendProgramToTerminalParam(param);
        //2.发送节目
        return param.getTerminals().stream().map(terminal -> {
            ItcResponse response = this.getStorePush(itcRequestQo, terminal.getTerminalName(), JSON.toJSONString(itcSendProgramParam));
            if (0 == response.getCode()) {
                return SendProgramToTerminalVo.builder().id(String.valueOf(IdWorker.getId()))
                        .terminalId(terminal.getTerminalName())
                        .sendResult("发送成功").sendStatus(0).build();
            } else {
                return SendProgramToTerminalVo.builder().id(String.valueOf(IdWorker.getId()))
                        .terminalId(terminal.getTerminalName())
                        .sendResult("发送失败").sendStatus(1).build();
            }
        }).collect(Collectors.toList());
    }

    /**
     * 获取基础配置
     *
     * @return 基础配置
     */
    private ItcRequestQo getBasicConfig() {
        String key = SubsystemTypeConstants.INFORMATION_SERVER + ":" + "itc" + ":" +
                     SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        return JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), ItcRequestQo.class);
    }

    private ItcSendProgramParam buildSendProgramToTerminalParam(SendProgramToTerminalParam param) {
        return ItcSendProgramParam.builder()
                .layoutResourceId(Integer.parseInt(param.getThirdProgramId()))
                .playType(ItcPlayTypeEnum.INSERT_PLAYBACK.getCode())
                .isOnline(ItcOnlineEnum.ONLINE.getCode())
                .pushType(ItcPushTypeEnum.PROGRAM.getCode())
                //多个逗号分隔
                .deviceIds(param.getTerminals().stream().map(DeviceDTO::getTerminalId)
                                   .collect(Collectors.joining(",")))
                .startDate(DateUtils.formatDate(param.getBeginDate()))
                .startTime(param.getBeginTime())
                .endDate(DateUtils.formatDate(param.getBeginDate()))
                .endTime(param.getEndTime())
                .weekDay(param.getExecutionCycle().stream()
                                 .map(week -> Objects.requireNonNull(YouZhiLedCronEnum.getByName(week))
                                         .getValue().toString())
                                 .collect(Collectors.joining(",")))
                .build();
    }

    /**
     * 获取token信息
     */
    private String getToken() {
        ItcRequestQo itcRequestQo = this.getBasicConfig();
        String url = itcRequestQo.getHttpFlag() + "://" + itcRequestQo.getUrl() + GET_APP_PASSWORD;
        String body = HttpUtil.createPost(url)
                .header("OCS-APIREQUEST", "true")
                .header("Accept", "application/json")
                .execute()
                .body();
        ItcTokenResponse itcTokenResponse = JSON.parseObject(body, ItcTokenResponse.class);
        if (itcTokenResponse.getOcs().getMeta().getStatuscode() != 200) {
            log.error("获取token失败, response:{}", body);
            return null;
        }
        Map<String, String> data = (Map) itcTokenResponse.getOcs().getData();
        return data.get("apppassword");
    }

    /**
     * 获取单个节目信息
     *
     * @param itcRequestQo itc信息发布配置文件
     * @param programId    节目id
     */
    public ItcMaterialResponse.ItcProgramDTO getResourceDetail(ItcRequestQo itcRequestQo, String programId) {
        String url = itcRequestQo.getHttpFlag() + "://" + itcRequestQo.getUrl() + RESOURCE_DETAIL;
        Map<String, Object> params = new HashMap<>();
        params.put("id", programId);
        //2.获取节目详情中的图片url
        String body = HttpUtil.createPost(url)
                .header("Authorization", getToken())
                .body(JSON.toJSONString(params))
                .execute()
                .body();
        ItcMaterialResponse itcMaterialResponse = JSON.parseObject(body, ItcMaterialResponse.class);
        log.info("itc信息发布获取单个节目信息 返回：{}", itcMaterialResponse);
        //3.处理返回请求
        if (!"0".equals(itcMaterialResponse.getCode())) {
            log.error("itc信息发布获取单个节目信息失败, response:{}", body);
            return null;
        } else {
            return itcMaterialResponse.getData();
        }
    }

    /**
     * 获取节目列表分页
     *
     * @param itcRequestQo itc信息发布配置文件
     */
    public ItcProgramPageResponse.DataObject getResourcePageList(ItcRequestQo itcRequestQo, Integer page, Integer pageSize) {
        String url = itcRequestQo.getHttpFlag() + "://" + itcRequestQo.getUrl() + RESOURCE_PAGE_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("pageSize", pageSize);
        //2.获取节目详情中的图片url
        String body = HttpUtil.createPost(url)
                .header("Authorization", getToken())
                .body(JSON.toJSONString(params))
                .execute()
                .body();
        ItcProgramPageResponse itcProgramPageResponse = JSON.parseObject(body, ItcProgramPageResponse.class);
        log.info("itc信息发布节目列表分页 返回：{}", itcProgramPageResponse);
        //3.处理返回请求
        if (!"0".equals(itcProgramPageResponse.getCode())) {
            log.error("itc信息发布获取节目列表分页失败, response:{}", body);
            return null;
        } else {
            return itcProgramPageResponse.getData();
        }
    }

    /**
     * 发送节目
     *
     * @param itcRequestQo itc信息发布配置文件
     * @param ledName      led设备名称
     * @param paramJson    入参body
     */
    public ItcResponse getStorePush(ItcRequestQo itcRequestQo, String ledName, String paramJson) {
        String url = itcRequestQo.getHttpFlag() + "://" + itcRequestQo.getUrl() + "/" + ledName + STORE_PUSH;
        //2.获取节目详情中的图片url
        String body = HttpUtil.createPost(url)
                .header("Cookie", getToken())
                .body(paramJson)
                .execute()
                .body();
        log.info("itc信息发布发送节目 返回：{}", body);
        return JSON.parseObject(body, ItcResponse.class);
    }

    /**
     * 获取单个素材详情
     *
     * @param itcRequestQo itc信息发布配置文件
     * @param materialId   素材id
     */
    public ItcMaterialResponse.ItcProgramDTO getMaterialDetail(ItcRequestQo itcRequestQo, String materialId) {
        String url = itcRequestQo.getHttpFlag() + "://" + itcRequestQo.getUrl() + MATERIAL_DETAIL + "/" + materialId;
        //2.获取节目详情中的图片url
        String body = HttpUtil.createGet(url)
                .header("Authorization", getToken())
                .execute()
                .body();
        ItcMaterialResponse itcMaterialResponse = JSON.parseObject(body, ItcMaterialResponse.class);
        log.info("itc信息发布素材详情 返回：{}", itcMaterialResponse);
        //3.处理返回请求
        if (!"0".equals(itcMaterialResponse.getCode())) {
            log.error("获取itc信息发布素材详情失败, response:{}", body);
            return null;
        } else {
            return itcMaterialResponse.getData();
        }
    }
}
