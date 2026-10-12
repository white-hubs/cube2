package com.guhecloud.cube.biz.mdata.server.scene.controller;

import com.guhecloud.cube.biz.mdata.server.scene.entity.BusinessSceneConfig;
import com.guhecloud.cube.biz.mdata.server.scene.param.BusinessSceneConfigRemoveParam;
import com.guhecloud.cube.biz.mdata.server.scene.param.StallSceneConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.service.StallSceneConfigService;
import com.guhecloud.cube.biz.mdata.server.scene.vo.BusinessSceneConfigVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;

/**
 * 档位场景配置接口
 *
 * @author lanchun
 * @date 2021/12/2 15:12
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallSceneConfigController {

    private final StallSceneConfigService stallSceneConfigService;

    /**
     * 档位场景配置新增
     *
     * @param param 参数
     *
     * @return
     */
    @PostMapping("/stall-scene-config")
    public void save(@Valid @RequestBody StallSceneConfigModifyParam param) {
        this.stallSceneConfigService.save(param);
    }

    /**
     * 档位场景配置列表(根据业务主键查询)
     *
     * @param stallId   业务主键
     * @param projectId 项目id
     *
     * @return
     */
    @GetMapping("/device-scene-configs/business/{stallId}")
    public List<BusinessSceneConfigVO> listBusinessSceneConfigsByBusinessId(@PathVariable("stallId") Long stallId,
                                                                            @RequestParam("projectId") Long projectId
                                                                           ) {
        List<BusinessSceneConfig> businessSceneConfigs = this.stallSceneConfigService.listBusinessSceneConfigByBusinessId(projectId, stallId);
        if (CollectionUtils.isEmpty(businessSceneConfigs)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(businessSceneConfigs, BusinessSceneConfigVO.class);
    }

    /**
     * 档位场景配置删除
     *
     * @param sceneType 场景类型
     * @param param     删除参数
     */
    @DeleteMapping("/stall-scene-config/{sceneType}/delete/stalls")
    public void remove(@PathVariable("sceneType") Long sceneType, @RequestBody @Valid BusinessSceneConfigRemoveParam param) {
        this.stallSceneConfigService.remove(param.getBusinessIds(), sceneType);
    }
}
