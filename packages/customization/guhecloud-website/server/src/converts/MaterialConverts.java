package com.guhecloud.cube.customized.guhecloudwebsite.converts;

import com.guhecloud.cube.customized.guhecloudwebsite.entity.Material;
import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.MaterialVO;

import java.util.List;

/**
 * 素材转换器
 */
public class MaterialConverts {

    /**
     * 实体转VO
     */
    public static MaterialVO toVO(Material entity) {
        if (entity == null) {
            return null;
        }
        MaterialVO vo = new MaterialVO();
        vo.setId(entity.getId());
        vo.setPathId(entity.getPathId());
        vo.setName(entity.getName());
        vo.setExt(entity.getExt());
        vo.setMimeType(entity.getMimeType());
        vo.setSize(entity.getSize());
        vo.setFileId(entity.getFileId());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    /**
     * 实体列表转VO列表
     */
    public static List<MaterialVO> toVOList(List<Material> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(MaterialConverts::toVO)
                .toList();
    }

    /**
     * 参数转实体
     */
    public static Material toEntity(MaterialParam param) {
        if (param == null) {
            return null;
        }
        Material entity = new Material();
        entity.setId(param.getId());
        entity.setPathId(param.getFileId());
        return entity;
    }

}
