package com.guhecloud.cube.base.message2.server.base.controller;

import com.guhecloud.cube.base.message2.server.base.entity.MsgChannel;
import com.guhecloud.cube.base.message2.server.base.entity.MsgChannelManufacturer;
import com.guhecloud.cube.base.message2.server.base.entity.MsgManufacturer;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.base.param.MsgChannelManufacturerModifyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgChannelModifyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgChannelQueryParam;
import com.guhecloud.cube.base.message2.server.base.service.MsgChannelManufacturerService;
import com.guhecloud.cube.base.message2.server.base.service.MsgChannelService;
import com.guhecloud.cube.base.message2.server.base.service.MsgManufacturerService;
import com.guhecloud.cube.base.message2.server.base.vo.MsgChannelVO;
import com.guhecloud.cube.base.message2.server.base.vo.MsgManufacturerVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 消息通道接口
 *
 * @author lanchun
 * @date 2021/11/10 13:56
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgChannelController {

    private final MsgChannelService msgChannelService;

    private final MsgChannelManufacturerService msgChannelManufacturerService;

    private final MsgManufacturerService msgManufacturerService;

    /**
     * 消息通道新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/msg-channel")
    public Long save(@Valid @RequestBody MsgChannelModifyParam param) {
        return msgChannelService.save(param);
    }

    /**
     * 消息通道详情
     *
     * @param code
     *
     * @return
     */
    @GetMapping("/msg-channel/{code}")
    public MsgChannelVO getMsgChannel(@PathVariable("code") String code) {
        MsgChannel msgChannel = msgChannelService.getByCode(code);
        Assert.isNotFound(msgChannel, "消息通道不存在");
        return ObjectUtils.copying(msgChannel, MsgChannelVO.class);
    }

    /**
     * 消息通道列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/msg-channels")
    public List<MsgChannelVO> listMsgChannels(MsgChannelQueryParam param) {
        return ObjectUtils.copying(this.msgChannelService.listMsgChannels(param), MsgChannelVO.class);
    }

    /**
     * 消息通道删除
     *
     * @param code
     */
    @DeleteMapping("/msg-channel/{code}")
    public void remove(@PathVariable("code") String code) {
        msgChannelService.remove(code);
    }

    /**
     * 通道厂家关联
     *
     * @param code
     * @param param
     */
    @PostMapping("/msg-channel/{code}/refer-manufacturer")
    public void saveReferManufacturer(@PathVariable("code") ChannelEnum code, @RequestBody MsgChannelManufacturerModifyParam param) {
        this.msgChannelManufacturerService.save(code, param);
    }

    /**
     * 通道厂家解除关联
     *
     * @param code
     * @param manufacturerCode
     */
    @DeleteMapping("/msg-channel/{code}/refer-manufacturer")
    public void removeReferManufacturer(@PathVariable("code") ChannelEnum code, @RequestParam ManufacturerCodeEnum manufacturerCode) {
        this.msgChannelManufacturerService.remove(code, manufacturerCode);
    }

    /**
     * 通道下已接入的厂家列表
     *
     * @param code
     *
     * @return
     */
    @GetMapping("/msg-channel/{code}/refer-manufacturers")
    public List<MsgManufacturerVO> getMsgChannelManufacturers(@PathVariable("code") ChannelEnum code) {
        List<MsgChannelManufacturer> msgChannelManufacturers = this.msgChannelManufacturerService.listMsgChannelManufacturersByCode(code);
        Set<ManufacturerCodeEnum> manufacturerCodes = msgChannelManufacturers.stream().map(MsgChannelManufacturer::getManufacturerCode)
                .collect(Collectors.toSet());
        List<MsgManufacturer> msgManufacturers = this.msgManufacturerService.listMsgManufacturersByCodes(manufacturerCodes);
        List<MsgManufacturerVO> msgManufacturerVOS = ObjectUtils.copying(msgManufacturers, MsgManufacturerVO.class);
        msgManufacturerVOS.stream().forEach(msgManufacturer -> {
            Optional<MsgChannelManufacturer> msgChannelManufacturerOptional = msgChannelManufacturers.stream()
                    .filter(msgChannelManufacturer -> msgManufacturer.getCode()
                            .equals(msgChannelManufacturer.getManufacturerCode().name()))
                    .findFirst();
            msgChannelManufacturerOptional.ifPresent(msgChannelManufacturer -> {
                msgManufacturer.setAccountRule(msgChannelManufacturer.getAccountRule());
            });
        });
        return msgManufacturerVOS;
    }
}
