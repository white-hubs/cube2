package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

/**
 * @author Appet
 * @Description 区域
 * @date 2023-03-16 10:08:36
 */
@Data
public class AreaTreeVO extends Tree.Simple<Long, AreaTreeVO> {

    //分类名称
    private String areaName;
    //设备数量
    private Long deviceNum;

}