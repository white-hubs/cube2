package com.guhecloud.cube.srv.gen.official.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author appet
 * @date 2023/02/24
 * @description 督办事项类型
 */
@Data
@TableName("focus_work_category")
public class FocusWorkCategoryPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    @NotEmpty(message = "事项类型名称不能为空")
    private String name;//名称

    private String remark;//备注或描述

}