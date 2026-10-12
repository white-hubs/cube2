package com.guhecloud.cube.biz.mdata.server.area.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaGroupConfig;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaGroupConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.stat.param.AreaStatQueryParam;

import java.util.Collection;
import java.util.List;

public interface AreaGroupConfigService extends IService<AreaGroupConfig> {

    Long save(AreaGroupConfigModifyParam param);

    AreaGroupConfig getAreaGroupAreas(Long areaGroupId, Long areaId);

    AreaGroupConfig getAreaGroupAreas(String areaGroup, String area);

    List<AreaGroupConfig> listAreaGroupsAreas(Long areaGroupId);

    List<AreaGroupConfig> listAreaGroupsAreas(Collection<Long> areaGroupIds);

    Page<AreaGroupConfig> pageAreaGroupsStatAreas(AreaStatQueryParam queryParam);

    void remove(Long areaGroupId);

    void remove(Long areaGroupId, Long areaId);
}
