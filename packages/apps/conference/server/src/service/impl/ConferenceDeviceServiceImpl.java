package com.guhecloud.cube.srv.gen.conferencemanage.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.ConferenceDeviceMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceDevice;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceDeviceAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceDeviceQueryParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceDeviceService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceDeviceVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceDeviceServiceImpl extends ServiceImpl<ConferenceDeviceMapper, ConferenceDevice> implements ConferenceDeviceService {

    private final ConferenceDeviceMapper conferenceDeviceMapper;

    @Override
    public Pagination<ConferenceDeviceVo> getConferenceDeviceInfo(ConferenceDeviceQueryParam param) {
        Page<ConferenceDevice> page = this.page(PagingUtils.convert(param), Wrappers.<ConferenceDevice>lambdaQuery()
                .like(StringUtils.isNotBlank(param.getName()), ConferenceDevice::getName, param.getName())
                .like(StringUtils.isNotBlank(param.getDeviceModel()), ConferenceDevice::getDeviceModel, param.getDeviceModel())
                .orderByDesc(ConferenceDevice::getCreateTime));
        Pagination<ConferenceDeviceVo> convert = PagingUtils.convert(page, ConferenceDeviceVo.class);
        return convert;
    }

    @Override
    public void insertConferenceDevice(ConferenceDeviceAddOrUpdateParam param) {
        ConferenceDevice conferenceDevice = new ConferenceDevice();
        ObjectUtils.copying(param, conferenceDevice);
        conferenceDeviceMapper.insert(conferenceDevice);
    }

    @Override
    public void updateConferenceDevice(ConferenceDeviceAddOrUpdateParam param) {
        ConferenceDevice conferenceDevice = new ConferenceDevice();
        if (StringUtils.isNotBlank(param.getDeviceCode())) {
            conferenceDevice.setDeviceCode(param.getDeviceCode());
        }
        if (StringUtils.isNotBlank(param.getType())) {
            conferenceDevice.setType(param.getType());
        }
        if (StringUtils.isNotBlank(param.getAreaName())) {
            conferenceDevice.setAreaName(param.getAreaName());
        }
        if (StringUtils.isNotBlank(param.getAreaId().toString())) {
            conferenceDevice.setAreaId(param.getAreaId());
        }
        if (StringUtils.isNotBlank(param.getDeviceModel())) {
            conferenceDevice.setDeviceModel(param.getDeviceModel());
        }
        if (StringUtils.isNotBlank(param.getName())) {
            conferenceDevice.setName(param.getName());
        }
        if (StringUtils.isNotBlank(param.getConferenceId().toString())) {
            conferenceDevice.setConferenceId(param.getConferenceId());
        }
        conferenceDeviceMapper.updateById(conferenceDevice);
    }

}
