package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

/**
 * @author Appet
 * @Description 支路
 * @date 2023-03-16 10:08:36
 */
@Data
public class RouteTreeVO extends Tree.Simple<Long, RouteTreeVO> {

    //分类名称
    private String routeName;

}