package com.guhecloud.cube.biz.mdata.server.area.controller;

import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaGroup;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaGroupConfig;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaGroupConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaGroupConfigService;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaGroupService;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaService;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaGroupConfigVO;
import com.guhecloud.platform.base.standard.exception.Assert;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author lanchun
 * @date 2022/4/18 15:24
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaGroupConfigController {

    private final AreaGroupConfigService areaGroupConfigService;

    private final AreaGroupService areaGroupService;

    private final AreaService areaService;

    /**
     * 区域组配置区域保存
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/area-group-config")
    public Long save(@Valid @RequestBody AreaGroupConfigModifyParam param) {
        AreaGroup areaGroup = this.areaGroupService.getById(param.getAreaGroupId());
        Assert.isNotFound(areaGroup, "区域组不存在");
        Area area = this.areaService.getById(param.getAreaId());
        Assert.isNotFound(area, "区域不存在");
        param.setAreaGroup(areaGroup.getSequence().substring(0, areaGroup.getSequence().lastIndexOf("-")));
        param.setArea(area.getSequence().substring(0, area.getSequence().lastIndexOf("-")));
        return this.areaGroupConfigService.save(param);
    }

    /**
     * 区域组获取配置区域列表
     *
     * @param areaGroupId
     *
     * @return
     */
    @GetMapping("/area-group-configs/{areaGroupId}")
    public List<AreaGroupConfigVO> listAreaGroupConfigs(@PathVariable("areaGroupId") Long areaGroupId) {
        List<AreaGroupConfig> areaGroupConfigs = this.areaGroupConfigService.listAreaGroupsAreas(areaGroupId);
        if (CollectionUtils.isEmpty(areaGroupConfigs)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(areaGroupConfigs, AreaGroupConfigVO.class);
    }

    /**
     * 区域组配置删除
     *
     * @param areaGroupId
     * @param areaId
     */
    @DeleteMapping("/area-group-configs/{areaGroupId}/{areaId}")
    public void remove(@PathVariable("areaGroupId") Long areaGroupId, @PathVariable("areaId") Long areaId) {
        this.areaGroupConfigService.remove(areaGroupId, areaId);
    }
}
