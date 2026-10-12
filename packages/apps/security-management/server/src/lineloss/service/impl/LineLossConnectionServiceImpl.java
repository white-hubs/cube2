package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dao.LineLossConnectionMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dao.LineLossNodeMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossConnection;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossNode;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossConnectionPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossConnectionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.utils.StringSplitIdUtil;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossConnectionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossDeviceVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 线损线路
 * 线路和节点在LineLossSupplyServiceImpl类中聚合，这里仅提供基础能力
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossConnectionServiceImpl extends ServiceImpl<LineLossConnectionMapper, LineLossConnection> implements LineLossConnectionService {

    private final DeviceCenterStdClient deviceCenterStdClient;
    private final LineLossNodeMapper lineLossNodeMapper;

    /**
     * 分页查询线损线路
     *
     * @param param 查询参数
     *
     * @return 分页结果
     */
    @Override
    public Pagination<LineLossConnectionVO> pageLossConnection(LineLossConnectionPageParam param) {
        // 执行分页查询
        Page<LineLossConnection> resultPage = this.page(PagingUtils.convert(param), Wrappers.<LineLossConnection>lambdaQuery()
                .like(StringUtils.isNotBlank(param.getLineName()), LineLossConnection::getLineName, param.getLineName()));
        if (CollectionUtils.isEmpty(resultPage.getRecords())) {
            return Pagination.empty();
        }
        // 获取节点信息
        var nodeList = lineLossNodeMapper.selectList(Wrappers.emptyWrapper());
        var nodeMap = Optional.ofNullable(nodeList).orElse(CollectionUtils.emptyList()).stream()
                .collect(Collectors.toMap(LineLossNode::getId, item -> item, (o, n) -> o));
        // 获取设备信息
        var deviceMap = new HashMap<Long, DeviceBaseArchivesInfoDTO>();
        var deviceIdSet = new HashSet<Long>();
        resultPage.getRecords().forEach(entity -> {
            deviceIdSet.add(entity.getFromDeviceId());
            deviceIdSet.add(entity.getToDeviceId());
        });
        if (CollectionUtils.isNotEmpty(deviceIdSet)) {
            DevicePostByIdSetParam deviceParam = new DevicePostByIdSetParam();
            deviceParam.setDeviceIdSet(deviceIdSet);
            var deviceList = deviceCenterStdClient.postListByDeviceId(deviceParam);
            Optional.ofNullable(deviceList).orElse(CollectionUtils.emptyList()).forEach(device -> deviceMap.put(device.getId(), device));
        }

        // 转换为 VO 列表
        var voList = resultPage.getRecords().stream()
                .map(entity -> convertToVO(entity, nodeMap, deviceMap))
                .collect(java.util.stream.Collectors.toList());

        // 构建并返回 Pagination 对象
        return Pagination.<LineLossConnectionVO>builder()
                .total(resultPage.getTotal())
                .pages(resultPage.getPages())
                .records(voList)
                .build();
    }

    /**
     * 将 Entity 转换为 VO
     */
    private LineLossConnectionVO convertToVO(LineLossConnection entity, Map<Long, LineLossNode> nodeMap,
                                             Map<Long, DeviceBaseArchivesInfoDTO> deviceMap
                                            ) {
        LineLossConnectionVO vo = new LineLossConnectionVO();
        BeanUtils.copyProperties(entity, vo);
        vo.setToNodeLocation(nodeMap.getOrDefault(entity.getToNodeId(), new LineLossNode()).getLocation());
        vo.setFromNodeLocation(nodeMap.getOrDefault(entity.getFromNodeId(), new LineLossNode()).getLocation());
        vo.setToDevice(deviceMap.getOrDefault(entity.getToDeviceId(), new DeviceBaseArchivesInfoDTO()));
        vo.setFromDevice(deviceMap.getOrDefault(entity.getFromDeviceId(), new DeviceBaseArchivesInfoDTO()));
        return vo;
    }

    /**
     * 根据线路 ID 查询线路设备信息
     *
     * @param param 查询参数
     *
     * @return 线路设备信息
     */
    @Override
    public LineLossDeviceVO getLineDevices(LineLossDeviceParam param) {
        Assert.isParameterError(param.getLineId() == null, "线路 ID 不能为空");

        // 查询线路信息
        LineLossConnection connection = this.getById(param.getLineId());
        Assert.isNotFound(connection, "未找到线路信息");

        // 查询起始节点和结束节点
        LineLossNode fromNode = null;
        LineLossNode toNode = null;

        if (connection.getFromNodeId() != null) {
            fromNode = lineLossNodeMapper.selectById(connection.getFromNodeId());
        }

        if (connection.getToNodeId() != null) {
            toNode = lineLossNodeMapper.selectById(connection.getToNodeId());
        }

        // 收集所有设备 ID
        var deviceIdSet = new HashSet<Long>();

        // 从起始节点收集设备 ID
        if (fromNode != null && StringUtils.isNotEmpty(fromNode.getDeviceIds())) {
            StringSplitIdUtil.handlerIdsAddSet(fromNode.getDeviceIds(), deviceIdSet);
        }

        // 从结束节点收集设备 ID
        if (toNode != null && StringUtils.isNotEmpty(toNode.getDeviceIds())) {
            StringSplitIdUtil.handlerIdsAddSet(toNode.getDeviceIds(), deviceIdSet);
        }

        // 批量获取设备信息
        List<DeviceBaseArchivesInfoDTO> allDeviceList = new ArrayList<>();
        if (!deviceIdSet.isEmpty()) {
            DevicePostByIdSetParam deviceParam = new DevicePostByIdSetParam();
            deviceParam.setDeviceIdSet(deviceIdSet);
            allDeviceList = deviceCenterStdClient.postListByDeviceId(deviceParam);
        }

        // 构建返回结果
        LineLossDeviceVO result = new LineLossDeviceVO();
        result.setLineId(connection.getId());
        result.setLineName(connection.getLineName());
        result.setFromNodeId(connection.getFromNodeId());
        result.setToNodeId(connection.getToNodeId());

        if (fromNode != null) {
            result.setFromNodeName(fromNode.getName());
        }

        if (toNode != null) {
            result.setToNodeName(toNode.getName());
        }

        // 分离起始节点和结束节点的设备
        if (!deviceIdSet.isEmpty() && CollectionUtils.isNotEmpty(allDeviceList)) {
            Map<Long, DeviceBaseArchivesInfoDTO> deviceMap = allDeviceList.stream()
                    .collect(Collectors.toMap(DeviceBaseArchivesInfoDTO::getId, d -> d, (o, n) -> o));

            // 起始节点设备列表
            List<DeviceBaseArchivesInfoDTO> fromDeviceList = new ArrayList<>();
            if (fromNode != null && StringUtils.isNotEmpty(fromNode.getDeviceIds())) {
                var fromDeviceIdSet = new HashSet<Long>();
                StringSplitIdUtil.handlerIdsAddSet(fromNode.getDeviceIds(), fromDeviceIdSet);
                for (Long deviceId : fromDeviceIdSet) {
                    if (deviceMap.containsKey(deviceId)) {
                        fromDeviceList.add(deviceMap.get(deviceId));
                    }
                }
            }
            result.setFromDeviceList(fromDeviceList);

            // 结束节点设备列表
            List<DeviceBaseArchivesInfoDTO> toDeviceList = new ArrayList<>();
            if (toNode != null && StringUtils.isNotEmpty(toNode.getDeviceIds())) {
                var toDeviceIdSet = new HashSet<Long>();
                StringSplitIdUtil.handlerIdsAddSet(toNode.getDeviceIds(), toDeviceIdSet);
                for (Long deviceId : toDeviceIdSet) {
                    if (deviceMap.containsKey(deviceId)) {
                        toDeviceList.add(deviceMap.get(deviceId));
                    }
                }
            }
            result.setToDeviceList(toDeviceList);
        } else {
            result.setFromDeviceList(new ArrayList<>());
            result.setToDeviceList(new ArrayList<>());
        }

        return result;
    }
}