package com.guhecloud.cube.srv.gen.product.market.server.common.entity;

import lombok.Data;

/**
 * @author Appet
 * @Description 行政区划
 * @date 2023-05-16 15:08:36
 */
@Data
public class AdministrativeDivision {

    //id或行政区域编号
    private String id;
    //名称
    private String name;
    //上级区域ID
    private String parentId;

}