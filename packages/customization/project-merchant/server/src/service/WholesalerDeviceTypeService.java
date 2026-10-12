package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerDeviceTypeDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerDeviceType;

/**
 * <p>
 * 商户设备类型 服务类
 * </p>
 *
 * @author bq
 * @since 2022-03-08
 */
public interface WholesalerDeviceTypeService extends IService<WholesalerDeviceType> {

    void save(WholesalerDeviceTypeDTO dto);

    void update(WholesalerDeviceTypeDTO dto);

    void remove(String id);

}
