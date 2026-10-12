package com.guhecloud.cube.srv.gen.device.supervise.client;

import com.guhecloud.cube.srv.gen.device.supervise.client.dto.DeviceAvgHealthDTO;
import com.guhecloud.cube.srv.gen.device.supervise.client.dto.DeviceHealthValueDTO;
import com.guhecloud.cube.srv.gen.device.supervise.client.query.DeviceHealthValueQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/8/10 15:04
 * @description
 */
@FeignClient(ServiceRoute.TAG)
public interface HealthValueClient {
    /**
     * 获取场景的健康值
     *
     * @param sceneType
     *
     * @return
     */
    @GetMapping("/device/instance/analysis/health/value/scene-type")
    Integer getHealthValueBySceneType(@RequestParam("sceneType") Long sceneType);

    /**
     * 批量获取设备健康值
     *
     * @return
     */
    @PostMapping("/device/instance/analysis/health/value/last/batch")
    List<DeviceHealthValueDTO> getHealthValue(@RequestBody DeviceHealthValueQuery query);

    /**
     * 获取设备标签的健康值
     *
     * @param deviceTag
     *
     * @return
     */
    @GetMapping("/device/instance/analysis/health/avg-value")
    DeviceAvgHealthDTO getHealthValueByTag(@RequestParam("sceneType") String deviceTag);
}
