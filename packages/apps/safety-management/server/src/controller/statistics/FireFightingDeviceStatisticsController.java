package com.guhecloud.cube.srv.gen.safety.management.controller.statistics;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.FireFightingDeviceQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.device.DeviceApplicationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.FireFightingDeviceVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.FireFightingDeviceStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.device.DeviceCategoryCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.device.DeviceCountStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.DeviceStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.FireFightingDeviceStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * 消防设备统计
 *
 * @author wql
 * @describe 消防设备统计
 * @date 2023/9/8 13:54
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FireFightingDeviceStatisticsController {

    private final FireFightingDeviceStatisticsService fireFightingDeviceStatisticsService;

    private final MarketConfigService marketConfigService;
    private final DeviceStatisticsService deviceStatisticsService;

    /**
     * 消防设备统计
     *
     * @Author wql
     * @Description 消防设备统计
     * @Date 13:47 2023/9/8
     **/
    @GetMapping("/device/deviceFiling")
    public FireFightingDeviceStatisticsVO getFireFightingDeviceStatistics(BaseQuery query) {
        return fireFightingDeviceStatisticsService.geFireFightingDeviceStatistics(query);
    }

    /**
     * 消防设备列表
     *
     * @Author wql
     * @Description 消防设备列表
     * @Date 14:22 2023/9/8
     **/
    @GetMapping("/device/fireFightingDeviceList")
    public Pagination<FireFightingDeviceVO> getFireFightingDeviceList(@Valid FireFightingDeviceQuery fireFightingDevice) {
        Pagination<FireFightingDeviceVO> fireFightingDeviceList = fireFightingDeviceStatisticsService.getFireFightingDeviceList(fireFightingDevice);
        if (CollectionUtils.isEmpty(fireFightingDeviceList.getRecords())) {
            return fireFightingDeviceList;
        }
        List<MarketConfigPO> marketConfigPOS = this.marketConfigService.list();
        fireFightingDeviceList.getRecords().stream().forEach(fireFightingDeviceVO -> {
            if (StringUtils.isNotEmpty(fireFightingDeviceVO.getMarketTenant())) {
                int marketTenant = Integer.parseInt(fireFightingDeviceVO.getMarketTenant());
                Optional<MarketConfigPO> marketConfigOptional = marketConfigPOS
                        .stream()
                        .filter(marketConfigPO -> marketConfigPO.getMarketTenant().intValue() == marketTenant)
                        .findFirst();
                marketConfigOptional.ifPresent(marketConfigPO ->
                                                       fireFightingDeviceVO.setMarketName(marketConfigPO.getMarketName())
                                              );
            }
        });
        return fireFightingDeviceList;
    }

    /**
     * 消防设备类别列表
     *
     * @Author wql
     * @Description 消防设备类别列表
     * @Date 9:05 2023/9/13
     **/
    @GetMapping("/device/getDeviceTypeList")
    public List<String> getDeviceTypeList() {
        return fireFightingDeviceStatisticsService.getDeviceTypeList();
    }

    /**
     * 消防设备品类分组数量统计
     *
     * @param fireFightingDevice
     *
     * @return
     */
    @GetMapping("/device/fire/category/stat")
    public List<DeviceCategoryCountVO> getDeviceCategoryCount(FireFightingDeviceQuery fireFightingDevice) {
        return fireFightingDeviceStatisticsService.getDeviceCategoryCount(fireFightingDevice);
    }

    /**
     * 根据应用种类统计设备状态数
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-device-statistics/count/status/application")
    List<DeviceCountStatusVO> countStatusByApplication(DeviceApplicationStatisticsQuery query) {
        return deviceStatisticsService.countStatusByApplication(query);
    }
}
