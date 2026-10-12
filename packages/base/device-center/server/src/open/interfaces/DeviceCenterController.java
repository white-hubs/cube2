package com.guhecloud.cube.base.device.center.open.interfaces;

import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceExpandMapper;
import com.guhecloud.cube.base.device.center.core.model.BizDeviceExpand;
import com.guhecloud.cube.base.device.center.core.param.DeviceCustomerPageParam;
import com.guhecloud.cube.base.device.center.metadata.annotation.VersionInfo;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoWithFullAreaDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceIdTypeDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductCountDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBaseInfoPageQry;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceFilterableByGenreQuery;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByAreasOrGroupsAndTagParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceCustomInfoParam;
import com.guhecloud.cube.base.device.center.metadata.std.dto.AreaDeviceNumberDTO;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.CenterBizDeviceParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceProductCountQueryParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.ListWithAreaLevelParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.VerifyPermissionCommand;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceInfoVO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceOfflineCountVO;
import com.guhecloud.cube.base.device.center.metadata.vo.AreaPointVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceInstanceListWithAreaLevelVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceListByGenreVO;
import com.guhecloud.cube.base.device.center.metadata.vo.GenreVO;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.open.service.DeviceCenterService;
import com.guhecloud.cube.base.device.center.open.vo.AreaEmptyDeviceTotalVO;
import com.guhecloud.cube.base.device.center.open.vo.CenterBizDeviceVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceCenterController {

    private final DeviceCenterService deviceCenterService;
    //read only
    private final CenterBizDeviceExpandMapper centerBizDeviceExpandMapper;

    private final AreaClient areaClient;

    /**
     * 密码校验
     *
     * @param verifyPermissionCommand 校验参数
     *                                throw BusinessException if password is not correct
     */
    @PostMapping("/device/instance/running/attr/control/permission/verify")
    public void verifyPermission(@RequestBody @Valid VerifyPermissionCommand verifyPermissionCommand) {
        BizDeviceExpand deviceExpand = centerBizDeviceExpandMapper.selectById(verifyPermissionCommand.getDeviceInstanceId());

        if (Objects.isNull(deviceExpand)) {
            throw new ServiceException("设备不存在");
        }

        String password = deviceExpand.getSecurityPwd();
        // 验证密码
        if (!StringUtils.equals(password, verifyPermissionCommand.getPassword())) {
            throw new ServiceException("密码不正确");
        }
    }

    /**
     * 设备信息分页<br/>
     * 前端通用
     *
     * @return 设备实例详情
     */
    @PostMapping("/device/instance/page")
    public Pagination<DevicePageInfoVO> deviceInfoPage(@RequestBody DeviceFilterPageParam deviceFilterPageParam) {
        // 需要进行其他信息处理
        return deviceCenterService.deviceInfoPage(deviceFilterPageParam);
    }

    /**
     * 获取设备ID列表
     *
     * @param deviceFilterPageParam 查询参数
     *
     * @return 设备ID列表
     */
    @PostMapping("/device/instance/idTypes")
    public List<DeviceIdTypeDTO> deviceInfoIdTypes(@RequestBody DeviceFilterPageParam deviceFilterPageParam) {
        //        var deviceIds = new ArrayList<DeviceIdTypeDTO>();
        //        for (int i = 0; i < 1; i++) {
        //            deviceIds.add(new DeviceIdTypeDTO((long) i, i + ""));
        //        }
        //        return deviceIds;
        return deviceCenterService.deviceInfoIdTypes(deviceFilterPageParam);
    }

    /**
     * 自定义条件设备信息分页
     * 前端通用
     *
     * @return 设备实例详情
     */
    @PostMapping("/device/instance/rule-condition/page")
    public Pagination<DevicePageInfoVO> deviceRuleConditionInfoPage(@RequestBody DeviceCustomerPageParam deviceCustomerPageParam) {
        return deviceCenterService.deviceRuleConditionInfoPage(deviceCustomerPageParam);
    }

    @GetMapping("/device/base/info/page")
    public Pagination<DeviceBaseArchivesInfoDTO> pageOfBaseInfo(@SpringQueryMap DeviceBaseInfoPageQry baseInfoPageQry) {
        return deviceCenterService.pageOfBaseInfo(baseInfoPageQry);
    }

    @GetMapping("/device/base/info/page-with-full-area")
    public Pagination<DeviceBaseArchivesInfoWithFullAreaDTO> pageOfBaseInfoWithFullArea(
            @SpringQueryMap DeviceBaseInfoPageQry baseInfoPageQry
                                                                                       ) {
        return deviceCenterService.pageOfBaseInfoWithFullArea(baseInfoPageQry);
    }

    /**
     * 设备信息列表
     *
     * @return 设备列表
     */
    @PostMapping("/devices/instance/list")
    public List<DeviceInfoVO> deviceInfoList(@RequestBody DeviceFilterParam deviceFilterParam) {
        return deviceCenterService.deviceInfoList(deviceFilterParam);
    }

    /**
     * 设备详情
     *
     * @param deviceId 设备id
     *
     * @return 设备详情
     */
    @GetMapping("/devices/instance/single/{deviceId}")
    public DeviceInfoVO deviceInfoSingle(@PathVariable("deviceId") Long deviceId) {
        return deviceCenterService.deviceInfoSingle(deviceId);
    }

    /**
     * 换取设备孪生编码
     *
     * @param deviceIds 设备id集合
     *
     * @return 设备孪生编码, 设备id -> 设备孪生编码
     */
    @PostMapping("/device/queryCodeByDeviceId")
    public List<Pair<String, String>> getCodeByDeviceId(@RequestBody Set<String> deviceIds) {
        return deviceCenterService.getCodeByDeviceId(deviceIds);
    }

    /**
     * 统计在线设备数量
     *
     * @param deviceIds 设备实例id集合
     *
     * @return 在线设备数量
     */
    @PostMapping("/device/online/count")
    public Integer getNormalDevice(@RequestBody Set<Long> deviceIds) {
        return deviceCenterService.queryCountNormalDevice(deviceIds);
    }

    /**
     * 统计设备在线、离线数量<br/>
     * <p>
     * 不再支持normalNumber、alarmNumber、faultNumber<br/>
     * 默认都为0
     *
     * @param deviceIds 设备实例id集合
     *
     * @return 分组后的设备信息
     */
    @PostMapping("/device/status/count")
    @VersionInfo(plannedRemove = "接下来的版本将移除，用设备标签进行替代")
    public DeviceStateCountDTO selectNumberOfGroupByStatusFromSecurity(@RequestBody List<Long> deviceIds) {
        return deviceCenterService.queryNumberOfGroupByStatus(deviceIds);
    }

    /**
     * 类型统计
     */
    @PostMapping("/genre-devices-num")
    public List<GenreVO> getGenreDevicesNum(@RequestBody List<Long> deviceIds) {
        return deviceCenterService.queryGenreDevicesNum(deviceIds);
    }

    /**
     * 设备按照区域分组并统计
     *
     * @param deviceIds 设备实例id集合
     *
     * @return 分组后的设备信息
     */
    @PostMapping("device-area-num")
    public List<AreaDeviceNumberDTO> getDeviceAreaNum(@RequestBody List<Long> deviceIds) {
        return deviceCenterService.queryDeviceAreaNum(deviceIds);
    }

    /**
     * 获取所有设备id
     */
    @GetMapping("/all-device")
    public List<Long> getAllDevice() {
        return deviceCenterService.queryDeviceIdsByAll();
    }

    /**
     * 对一组设备进行经纬度分组<br/>
     * eg: 一个应用所导入的设备，对他进行经纬度分组，返回分组后的设备信息
     *
     * @param deviceIds 设备实例id集合
     *
     * @return 分组后的设备信息
     */
    @PostMapping("/area-point")
    @VersionInfo(plannedRemove = "接下来的版本将移除，用设备标签进行替代")
    public List<AreaPointVO> getAreaPoint(@RequestBody List<Long> deviceIds) {
        return deviceCenterService.queryAreaPoint(deviceIds);
    }

    /**
     * 根据品类统计在线、离线、以及占比
     *
     * @param deviceIds 设备实例id集合
     *
     * @return 分组后的设备信息
     */
    @PostMapping("/device-classification-offline")
    public List<DeviceOfflineCountVO> getDeviceOffline(@RequestBody Set<Long> deviceIds) {
        return deviceCenterService.queryDeviceOffline(deviceIds);
    }

    /**
     * 过滤设备后的类型分组设备实例列表
     *
     * @param filterableByGenreQuery 过滤参数
     *
     * @return 分组后的设备信息
     */
    @PostMapping("/device/instance/filtered/list-by-genre")
    public List<DeviceListByGenreVO> deviceListFilterableByGenre(@RequestBody DeviceFilterableByGenreQuery filterableByGenreQuery) {
        return deviceCenterService.queryDeviceListFilterableByGenre(filterableByGenreQuery);
    }

    /**
     * @Description 类型数据
     */
    @PostMapping("/device/instance/list-by-genre")
    public List<DeviceListByGenreVO> deviceListByGenre(@RequestBody DeviceFilterableByGenreQuery filterableByGenreQuery) {
        return deviceCenterService.deviceListByGenre(filterableByGenreQuery);
    }

    /**
     * 带有区域层级的设备实例
     *
     * @param query
     *
     * @return
     */
    @PostMapping("/device/instance/list-with-area-level")
    public DeviceInstanceListWithAreaLevelVO listWithAreaLevel(@RequestBody ListWithAreaLevelParam query) {
        if (CollectionUtils.isEmpty(query.getDeviceIds())) {
            return new DeviceInstanceListWithAreaLevelVO();
        }
        QueryDeviceCustomInfoParam param = new QueryDeviceCustomInfoParam();
        param.setFilteredDeviceIds(query.getDeviceIds());
        param.setWithDeviceType(false);
        param.setWithCustomExt(false);
        List<DeviceInfoVO> deviceInfoVOS = this.deviceCenterService.queryCustomDeviceInfos(param);
        return build(deviceInfoVOS);
    }

    private DeviceInstanceListWithAreaLevelVO build(List<DeviceInfoVO> deviceInfoVOS) {
        List<DeviceInstanceListWithAreaLevelVO.AreaNode> areaNodes = new ArrayList<>();
        Set<Long> areaIdList = new HashSet<>();
        deviceInfoVOS.forEach(deviceInstancePO -> {
            String areaIdStr = deviceInstancePO.getAreaId();
            String[] areaIdArray = areaIdStr.split("-");
            List<Long> areaIds = Arrays.stream(areaIdArray).map(v -> Long.parseLong(v)).collect(Collectors.toList());
            Long parentId = 0L;
            for (int i = 0; i < areaIds.size(); i++) {
                Long areaId = areaIds.get(i);
                DeviceInstanceListWithAreaLevelVO.AreaNode areaNode = new DeviceInstanceListWithAreaLevelVO.AreaNode();
                areaNode.setId(areaId);
                areaNode.setParentId(parentId);
                areaNode.setIsArea(true);
                areaNode.setLevel(i);
                parentId = areaId;
                if (!areaIdList.contains(areaId)) {
                    areaIdList.add(areaId);
                    areaNodes.add(areaNode);
                }

            }

            //构建设备节点
            DeviceInstanceListWithAreaLevelVO.AreaNode areaNode = new DeviceInstanceListWithAreaLevelVO.AreaNode();
            if (areaIds.size() > 1) {
                areaNode.setParentId(areaIds.get(areaIds.size() - 2));
            } else {
                areaNode.setParentId(areaIds.get(0));
            }
            areaNode.setName(deviceInstancePO.getDeviceName());
            areaNode.setId(deviceInstancePO.getId());
            areaNode.setLevel(areaIds.size() - 1);
            areaNodes.add(areaNode);

        });

        areaNodes.forEach(areaNode -> {
            if (areaNode.getIsArea() == true) {
                String areaNameById = areaClient.getAreaNameById(areaNode.getId(), SessionContextHolder.getRequester().getTenant());
                if (areaNameById != null) {
                    areaNode.setName(areaNameById);
                }

            }
        });

        //构件树
        List<DeviceInstanceListWithAreaLevelVO.AreaNode> nodes = buildTree(areaNodes);
        DeviceInstanceListWithAreaLevelVO deviceInstanceListWithAreaLevelVO = new DeviceInstanceListWithAreaLevelVO(nodes);
        return deviceInstanceListWithAreaLevelVO;
    }

    public static List<DeviceInstanceListWithAreaLevelVO.AreaNode> buildTree(List<DeviceInstanceListWithAreaLevelVO.AreaNode> trees) {
        //获取parentId = 0的根节点
        List<DeviceInstanceListWithAreaLevelVO.AreaNode> list = trees.stream().filter(item -> {
            if (item.getParentId() == 0L && item.getIsArea() == true) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        //根据parentId进行分组
        Map<Long, List<DeviceInstanceListWithAreaLevelVO.AreaNode>> map = trees.stream()
                .collect(Collectors.groupingBy(DeviceInstanceListWithAreaLevelVO.AreaNode::getParentId));
        recursionFnTree(list, map);
        return list;
    }

    /**
     * 递归遍历节点
     *
     * @param list
     * @param map
     */
    public static void recursionFnTree(List<DeviceInstanceListWithAreaLevelVO.AreaNode> list,
                                       Map<Long, List<DeviceInstanceListWithAreaLevelVO.AreaNode>> map
                                      ) {
        for (DeviceInstanceListWithAreaLevelVO.AreaNode treeSelect : list) {
            List<DeviceInstanceListWithAreaLevelVO.AreaNode> childList = map.get(treeSelect.getId());
            treeSelect.setChildren(childList);
            if (null != childList && 0 < childList.size()) {
                recursionFnTree(childList, map);
            }
        }
    }

    /**
     * 获取设备管理人员
     *
     * @param deviceInstanceIds 设备id
     *
     * @return 设备对应的管理人员集合
     */
    @GetMapping("/device/instance/manager")
    public Map<Long, Set<Long>> getManagerByDevice(@RequestParam("deviceInstanceIds") Set<Long> deviceInstanceIds) {
        return deviceCenterService.getManagerByDevice(deviceInstanceIds);
    }

    /**
     * 根据设备管理员获取设备实例
     *
     * @param managerId 管理人员id
     *
     * @return 管理的所有设备id
     */
    @GetMapping("/device/instance/list/{managerId}")
    public List<Long> listByManager(@PathVariable("managerId") Long managerId) {
        return deviceCenterService.listByManager(managerId);
    }

    /**
     * 统计属性扩展表中已过期的设备id
     *
     * @return 设备id
     */
    @GetMapping("/device/instance/expiredDevices")
    public List<Long> expiredDevices() {
        return deviceCenterService.expiredDevices();
    }

    /**
     * 区域为空设备总数统计
     *
     * @param deviceFilterParam
     *
     * @return
     */
    @PostMapping("/device/area-empty/device-total")
    public AreaEmptyDeviceTotalVO getAlarmEmptyDeviceTotal(@Valid @RequestBody DeviceFilterParam deviceFilterParam) {
        return deviceCenterService.getAlarmEmptyDeviceTotal(deviceFilterParam);
    }

    /**
     * 根据部门ids获取设备信息(center_biz_device表的信息)
     *
     * @param param organizationList 部门id集合
     *
     * @return 设备列表
     */
    @PostMapping("/device/instance/by/organizations")
    public List<CenterBizDeviceVO> getCenterBizDeviceByOrganizations(@RequestBody CenterBizDeviceParam param) {
        return deviceCenterService.getCenterBizDeviceByOrganizations(param.getOrganizationList());
    }

    /**
     * 获取产品设备数量
     *
     * @param param 条件
     *
     * @return 产品设备数量集合
     */
    @PostMapping("/device/product/device-count")
    public List<DeviceProductCountDTO> productDeviceCount(@RequestBody DeviceProductCountQueryParam param) {
        return deviceCenterService.productDeviceCount(param);
    }

    /**
     * 根据区域集合或分组集合获取设备id集合
     *
     * @param param
     *
     * @return 设备id集合
     */
    @PostMapping("/devices/ids/by-areas-or-groups-and-tag")
    public Set<Long> queryCameraDeviceIdsByAreasOrGroups(@Valid @RequestBody QueryDeviceByAreasOrGroupsAndTagParam param) {
        return deviceCenterService.queryCameraDeviceIdsByAreasOrGroups(param);
    }

    /**
     * 根据区域id列表批量获取设备数统计
     *
     * @param areaIds 区域id列表
     * @return 区域id与设备数的映射，key为区域id，value为设备数
     */
    @PostMapping("/device/count/by/areaIds")
    public Map<Long, Long> countByAreaIds(@RequestBody List<Long> areaIds) {
        return deviceCenterService.countByAreaIds(areaIds);
    }
}
