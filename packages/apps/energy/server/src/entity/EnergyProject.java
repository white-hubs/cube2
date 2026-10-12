package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 能耗分项
 * @date 2022-07-04 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyProject extends BasicEnhancedTenantTreeEntity<Long> {

    //
    private Long id;
    //编码
    private String code;
    //分项名称
    private String projectName;
    //仪表类型
    private String instrumentType;
    //备注
    private String remark;

}