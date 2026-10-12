package com.guhecloud.cube.base.device.center.std.statistics;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceStatisticsNumDTO;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByAreasOrGroupsAndTagParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByTagParam;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceBaseInfoSafetyReportVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceStateStatisticsVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceUsefulLifeExpireStatisticsVO;
import com.guhecloud.cube.base.device.center.metadata.vo.ProductCategoryOffLineCountVO;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.CLIENT_NAME;

/**
 * @author pengjian
 * @date 2024/1/10 14:10
 * @description 设备基础统计
 */
@FeignClient(CLIENT_NAME)
public interface DeviceBaseStatisticsClient {
    /**
     * 统计在线设备数量
     *
     * @param tagTypeCode 标签类型编码
     * @param tagCode     标签编码
     *
     * @return 在线设备数量
     */
    @Deprecated
    @GetMapping("/device/{tagTypeCode}/{tagCode}/online/count")
    Integer onlineDeviceCount(@PathVariable("tagTypeCode") String tagTypeCode, @PathVariable("tagCode") String tagCode);

    /**
     * @param tagCodes
     *
     * @return
     */
    @GetMapping("/device/tagCode/status/count")
    DeviceStateCountDTO countingNumberOfGroupByStatusFromSecurity(@RequestParam(value = "tagCodes", required = false) List<String> tagCodes
                                                                 );

    /**
     * @description: 获取产品品类下的设备数量
     * @author: lijie
     * @date: 2024/4/2 13:51
     * @param:
     * @return:
     **/
    @GetMapping("/getDevicesNumber")
    List<DeviceStatisticsNumDTO> getDevicesNumber(@RequestParam("period") String period);

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
    DeviceBaseInfoSafetyReportVO getDeviceNumberGroupOnStatusByTagCode(@RequestParam(value = "tagCodes") List<String> tagCodes,
                                                                       @RequestParam(value = "period") Period period
                                                                      );

    /**
     * 设备到期数统计
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/device/expired/statistics")
    DeviceUsefulLifeExpireStatisticsVO getDeviceExpireStatistics(@Valid @RequestBody QueryDeviceByTagParam param);

    /**
     * 按品类统计设备数量
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/device/product-category/status/count")
    List<ProductCategoryOffLineCountVO> getProductCategoryStatisticsByTagCodes(@Valid @RequestBody QueryDeviceByTagParam param);

    /**
     * 根据区域集合或分组集合获取设备数量
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/devices/count/by-areas-or-groups-and-tag")
    Integer deviceCountByAreasOrGroupsAndTag(@Valid @RequestBody QueryDeviceByAreasOrGroupsAndTagParam param);

    /**
     * 获取设备状态统计信息
     *
     * @param ids
     *
     * @return
     */
    @PostMapping("/device/state/statistics/by-ids")
    Map<Long, DeviceStateStatisticsVO> deviceStatistics(@RequestBody Set<Long> ids);
}
