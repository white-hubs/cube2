package com.guhecloud.cube.biz.dispatch.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.dispatch.server.dao.DispatchDeviceExtendMapper;
import com.guhecloud.cube.biz.dispatch.server.dao.DispatchDeviceMapper;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDevice;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceExtend;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceGroupRefer;
import com.guhecloud.cube.biz.dispatch.server.model.extend.DispatchDeviceExtendModel;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceGroupReferParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceQueryParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceUpdateParam;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceGroupReferService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchService;
import com.guhecloud.cube.biz.dispatch.server.util.GeoToolUtil;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceDetailVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGroupVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchNodeVO;
import com.guhecloud.cube.biz.dispatch.server.vo.extend.DispatchDeviceExtendVO;
import com.guhecloud.cube.biz.dispatch.server.vo.extend.DispatchNodeExtendVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 描述：调度设备服务层
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchDeviceServiceImpl extends ServiceImpl<DispatchDeviceMapper, DispatchDevice> implements DispatchDeviceService {

    private final DispatchService dispatchService;
    private final DispatchDeviceMapper dispatchDeviceMapper;
    private final DispatchDeviceGroupReferService dispatchDeviceGroupReferService;
    private final DispatchDeviceExtendMapper dispatchDeviceExtendMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(DispatchDeviceParam param) {
        DispatchDevice dispatchDevice = ObjectUtils.copying(param, DispatchDevice.class);
        //添加设备
        dispatchDevice.setId(param.getCode());//将业务平台的ID与第三方平台的code保持一致
        dispatchDevice.setPassword(StringUtils.isEmpty(param.getPassword()) ? param.getCode() : param.getPassword());
        this.baseMapper.insert(dispatchDevice);
        //添加设备扩展信息
        this.addDeviceExtend(dispatchDevice.getId(), ObjectUtils.copying(param, DispatchDeviceExtend.class));

        //第三方调度台 增加设备
        this.dispatchService.addDevice(ObjectUtils.copying(param, DispatchDeviceExtendModel.class));
        //添加设备与组的关系
        this.dispatchDeviceGroupReferService.add(DispatchDeviceGroupReferParam.builder().groupId(param.getParentCode())
                                                         .deviceId(param.getCode()).build());

        return dispatchDevice.getId();
    }

    /**
     * 插入扩展信息
     *
     * @param id
     * @param extend
     */
    private void addDeviceExtend(String id, DispatchDeviceExtend extend) {
        extend.setDeviceId(id);
        dispatchDeviceExtendMapper.insertOrUpdateDeviceExtendBatch(Arrays.asList(extend));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, DispatchDeviceUpdateParam param) {
        DispatchDevice dispatchDevice = this.baseMapper.selectById(id);
        Assert.isNotFound(dispatchDevice == null, "未查询到相关信息");
        ObjectUtils.copying(param, dispatchDevice);
        //第三方调度台 修改设备
        this.dispatchService.updateDevice(ObjectUtils.copying(param, DispatchDeviceExtendModel.class));
        this.baseMapper.updateById(dispatchDevice);
        //扩展信息
        DispatchDeviceExtend deviceExtend = ObjectUtils.copying(param, DispatchDeviceExtend.class);
        this.addDeviceExtend(id, deviceExtend);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        //查询调度设备
        DispatchDeviceVO device = this.getById(id);
        Assert.isNotFound(device == null, "未找到删除设备");
        //删除调度设备
        this.removeById(id);
        //删除 第三方调度台设备
        this.dispatchService.delDevice(device.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        ids.stream().forEach(id -> this.removeById(id));
    }

    @Override
    public DispatchDeviceVO getById(String id) {
        DispatchDevice dispatchDevice = this.baseMapper.selectById(id);
        DispatchDeviceExtendVO deviceDetail = ObjectUtils.copying(dispatchDevice, DispatchDeviceExtendVO.class);
        //查询扩展信息
        DispatchDeviceExtend deviceExtend = dispatchDeviceExtendMapper.selectOne(Wrappers.<DispatchDeviceExtend>lambdaQuery()
                                                                                         .eq(DispatchDeviceExtend::getDeviceId, id));
        ObjectUtils.copying(deviceExtend, deviceDetail);
        return deviceDetail;
    }

    @Override
    public Pagination<DispatchDeviceVO> getPage(DispatchDeviceQueryParam param) {
        //请自行添加查询条件
        Page<DispatchDevice> page = this.lambdaQuery()
                .and(!StringUtils.isEmpty(param.getKeyword()), q ->
                        q.like(DispatchDevice::getName, param.getKeyword())
                                .or().like(DispatchDevice::getCode, param.getKeyword()))
                .eq(!StringUtils.isEmpty(param.getType()), DispatchDevice::getType, param.getType())
                .page(PagingUtils.convert(param));
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            List<DispatchDeviceVO> records = page.getRecords().stream()
                    .map(this::withExtend).collect(Collectors.toList());
            return PagingUtils.convert(page, records);
        }
        return Pagination.empty();
    }

    private DispatchDeviceExtendVO withExtend(DispatchDevice device) {
        //扩展信息
        DispatchDeviceExtendVO deviceExtendVO = ObjectUtils.copying(device, DispatchDeviceExtendVO.class);
        DispatchDeviceExtend extend = dispatchDeviceExtendMapper.selectOne(Wrappers.<DispatchDeviceExtend>lambdaQuery()
                                                                                   .eq(DispatchDeviceExtend::getDeviceId, device.getId()));
        if (extend != null) {
            deviceExtendVO.setIsRecord(extend.getIsRecord());
            deviceExtendVO.setIsVideo(extend.getIsVideo());
            deviceExtendVO.setDeviceLevel(extend.getDeviceLevel());
            deviceExtendVO.setRemark(extend.getRemark());
        }
        //设备所在的组
        List<DispatchGroupVO> groupList = this.dispatchDeviceMapper.deviceBelongGroup(device.getCode());
        deviceExtendVO.setGroups(groupList);
        return deviceExtendVO;
    }

    /**
     * 同步第三方调度台的用户数据
     */
    @Override
    public void obtainThirdPlatformDevice(String code) {
        List<DispatchNodeVO> devices = dispatchService.allDevice(code);
        //向数据库插入数据
        //        log.info("调度设备数据：" + JsonUtils.serialize(devices));
        this.batchAddDevice(devices);//批量插入设备
        this.batchAddDeviceGroupRefer(devices);//批量插入设备与组的关系
        this.delInvalidRefer(devices);//删除无效的关系
    }

    private void batchAddDevice(List<DispatchNodeVO> devices) {
        String operator = SecurityContextHolder.getPrincipal(true) != null ? SecurityContextHolder.getPrincipal().getAccount() : "admin";
        List<DispatchDevice> datas = devices.stream().map(d -> buildDispatchDevice(d, operator)).collect(Collectors.toList());
        this.dispatchDeviceMapper.insertOrUpdateDeviceBatch(datas);
        //删除无效的设备  因为是全量查询的 所以不在当前列表中的数据 全部删除
        List<String> ids = datas.stream().map(DispatchDevice::getCode).collect(Collectors.toList());
        this.dispatchDeviceMapper.delete(Wrappers.<DispatchDevice>lambdaQuery()
                                                 .notIn(DispatchDevice::getId, ids));
        //设备扩展信息
        List<DispatchDeviceExtend> extendList = devices.stream().map(d -> buildDispatchDeviceExtend(d, operator))
                .collect(Collectors.toList());
        this.dispatchDeviceExtendMapper.insertOrUpdateDeviceExtendBatch(extendList);
    }

    private void batchAddDeviceGroupRefer(List<DispatchNodeVO> devices) {
        String operator = SecurityContextHolder.getPrincipal(true) != null ? SecurityContextHolder.getPrincipal().getAccount() : "admin";
        List<DispatchDeviceGroupRefer> datas = new ArrayList<>();
        devices.stream().forEach(d -> {
            List<String> parentCodes = d.getParentCodes();//用户可能存在多个组下
            if (!CollectionUtils.isEmpty(parentCodes)) {
                parentCodes.forEach(g -> {
                    DispatchDeviceGroupRefer refer = new DispatchDeviceGroupRefer();
                    refer.setGroupId(g);
                    refer.setDeviceId(d.getCode());
                    refer.setTenant(SessionContextHolder.getRequester().getTenant());
                    refer.setCreateBy(operator);
                    refer.setCreateTime(LocalDateTime.now());
                    refer.setUpdateBy(operator);
                    refer.setUpdateTime(LocalDateTime.now());
                    refer.setDeleted(false);
                    datas.add(refer);
                });
            }
        });
        this.dispatchDeviceGroupReferService.batchAdd(datas);

    }

    private void delInvalidRefer(List<DispatchNodeVO> devices) {
        //删除无效的关系
        List<DispatchDeviceGroupRefer> referList = this.dispatchDeviceGroupReferService.list();
        Map<String, List<String>> deviceInGroupMap = referList.stream().collect(Collectors.groupingBy(DispatchDeviceGroupRefer::getDeviceId,
                                                                                                      Collectors.mapping(refer -> refer.getGroupId(), Collectors.toList())));
        devices.forEach(d -> {
            //没有组信息 直接删除
            if (CollectionUtils.isEmpty(d.getParentCodes())) {
                this.dispatchDeviceGroupReferService.remove(Wrappers.<DispatchDeviceGroupRefer>lambdaQuery()
                                                                    .eq(DispatchDeviceGroupRefer::getDeviceId, d.getCode()));
            } else {
                //获取现在的关系
                List<String> oldGroupIds = deviceInGroupMap.get(d.getCode());
                var removeList = new ArrayList<>(oldGroupIds);
                removeList.removeAll(d.getParentCodes());
                if (!CollectionUtils.isEmpty(removeList)) {
                    removeList.forEach(rg -> {
                        this.dispatchDeviceGroupReferService.remove(Wrappers.<DispatchDeviceGroupRefer>lambdaQuery()
                                                                            .eq(DispatchDeviceGroupRefer::getDeviceId, d.getCode())
                                                                            .eq(DispatchDeviceGroupRefer::getGroupId, rg));
                    });
                }
            }
        });
    }

    /**
     * 构建设备对象
     *
     * @param data
     * @param operator
     *
     * @return
     */
    private DispatchDevice buildDispatchDevice(DispatchNodeVO data, String operator) {
        DispatchNodeExtendVO extendNode = (DispatchNodeExtendVO) data;
        DispatchDevice device = new DispatchDevice();
        device.setId(extendNode.getCode());
        device.setCode(extendNode.getCode());
        device.setPassword(extendNode.getPassword());
        device.setName(extendNode.getName());
        device.setTenant(SessionContextHolder.getRequester().getTenant());
        device.setCreateBy(operator);
        device.setCreateTime(LocalDateTime.now());
        device.setUpdateBy(operator);
        device.setUpdateTime(LocalDateTime.now());
        device.setDeleted(false);
        if (dispatchService instanceof PTDispatchServiceImpl) {
            device.setType(extendNode.getSubType());
        } else {
            device.setType(extendNode.getType());
        }
        return device;
    }

    /**
     * 构建设备扩展对象
     *
     * @param data
     * @param operator
     *
     * @return
     */
    private DispatchDeviceExtend buildDispatchDeviceExtend(DispatchNodeVO data, String operator) {
        DispatchDeviceExtend extend = ObjectUtils.copying(data, DispatchDeviceExtend.class);
        extend.setDeviceId(data.getCode());
        extend.setTenant(SessionContextHolder.getRequester().getTenant());
        extend.setCreateBy(operator);
        extend.setCreateTime(LocalDateTime.now());
        extend.setUpdateBy(operator);
        extend.setUpdateTime(LocalDateTime.now());
        extend.setDeleted(false);
        return extend;
    }

    @Override
    public List<DispatchDeviceDetailVO> getDeviceInCircle(double lng, double lat, double radius, Boolean onlineStatus) {
        DispatchDeviceQueryParam param = new DispatchDeviceQueryParam();
        param.setOnlineStatus(onlineStatus);
        List<DispatchDeviceDetailVO> devicesStatus = this.detailList(param);
        Polygon circle = GeoToolUtil.createCircle(lng, lat, radius);
        return Optional.ofNullable(devicesStatus).orElse(Collections.emptyList()).stream()
                .filter(d -> {
                    if (d.getLng() == null || d.getLat() == null) {
                        return false;
                    } else {
                        return circle.contains(GeoToolUtil.createPoint(d.getLng(), d.getLat()));
                    }
                })
                .map(d -> ObjectUtils.copying(d, DispatchDeviceDetailVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Pagination<DispatchDeviceDetailVO> detailPage(DispatchDeviceQueryParam param) {
        Page<DispatchDeviceDetailVO> page = dispatchDeviceMapper.deviceDetail(PagingUtils.convert(param), param);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            page.getRecords().stream().forEach(s -> {
                s.setTypeName(dispatchService.getDeviceTypeName(s.getType()));
            });
        }
        return PagingUtils.convert(page);
    }

    @Override
    public List<DispatchDeviceDetailVO> detailList(DispatchDeviceQueryParam param) {
        return dispatchDeviceMapper.deviceDetail(param != null ? param : new DispatchDeviceQueryParam());
    }

    @Override
    public List<DispatchDeviceDetailVO> deviceInGroup(String groupId) {
        return dispatchDeviceMapper.deviceInGroupWithStatus(groupId);
    }
}