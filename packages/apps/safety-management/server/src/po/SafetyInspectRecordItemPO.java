package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("safety_inspect_record_item")
@EqualsAndHashCode(callSuper = true)
public class SafetyInspectRecordItemPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long recordId;
    private Boolean tempAddFlag;
    private Long standardId;
    private String standardName;
    private Long itemId;
    private String itemName;
    private String itemTag;
    private String itemImageResult;
    private Long itemResultId;
    private String itemResultValue;
    private String itemResultName;
    private Boolean errorValueFlag;
    private String remarkResult;
    private String errorDesc;
    private Integer marketTenant;
    private String marketName;
    private String businessType;
}
