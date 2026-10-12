package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 区域分项配置
 * @date 2022-07-04 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AreaProject extends BasicEnhancedTenantTreeEntity<Long> {

    //
    private Long id;
    //区域名称
    @Unique(composites = "deleted", message = "名称不能重复")
    private String areaName;
    //备注
    private String remark;
    //仪表类型
    private String instrumentType;

}