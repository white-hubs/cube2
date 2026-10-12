package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author bq
 * @date 2022/7/28
 * @description 实体类型和属性树形结构
 */
@Data
@Builder
public class EntityTreeVO {

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 属性列表
     */
    private List<EntityTreeVO> attributeVOS;

}