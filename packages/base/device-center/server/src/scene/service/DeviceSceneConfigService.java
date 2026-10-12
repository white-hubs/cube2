package com.guhecloud.cube.base.device.center.scene.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.scene.enumeration.BusinessSceneConfigTypeEnum;
import com.guhecloud.cube.base.device.center.scene.mapper.DeviceSceneConfigMapper;
import com.guhecloud.cube.base.device.center.scene.model.DeviceSceneConfig;
import com.guhecloud.cube.base.device.center.scene.param.BusinessSceneConfigModifyParam;
import com.guhecloud.cube.biz.mdata.client.SceneTypeClient;
import com.guhecloud.cube.biz.mdata.client.dto.SceneTypeDTO;
import com.guhecloud.cube.biz.mdata.client.param.SceneTypeQueryParam;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = Exception.class)
public class DeviceSceneConfigService extends ServiceImpl<DeviceSceneConfigMapper, DeviceSceneConfig> {

    private final SceneTypeClient sceneTypeClient;

    private final VisualSceneTagService visualSceneTagService;

    /**
     * 业务场景配置保存
     *
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull BusinessSceneConfigModifyParam param) {
        SceneTypeQueryParam sceneTypeQueryParam = new SceneTypeQueryParam();
        sceneTypeQueryParam.setIds(Collections.singletonList(param.getSceneType()));
        List<SceneTypeDTO> sceneTypeDTOS = this.sceneTypeClient.listSceneTypes(sceneTypeQueryParam);
        if (CollectionUtils.isEmpty(sceneTypeDTOS)) {
            throw new ResourceNotFoundException("所选业务菜单不存在或已被删除");
        }
        Set<Long> deviceIds = visualSceneTagService.getSceneBindDevices(param);
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }
        this.remove(deviceIds, param.getSceneType());
        SceneTypeDTO sceneTypeDTO = sceneTypeDTOS.get(0);
        for (Long deviceId : deviceIds) {
            //新增
            DeviceSceneConfig businessSceneConfig = new DeviceSceneConfig();
            businessSceneConfig.setProjectId(sceneTypeDTO.getProjectId());
            businessSceneConfig.setBusinessId(deviceId.toString());
            businessSceneConfig.setBusinessType(BusinessSceneConfigTypeEnum.DEVICE);
            businessSceneConfig.setSceneType(sceneTypeDTO.getId());
            businessSceneConfig.setSceneTypeSequence(sceneTypeDTO.getSequence());
            this.baseMapper.insert(businessSceneConfig);
        }
        //绑定TAG
        visualSceneTagService.bindSceneTag(deviceIds, sceneTypeDTO.getId(), sceneTypeDTO.getName());
    }

    /**
     * 根据业务主键查询业务场景配置
     *
     * @param projectId
     * @param businessId
     *
     * @return
     */
    public List<DeviceSceneConfig> listBusinessSceneConfigByBusinessId(@NonNull Long projectId, @NonNull String businessId) {
        return this.lambdaQuery()
                .eq(DeviceSceneConfig::getProjectId, projectId)
                .eq(DeviceSceneConfig::getBusinessId, businessId).list();
    }

    /**
     * 根据业务主键查询业务场景配置
     *
     * @param businessId
     *
     * @return
     */
    public List<DeviceSceneConfig> listBusinessSceneConfigByBusinessId(@NonNull String businessId) {
        return this.lambdaQuery()
                .eq(DeviceSceneConfig::getBusinessId, businessId).list();
    }

    /**
     * 根据场景类型查询业务场景配置
     *
     * @param sceneType
     *
     * @return
     */
    public List<DeviceSceneConfig> listBusinessSceneConfigBySceneType(@NonNull Long sceneType) {
        return this.lambdaQuery().eq(DeviceSceneConfig::getSceneType, sceneType).list();
    }

    /**
     * 根据业务主键加场景类型查询业务场景配置
     *
     * @param businessId
     * @param sceneType
     *
     * @return
     */
    public DeviceSceneConfig getByBusinessScene(@NonNull String businessId, @NonNull Long sceneType) {
        return this.baseMapper.selectOne(Wrappers.<DeviceSceneConfig>lambdaQuery()
                                                 .eq(DeviceSceneConfig::getBusinessId, businessId)
                                                 .eq(DeviceSceneConfig::getSceneType, sceneType));
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Set<Long> businessIds, @NonNull Long sceneType) {
        Set<String> businessIdStrings = businessIds.stream().map(Object::toString).collect(Collectors.toSet());
        this.baseMapper.delete(Wrappers.<DeviceSceneConfig>lambdaQuery()
                                       .in(DeviceSceneConfig::getBusinessId, businessIdStrings)
                                       .eq(DeviceSceneConfig::getSceneType, sceneType));
        this.visualSceneTagService.unBindSceneTag(sceneType, businessIds);
    }
}
