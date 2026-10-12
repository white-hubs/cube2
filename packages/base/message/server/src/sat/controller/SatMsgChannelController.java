package com.guhecloud.cube.base.message2.server.sat.controller;

import com.guhecloud.cube.base.message2.server.base.entity.MsgChannel;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.service.MsgChannelService;
import com.guhecloud.cube.base.message2.server.initialization.rocketmq.message.MsgChannelModifyMessage;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgChannel;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgManufacturer;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgChannelModifyParam;
import com.guhecloud.cube.base.message2.server.sat.param.validator.SatMsgChannelManufacturerValidator;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgChannelService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgManufacturerService;
import com.guhecloud.cube.base.message2.server.sat.vo.SatMsgChannelVO;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 租户通道厂家配置管理接口
 *
 * @author lanchun
 * @date 2023/3/7 18:28
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatMsgChannelController {

    private final SatMsgChannelService satMsgChannelService;

    private final MsgChannelService msgChannelService;

    private final SatMsgManufacturerService satMsgManufacturerService;

    private final SatMsgChannelManufacturerValidator manufacturerValidator;

    /**
     * 租户通道厂家配置新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-msg-channel")
    public void save(@Valid @RequestBody SatMsgChannelModifyParam param) {
        SatMsgChannel satMsgChannel = this.satMsgChannelService.getByChannelCode(param.getChannelCode());
        if (Objects.nonNull(satMsgChannel)) {
            //前置校验是否更改了厂家配置
            SatMsgManufacturer satMsgManufacturer = this.satMsgManufacturerService.getById(satMsgChannel.getSatMsgManufacturerId());
            boolean updateManufacturer = this.manufacturerValidator.verifyChannelManufacturerUpdate(satMsgManufacturer, param);
            if (updateManufacturer) {
                this.satMsgChannelService.updateManufacturer(satMsgChannel, param);
                //发送消息,修改厂家成功后-重新初始化模板
                RocketMQContextHolder.send(MsgChannelModifyMessage.TOPIC, MsgChannelModifyMessage.builder()
                        .channelCode(param.getChannelCode())
                        .manufacturerCode(param.getManufacturerCode().name())
                        .config(param.getConfig())
                        .build());
            } else {
                this.satMsgChannelService.update(satMsgChannel, param);
            }
        } else {
            this.satMsgChannelService.save(param);
            //发送消息,新增成功后-初始化模板
            RocketMQContextHolder.send(MsgChannelModifyMessage.TOPIC, MsgChannelModifyMessage.builder()
                    .channelCode(param.getChannelCode())
                    .manufacturerCode(param.getManufacturerCode().name())
                    .config(param.getConfig())
                    .build());
        }
    }

    /**
     * 租户通道厂家配置状态修改
     *
     * @param id
     * @param enable
     */
    @PutMapping("/sat-msg-channel/{id}/enable/{enable}")
    public void updateEnable(@PathVariable("id") Long id, @PathVariable("enable") Boolean enable) {
        this.satMsgChannelService.updateEnabled(id, enable);
    }

    /**
     * 租户通道厂家列表
     *
     * @return
     */
    @GetMapping("/sat-msg-channels")
    public List<SatMsgChannelVO> getSatMsgChannels() {
        //系统支持的通道列表
        List<MsgChannel> msgChannels = this.msgChannelService.list();
        if (CollectionUtils.isEmpty(msgChannels)) {
            return Collections.emptyList();
        }
        List<SatMsgChannelVO> satMsgChannelVOS = new ArrayList<>(msgChannels.size());
        //租户通道配置
        List<SatMsgChannel> satMsgChannels = this.satMsgChannelService.listSatMsgChannels();
        //租户通道关联的厂家配置
        Set<Long> satMsgManufacturerIds = satMsgChannels.stream().map(satMsgChannel -> satMsgChannel.getSatMsgManufacturerId())
                .collect(Collectors.toSet());
        Map<ChannelEnum, SatMsgManufacturer> satMsgManufacturerMap = this.satMsgManufacturerService
                .listSatMsgManufacturerByIds(satMsgManufacturerIds).stream().collect(
                        Collectors.toMap(SatMsgManufacturer::getChannelCode, obj -> obj, (o, n) -> n));
        msgChannels.stream().forEach(msgChannel -> {
            ChannelEnum channelCode = ChannelEnum.valueOf(msgChannel.getCode());
            SatMsgManufacturer satMsgManufacturer = satMsgManufacturerMap.get(channelCode);
            SatMsgChannelVO satMsgChannelVO = new SatMsgChannelVO();
            satMsgChannelVO.setChannelCode(channelCode);
            satMsgChannelVO.setChannelName(channelCode.getDescription());
            if (Objects.nonNull(satMsgManufacturer)) {
                satMsgChannelVO.setManufacturerCode(satMsgManufacturer.getManufacturerCode());
                satMsgChannelVO.setManufacturerName(satMsgManufacturer.getManufacturerCode().getDescription());
                satMsgChannelVO.setConfig(satMsgManufacturer.getConfig());
            }
            satMsgChannelVOS.add(satMsgChannelVO);
        });
        return satMsgChannelVOS;
    }
}
