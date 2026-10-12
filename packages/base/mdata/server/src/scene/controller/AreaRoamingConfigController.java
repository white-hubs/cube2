package com.guhecloud.cube.biz.mdata.server.scene.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaService;
import com.guhecloud.cube.biz.mdata.server.scene.entity.AreaRoamingConfig;
import com.guhecloud.cube.biz.mdata.server.scene.param.AreaRoamingConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.param.AreaRoamingConfigQueryParam;
import com.guhecloud.cube.biz.mdata.server.scene.service.AreaRoamingConfigService;
import com.guhecloud.cube.biz.mdata.server.scene.vo.AreaRoamingConfigPageVO;
import com.guhecloud.cube.biz.mdata.server.scene.vo.AreaRoamingConfigVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义漫游接口管理
 *
 * @author lanchun
 * @date 2022/9/19 16:21
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaRoamingConfigController {

    private final AreaRoamingConfigService areaRoamingConfigService;

    private final AreaService areaService;

    /**
     * 漫游配置新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/area-roaming-config")
    public Long save(@Valid @RequestBody AreaRoamingConfigModifyParam param) {
        return this.areaRoamingConfigService.save(param);
    }

    /**
     * 漫游配置修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/area-roaming-config/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody AreaRoamingConfigModifyParam param) {
        this.areaRoamingConfigService.update(id, param);
    }

    /**
     * 漫游配置详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/area-roaming-config/{id}")
    public AreaRoamingConfigVO getAreaRoamingConfig(@PathVariable("id") Long id) {
        AreaRoamingConfig areaRoamingConfig = this.areaRoamingConfigService.getById(id);
        Assert.isNotFound(areaRoamingConfig, "漫游配置不存在");
        AreaRoamingConfigVO areaRoamingConfigVO = ObjectUtils.copying(areaRoamingConfig, AreaRoamingConfigVO.class);
        areaRoamingConfigVO.setAreaIds(Arrays.asList(areaRoamingConfig.getAreas().split("-")));
        return areaRoamingConfigVO;
    }

    /**
     * 漫游配置分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/area-roaming-config/page")
    public Pagination<AreaRoamingConfigPageVO> getPage(AreaRoamingConfigQueryParam param) {
        Page<AreaRoamingConfig> areaRoamingConfigPage = this.areaRoamingConfigService.page(param);
        List<AreaRoamingConfigPageVO> areaRoamingConfigPageVOS = ObjectUtils.copying(areaRoamingConfigPage.getRecords(), AreaRoamingConfigPageVO.class);
        if (CollectionUtils.isEmpty(areaRoamingConfigPageVOS)) {
            return Pagination.empty();
        }
        areaRoamingConfigPageVOS.stream().forEach(areaRoamingConfigPageVO -> {
            // 扩展区域信息
            List<String> areaIds = Arrays.asList(areaRoamingConfigPageVO.getAreas().split("-"));
            List<Area> areas = this.areaService.getByIds(areaIds.stream().map(Long::parseLong).collect(Collectors.toList()));
            areaRoamingConfigPageVO.setAreaNames(areas.stream().map(Area::getName).collect(Collectors.toList()));
        });
        return PagingUtils.convert(areaRoamingConfigPage, areaRoamingConfigPageVOS);
    }

    /**
     * 漫游配置列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/area-roaming-configs")
    public List<AreaRoamingConfigVO> getAreaRoamingConfigs(AreaRoamingConfigQueryParam param) {
        List<AreaRoamingConfig> areaRoamingConfigs = this.areaRoamingConfigService.listAreaRoamingConfigs(param);
        return ObjectUtils.copying(areaRoamingConfigs, AreaRoamingConfigVO.class);
    }

    /**
     * 漫游配置删除
     *
     * @param id
     */
    @DeleteMapping("/area-roaming-config/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.areaRoamingConfigService.remove(id);
    }
}
