package com.guhecloud.cube.customized.guhecloudwebsite.converts;

import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSiteLayout;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteLayoutAddParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteLayoutUpdateParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSiteLayoutVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 布局对象转换
 */
public final class SysSiteLayoutConverts {

    private SysSiteLayoutConverts() {
    }

    /**
     * 新增参数转实体
     */
    public static SysSiteLayout toEntity(SysSiteLayoutAddParam param) {
        SysSiteLayout entity = new SysSiteLayout();
        entity.setLayoutName(param.getLayoutName());
        entity.setConfig(param.getConfig());
        return entity;
    }

    /**
     * 编辑参数转实体
     */
    public static SysSiteLayout toEntity(SysSiteLayoutUpdateParam param) {
        SysSiteLayout entity = new SysSiteLayout();
        entity.setId(param.getId());
        entity.setLayoutName(param.getLayoutName());
        entity.setConfig(param.getConfig());
        return entity;
    }

    /**
     * 实体转VO
     */
    public static SysSiteLayoutVO toVO(SysSiteLayout entity) {
        SysSiteLayoutVO vo = new SysSiteLayoutVO();
        vo.setId(entity.getId());
        vo.setLayoutName(entity.getLayoutName());
        vo.setConfig(entity.getConfig());
        vo.setCreatedTime(entity.getCreateTime());
        vo.setUpdatedTime(entity.getUpdateTime());
        return vo;
    }

    /**
     * 实体列表转VO列表
     */
    public static List<SysSiteLayoutVO> toVOList(List<SysSiteLayout> entities) {
        return entities.stream()
                .map(SysSiteLayoutConverts::toVO)
                .collect(Collectors.toList());
    }

}
