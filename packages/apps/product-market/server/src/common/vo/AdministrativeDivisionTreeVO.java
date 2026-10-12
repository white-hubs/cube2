package com.guhecloud.cube.srv.gen.product.market.server.common.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 行政区划
 * </p>
 *
 * @author appet
 * @since 2021-07-15
 */
@Data
public class AdministrativeDivisionTreeVO {

    private String id;

    private String name;

    private String parentId;

    List<AdministrativeDivisionTreeVO> children;
}
