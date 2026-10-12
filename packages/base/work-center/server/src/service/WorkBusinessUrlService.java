package com.guhecloud.cube.biz.work.center.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.work.center.server.entity.WorkBusinessUrl;
import com.guhecloud.cube.biz.work.center.server.param.WorkBusinessUrlModifyParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface WorkBusinessUrlService extends IService<WorkBusinessUrl> {

    Long save(WorkBusinessUrlModifyParam param);

    void update(Long id, WorkBusinessUrlModifyParam param);

    WorkBusinessUrl getById(Serializable id);

    List<WorkBusinessUrl> listByBusinessTypes(Collection<String> businessTypes, String terminal);
}
