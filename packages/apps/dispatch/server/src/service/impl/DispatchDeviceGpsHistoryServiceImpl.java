package com.guhecloud.cube.biz.dispatch.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.dispatch.server.dao.DispatchDeviceGpsHistoryMapper;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceGpsHistory;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchGPSQueryModel;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceGpsHistoryService;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGPSVO;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchDeviceGpsHistoryServiceImpl extends ServiceImpl<DispatchDeviceGpsHistoryMapper, DispatchDeviceGpsHistory> implements DispatchDeviceGpsHistoryService {

    @Override
    public List<DispatchDeviceGpsHistory> getLastGpsHistoryByDeviceIds(Set<String> deviceIds) {
        return this.baseMapper.selectLastGpsHistoryByDeviceIds(deviceIds);
    }

    @Override
    public Page<DispatchGPSVO> gpsHistory(DispatchGPSQueryModel param) {
        return this.baseMapper.selectHistory(PagingUtils.convert(param), param);
    }
}
