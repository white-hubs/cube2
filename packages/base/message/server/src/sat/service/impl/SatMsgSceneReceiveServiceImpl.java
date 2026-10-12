package com.guhecloud.cube.base.message2.server.sat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.server.base.entity.MsgScene;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.service.MsgSceneService;
import com.guhecloud.cube.base.message2.server.sat.dao.SatMsgSceneReceiveMapper;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgChannel;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceive;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceiveChannel;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceiveUser;
import com.guhecloud.cube.base.message2.server.sat.enumeration.SatMsgSceneReceiveModeEnum;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgSceneReceiveModifyParam;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgSceneReceiveQueryParam;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgChannelService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgSceneReceiveChannelService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgSceneReceiveService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgSceneReceiveUserService;
import com.guhecloud.cube.base.message2.server.sat.vo.SatMsgApplicationVO;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatMsgSceneReceiveServiceImpl extends ServiceImpl<SatMsgSceneReceiveMapper, SatMsgSceneReceive> implements SatMsgSceneReceiveService {

    private final MsgSceneService msgSceneService;

    private final SatMsgChannelService satMsgChannelService;

    private final SatMsgSceneReceiveChannelService satMsgSceneReceiveChannelService;

    private final SatMsgSceneReceiveUserService satMsgSceneReceiveUserService;

    private final ApplicationClient applicationClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshApplication() {
        List<ApplicationDTO> tenantApplications = this.applicationClient.getTenantApplications(SessionContextHolder.getRequester()
                                                                                                       .getTenant());
        if (CollectionUtils.isEmpty(tenantApplications)) {
            return;
        }
        Set<String> applicationCodes = tenantApplications.stream().map(ApplicationDTO::getCode).collect(Collectors.toSet());
        List<MsgScene> applicationMsgScenes = this.msgSceneService.listMsgScenesByApplicationCodes(applicationCodes);
        List<SatMsgSceneReceive> addMsgScene = new ArrayList<>();
        List<SatMsgSceneReceiveChannel> msgSceneReceiveChannels = new ArrayList<>();
        List<SatMsgChannel> satMsgChannels = satMsgChannelService.listSatMsgChannels();
        applicationMsgScenes.forEach(msgScene -> {
            SatMsgSceneReceive satMsgSceneReceive = this.baseMapper.selectOne(new LambdaQueryWrapper<>(SatMsgSceneReceive.class)
                                                                                      .eq(SatMsgSceneReceive::getMsgSceneCode, msgScene.getCode()));
            if (ObjectUtils.isNotEmpty(satMsgSceneReceive)) {
                satMsgSceneReceive.setMsgSceneCode(msgScene.getCode());
                satMsgSceneReceive.setMsgSceneName(msgScene.getName());
                satMsgSceneReceive.setApplicationCode(msgScene.getApplicationCode());
                satMsgSceneReceive.setApplicationName(msgScene.getApplicationName());
                this.updateById(satMsgSceneReceive);
            } else {
                Long id = IdWorker.getId();
                satMsgSceneReceive = SatMsgSceneReceive.builder()
                        .id(id)
                        .msgSceneCode(msgScene.getCode())
                        .msgSceneName(msgScene.getName())
                        .applicationCode(msgScene.getApplicationCode())
                        .applicationName(msgScene.getApplicationName())
                        .tenant(SessionContextHolder.getRequester().getTenant())
                        .createBy(SecurityContextHolder.getPrincipal().getAccount())
                        .createTime(DateUtils.now())
                        .updateBy(SecurityContextHolder.getPrincipal().getAccount())
                        .updateTime(DateUtils.now())
                        .build();
                addMsgScene.add(satMsgSceneReceive);
                //初始化全选勾选通道
                satMsgChannels.forEach(channel -> {
                    SatMsgSceneReceiveChannel satMsgSceneReceiveChannel = new SatMsgSceneReceiveChannel();
                    satMsgSceneReceiveChannel.setSatMsgSceneReceiveId(id);
                    satMsgSceneReceiveChannel.setChannelCode(channel.getChannelCode());
                    msgSceneReceiveChannels.add(satMsgSceneReceiveChannel);
                });
            }
        });
        if (!CollectionUtils.isEmpty(addMsgScene)) {
            this.baseMapper.insertBatch(addMsgScene);
        }
        if (!CollectionUtils.isEmpty(msgSceneReceiveChannels)) {
            this.satMsgSceneReceiveChannelService.saveBatch(msgSceneReceiveChannels);
        }
    }

    /**
     * 场景设置接收方式
     *
     * @param id
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settingReceiveMode(@NonNull Long id, @NonNull SatMsgSceneReceiveModifyParam param) {
        SatMsgSceneReceive satMsgSceneReceive = this.baseMapper.selectById(id);
        Assert.isNotFound(satMsgSceneReceive, "数据未初始化,无法配置");
        satMsgSceneReceive.setReceiveMode(param.getReceiveMode());
        this.satMsgSceneReceiveUserService.remove(Wrappers.<SatMsgSceneReceiveUser>lambdaQuery()
                                                          .eq(SatMsgSceneReceiveUser::getSatMsgSceneReceiveId, id));
        //指定用户保存
        if (SatMsgSceneReceiveModeEnum.SPECIFY_USER == satMsgSceneReceive.getReceiveMode() &&
            !CollectionUtils.isEmpty(param.getUsers())) {
            List<SatMsgSceneReceiveUser> satMsgSceneReceiveUsers = new ArrayList<>(param.getUsers().size());
            param.getUsers().stream().forEach(user -> {
                SatMsgSceneReceiveUser satMsgSceneReceiveUser = ObjectUtils.copying(user, SatMsgSceneReceiveUser.class);
                satMsgSceneReceiveUser.setSatMsgSceneReceiveId(id);
                satMsgSceneReceiveUsers.add(satMsgSceneReceiveUser);
            });
            this.satMsgSceneReceiveUserService.saveBatch(satMsgSceneReceiveUsers);
        }
        this.baseMapper.updateById(satMsgSceneReceive);
    }

    /**
     * 场景设置发送通道
     *
     * @param id
     * @param channelCode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settingReceiveChannel(@NonNull Long id, @NonNull ChannelEnum channelCode) {
        SatMsgSceneReceive satMsgSceneReceive = this.baseMapper.selectById(id);
        Assert.isNotFound(satMsgSceneReceive, "数据未初始化,无法配置");
        this.satMsgSceneReceiveChannelService.remove(Wrappers.<SatMsgSceneReceiveChannel>lambdaQuery()
                                                             .eq(SatMsgSceneReceiveChannel::getSatMsgSceneReceiveId, id)
                                                             .eq(SatMsgSceneReceiveChannel::getChannelCode, channelCode)
                                                    );
        SatMsgSceneReceiveChannel satMsgSceneReceiveChannel = new SatMsgSceneReceiveChannel();
        satMsgSceneReceiveChannel.setSatMsgSceneReceiveId(id);
        satMsgSceneReceiveChannel.setChannelCode(channelCode);
        this.satMsgSceneReceiveChannelService.save(satMsgSceneReceiveChannel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeReceiveChannel(@NonNull Long id, @NonNull ChannelEnum channelCode) {
        this.satMsgSceneReceiveChannelService.remove(Wrappers.<SatMsgSceneReceiveChannel>lambdaQuery()
                                                             .eq(SatMsgSceneReceiveChannel::getSatMsgSceneReceiveId, id)
                                                             .eq(SatMsgSceneReceiveChannel::getChannelCode, channelCode)
                                                    );
    }

    @Override
    public SatMsgSceneReceive getSatMsgSceneReceiveByCode(@NonNull String code) {
        return this.baseMapper.selectOne(Wrappers.<SatMsgSceneReceive>lambdaQuery()
                                                 .eq(SatMsgSceneReceive::getMsgSceneCode, code));
    }

    @Override
    public Page<SatMsgSceneReceive> getSatMsgSceneReceivePage(SatMsgSceneReceiveQueryParam param) {
        return this.baseMapper.selectPage(PagingUtils.convert(param), Wrappers.<SatMsgSceneReceive>lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getApplicationCode()), SatMsgSceneReceive::getApplicationCode, param.getApplicationCode())
                .like(StringUtils.isNotEmpty(param.getKeyword()), SatMsgSceneReceive::getMsgSceneName, param.getKeyword())
                .orderByDesc(SatMsgSceneReceive::getCreateTime));
    }

    @Override
    public List<SatMsgApplicationVO> getSatMsgSceneApplications() {
        return this.baseMapper.selectSatMsgSceneApplications();
    }
}
