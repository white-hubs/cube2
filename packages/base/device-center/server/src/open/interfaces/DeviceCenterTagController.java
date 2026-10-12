package com.guhecloud.cube.base.device.center.open.interfaces;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseAndIotPropertyInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBaseAndIotPropertyInfoQry;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByAreaAndTagParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByAreaLevelAndTagParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByTagParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceSelfInfoParamPage;
import com.guhecloud.cube.base.device.center.metadata.std.dto.AreaDeviceNumberDTO;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.std.dto.TagPercentageDTO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceOfflineCountVO;
import com.guhecloud.cube.base.device.center.metadata.vo.AreaPointVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceSelfInfoVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceUsefulLifeExpireStatisticsVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicesVO;
import com.guhecloud.cube.base.device.center.metadata.vo.GenreVO;
import com.guhecloud.cube.base.device.center.metadata.vo.ProductCategoryOffLineCountVO;
import com.guhecloud.cube.base.device.center.open.service.DeviceCenterTagService;
import com.guhecloud.cube.base.device.center.open.vo.DeviceBaseInfoSafetyReportVO;
import com.guhecloud.cube.base.device.center.open.vo.DeviceTags;
import com.guhecloud.cube.base.device.center.open.vo.ProductDeviceCountVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceCenterTagController {

    private final DeviceCenterTagService deviceCenterTagService;

    /**
     * 获取设备自身信息<br/>
     * 设备没有图片使用类型图片！！
     *
     * @param tagCode                      标签编码()
     * @param queryDeviceSelfInfoParamPage 查询参数
     *
     * @return 设备实例详情
     */
    @Deprecated
    @PostMapping("/device/{tagCode}/instance-self-info/page")
    public Pagination<DeviceSelfInfoVO> queryDeviceSelfInfoPage(@PathVariable("tagCode") String tagCode,
                                                                @RequestBody QueryDeviceSelfInfoParamPage queryDeviceSelfInfoParamPage
                                                               ) {

        return deviceCenterTagService.queryDeviceSelfInfoPage(tagCode, queryDeviceSelfInfoParamPage);
    }

    /**
     * 换取设备孪生编码
     *
     * @param tagCode 标签编码
     *
     * @return 设备孪生编码, 设备id -> 设备孪生编码
     */
    @GetMapping("/device/{tagCode}/queryDeviceCode")
    public List<Pair<String, String>> deviceCode(@PathVariable("tagCode") String tagCode) {
        return deviceCenterTagService.deviceCode(tagCode);
    }

    /**
     * 统计在线设备数量
     *
     * @param tagCode 标签编码
     *
     * @return 在线设备数量
     */
    @GetMapping("/device/{tagCode}/online/count")
    public Integer getNormalDevice(@PathVariable("tagCode") String tagCode) {
        return deviceCenterTagService.queryNormalDevice(tagCode);
    }

    /**
     * 统计设备在线、离线数量<br/>
     * <p>
     * 不再支持normalNumber、alarmNumber、faultNumber<br/>
     * 默认都为0
     *
     * @param tagCodes 标签编码
     *
     * @return 分组后的设备信息
     */
    @GetMapping("/device/tagCode/status/count")
    public DeviceStateCountDTO selectNumberOfGroupByStatus(@RequestParam(value = "tagCodes", required = false) List<String> tagCodes) {
        return deviceCenterTagService.getNumberOfGroupByStatusByTags(tagCodes);
    }

    /**
     * 集合获取设备列表
     *
     * @param tagCode 标签编码
     *
     * @return 设备列表
     */
    @PostMapping("/devices/{tagCode}/list")
    @Deprecated
    public List<DevicesVO> listDevicesVo(@PathVariable("tagCode") String tagCode) {
        return deviceCenterTagService.listDevicesVo(tagCode);
    }

    /**
     * 类型统计
     *
     * @param tagCode 标签编码
     *
     * @return 设备列表
     */
    @PostMapping("/devices/genre/{tagCode}/num")
    @Deprecated
    public List<GenreVO> getGenreDevicesNum(@PathVariable("tagCode") String tagCode) {
        return deviceCenterTagService.queryGenreDevicesNum(tagCode);
    }

    /**
     * 设备按照区域分组并统计
     *
     * @param tagCode 标签编码
     *
     * @return 分组后的设备信息
     */
    @PostMapping("/device/area/{tagCode}/num")
    public List<AreaDeviceNumberDTO> getDeviceAreaNum(@PathVariable("tagCode") String tagCode) {
        return deviceCenterTagService.queryDeviceAreaNum(tagCode);
    }

    /**
     * 设备按照区域层次分组进行左匹配并统计
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/device/start-with-area/num/by-area-level")
    public List<AreaDeviceNumberDTO> queryDeviceStartWithAreaNumGroupByAreaLevel(
            @Valid @RequestBody QueryDeviceByAreaLevelAndTagParam param
                                                                                ) {
        return deviceCenterTagService.queryDeviceStartWithAreaNumGroupByAreaLevel(param.getTagCodes(), param.getAreaLevel());
    }

    /**
     * 设备按照区域进行左匹配并统计
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/device/start-with-area/num")
    public Integer queryDeviceNumByArea(@Valid @RequestBody QueryDeviceByAreaAndTagParam param) {
        return deviceCenterTagService.queryDeviceNumByArea(param.getTagCodes(), param.getAreaIds());
    }

    /**
     * 对一组设备进行经纬度分组<br/>
     * eg: 一个应用所导入的设备，对他进行经纬度分组，返回分组后的设备信息
     *
     * @param tagCode 标签编码
     *
     * @return 分组后的设备信息
     */
    @PostMapping("/device/group/{tagCode}/area-point")
    public List<AreaPointVO> getAreaPoint(@PathVariable("tagCode") String tagCode) {
        List<AreaPointVO> areaPointVOS = deviceCenterTagService.queryAreaPoint(tagCode);
        return areaPointVOS;
    }

    /**
     * 根据标签统计在线、离线、以及占比
     *
     * @param tagCode 标签编码
     *
     * @return 分组后的设备信息
     */
    @PostMapping("/device/classification/{tagCode}/offline")
    public List<DeviceOfflineCountVO> getDeviceOffline(@PathVariable("tagCode") String tagCode) {
        return deviceCenterTagService.queryDeviceOffline(tagCode);
    }

    /**
     * 查询标签下设备占比, 仅支持内置标签
     *
     * @param tagCodes 标签编码列表 {@link Const#CODE_APP_INNER_TAG_TYPE}
     *
     * @return 标签下设备占比
     */
    @PostMapping("/device/tag/percentage/inner-tag")
    public List<TagPercentageDTO> tagPercentageByInnerTag(@RequestBody List<String> tagCodes) {
        return deviceCenterTagService.tagPercentageByInnerTag(tagCodes);
    }

    /**
     * 获取设备拥有的标签<br/>
     * 如果设备不存在标签，将返回空
     *
     * @param deviceIds 设备id列表
     *
     * @return map
     */
    @PostMapping("/device/tagCode")
    public Map<String, DeviceTags> getDeviceTagCode(@RequestBody List<String> deviceIds) {
        return deviceCenterTagService.getDeviceTagCode(deviceIds);
    }

    /**
     * 根据顶层产品品类分组统计设备在线离线数量
     *
     * @param tagCode 标签编号
     *
     * @return
     */
    @GetMapping("/device/product-category/top/{tagCode}/status/count")
    public List<ProductCategoryOffLineCountVO> getTopProductCategoryStatisticsByTagCode(@PathVariable("tagCode") String tagCode) {
        return deviceCenterTagService.getTopProductCategoryStatisticsByTagCode(tagCode);
    }

    /**
     * 根据标签加一组属性编号获取设备以及属性值信息
     *
     * @param qry
     *
     * @return
     */
    @PostMapping("/device/devRelVal/by-tag/properties")
    public List<DeviceBaseAndIotPropertyInfoDTO> getDeviceBaseAndIotPropertyInfoByTagCode(
            @RequestBody @Valid DeviceBaseAndIotPropertyInfoQry qry
                                                                                         ) {
        return deviceCenterTagService.getDeviceBaseAndIotPropertyInfoByTagCode(qry.getTagCode(), qry.getIotPropertyCodes());
    }

    /**
     * 根据设备标签获取在线数和离线数
     * 创建时间可不传
     *
     * @param tagCodes
     * @param period
     *
     * @return
     */
    @GetMapping("/device/devRelVal/safety-report")
    public DeviceBaseInfoSafetyReportVO getDeviceNumberGroupOnStatusByTagCode(@RequestParam(value = "tagCodes") List<String> tagCodes,
                                                                              @RequestParam(value = "period") Period period
                                                                             ) {
        return deviceCenterTagService.getDeviceNumberGroupOnStatusByTagCode(tagCodes, period);
    }

    /**
     * 根据产品分组统计设备数量
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/device/product-devices/count")
    public List<ProductDeviceCountVO> getProductStatisticsByTagCode(@Valid @RequestBody QueryDeviceByTagParam param) {
        return deviceCenterTagService.getProductStatisticsByTagCode(param.getTagCodes());
    }

    /**
     * 设备到期数统计
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/device/expired/statistics")
    public DeviceUsefulLifeExpireStatisticsVO getDeviceExpireStatistics(@Valid @RequestBody QueryDeviceByTagParam param) {
        return deviceCenterTagService.getDeviceExpireStatistics(param.getTagCodes());
    }

    /**
     * 按品类统计设备数量
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/device/product-category/status/count")
    public List<ProductCategoryOffLineCountVO> getProductCategoryStatisticsByTagCodes(@Valid @RequestBody QueryDeviceByTagParam param) {
        return deviceCenterTagService.getProductCategoryStatisticsByTagCodes(param);
    }
}

