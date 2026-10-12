package com.guhecloud.cube.srv.gen.adapter.access.control.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("door_device")
public class DoorDevice extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String doorCode;

    /**
     * 门禁类型
     */
    private Long type;

    /**
     * 型号
     */
    private Long doorModel;

    /**
     * 门禁区域
     */
    private String doorArea;

    /**
     * 区域ids
     */
    private String doorAreaIds;

    /**
     * 业务系统冗余id
     */
    private String businessId;

    /**
     * 三方系统key
     */
    private String outBusinessId;

    /**
     * 状态
     */
    private String status;

}
