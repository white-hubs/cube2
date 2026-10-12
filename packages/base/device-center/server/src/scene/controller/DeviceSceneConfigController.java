package com.guhecloud.cube.base.device.center.scene.controller;

import com.guhecloud.cube.base.device.center.scene.model.DeviceSceneConfig;
import com.guhecloud.cube.base.device.center.scene.param.BusinessSceneConfigModifyParam;
import com.guhecloud.cube.base.device.center.scene.param.BusinessSceneConfigRemoveParam;
import com.guhecloud.cube.base.device.center.scene.service.DeviceSceneConfigService;
import com.guhecloud.cube.base.device.center.scene.vo.BusinessSceneConfigVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 设施场景配置接口
 *
 * @author lanchun
 * @date 2021/12/2 15:12
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceSceneConfigController {

    private final DeviceSceneConfigService deviceSceneConfigService;

    /**
     * 设备场景配置新增
     *
     * @param param 参数
     *
     * @return
     */
    @PostMapping("/device-scene-config")
    public void save(@Valid @RequestBody BusinessSceneConfigModifyParam param) {
        this.deviceSceneConfigService.save(param);
    }

    /**
     * 设施场景配置详情
     *
     * @param businessId 设施主键
     * @param sceneType  场景类型
     *
     * @return
     */
    @GetMapping("/device-scene-config/{businessId}/{sceneType}")
    public BusinessSceneConfigVO getBusinessSceneConfig(@PathVariable("businessId") String businessId,
                                                        @PathVariable("sceneType") Long sceneType
                                                       ) {
        DeviceSceneConfig businessSceneConfig = this.deviceSceneConfigService.getByBusinessScene(businessId, sceneType);
        if (Objects.isNull(businessSceneConfig)) {
            return null;
        }
        return ObjectUtils.copying(businessSceneConfig, BusinessSceneConfigVO.class);
    }

    /**
     * 设施场景配置列表(根据业务主键查询)
     *
     * @param businessId 业务主键
     * @param projectId  项目id
     *
     * @return
     */
    @GetMapping("/device-scene-configs/business/{businessId}")
    public List<BusinessSceneConfigVO> listBusinessSceneConfigsByBusinessId(@PathVariable("businessId") String businessId,
                                                                            @RequestParam("projectId") Long projectId
                                                                           ) {
        List<DeviceSceneConfig> businessSceneConfigs = this.deviceSceneConfigService.listBusinessSceneConfigByBusinessId(projectId, businessId);
        if (CollectionUtils.isEmpty(businessSceneConfigs)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(businessSceneConfigs, BusinessSceneConfigVO.class);
    }

    /**
     * 设施场景配置列表(根据场景类型查询)
     *
     * @param sceneType 场景类型
     *
     * @returnd
     */
    @GetMapping("/device-scene-configs/scene/{sceneType}")
    public List<BusinessSceneConfigVO> listBusinessSceneConfigsBySceneType(@PathVariable("sceneType") Long sceneType) {
        List<DeviceSceneConfig> businessSceneConfigs = this.deviceSceneConfigService.listBusinessSceneConfigBySceneType(sceneType);
        if (CollectionUtils.isEmpty(businessSceneConfigs)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(businessSceneConfigs, BusinessSceneConfigVO.class);
    }

    /**
     * 设施场景配置删除
     *
     * @param sceneType 场景类型
     * @param param     删除参数
     */
    @PostMapping("/device-scene-config/{sceneType}/delete/devices")
    public void remove(@PathVariable("sceneType") Long sceneType, @Valid @RequestBody BusinessSceneConfigRemoveParam param) {
        this.deviceSceneConfigService.remove(param.getBusinessIds(), sceneType);
    }
}
