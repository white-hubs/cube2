package com.guhecloud.cube.biz.dispatch.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.dispatch.server.dao.DispatchDeviceGroupReferMapper;
import com.guhecloud.cube.biz.dispatch.server.dao.DispatchDeviceMapper;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceGroupRefer;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceGroupReferParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceGroupReferQueryParam;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceGroupReferService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchService;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceGroupReferVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：调度组与设备关联服务层
 *
 * @author Appet
 * @date 2022-08-09 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchDeviceGroupReferServiceImpl extends ServiceImpl<DispatchDeviceGroupReferMapper, DispatchDeviceGroupRefer> implements DispatchDeviceGroupReferService {
    private final DispatchDeviceMapper dispatchDeviceMapper;
    private final DispatchService dispatchService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(DispatchDeviceGroupReferParam param) {
        //调用第三方平台
        if (StringUtils.isNotEmpty(param.getGroupId()) && StringUtils.isNotEmpty(param.getDeviceId())) {
            Arrays.stream(param.getGroupId().split(",")).forEach(groupId -> {
                Arrays.stream(param.getDeviceId().split(",")).forEach(deviceId -> {
                    this.baseMapper.insert(new DispatchDeviceGroupRefer(groupId, deviceId));
                    dispatchService.addDeviceToGroup(groupId, Arrays.asList(deviceId));
                });
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, DispatchDeviceGroupReferParam param) {
        DispatchDeviceGroupRefer dispatchDeviceGroupRefer = this.baseMapper.selectById(id);
        Assert.isNotFound(dispatchDeviceGroupRefer == null, "未查询到相关信息");
        ObjectUtils.copying(param, dispatchDeviceGroupRefer);
        this.baseMapper.updateById(dispatchDeviceGroupRefer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public DispatchDeviceGroupReferVO getById(Long id) {
        DispatchDeviceGroupRefer dispatchDeviceGroupRefer = this.baseMapper.selectById(id);
        return ObjectUtils.copying(dispatchDeviceGroupRefer, DispatchDeviceGroupReferVO.class);
    }

    @Override
    public Pagination<DispatchDeviceGroupReferVO> getPage(DispatchDeviceGroupReferQueryParam param) {
        //请自行添加查询条件
        Page<DispatchDeviceGroupRefer> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, DispatchDeviceGroupReferVO.class);
    }

    @Override
    public void batchAdd(String groupId, List<String> members) {
        if (!CollectionUtils.isEmpty(members)) {
            List<DispatchDeviceGroupRefer> datas = members.stream()
                    .filter(deviceId -> dispatchDeviceMapper.selectById(deviceId) != null)//过滤掉不存在的设备
                    .map(deviceId ->
                                 DispatchDeviceGroupRefer.builder()
                                         .groupId(groupId)
                                         .deviceId(deviceId)
                                         .tenant(SessionContextHolder.getRequester().getTenant())
                                         .createBy(SecurityContextHolder.getPrincipal().getAccount())
                                         .createTime(LocalDateTime.now())
                                         .updateBy(SecurityContextHolder.getPrincipal().getAccount())
                                         .updateTime(LocalDateTime.now())
                                         .deleted(false)
                                         .build()
                        ).collect(Collectors.toList());
            this.baseMapper.insertOrUpdateDeviceGroupReferBatch(datas);
        }
    }

    @Override
    public void delete(String groupId, String deviceId) {
        this.baseMapper.delete(Wrappers.<DispatchDeviceGroupRefer>lambdaQuery()
                                       .eq(DispatchDeviceGroupRefer::getGroupId, groupId)
                                       .eq(DispatchDeviceGroupRefer::getDeviceId, deviceId));
    }

    @Override
    public void batchDelete(String groupId, List<String> members) {
        if (!CollectionUtils.isEmpty(members)) {
            members.forEach(m -> this.delete(groupId, m));
        }
    }

    @Override
    public void batchAdd(List<DispatchDeviceGroupRefer> refers) {
        this.baseMapper.insertOrUpdateDeviceGroupReferBatch(refers);
    }
}