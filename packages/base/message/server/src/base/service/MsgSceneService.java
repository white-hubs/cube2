package com.guhecloud.cube.base.message2.server.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.message2.server.base.entity.MsgScene;
import com.guhecloud.cube.base.message2.server.base.param.MsgSceneModifyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgSceneQueryParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgSceneUpdateParam;

import java.util.Collection;
import java.util.List;

public interface MsgSceneService extends IService<MsgScene> {

    Long save(MsgSceneModifyParam param);

    void update(Long id, MsgSceneUpdateParam param);

    MsgScene getById(Long id);

    MsgScene getByCode(String code);

    Page<MsgScene> msgScenePage(MsgSceneQueryParam param);

    List<MsgScene> msgScenes(MsgSceneQueryParam param);

    List<MsgScene> listMsgScenesByApplicationCodes(Collection<String> codes);

    List<MsgScene> listMsgScenesByCodes(Collection<String> codes);

    void remove(Long id);
}
