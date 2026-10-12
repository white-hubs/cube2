package com.guhecloud.cube.biz.mdata.server.scene.controller;

import com.guhecloud.cube.biz.mdata.server.scene.entity.BusinessPanelConfig;
import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessPanelPositionEnum;
import com.guhecloud.cube.biz.mdata.server.scene.param.BusinessPanelConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.service.BusinessPanelConfigService;
import com.guhecloud.cube.biz.mdata.server.scene.vo.BusinessPanelConfigVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 设施面板配置管理接口
 *
 * @author lanchun
 * @date 2022/4/1 16:03
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BusinessPanelConfigController {

    private final BusinessPanelConfigService businessPanelConfigService;

    /**
     * 设施面板配置保存
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/business-panel-config")
    public Long save(@Valid @RequestBody BusinessPanelConfigModifyParam param) {
        return this.businessPanelConfigService.save(param);
    }

    /**
     * 业务主键查询单个面板配置
     *
     * @param businessId
     * @param projectId
     *
     * @return
     */
    @GetMapping("/business-panel-config/{businessId}")
    public BusinessPanelConfigVO getBusinessPanelByBusinessId(@PathVariable("businessId") Long businessId,
                                                              @RequestParam("projectId") Long projectId
                                                             ) {
        BusinessPanelConfig businessPanelConfig = this.businessPanelConfigService.getByBusinessId(projectId, businessId);
        return ObjectUtils.copying(businessPanelConfig, BusinessPanelConfigVO.class);
    }

    /**
     * 业务主键查询面板配置列表
     *
     * @param businessId
     * @param projectId
     *
     * @return
     */
    @GetMapping("/business-panel-configs/{businessId}")
    public List<BusinessPanelConfigVO> listBusinessPanelsByBusinessId(@PathVariable("businessId") Long businessId,
                                                                      @RequestParam("projectId") Long projectId
                                                                     ) {
        List<BusinessPanelConfig> businessPanelConfigs = this.businessPanelConfigService.listByBusinessId(projectId, businessId);
        if (CollectionUtils.isEmpty(businessPanelConfigs)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(businessPanelConfigs, BusinessPanelConfigVO.class);
    }

    /**
     * 设施面板配置删除
     *
     * @param businessId 场景类型
     */
    @DeleteMapping("/business-panel-config/{businessId}")
    public void remove(@PathVariable("businessId") Long businessId,
                       @RequestParam("projectId") Long projectId
                      ) {
        this.businessPanelConfigService.remove(projectId, businessId);
    }

    /**
     * 设施面板配置删除
     *
     * @param businessId
     * @param panelPosition
     * @param projectId
     */
    @DeleteMapping("/business-panel-config/{businessId}/{panelPosition}")
    public void remove(@PathVariable("businessId") Long businessId, @PathVariable("panelPosition") BusinessPanelPositionEnum panelPosition,
                       @RequestParam("projectId") Long projectId
                      ) {
        this.businessPanelConfigService.remove(projectId, businessId, panelPosition);
    }
}
