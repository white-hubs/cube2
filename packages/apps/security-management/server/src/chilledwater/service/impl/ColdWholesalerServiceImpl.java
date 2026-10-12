package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.ColdWholesalerMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.GearPositionMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.ColdWholesaler;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.GearPosition;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.AssociationParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.GearPositionTimeParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.WholesalerQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ColdWholesalerService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.GearPositionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.OperateLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataDeviceVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.WholesalerVo;
import com.guhecloud.cube.srv.gen.stall.client.GenStallClient;
import com.guhecloud.cube.srv.gen.stall.vo.GenStallListVo;
import com.guhecloud.cube.srv.gen.supplier.client.GenSupplierClient;
import com.guhecloud.cube.srv.gen.supplier.vo.GenSupplierVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ColdWholesalerServiceImpl extends ServiceImpl<ColdWholesalerMapper, ColdWholesaler> implements ColdWholesalerService {
    private final ColdWholesalerMapper wholesalerMapper;

    private final GearPositionMapper gearPositionMapper;

    private final GearPositionService gearPositionService;

    private final GenStallClient stallClient;

    private final GenSupplierClient genSupplierClient;

    private final OrganizationClient organizationClient;

    private final OperateLogService operateLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addWholesalers() {
        List<GenSupplierVo> supplierDTOS = genSupplierClient.listGenSuppliers("", SessionContextHolder.getRequester().getTenant());
        List<Long> supplierIds = supplierDTOS.stream().map(GenSupplierVo::getId).collect(Collectors.toList());
        List<ColdWholesaler> coldWholesalers = wholesalerMapper.selectList(null);
        List<Long> coldSupplierIds = coldWholesalers.stream().map(ColdWholesaler::getWholesalerId).collect(Collectors.toList());
        supplierIds.removeAll(coldSupplierIds);
        Map<Long, GenSupplierVo> collect = supplierDTOS.stream().collect(Collectors.toMap(GenSupplierVo::getId, s -> s));
        List<ColdWholesaler> wholesalers = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(supplierIds)) {
            for (Long supplierId : supplierIds) {
                GenSupplierVo supplierDTO = collect.get(supplierId);
                ColdWholesaler wholesaler = new ColdWholesaler();
                wholesaler.setWholesalerId(supplierDTO.getId());
                wholesaler.setWholesalerName(supplierDTO.getName());
                wholesaler.setCode(supplierDTO.getCode());
                wholesaler.setShiftMasterPhone(supplierDTO.getShiftMasterPhone());
                wholesaler.setShiftMaster(supplierDTO.getShiftMaster());
                wholesaler.setShiftMasterCard(supplierDTO.getShiftMasterCard());
                wholesalers.add(wholesaler);
            }
            this.saveBatch(wholesalers);
        }
        operateLogService.saveBusinessLog("拉去商户数据");
    }

    @Override
    public Pagination<WholesalerVo> getWholesalers(WholesalerQueryParam param) {
        Page<WholesalerVo> wholesalers = wholesalerMapper.getWholesalers(PagingUtils.convert(param), param.getKeyword());
        return PagingUtils.convert(wholesalers);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void association(AssociationParam param) {
        List<Long> wholesalerId = param.getWholesalerId();
        List<GearPositionTimeParam> timePositionParams = param.getTimePositionParams();
        List<Long> collect = timePositionParams.stream().map(GearPositionTimeParam::getGearPositionId).collect(Collectors.toList());
        List<GenStallListVo> stallByIds = stallClient.getStallByIds(collect);
        Map<Long, GenStallListVo> collectMap = stallByIds.stream().collect(Collectors.toMap(GenStallListVo::getId, s -> s));
        List<GearPosition> copying = ObjectUtils.copying(timePositionParams, GearPosition.class);
        for (GearPosition gearPosition : copying) {
            GenStallListVo stallDTO = collectMap.get(gearPosition.getGearPositionId());
            if (StringUtils.isNotEmpty(stallDTO.getArea())) {
                gearPosition.setGearPositionAreaId(stallDTO.getArea());
                gearPosition.setGearPositionArea(stallDTO.getAreaNames());
            }
            gearPosition.setGearPositionCode(stallDTO.getCode());
            gearPosition.setGearPositionName(stallDTO.getName());
            if (StringUtils.isNotEmpty(stallDTO.getDeptId())) {
                String[] split = stallDTO.getDeptId().split(",");
                String depIds = Arrays.stream(split).collect(Collectors.joining("-"));
                gearPosition.setDepartmentId(depIds);
                List<Long> ids = new ArrayList<>();
                for (String s : split) {
                    ids.add(Long.valueOf(s));
                }
                List<OrganizationDTO> organizationsByIds = organizationClient.getOrganizationsByIds(ids);
                if (CollectionUtils.isNotEmpty(organizationsByIds)) {
                    String name = organizationsByIds.stream().map(OrganizationDTO::getName).collect(Collectors.joining("/"));
                    gearPosition.setDepartment(name);
                }
            }
        }
        for (Long aLong : wholesalerId) {
            gearPositionMapper.delete(Wrappers.<GearPosition>lambdaQuery()
                                              .eq(GearPosition::getWholesaler, aLong));
            for (GearPosition gearPosition : copying) {
                long id = IdWorker.getId();
                gearPosition.setWholesaler(aLong);
                gearPosition.setId(id);
                gearPositionService.save(gearPosition);
            }
        }

        operateLogService.saveBusinessLog("商户关联档位");

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void associationCancellation(Long id) {
        List<GearPosition> gearPositions = gearPositionMapper.selectList(Wrappers.<GearPosition>lambdaQuery()
                                                                                 .eq(GearPosition::getWholesaler, id));
        DatabaseContextHolder.softDelete(GearPosition.class, gearPositions);
        List<String> collect = gearPositions.stream().map(GearPosition::getGearPositionName).collect(Collectors.toList());
        String gearPositionNames = collect.stream().collect(Collectors.joining("、"));
        operateLogService.saveBusinessLog("商户取消关联档位:" + gearPositionNames);
    }

    @Override
    public List<DataDeviceVo> getWholesalerDevices() {
        return wholesalerMapper.getWholesalerDevices();
    }

    @Override
    public List<DataDeviceVo> getWholesalerDevicesByUpdate(String deviceCode) {
        return wholesalerMapper.getWholesalerDevicesForUpdate(deviceCode);
    }

}
