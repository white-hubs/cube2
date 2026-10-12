package com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration;

import com.guhecloud.cube.srv.gen.property.management.server.common.param.AllotApprovalParam;
import com.guhecloud.cube.srv.gen.property.management.server.common.param.DepositoryDetailedApprovalParam;
import com.guhecloud.cube.srv.gen.property.management.server.common.param.DistributionApprovalParam;
import com.guhecloud.cube.srv.gen.property.management.server.common.param.WeedOutApprovalParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddRecordApprovalParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceRecordApprovalParam;
import lombok.Getter;

/**
 * 资产与耗材 都是资产，通过前缀区分
 *
 * @author appet
 * @date 2022/11/28
 */
@Getter
public enum PropertyApprovalBizEnum {
    CONSUMABLE_ADD("耗材入库", ConsumableAddRecordApprovalParam.class),
    CONSUMABLE_REDUCE("耗材出库", ConsumableReduceRecordApprovalParam.class),
    PROPERTY_DEPOSITORY("资产入库", DepositoryDetailedApprovalParam.class),
    PROPERTY_ALLOT_PERSONAL("资产派发", DistributionApprovalParam.class),
    PROPERTY_ALLOT_UNIT("资产调拨", AllotApprovalParam.class),
    PROPERTY_WEED_OUT("清理报废", WeedOutApprovalParam.class),
    ;

    private final String desc;
    private final Class<?> clazz;

    PropertyApprovalBizEnum(String desc, Class<?> clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }

}
