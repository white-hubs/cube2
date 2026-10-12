package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.ScenePointDeviceBaseVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectScenePointDevicePO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 现场点关联设备
 */
public interface InspectScenePointDeviceService extends IService<InspectScenePointDevicePO> {

    /**
     * 用新的设备ID列表覆盖现场点下的设备关联（空列表表示清空）
     */
    void replaceScenePointDevices(Long scenePointId, List<Long> deviceIdList);

    /**
     * 按现场点批量逻辑删除关联
     */
    void removeByScenePointIds(Collection<Long> scenePointIds);

    /**
     * 批量查询现场点关联的设备ID
     */
    Map<Long, List<Long>> mapDeviceIdsByScenePointIds(Collection<Long> scenePointIds);

    /**
     * 批量查询现场点关联的设备基础信息(id、name)
     */
    Map<Long, List<ScenePointDeviceBaseVO>> mapDevicesByScenePointIds(Collection<Long> scenePointIds);
}
