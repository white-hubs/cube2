package com.guhecloud.cube.biz.mdata.server.area.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaGroup;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaGroupModifyParam;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaGroupQueryParam;

import java.io.Serializable;
import java.util.List;

public interface AreaGroupService extends IService<AreaGroup> {

    Long save(AreaGroupModifyParam param);

    void update(Long id, AreaGroupModifyParam param);

    AreaGroup getById(Serializable id);

    List<AreaGroup> listAreaGroups(AreaGroupQueryParam param);

    List<AreaGroup> getSequence(List<String> sequences);

    List<AreaGroup> getByParentId(Long parentId);

    List<AreaGroup> getFirstParent();

    void remove(Long id);
}
