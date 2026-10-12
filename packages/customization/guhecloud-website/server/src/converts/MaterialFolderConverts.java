package com.guhecloud.cube.customized.guhecloudwebsite.converts;

import com.guhecloud.cube.customized.guhecloudwebsite.entity.MaterialFolder;
import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialFolderParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.MaterialFolderVO;

import java.util.List;

/**
 * 素材文件夹转换器
 */
public class MaterialFolderConverts {

    /**
     * 实体转VO
     */
    public static MaterialFolderVO toVO(MaterialFolder entity) {
        if (entity == null) {
            return null;
        }
        MaterialFolderVO vo = new MaterialFolderVO();
        vo.setId(entity.getId());
        vo.setPathId(entity.getPathId());
        vo.setName(entity.getName());
        vo.setSort(entity.getSort());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    /**
     * 实体列表转VO列表
     */
    public static List<MaterialFolderVO> toVOList(List<MaterialFolder> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(MaterialFolderConverts::toVO)
                .toList();
    }

    /**
     * 参数转实体
     */
    public static MaterialFolder toEntity(MaterialFolderParam param) {
        if (param == null) {
            return null;
        }
        MaterialFolder entity = new MaterialFolder();
        entity.setPathId(param.getPathId());
        entity.setName(param.getName());
        entity.setSort(param.getSort());
        return entity;
    }

}
