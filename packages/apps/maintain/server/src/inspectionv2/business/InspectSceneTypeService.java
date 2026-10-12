package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectSceneTypePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.SceneTypeVO;

import java.util.List;

/**
 * @author wql
 * @describe 现场点类型
 * @date 2023/6/5 15:53
 */
public interface InspectSceneTypeService extends IService<InspectSceneTypePO> {
    /**
     * @Author wql
     * @Description 现场点类型列表
     * @Date 16:30 2023/6/5
     **/
    void addType(String typeName);

    /**
     * @Author wql
     * @Description 现场点类型列表
     * @Date 16:30 2023/6/5
     **/
    List<SceneTypeVO> sceneTypeList();

    /**
     * @Author wql
     * @Description 更新现场点类型信息
     * @Date 17:00 2023/6/5
     **/
    void updateType(String typeId, String typeName);

    /**
     * @Author wql
     * @Description 删除现场点类型信息
     * @Date 17:03 2023/6/5
     **/
    void deleteType(String typeId);
}
