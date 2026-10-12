package com.guhecloud.cube.biz.mdata.server.scene.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.scene.entity.SceneType;
import com.guhecloud.cube.biz.mdata.server.scene.param.SceneTypeModifyParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author lzy
 * @date 2021/11/16
 */
public interface SceneTypeService extends IService<SceneType> {

    Long save(SceneTypeModifyParam param);

    void update(Long id, SceneTypeModifyParam param);

    SceneType getById(Serializable id);

    List<SceneType> list(Long projectId);

    List<SceneType> getByIds(Long projectId, Collection<Long> ids);

    List<SceneType> listByLevel(Long projectId, Integer level);

    List<SceneType> getSequence(Long projectId, List<String> sequences);

    SceneType getByName(Long projectId, String name);

    SceneType getByCode(Long projectId, String code);

    void remove(Long id);

}
