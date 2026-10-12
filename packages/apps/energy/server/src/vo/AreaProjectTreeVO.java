package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.guhecloud.platform.base.model.Tree;
import com.guhecloud.platform.database.annotation.Unique;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 区域分项配置
 * @date 2022-07-04 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AreaProjectTreeVO extends Tree.Simple<Long, AreaProjectTreeVO> {

    //
    private Long id;
    //区域名称
    @Unique(composites = "deleted", message = "名称不能重复")
    private String areaName;
    //备注
    private String remark;
    //仪表类型
    private String instrumentType;

    /**
     * 序列号
     */
    private String sequence;
}