package com.guhecloud.cube.base.message2.server.sat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.sat.dao.SatMsgChannelMapper;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgChannel;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgManufacturer;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmpManufacturer;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgChannelModifyParam;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgManufacturerModifyParam;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgChannelService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgManufacturerService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpManufacturerService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatMsgChannelServiceImpl extends ServiceImpl<SatMsgChannelMapper, SatMsgChannel> implements SatMsgChannelService {

    private final SatMsgManufacturerService satMsgManufacturerService;

    private final SatMsgTmpManufacturerService satMsgTmpManufacturerService;

    /**
     * 租户通道配置保存
     *
     * @param param
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull SatMsgChannelModifyParam param) {
        SatMsgChannel satMsgChannel = ObjectUtils.copying(param, SatMsgChannel.class);
        //新增通道厂家配置
        Long satMsgManufacturerId = this.satMsgManufacturerService.save(ObjectUtils.copying(param, SatMsgManufacturerModifyParam.class));
        satMsgChannel.setChannelName(satMsgChannel.getChannelCode().getDescription());
        satMsgChannel.setSatMsgManufacturerId(satMsgManufacturerId);
        satMsgChannel.setEnabled(true);
        this.baseMapper.insert(satMsgChannel);
        //查询通道下的模板
        return satMsgChannel.getId();
    }

    /**
     * 租户通道配置修改
     * 建议这里不允许做修改(只能新增和删除,更换信息后,之前的模板会直接失效)
     *
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull SatMsgChannel satMsgChannel, @NonNull SatMsgChannelModifyParam param) {
        ObjectUtils.copying(param, satMsgChannel);
        //前置校验是否更改了厂家配置
        SatMsgManufacturer satMsgManufacturer = this.satMsgManufacturerService.getById(satMsgChannel.getSatMsgManufacturerId());
        satMsgChannel.setEnabled(true);
        satMsgManufacturer.setConfig(param.getConfig());
        this.satMsgManufacturerService.updateById(satMsgManufacturer);
        this.baseMapper.updateById(satMsgChannel);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateManufacturer(@NonNull SatMsgChannel satMsgChannel, @NonNull SatMsgChannelModifyParam param) {
        ObjectUtils.copying(param, satMsgChannel);
        satMsgChannel.setEnabled(true);
        Long oldSatMsgManufacturerId = satMsgChannel.getSatMsgManufacturerId();
        this.satMsgManufacturerService.remove(oldSatMsgManufacturerId);
        //新增通道厂家配置
        Long satMsgManufacturerId = this.satMsgManufacturerService.save(ObjectUtils.copying(param, SatMsgManufacturerModifyParam.class));
        satMsgChannel.setSatMsgManufacturerId(satMsgManufacturerId);
        //删除之前的模板
        this.satMsgTmpManufacturerService.remove(Wrappers.<SatMsgTmpManufacturer>lambdaQuery()
                                                         .eq(SatMsgTmpManufacturer::getSatMsgManufacturerId, oldSatMsgManufacturerId));
        this.baseMapper.updateById(satMsgChannel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnabled(@NonNull Long id, @NonNull Boolean enable) {
        SatMsgChannel satMsgChannel = this.getById(id);
        if (Objects.nonNull(satMsgChannel)) {
            satMsgChannel.setEnabled(enable);
            this.baseMapper.updateById(satMsgChannel);
        }
    }

    /**
     * 获取通道配置
     *
     * @param channelCode
     *
     * @return
     */
    @Override
    public SatMsgChannel getByChannelCode(@NonNull ChannelEnum channelCode) {
        return this.baseMapper.selectOne(Wrappers.<SatMsgChannel>lambdaQuery().eq(SatMsgChannel::getChannelCode, channelCode));
    }

    @Override
    public SatMsgChannel getEnableConfigByCode(@NonNull ChannelEnum channelCode) {
        List<SatMsgChannel> satMsgChannelManufacturerConfigs = this.listSatMsgChannels(channelCode);
        return CollectionUtils.isEmpty(satMsgChannelManufacturerConfigs) ? null : satMsgChannelManufacturerConfigs.get(0);
    }

    @Override
    public List<SatMsgChannel> getEnableConfigByCodes(@NonNull Collection<ChannelEnum> channelCode) {
        return CollectionUtils.isEmpty(channelCode) ? Collections.emptyList() : this.lambdaQuery()
                .in(SatMsgChannel::getChannelCode, channelCode)
                .eq(SatMsgChannel::getEnabled, true)
                .list();
    }

    @Override
    public List<SatMsgChannel> listSatMsgChannels() {
        return this.lambdaQuery().orderByAsc(SatMsgChannel::getCreateTime).list();
    }

    @Override
    public List<SatMsgChannel> listSatMsgChannels(@NonNull ChannelEnum channelCode) {
        return this.lambdaQuery()
                .eq(SatMsgChannel::getChannelCode, channelCode)
                .orderByAsc(SatMsgChannel::getCreateTime).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        SatMsgChannel satMsgChannelManufacturerConfig = this.getById(id);
        if (Objects.nonNull(satMsgChannelManufacturerConfig)) {
            DatabaseContextHolder.softDelete(this, id);
        }
    }
}
