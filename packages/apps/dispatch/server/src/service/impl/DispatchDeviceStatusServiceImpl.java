package com.guhecloud.cube.biz.dispatch.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.dispatch.server.dao.DispatchDeviceStatusMapper;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceStatus;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceStatusParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceStatusQueryParam;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceStatusService;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceStatusVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 描述：调度设备-状态服务层
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchDeviceStatusServiceImpl extends ServiceImpl<DispatchDeviceStatusMapper, DispatchDeviceStatus> implements DispatchDeviceStatusService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(DispatchDeviceStatusParam param) {
        DispatchDeviceStatus dispatchDeviceStatus = ObjectUtils.copying(param, DispatchDeviceStatus.class);
        this.baseMapper.insert(dispatchDeviceStatus);
        return dispatchDeviceStatus.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, DispatchDeviceStatusParam param) {
        DispatchDeviceStatus dispatchDeviceStatus = this.baseMapper.selectById(id);
        Assert.isNotFound(dispatchDeviceStatus == null, "未查询到相关信息");
        ObjectUtils.copying(param, dispatchDeviceStatus);
        this.baseMapper.updateById(dispatchDeviceStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        ids.stream().forEach(id -> this.removeById(id));
    }

    @Override
    public DispatchDeviceStatusVO getById(String id) {
        DispatchDeviceStatus dispatchDeviceStatus = this.baseMapper.selectById(id);
        return ObjectUtils.copying(dispatchDeviceStatus, DispatchDeviceStatusVO.class);
    }

    @Override
    public Pagination<DispatchDeviceStatusVO> getPage(DispatchDeviceStatusQueryParam param) {
        //请自行添加查询条件
        Page<DispatchDeviceStatus> page = this.lambdaQuery()
                .like(!StringUtils.isEmpty(param.getCode()), DispatchDeviceStatus::getCode, param.getCode())
                .eq(param.getOnlineStatus() != null, DispatchDeviceStatus::getOnlineStatus, param.getOnlineStatus())
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, DispatchDeviceStatusVO.class);
    }

    @Override
    public List<DispatchDeviceStatusVO> getListByCode(String codes, Boolean online) {
        List<String> deviceCodes = null;
        if (StringUtils.isNotEmpty(codes)) {
            deviceCodes = Arrays.asList(codes.split(","));
        }
        List<DispatchDeviceStatus> deviceStatusList = this.lambdaQuery()
                .in(CollectionUtils.isNotEmpty(deviceCodes), DispatchDeviceStatus::getCode, deviceCodes)
                .eq(online != null, DispatchDeviceStatus::getOnlineStatus, online)
                .list();
        if (CollectionUtils.isNotEmpty(deviceStatusList)) {
            return ObjectUtils.copying(deviceStatusList, DispatchDeviceStatusVO.class);
        }
        return Collections.emptyList();
    }
}