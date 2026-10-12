package com.guhecloud.cube.base.message2.server.sat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceive;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceiveChannel;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceiveUser;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgSceneReceiveModifyParam;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgSceneReceiveQueryParam;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgSceneReceiveChannelService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgSceneReceiveService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgSceneReceiveUserService;
import com.guhecloud.cube.base.message2.server.sat.vo.SatMsgSceneReceivePageVO;
import com.guhecloud.cube.base.message2.server.sat.vo.SatMsgSceneReceiveUserVO;
import com.guhecloud.cube.base.message2.server.sat.vo.SatMsgSceneReceiveVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 消息场景接收配置管理
 *
 * @author lanchun
 * @date 2023/6/27 9:53
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatMsgSceneReceiveController {

    private final SatMsgSceneReceiveService satMsgSceneReceiveService;

    private final SatMsgSceneReceiveUserService satMsgSceneReceiveUserService;

    private final SatMsgSceneReceiveChannelService satMsgSceneReceiveChannelService;

    /**
     * 刷新应用
     */
    @PostMapping("/sat-msg-scene/refresh-application")
    public void refreshApplication() {
        this.satMsgSceneReceiveService.refreshApplication();
    }

    /**
     * 消息场景接收配置列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/sat-msg-scene-receives")
    public Pagination<SatMsgSceneReceivePageVO> getSatMsgSceneReceivePage(@Valid SatMsgSceneReceiveQueryParam param) {
        Page<SatMsgSceneReceive> msgSceneReceivePage = this.satMsgSceneReceiveService.getSatMsgSceneReceivePage(param);
        if (CollectionUtils.isEmpty(msgSceneReceivePage.getRecords())) {
            return Pagination.empty();
        }
        List<SatMsgSceneReceivePageVO> satMsgSceneReceivePageVOS = ObjectUtils.copying(
                msgSceneReceivePage.getRecords(),
                SatMsgSceneReceivePageVO.class);
        Set<Long> sceneReceiveIds = satMsgSceneReceivePageVOS.stream().map(SatMsgSceneReceivePageVO::getId)
                .collect(Collectors.toSet());
        //场景接收通道配置
        Map<Long, List<SatMsgSceneReceiveChannel>> satMsgSceneReceiveChannelMap = this.satMsgSceneReceiveChannelService
                .getChannelsBySceneReceiveIds(sceneReceiveIds).stream()
                .collect(Collectors.groupingBy(SatMsgSceneReceiveChannel::getSatMsgSceneReceiveId));
        satMsgSceneReceivePageVOS.stream().forEach(satMsgSceneReceivePageVO -> {
            List<SatMsgSceneReceiveChannel> satMsgSceneReceiveChannels = satMsgSceneReceiveChannelMap.get(satMsgSceneReceivePageVO.getId());
            if (!CollectionUtils.isEmpty(satMsgSceneReceiveChannels)) {
                satMsgSceneReceivePageVO.setChannelCodes(satMsgSceneReceiveChannels.stream()
                                                                 .map(SatMsgSceneReceiveChannel::getChannelCode)
                                                                 .collect(Collectors.toList()));
            }
        });
        return PagingUtils.convert(msgSceneReceivePage, satMsgSceneReceivePageVOS);
    }

    /**
     * 消息场景接收配置详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/sat-msg-scene-receive/{id}")
    public SatMsgSceneReceiveVO getSatMsgSceneReceive(@PathVariable("id") Long id) {
        SatMsgSceneReceive satMsgSceneReceive = this.satMsgSceneReceiveService.getById(id);
        SatMsgSceneReceiveVO satMsgSceneReceiveVO = getSatMsgSceneReceiveVO(satMsgSceneReceive);
        return satMsgSceneReceiveVO;
    }

    /**
     * 消息场景接收配置详情
     *
     * @param code 场景编号
     *
     * @return
     */
    @GetMapping("/sat-msg-scene-receive/code/{code}")
    public SatMsgSceneReceiveVO getSatMsgSceneReceiveByCode(@PathVariable("code") String code) {
        SatMsgSceneReceive satMsgSceneReceive = this.satMsgSceneReceiveService.getSatMsgSceneReceiveByCode(code);
        SatMsgSceneReceiveVO satMsgSceneReceiveVO = getSatMsgSceneReceiveVO(satMsgSceneReceive);
        return satMsgSceneReceiveVO;
    }

    /**
     * 消息场景接收模式设置
     *
     * @param id
     * @param param
     */
    @PutMapping("/sat-msg-scene-receive/{id}/setting-mode")
    public void settingReceiveMode(@PathVariable("id") Long id, @Valid @RequestBody SatMsgSceneReceiveModifyParam param) {
        this.satMsgSceneReceiveService.settingReceiveMode(id, param);
    }

    /**
     * 消息场景接收通道设置
     *
     * @param id
     * @param channelCode
     */
    @PutMapping("/sat-msg-scene-receive/{id}/setting-channel/{channelCode}")
    public void settingReceiveChannel(@PathVariable("id") Long id, @PathVariable("channelCode") ChannelEnum channelCode) {
        this.satMsgSceneReceiveService.settingReceiveChannel(id, channelCode);
    }

    /**
     * 消息场景接收通道删除
     *
     * @param id
     * @param channelCode
     */
    @DeleteMapping("/sat-msg-scene-receive/{id}/setting-channel/{channelCode}")
    public void removeReceiveChannel(@PathVariable("id") Long id, @PathVariable("channelCode") ChannelEnum channelCode) {
        this.satMsgSceneReceiveService.removeReceiveChannel(id, channelCode);
    }

    private SatMsgSceneReceiveVO getSatMsgSceneReceiveVO(SatMsgSceneReceive satMsgSceneReceive) {
        Assert.isNotFound(satMsgSceneReceive, "场景不存在");
        SatMsgSceneReceiveVO satMsgSceneReceiveVO = ObjectUtils.copying(satMsgSceneReceive, SatMsgSceneReceiveVO.class);
        //发送通道信息
        List<ChannelEnum> channelCodes = this.satMsgSceneReceiveChannelService
                .getChannelsBySceneReceiveIds(Collections.singleton(satMsgSceneReceive.getId()))
                .stream()
                .map(SatMsgSceneReceiveChannel::getChannelCode)
                .collect(Collectors.toList());
        satMsgSceneReceiveVO.setChannelCodes(channelCodes);
        //接收人信息
        List<SatMsgSceneReceiveUser> users = this.satMsgSceneReceiveUserService
                .getUsersBySatMsgSceneReceiveIds(Collections.singleton(satMsgSceneReceive.getId()));
        satMsgSceneReceiveVO.setUsers(ObjectUtils.copying(users, SatMsgSceneReceiveUserVO.class));
        return satMsgSceneReceiveVO;
    }
}
