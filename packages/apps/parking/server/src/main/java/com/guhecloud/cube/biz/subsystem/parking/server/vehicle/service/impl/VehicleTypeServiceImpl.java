package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.dao.VehicleTypeMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity.VehicleTypePO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleTypeParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleTypeQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.VehicleTypeService;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleTypeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 16:38
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleTypeServiceImpl extends ServiceImpl<VehicleTypeMapper, VehicleTypePO> implements VehicleTypeService {
    @Override
    public Boolean saveOrUpdate(VehicleTypeParam param) {
        if (Objects.nonNull(param.getId())) {
            VehicleTypePO vehicleTypePO = this.getById(param.getId());
            vehicleTypePO.setName(param.getName());
            this.updateById(vehicleTypePO);
            return true;
        }
        VehicleTypePO vehicleTypePO = new VehicleTypePO();
        vehicleTypePO.setId(IdWorker.getId());
        vehicleTypePO.setName(param.getName());
        return this.save(vehicleTypePO);
    }

    @Override
    public Pagination<VehicleTypeVO> getVehicleTypPage(VehicleTypeQueryParam param) {
        Page<VehicleTypePO> vehicleTypePOPage = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getKeyword()), VehicleTypePO::getName, param.getKeyword())
                .orderByDesc(VehicleTypePO::getUpdateTime)
                .page(PagingUtils.convert(param));
        if (CollectionUtils.isEmpty(vehicleTypePOPage.getRecords())) {
            return new Pagination<>();
        }
        List<VehicleTypeVO> vehicleTypeVOList = vehicleTypePOPage.getRecords().stream().map(t -> {
            VehicleTypeVO vehicleTypeVO = new VehicleTypeVO();
            vehicleTypeVO.setId(t.getId());
            vehicleTypeVO.setName(t.getName());
            return vehicleTypeVO;
        }).collect(Collectors.toList());
        return PagingUtils.convert(vehicleTypePOPage, vehicleTypeVOList);
    }

    @Override
    public List<VehicleTypeVO> getVehicleTypList(VehicleTypeQueryParam param) {
        List<VehicleTypePO> vehicleTypePOList = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getKeyword()), VehicleTypePO::getName, param.getKeyword())
                .orderByDesc(VehicleTypePO::getUpdateTime)
                .list();
        if (CollectionUtils.isEmpty(vehicleTypePOList)) {
            return new ArrayList<>();
        }
        return vehicleTypePOList.stream().map(t -> {
            VehicleTypeVO vehicleTypeVO = new VehicleTypeVO();
            vehicleTypeVO.setId(t.getId());
            vehicleTypeVO.setName(t.getName());
            return vehicleTypeVO;
        }).collect(Collectors.toList());
    }
}
