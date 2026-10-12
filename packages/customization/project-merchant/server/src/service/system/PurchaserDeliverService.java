package com.guhecloud.cube.deliver.project.merchant.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.system.PurchaserDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.system.PurchaserDeliver;

/**
 * <p>
 * 商户端-采购商 服务类
 * </p>
 *
 * @author bq
 * @since 2021-12-28
 */
public interface PurchaserDeliverService extends IService<PurchaserDeliver> {

    void update(PurchaserDTO dto);

}
