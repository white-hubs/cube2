package com.guhecloud.cube.base.message2.server.sat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.service.MsgTmpService;
import com.guhecloud.cube.base.message2.server.base.vo.MsgTmpPageVO;
import com.guhecloud.cube.base.message2.server.initialization.event.ChangeSatMsgTmpEvent;
import com.guhecloud.cube.base.message2.server.initialization.event.IncreaseSatMsgTmpEvent;
import com.guhecloud.cube.base.message2.server.sat.dao.SatMsgTmpMapper;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgChannel;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgManufacturer;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmp;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmpVersion;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgTmpModifyParam;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgTmpQueryParam;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgTmpSettingConfigParam;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgChannelService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgManufacturerService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpManufacturerService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpVersionService;
import com.guhecloud.cube.base.message2.server.sat.vo.SatMsgTmpPageVO;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatMsgTmpServiceImpl extends ServiceImpl<SatMsgTmpMapper, SatMsgTmp> implements SatMsgTmpService {

    private final SatMsgChannelService satMsgChannelService;

    private final SatMsgManufacturerService satMsgManufacturerService;

    private final SatMsgTmpVersionService satMsgTmpVersionService;

    private final SatMsgTmpManufacturerService satMsgTmpManufacturerService;

    private final MsgTmpService msgTmpService;

    private final ApplicationClient applicationClient;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull SatMsgTmpModifyParam param) {
        SatMsgTmp satMsgTmp = ObjectUtils.copying(param, SatMsgTmp.class);
        this.baseMapper.insert(satMsgTmp);
        return satMsgTmp.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull SatMsgTmpModifyParam param) {
        SatMsgTmp satMsgTmp = this.getById(id);
        if (Objects.nonNull(satMsgTmp)) {
            //存储历史副本
            SatMsgTmpVersion satMsgTmpVersion = ObjectUtils.copying(satMsgTmp, SatMsgTmpVersion.class);
            satMsgTmpVersion.setCreateTime(null);
            satMsgTmpVersion.setUpdateTime(null);
            this.satMsgTmpVersionService.save(satMsgTmpVersion);
            ObjectUtils.copying(param, satMsgTmp);
            this.baseMapper.updateById(satMsgTmp);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settingConfig(@NonNull Long id, @NonNull SatMsgTmpSettingConfigParam param) {
        SatMsgTmp satMsgTmp = this.getById(id);
        Assert.isNotFound(satMsgTmp, "模板不存在");
        this.settingSatMsgTmpManufacturerConfig(param, satMsgTmp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshTemplate() {
        List<ApplicationDTO> tenantApplications = this.applicationClient
                .getTenantApplications(SessionContextHolder.getRequester().getTenant());
        if (!CollectionUtils.isEmpty(tenantApplications)) {
            Set<String> appCodes = tenantApplications.stream()
                    .map(ApplicationDTO::getCode).collect(Collectors.toSet());
            refreshAppTemplate(appCodes, SessionContextHolder.getRequester().getTenant());
        }
    }

    @Override
    public List<SatMsgTmp> listSatMsgTmpBySceneCodes(@NonNull Collection<String> sceneCodes) {
        return this.lambdaQuery()
                .in(SatMsgTmp::getMsgSceneCode, sceneCodes)
                .list();
    }

    @Override
    public List<SatMsgTmp> listSatMsgTmpByApplicationCodes(@NonNull Collection<String> applicationCodes) {
        return this.lambdaQuery().in(SatMsgTmp::getApplicationCode, applicationCodes).list();
    }

    @Override
    public List<SatMsgTmp> listSatMsgTmp(@NonNull String sceneCode, @NonNull Collection<ChannelEnum> channelCodes) {
        return this.lambdaQuery()
                .eq(SatMsgTmp::getMsgSceneCode, sceneCode)
                .in(SatMsgTmp::getChannelCode, channelCodes)
                .list();
    }

    @Override
    public List<SatMsgTmp> listSatMsgTmp(@NonNull ChannelEnum channelCode) {
        return this.lambdaQuery()
                .eq(SatMsgTmp::getChannelCode, channelCode)
                .list();
    }

    @Override
    public Page<SatMsgTmpPageVO> getSatMsgTmpPage(SatMsgTmpQueryParam param) {
        return this.baseMapper.selectSatMsgTmpPage(PagingUtils.convert(param), param);
    }

    @Override
    public List<SatMsgTmpPageVO> getSatMsgTemplates(SatMsgTmpQueryParam param) {
        return this.baseMapper.selectSatMsgTemplates(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshAppTemplate(Collection<String> appCodes, Integer tenant) {
        List<MsgTmpPageVO> msgTmpListByApplicationCodes = this.msgTmpService.getMsgTmpListByApplicationCodes(appCodes);
        if (!CollectionUtils.isEmpty(msgTmpListByApplicationCodes)) {
            SessionContextHolder.requestTenant(tenant);
            List<SatMsgTmp> applicationSatMsgTmpList = this.listSatMsgTmpByApplicationCodes(appCodes);
            Set<Long> tenantSatMsgTmp = applicationSatMsgTmpList.stream().map(SatMsgTmp::getMsgTmpId).collect(Collectors.toSet());
            List<MsgTmpPageVO> notExitMsgTmpList = msgTmpListByApplicationCodes.stream()
                    .filter(msgTmpPageVO -> !tenantSatMsgTmp.contains(msgTmpPageVO.getId()))
                    .collect(Collectors.toList());
            List<SatMsgTmp> satMsgTmpList = new ArrayList<>(msgTmpListByApplicationCodes.size());
            notExitMsgTmpList.forEach(msgTmp -> {
                SatMsgTmp satMsgTmp = ObjectUtils.copying(msgTmp, SatMsgTmp.class);
                satMsgTmp.setId(null);
                satMsgTmp.setMsgTmpId(msgTmp.getId());
                satMsgTmpList.add(satMsgTmp);
            });
            saveBatch(satMsgTmpList);
            //发送模板创建事件
            this.applicationEventPublisher.publishEvent(new IncreaseSatMsgTmpEvent(this, satMsgTmpList));
            //发送模板修改事件
            List<MsgTmpPageVO> exitMsgTmpList = msgTmpListByApplicationCodes.stream()
                    .filter(msgTmpPageVO -> tenantSatMsgTmp.contains(msgTmpPageVO.getId()))
                    .collect(Collectors.toList());
            this.applicationEventPublisher.publishEvent(new ChangeSatMsgTmpEvent(this, exitMsgTmpList));
        }
    }

    private void settingSatMsgTmpManufacturerConfig(SatMsgTmpSettingConfigParam param, SatMsgTmp satMsgTmp) {
        SatMsgChannel satMsgChannel = this.satMsgChannelService.getEnableConfigByCode(satMsgTmp.getChannelCode());
        Assert.isNotFound(satMsgChannel, "不存在通道配置,无法进行模板配置");
        SatMsgManufacturer SatMsgManufacturer = this.satMsgManufacturerService.getById(satMsgChannel.getSatMsgManufacturerId());
        this.satMsgTmpManufacturerService.settingTmpManufacturerConfig(satMsgTmp.getId(), SatMsgManufacturer.getChannelCode(),
                                                                       SatMsgManufacturer.getId(),
                                                                       param.getConfig());
    }
}
