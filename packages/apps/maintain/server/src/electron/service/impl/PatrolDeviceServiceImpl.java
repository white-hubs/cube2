package com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl;

import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolDeviceMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolDevice;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolDeviceParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolDeviceService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.DeviceStatusStatVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolDeviceVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 巡更设备
 *
 * @author : appet
 * @date : 2023-10-19
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolDeviceServiceImpl extends ServiceImpl<PatrolDeviceMapper, PatrolDevice> implements PatrolDeviceService {
    private final PatrolDeviceMapper patrolDeviceMapper;

    @Override
    public List<DeviceStatusStatVO> getDeviceStatusStat() {
        return patrolDeviceMapper.getDeviceStatusStat();
    }

    @Override
    public Pagination<PatrolDeviceVO> getPage(PatrolDeviceParam param) {
        Page<PatrolDevice> page = patrolDeviceMapper.selectPage(PagingUtils.convert(param), Wrappers.<PatrolDevice>lambdaQuery()
                .like(StringUtils.isNotBlank(param.getKeyword()), PatrolDevice::getName, param.getKeyword()));

        return PagingUtils.convert(page, PatrolDeviceVO.class);
    }
}
