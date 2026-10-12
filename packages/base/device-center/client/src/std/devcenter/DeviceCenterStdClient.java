package com.guhecloud.cube.base.device.center.std.devcenter;

import com.guhecloud.cube.base.device.center.metadata.dto.CenterBizDeviceDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoWithFullAreaDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceExpandInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceIdTypeDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductCountDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoWithDeviceDTO;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBaseInfoPageQry;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByAreasOrGroupsAndTagParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.CenterBizDeviceParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceProductCountQueryParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceRuleConditionPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.ListWithAreaLevelParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceInstanceListWithAreaLevelVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceTagRuleVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.CLIENT_NAME;

@FeignClient(CLIENT_NAME)
public interface DeviceCenterStdClient {

    /**
     * 不支持大批量设备查询
     *
     * @param deviceIds 设备id
     *
     * @return 设备基础档案信息
     */
    @GetMapping("/device/base/information/list")
    List<DeviceBaseArchivesInfoDTO> getListByDeviceId(@RequestParam(value = "deviceIds") Set<Long> deviceIds);

    /**
     * 根据设备id集合查询设备基础信息列表
     *
     * @param param 设备id
     *
     * @return 设备基础档案信息
     */
    @PostMapping("/device/base/information/list/post")
    List<DeviceBaseArchivesInfoDTO> postListByDeviceId(@RequestBody DevicePostByIdSetParam param);

    /**
     * 不支持大批量设备查询
     *
     * @param deviceIds 设备id
     *
     * @return 设备扩展信息
     */
    @GetMapping("/device/expand/information/list")
    List<DeviceExpandInfoDTO> getExpandListByDeviceId(@RequestParam(value = "deviceIds") Set<Long> deviceIds);

    @GetMapping("/device/product/information/list")
    List<DeviceProductInfoWithDeviceDTO> getProductInfoByDeviceId(@RequestParam(value = "deviceIds") Set<Long> deviceIds);

    @PostMapping("/device/product/information/list/post")
    List<DeviceProductInfoWithDeviceDTO> postProductInfoByDeviceId(@RequestBody DevicePostByIdSetParam param);

    /**
     * 设备详情查询 不允许循环调用
     *
     * @param deviceId 设备id
     *
     * @return 设备详细信息
     */
    @GetMapping("/device/detail/{deviceId}")
    DeviceDetailDTO getDeviceById(@PathVariable("deviceId") Long deviceId);

    /**
     * 设备列表查询
     * tagTypeCode
     *
     * @param tagCodes 设备标签
     *
     * @return
     */
    @GetMapping("/device/tag/base/information/list")
    List<DeviceBaseArchivesInfoDTO> getListByTag(@RequestParam(value = "tagCodes") Set<String> tagCodes);

    /**
     * 设备基础信息分页列表
     *
     * @param baseInfoPageQry
     *
     * @return
     */
    @GetMapping("/device/base/info/page")
    Pagination<DeviceBaseArchivesInfoDTO> pageOfBaseInfo(@SpringQueryMap DeviceBaseInfoPageQry baseInfoPageQry);

    @GetMapping("/device/base/info/page-with-full-area")
    Pagination<DeviceBaseArchivesInfoWithFullAreaDTO> pageOfBaseInfoWithFullArea(@SpringQueryMap DeviceBaseInfoPageQry baseInfoPageQry);

    @PostMapping("/device/instance/page")
    Pagination<DevicePageInfoVO> deviceInfoPage(@RequestBody DeviceFilterPageParam deviceFilterPageParam);

    /**
     * 获取标签下保存的设备筛选规则
     *
     * @param code 标签编号
     *
     * @return 规则 VO，规则 JSON 字段为 {@link DeviceTagRuleVO#getRule()}
     */
    @GetMapping("/tag/rule/{code}")
    DeviceTagRuleVO getTagRule(@PathVariable("code") String code);

    /**
     * 按自定义规则条件分页查询设备实例
     *
     * @param param 分页参数 + ruleGroup
     *
     * @return 设备分页
     */
    @PostMapping("/device/instance/rule-condition/page")
    Pagination<DevicePageInfoVO> deviceRuleConditionInfoPage(@RequestBody DeviceRuleConditionPageParam param);

    /**
     * 带有区域层级的设备实例
     *
     * @param query
     *
     * @return
     */
    @PostMapping("/device/instance/list-with-area-level")
    DeviceInstanceListWithAreaLevelVO listWithAreaLevel(@RequestBody ListWithAreaLevelParam query);

    /**
     * 根据设备管理员获取设备实例
     *
     * @param managerId 管理人员id
     *
     * @return 设备id集合
     */
    @GetMapping("/device/manager/{managerId}/list")
    List<DeviceBaseArchivesInfoDTO> listByManager(
            @PathVariable("managerId") @NotNull(message = "参数错误，设备管理员不能为空") Long managerId
                                                 );

    /**
     * 批量查询设备基础信息
     *
     * @param deviceIds 设备id
     *
     * @return 设备信息
     */
    @GetMapping("/device/details/list")
    List<DeviceBaseDetailsDTO> getDetailsListByDeviceId(@RequestParam(value = "deviceIds") Set<Long> deviceIds);

    /**
     * 按三方 id（如智慧仓库仓 id 与物模型 thirdparty_id 一致）查设备基础详情（单条）。
     */
    @GetMapping("/device/details/by-third-party-id")
    DeviceBaseDetailsDTO getDetailsByThirdPartyId(@RequestParam("thirdPartyId") String thirdPartyId);

    /**
     * 批量查询设备基础信息
     *
     * @param param deviceIdSet 设备id
     *
     * @return 设备信息
     */
    @PostMapping("/device/details/post/list")
    List<DeviceBaseDetailsDTO> postDetailsListByDeviceId(@RequestBody DevicePostByIdSetParam param);

    /**
     * 根据部门ids获取设备信息(center_biz_device表的信息)
     *
     * @param param organizationList 部门id集合
     *
     * @return 设备列表
     */
    @PostMapping("/device/instance/by/organizations")
    List<CenterBizDeviceDTO> getCenterBizDeviceByOrganizations(@RequestBody CenterBizDeviceParam param);

    /**
     * 获取产品设备数量
     *
     * @param param 条件
     *
     * @return 产品设备数量集合
     */
    @PostMapping("/device/product/device-count")
    List<DeviceProductCountDTO> productDeviceCount(@RequestBody DeviceProductCountQueryParam param);

    /**
     * 获取设备ID列表
     *
     * @param deviceFilterPageParam 查询参数
     *
     * @return 设备ID列表
     */
    @PostMapping("/device/instance/idTypes")
    List<DeviceIdTypeDTO> deviceInfoIdTypes(@RequestBody DeviceFilterPageParam deviceFilterPageParam);

    /**
     * 根据区域id列表批量获取设备数统计
     *
     * @param areaIds 区域id列表
     *
     * @return 区域id与设备数的映射，key为区域id，value为设备数
     */
    @PostMapping("/device/count/by/areaIds")
    Map<Long, Long> countByAreaIds(@RequestBody List<Long> areaIds);

    /**
     * 根据区域集合或分组集合获取设备id集合
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/devices/ids/by-areas-or-groups-and-tag")
    Set<Long> queryCameraDeviceIdsByAreasOrGroups(@Valid @RequestBody QueryDeviceByAreasOrGroupsAndTagParam param);
}
