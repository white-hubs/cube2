package com.guhecloud.cube.srv.gen.conferencemanage.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.RoomScreenConfigMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.Conference;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.RoomScreenConfig;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.DisplayStatusEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.RoomScreenConfigParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.RoomScreenConfigService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.service.DeviceProgramSendService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.vo.RoomScreenAdapterConfig;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.RoomScreenConfigVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @description 会议屏配置默认实现
 * @createDate 2024-09-10 15:41:00
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomScreenConfigServiceImpl extends ServiceImpl<RoomScreenConfigMapper, RoomScreenConfig>
        implements RoomScreenConfigService {
    private final static String MEETING_SCREEN = "meetingScreenServer:southWing";
    private final ConferenceService conferenceService;
    private final List<DeviceProgramSendService> deviceProgramSendServiceList;
    private final DataDictionaryAdapter dataDictionaryAdapter;

    @Override
    public List<RoomScreenConfigVO> roomScreenConfigList(String keyword) {
        List<Long> conferenceIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(keyword)) {
            List<Conference> list = conferenceService.lambdaQuery().select(Conference::getId)
                    .like(Conference::getName, keyword)
                    .list();
            if (CollectionUtils.isEmpty(list)) {
                return new ArrayList<>();
            }
            conferenceIdList = list.stream().map(Conference::getId).collect(Collectors.toList());
        }
        List<RoomScreenConfig> roomScreenConfigs = this.lambdaQuery()
                .in(CollectionUtils.isNotEmpty(conferenceIdList), RoomScreenConfig::getConferenceId, conferenceIdList)
                .list();
        if (CollectionUtils.isEmpty(roomScreenConfigs)) {
            return new ArrayList<>();
        }
        Map<Long, String> conferenceMap = conferenceService.lambdaQuery()
                .in(Conference::getId, roomScreenConfigs.stream().map(RoomScreenConfig::getConferenceId).collect(Collectors.toList()))
                .list().stream().collect(Collectors.toMap(Conference::getId, Conference::getName));
        return roomScreenConfigs.stream().map(roomScreenConfig -> {
                                                  RoomScreenConfigVO copying = ObjectUtils.copying(roomScreenConfig, RoomScreenConfigVO.class);
                                                  copying.setConferenceName(conferenceMap.get(roomScreenConfig.getConferenceId()));
                                                  return copying;
                                              }
                                             ).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoomScreenConfig(RoomScreenConfigParam roomScreenConfigParam) {
        List<RoomScreenConfig> roomScreenConfigs = this.lambdaQuery()
                .eq(RoomScreenConfig::getDeviceCode, roomScreenConfigParam.getDeviceCode()).list();
        Assert.isConflict(CollectionUtils.isNotEmpty(roomScreenConfigs), "该设备已绑定会议室请重新选择");
        RoomScreenConfig roomScreenConfig = ObjectUtils.copying(roomScreenConfigParam, RoomScreenConfig.class);
        roomScreenConfig.setDisplayStatus(DisplayStatusEnum.CLOSE.name());
        //获取厂家配置
        DataDictionaryaramByItemAndCodeParam dictionaryaram = new DataDictionaryaramByItemAndCodeParam();
        dictionaryaram.setItemCode("SUBSYSTEM_ADAPTER_CONFIG");
        dictionaryaram.setCode(MEETING_SCREEN);
        DataDictionaryDTO itemAndCode = dataDictionaryAdapter.getDataDictionaryByItemAndCode(dictionaryaram);
        if (StringUtils.isBlank(roomScreenConfigParam.getProgramUrl())
            && Objects.nonNull(itemAndCode) && StringUtils.isNotBlank(itemAndCode.getVariable())) {
            RoomScreenAdapterConfig roomScreenAdapterConfig = JsonUtils.deserialize(itemAndCode.getVariable(), RoomScreenAdapterConfig.class);
            if (Objects.nonNull(roomScreenAdapterConfig)) {
                roomScreenConfig.setManufacturerCode(roomScreenAdapterConfig.getManufacturer());
                roomScreenConfig.setProgramUrl(roomScreenAdapterConfig.getServiceAddr());
                roomScreenConfig.setAuthInfo(Objects.nonNull(roomScreenAdapterConfig.getManufacturerJson())
                                             ? roomScreenAdapterConfig.getManufacturerJson().getAuthInfo() : null);
            }
        }
        this.save(roomScreenConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void openScreenShow(RoomScreenConfigParam configParam, HttpServletRequest httpServletRequest) {
        //更新状态
        RoomScreenConfig screenConfig = this.getById(configParam.getId());
        screenConfig.setDisplayStatus(configParam.getDisplayStatus().name());
        Optional<DeviceProgramSendService> sendServiceOptional = deviceProgramSendServiceList.stream()
                .filter(t -> t.support(screenConfig.getManufacturerCode())).findFirst();
        if (sendServiceOptional.isEmpty()) {
            throw new ServiceException("未找到具体实现厂家");
        }
        DeviceProgramSendService deviceProgramSendService = sendServiceOptional.get();
        if (DisplayStatusEnum.OPEN.equals(configParam.getDisplayStatus())) {
            // 调用第三方接口发布节目
            try {
                deviceProgramSendService.newProgram(screenConfig, httpServletRequest);
            } catch (Exception exception) {
                log.error("调用第三方接口发布节目失败原因：{}", exception);
                throw new ServiceException("调用第三方接口发布节目失败");
            }
        } else if (DisplayStatusEnum.CLOSE.equals(configParam.getDisplayStatus())) {
            // 调用第三方接口取消节目
            try {
                deviceProgramSendService.clearProgram(screenConfig);
            } catch (Exception exception) {
                log.error("调用第三方接口取消节目失败原因：", exception);
                throw new ServiceException("调用第三方接口取消节目失败");
            }
        }
        this.updateById(screenConfig);

    }
}




