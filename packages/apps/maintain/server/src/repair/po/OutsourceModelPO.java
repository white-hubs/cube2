package com.guhecloud.cube.srv.gen.maintain.server.repair.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/5/18 15:03
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("outsource_model")
public class OutsourceModelPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String name;
}
