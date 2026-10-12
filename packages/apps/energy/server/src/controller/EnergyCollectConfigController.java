package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectConfigParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectConfigQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyCollectConfigService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyCollectConfigVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Appet
 * @Description 能耗采集配置
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyCollectConfigController {
    private final EnergyCollectConfigService energyCollectConfigService;

    /**
     * 功能描述： 能耗采集配置添加
     */
    @PostMapping(value = "/energy-collect-config")
    public Long add(@Valid @RequestBody EnergyCollectConfigParam param) {
        return energyCollectConfigService.add(param);
    }

    /**
     * 功能描述： 能耗采集配置修改
     */
    @PutMapping(value = "/energy-collect-config/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EnergyCollectConfigParam param) {
        energyCollectConfigService.update(id, param);
    }

    /**
     * 功能描述： 能耗采集配置删除
     */
    @DeleteMapping(value = "/energy-collect-config/{id}")
    public void delete(@PathVariable Long id) {
        energyCollectConfigService.delete(id);
    }

    /**
     * 功能描述： 能耗采集配置批量删除
     */
    @DeleteMapping(value = "/energy-collect-config/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        energyCollectConfigService.batchDelete(ids);
    }

    /**
     * 功能描述： 能耗采集配置分页
     */
    @GetMapping(value = "/energy-collect-config/page")
    public Pagination<EnergyCollectConfigVO> getPage(EnergyCollectConfigQueryParam param) {
        return energyCollectConfigService.getPage(param);
    }

    /**
     * 查询列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/energy-collect-config/list")
    public List<EnergyCollectConfigVO> getList(EnergyCollectConfigQueryParam param) {
        return energyCollectConfigService.getList(param);
    }

    /**
     * 功能描述： 能耗采集配置详情
     */
    @GetMapping(value = "/energy-collect-config/{id}")
    public EnergyCollectConfigVO getById(@PathVariable Long id) {
        return energyCollectConfigService.getById(id);
    }

}