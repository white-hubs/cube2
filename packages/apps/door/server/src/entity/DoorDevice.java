package com.guhecloud.cube.biz.door.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoorDevice extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //名称
    private String name;
    //门禁类型
    private Long type;
    //门禁型号
    private Long doorModel;
    //门禁区域
    private String doorArea;
    //区域ids
    private String doorAreaIds;
    //设备id
    private Long businessId;
    //编码
    private String doorCode;
    //状态
    private String status;
    //三方系统key
    private String outBusinessId;
}
