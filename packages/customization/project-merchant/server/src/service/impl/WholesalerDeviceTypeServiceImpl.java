package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerDeviceType;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerDeviceTypeMapper;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerDeviceTypeDTO;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerDeviceTypeService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商户设备类型 服务实现类
 * </p>
 *
 * @author bq
 * @since 2022-03-08
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerDeviceTypeServiceImpl extends ServiceImpl<WholesalerDeviceTypeMapper, WholesalerDeviceType> implements WholesalerDeviceTypeService {

    @Override
    public void save(WholesalerDeviceTypeDTO dto) {
        WholesalerDeviceType deviceType = this.lambdaQuery().eq(WholesalerDeviceType::getName, dto.getName()).one();
        if (deviceType != null) {
            throw new ServiceException("名称已经存在！");
        }
        this.save(dto.convert());
    }

    @Override
    public void update(WholesalerDeviceTypeDTO dto) {
        WholesalerDeviceType deviceType = this.lambdaQuery().eq(WholesalerDeviceType::getName, dto.getName()).one();
        if (deviceType != null && !deviceType.getId().equals(dto.getId())) {
            throw new ServiceException("名称已经存在！");
        }
        this.updateById(dto.convert());
    }

    @Override
    public void remove(String id) {
        this.removeById(id);
    }

}
