package com.guhecloud.cube.iot.north.bridge.client;

import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceSamplingTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropHistoryPageParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropHistoryParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceSamplingPageParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.HistorySampleParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiHistorySampleParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

import static com.guhecloud.cube.iot.north.bridge.client.Profile.CLIENT_NAME;

/**
 *
 */
@FeignClient(name = CLIENT_NAME)
public interface IotDevicePropClient {

    /**
     * 查询设备属性
     *
     * @param devicePropParam devicePropParam
     *
     * @return List<KeyValueWithTsDTO>
     */
    @GetMapping("/north/device/prop")
    List<KeyValueWithTsDTO> deviceProp(@SpringQueryMap @Valid DevicePropParam devicePropParam);

    /**
     * 查询多设备属性<br/>
     * eg: deviceId1.propA,deviceId2.propB === 1329841713324.temp,1329841713324.hum
     *
     * @param multiDevicePropParam multiDevicePropParam
     *
     * @return Map<String, List < KeyValueWithTsDTO>> key: deviceId
     */
    @PostMapping("/north/device/prop/multi-prop")
    Map<String, List<KeyValueWithTsDTO>> multiProp(@RequestBody @Valid MultiDevicePropParam multiDevicePropParam);

    /**
     * 获取设备历史采样数据
     *
     * @param historySampleParam historySampleItemParam
     *
     * @return DeviceTSDTO
     */
    @GetMapping("/north/device/prop/history-sample-prop")
    DeviceTSDTO historySampleProp(@SpringQueryMap @Valid HistorySampleParam historySampleParam);

    /**
     * 单设备-历史属性分页获取
     *
     * @param historySampleParam historySampleItemParam
     *
     * @return List<KeyValueWithTs>
     */
    @GetMapping("/north/device/prop/history-prop-page")
    Pagination<KeyValueWithTsDTO> historyPropPage(@SpringQueryMap @Valid DevicePropHistoryPageParam historySampleParam);

    /**
     * 单设备-采样分页获取<br/>
     * 目前只支持单个设备的多个属性的历史采样数据，且只支持10分钟的时间粒度
     *
     * @param deviceSamplingPageParam deviceSamplingPageParam
     *
     * @return Pagination<DeviceSamplingTSDTO>
     */
    @GetMapping("/north/device/prop/device-history-page")
    Pagination<DeviceSamplingTSDTO> deviceHistorySamplingPage(@SpringQueryMap @Valid DeviceSamplingPageParam deviceSamplingPageParam);

    /**
     * 单设备-历史属性获取
     *
     * @param devicePropHistoryParam devicePropHistoryParam
     *
     * @return List<KeyValueWithTs>
     */
    @GetMapping("/north/device/prop/history")
    List<KeyValueWithTsDTO> devicePropHistoryProp(@SpringQueryMap DevicePropHistoryParam devicePropHistoryParam);

    /**
     * 单设备-历史属性获取<br/>
     * 将多个属性进行归类，便于前端展示,注意:最大返回记录数为单个属性的最大记录数
     *
     * @param devicePropHistoryParam devicePropHistoryParam
     *
     * @return List<KeyValueWithTs>
     */
    @GetMapping("/north/device/prop/history-group")
    Map<String, List<KeyValueWithTsDTO>> devicePropHistoryPropGroup(@SpringQueryMap DevicePropHistoryParam devicePropHistoryParam);

    /**
     * 多设备-历史属性采样获取
     *
     * @param multiHistorySampleParam 查询参数
     *
     * @return map key:设备id value:设备采样数据
     */
    @PostMapping("/north/device/prop/multi-history-sample-prop")
    Map<String, DeviceTSDTO> multiHistorySampleProp(@RequestBody @Valid MultiHistorySampleParam multiHistorySampleParam);
}
