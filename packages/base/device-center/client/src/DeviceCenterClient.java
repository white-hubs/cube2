package com.guhecloud.cube.base.device.center;

import com.guhecloud.cube.base.device.center.metadata.annotation.VersionInfo;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceFilterableByGenreQuery;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceCustomInfoParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceCustomInfoParamPage;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceSelfInfoParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceSelfInfoParamPage;
import com.guhecloud.cube.base.device.center.metadata.std.dto.AreaDeviceNumberDTO;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceInfoVO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceOfflineCountVO;
import com.guhecloud.cube.base.device.center.metadata.vo.AreaPointVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceDetailThinVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceInstanceDetailVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceListByGenreVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceSelfInfoVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicesVO;
import com.guhecloud.cube.base.device.center.metadata.vo.GenreVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.CLIENT_NAME;

@Deprecated
@FeignClient(CLIENT_NAME)
public interface DeviceCenterClient {

    /**
     * 获取设备自身信息<br/>
     * 设备没有图片使用类型图片！！
     *
     * @param queryDeviceSelfInfoParam 查询参数
     *
     * @return 设备实例详情
     */
    @PostMapping("/device/instance-self-info/list")
    List<DeviceSelfInfoVO> queryDeviceSelfInfos(@RequestBody QueryDeviceSelfInfoParam queryDeviceSelfInfoParam);

    /**
     * 获取设备自身信息<br/>
     * 设备没有图片使用类型图片！！
     *
     * @param queryDeviceSelfInfoParamPage 查询参数
     *
     * @return 设备实例详情
     */
    @PostMapping("/device/instance-self-info/page")
    Pagination<DeviceSelfInfoVO> queryDeviceSelfInfoPage(@RequestBody QueryDeviceSelfInfoParamPage queryDeviceSelfInfoParamPage);

    /**
     * 获取设备信息，自定义扩展信息查询<br/>
     * 设备没有图片使用类型图片！！
     *
     * @return 设备实例详情
     */
    @PostMapping("/device/instance-custom-info/list")
    List<DeviceInfoVO> queryCustomDeviceInfos(@RequestBody QueryDeviceCustomInfoParam queryDeviceCustomInfoParam);

    /**
     * 获取设备信息，自定义扩展信息查询<br/>
     * 设备没有图片使用类型图片！！
     *
     * @return 设备实例详情
     */
    @PostMapping("/device/instance-custom-info/page")
    Pagination<DeviceInfoVO> queryCustomDeviceInfoPage(@RequestBody QueryDeviceCustomInfoParamPage queryDeviceCustomInfoParamPage);

    /**
     * 获取设备信息
     *
     * @param deviceInstanceId 设备实例id(IotDeviceId==DeviceId)
     *
     * @return 设备实例详情
     */
    @GetMapping("/device/instance/{deviceInstanceId}")
    DeviceInstanceDetailVO detail(@PathVariable("deviceInstanceId") @NotNull Long deviceInstanceId);

    /**
     * 获取设备信息
     *
     * @param deviceInstanceId 设备实例id(IotDeviceId==DeviceId)
     *
     * @return 设备实例详情精简版
     */
    @GetMapping("/device/instance-thin/{deviceInstanceId}")
    DeviceDetailThinVO detailThin(@PathVariable("deviceInstanceId") @NotNull Long deviceInstanceId);

    /**
     * 换取设备孪生编码
     *
     * @param deviceIds 设备id集合
     *
     * @return 设备孪生编码, 设备id -> 设备孪生编码
     */
    @PostMapping("/device/queryCodeByDeviceId")
    List<Pair<String, String>> getCodeByDeviceId(@RequestBody Set<String> deviceIds);

    /**
     * 统计在线设备数量
     *
     * @param deviceIds 设备实例id集合
     *
     * @return 在线设备数量
     */
    @PostMapping("/device/online/count")
    Integer getNormalDevice(@RequestBody Set<Long> deviceIds);

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
    DeviceStateCountDTO selectNumberOfGroupByStatusFromSecurity(@RequestBody List<Long> deviceIds);

    /**
     * 集合获取设备列表
     *
     * @param deviceIds 设备实例id集合
     *
     * @return 设备列表
     */
    @PostMapping("/devices")
    List<DevicesVO> getDevicesVo(@RequestBody List<Long> deviceIds);

    /**
     * 类型统计
     */
    @PostMapping("/genre-devices-num")
    List<GenreVO> getGenreDevicesNum(@RequestBody List<Long> deviceIds);

    /**
     * 设备按照区域分组并统计
     *
     * @param deviceIds 设备实例id集合
     *
     * @return 分组后的设备信息
     */
    @PostMapping("device-area-num")
    List<AreaDeviceNumberDTO> getDeviceAreaNum(@RequestBody List<Long> deviceIds);

    /**
     * 获取所有设备id
     */
    @GetMapping("all-device")
    List<Long> getAllDevice();

    /**
     * 对一组设备进行经纬度分组<br/>
     * eg: 一个应用所导入的设备，对他进行经纬度分组，返回分组后的设备信息
     *
     * @param deviceIds 设备实例id集合
     *
     * @return 分组后的设备信息
     */
    @PostMapping("area-point")
    @VersionInfo(plannedRemove = "接下来的版本将移除，用设备标签进行替代")
    List<AreaPointVO> getAreaPoint(@RequestBody List<Long> deviceIds);

    /**
     * 根据品类统计在线、离线、以及占比
     *
     * @param deviceIds 设备实例id集合
     *
     * @return 分组后的设备信息
     */
    @PostMapping("/device-classification-offline")
    List<DeviceOfflineCountVO> getDeviceOffline(@RequestBody Set<Long> deviceIds);

    /**
     * 过滤设备后的类型分组设备实例列表
     *
     * @param filterableByGenreQuery 过滤参数
     *
     * @return 分组后的设备信息
     */
    @PostMapping("/device/instance/filtered/list-by-genre")
    List<DeviceListByGenreVO> deviceListFilterableByGenre(@RequestBody DeviceFilterableByGenreQuery filterableByGenreQuery);

    /**
     * 过滤设备后的类型分组设备实例列表
     *
     * @param filterableByGenreQuery 过滤参数
     *
     * @return 分组后的设备信息
     */
    @PostMapping("/device/instance/list-by-genre")
    List<DeviceListByGenreVO> deviceListByGenre(@RequestBody DeviceFilterableByGenreQuery filterableByGenreQuery);

    /**
     * 统计属性扩展表中已过期的设备id
     *
     * @return 已过期的设备id集合
     */
    @GetMapping("/device/instance/expiredDevices")
    List<Long> expiredDevices();

}
