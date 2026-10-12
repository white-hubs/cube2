package com.guhecloud.cube.biz.mdata.server.stat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaGroup;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaGroupConfig;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaQueryParam;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaGroupConfigService;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaGroupService;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaService;
import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallService;
import com.guhecloud.cube.biz.mdata.server.stat.param.AreaStatQueryParam;
import com.guhecloud.cube.biz.mdata.server.stat.vo.AreaStallStatVO;
import com.guhecloud.cube.biz.mdata.server.stat.vo.AreaStatVO;
import com.guhecloud.cube.biz.mdata.server.stat.vo.AreaSupplierStatVO;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierAuthentication;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierStall;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierAuthenticationService;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierService;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierStallService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @date 2022/4/19
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaBimStatController {

    private final AreaService areaService;
    private final StallService stallService;
    private final SupplierService supplierService;
    private final SupplierStallService supplierStallService;
    private final AreaGroupService areaGroupService;
    private final AreaGroupConfigService areaGroupConfigService;
    private final SupplierAuthenticationService supplierAuthenticationService;

    /**
     * 三维总览区域数据
     *
     * @return
     */
    @GetMapping("/area-bim-stat/area-group/list")
    public List<AreaStatVO> listAreaStat() {
        // 获取顶层组信息
        List<AreaGroup> areaGroups = this.areaGroupService.getFirstParent();
        Set<Long> areaGroupIds = areaGroups.stream().map(AreaGroup::getId).collect(Collectors.toSet());
        // 获取区组组配置信息
        List<AreaGroupConfig> areaGroupConfigs = this.areaGroupConfigService.listAreaGroupsAreas(areaGroupIds);
        Map<Long, List<AreaGroupConfig>> areaGroupConfigMap =
                areaGroupConfigs.stream().collect(Collectors.groupingBy(AreaGroupConfig::getAreaGroupId));

        // 获取区域信息
        Set<Long> areaIds = areaGroupConfigs.stream().map(AreaGroupConfig::getAreaId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(areaIds)) {
            return Collections.emptyList();
        }
        List<Area> areas = this.areaService.listByIds(areaIds);

        // 获取档位信息
        List<Stall> stalls = this.stallService.list();
        Set<Long> stallIds = stalls.stream().map(Stall::getId).collect(Collectors.toSet());

        // 获取租赁的商户信息
        List<SupplierStall> supplierStalls = this.supplierStallService.listByStalls(stallIds);
        Set<Long> supplierIds = supplierStalls.stream().map(SupplierStall::getSupplierId).collect(Collectors.toSet());

        // 获取批发商信息
        List<Supplier> suppliers = this.supplierService.listByIds(supplierIds);

        return areaGroups.stream().map(areaGroup -> {
            if (areaGroupConfigMap.containsKey(areaGroup.getId())) {
                AreaStatVO areaStatVO = new AreaStatVO();
                List<AreaGroupConfig> groupConfigs = areaGroupConfigMap.get(areaGroup.getId());
                areaStatVO.setName(areaGroup.getName());

                // 获取楼栋数及占地面积
                Set<Long> groupConfigAreaIds = groupConfigs.stream().map(AreaGroupConfig::getAreaId).collect(Collectors.toSet());
                List<Area> groupConfigAreas = areas.stream().filter(x -> groupConfigAreaIds.contains(x.getId()))
                        .collect(Collectors.toList());
                areaStatVO.setBuiltUpArea(groupConfigAreas.stream().map(Area::getBuiltUpArea).reduce(BigDecimal.ZERO, BigDecimal::add));
                areaStatVO.setBuilding(groupConfigs.size());

                // 获取档位数量
                Set<String> areaStallKeys = groupConfigs.stream().map(x -> x.getArea().replace("-", ",")).collect(Collectors.toSet());
                Set<Stall> areaStalls = stalls.stream().filter(stall -> areaStallKeys.contains(stall.getArea()))
                        .collect(Collectors.toSet());
                Set<Long> areaStallIds = areaStalls.stream().map(Stall::getId).collect(Collectors.toSet());
                areaStatVO.setStallCounts(areaStalls.size());
                areaStatVO.setLeaseCounts((int) areaStalls.stream().filter(Stall::getLeased).count());
                areaStatVO.setReleaseCounts((int) areaStalls.stream().filter(x -> Boolean.FALSE.equals(x.getLeased())).count());

                // 获取商户信息
                Set<SupplierStall> areaSupplierStalls = supplierStalls.stream().filter(x -> areaStallIds.contains(x.getStallId()))
                        .collect(Collectors.toSet());
                Set<Long> areaSupplierIds = areaSupplierStalls.stream().map(SupplierStall::getSupplierId).collect(Collectors.toSet());
                Set<Supplier> areaSuppliers = suppliers.stream().filter(x -> areaSupplierIds.contains(x.getId()))
                        .collect(Collectors.toSet());
                areaStatVO.setSupplierCounts(areaSuppliers.size());
                return areaStatVO;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 区域数据详情弹窗分页信息
     *
     * @param queryParam 查询参数
     *
     * @return 区域数据信息
     */
    @GetMapping("/area-bim-stat/area/page")
    public Pagination<AreaStatVO> pageAreaStat(AreaStatQueryParam queryParam) {
        // 获取区域组信息
        AreaGroup areaGroup = this.areaGroupService.getById(queryParam.getAreaGroupId());
        Assert.isNotFound(areaGroup, "不存在区域组信息!");

        // 获取区组组配置信息
        Page<AreaGroupConfig> page = this.areaGroupConfigService.pageAreaGroupsStatAreas(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        // 获取区域信息
        Set<Long> areaIds = page.getRecords().stream().map(AreaGroupConfig::getAreaId).collect(Collectors.toSet());
        List<Area> areas = this.areaService.listByIds(areaIds);
        Map<Long, List<Area>> areaMap = areas.stream().collect(Collectors.groupingBy(Area::getId));

        // 获取档位信息
        Set<String> areaStallKeys = page.getRecords().stream().map(x -> x.getArea().replace("-", ",")).collect(Collectors.toSet());
        List<Stall> stalls = this.stallService.listByAreas(areaStallKeys);
        Map<String, List<Stall>> stallMap = stalls.stream().collect(Collectors.groupingBy(Stall::getArea));
        Set<Long> stallIds = stalls.stream().map(Stall::getId).collect(Collectors.toSet());

        // 获取档位租赁信息
        List<SupplierStall> supplierStalls = this.supplierStallService.listByStalls(stallIds);

        // 获取设备信息
        Set<String> areaKeys = page.getRecords().stream().map(AreaGroupConfig::getArea).collect(Collectors.toSet());

        // 构建信息
        List<AreaStatVO> records = page.getRecords().stream().map(areaGroupConfig -> {
            AreaStatVO areaStatVO = new AreaStatVO();
            if (areaMap.containsKey(areaGroupConfig.getAreaId())) {
                Area area = areaMap.get(areaGroupConfig.getAreaId()).get(0);
                areaStatVO.setName(area.getName());
                areaStatVO.setBuiltUpArea(area.getBuiltUpArea());
            }
            String areaStallKey = areaGroupConfig.getArea().replace("-", ",");
            if (stallMap.containsKey(areaStallKey)) {
                // 获取档位信息
                List<Stall> areaStalls = stallMap.get(areaStallKey);
                areaStatVO.setLeaseCounts((int) areaStalls.stream().filter(Stall::getLeased).count());
                areaStatVO.setReleaseCounts((int) areaStalls.stream().filter(x -> Boolean.FALSE.equals(x.getLeased())).count());
                areaStatVO.setStallCounts(areaStalls.size());

                // 获取商户信息
                Set<Long> areaStallIds = areaStalls.stream().map(Stall::getId).collect(Collectors.toSet());
                Set<SupplierStall> areaSupplierStalls = supplierStalls.stream().filter(x -> areaStallIds.contains(x.getStallId()))
                        .collect(Collectors.toSet());
                Set<Long> areaSupplierIds = areaSupplierStalls.stream().map(SupplierStall::getSupplierId).collect(Collectors.toSet());
                areaStatVO.setSupplierCounts(areaSupplierIds.size());
            } else {
                areaStatVO.setLeaseCounts(0);
                areaStatVO.setReleaseCounts(0);
                areaStatVO.setStallCounts(0);
            }
            // 获取设备信息
            areaStatVO.setDevicesCounts(0);
            areaStatVO.setOnlineCounts(0);
            areaStatVO.setOfflineCounts(0);
            return areaStatVO;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, records);
    }

    /**
     * 区域下商户分页
     *
     * @param queryParam 查询参数
     *
     * @return 分页信息
     */
    @GetMapping("/area-bim-stat/supplier/page")
    public Pagination<AreaSupplierStatVO> pageAreaSupplier(AreaStatQueryParam queryParam) {
        // 获取区域组信息
        AreaGroup areaGroup = this.areaGroupService.getById(queryParam.getAreaGroupId());
        Assert.isNotFound(areaGroup, "不存在区域组信息!");

        // 获取区组组配置信息
        List<AreaGroupConfig> areaGroupConfigs = this.areaGroupConfigService.listAreaGroupsAreas(areaGroup.getId());
        if (CollectionUtils.isEmpty(areaGroupConfigs)) {
            return Pagination.empty();
        }

        // 获取档位信息
        Set<String> areaStallKeys = areaGroupConfigs.stream().map(x -> x.getArea().replace("-", ",")).collect(Collectors.toSet());
        List<Stall> stalls = this.stallService.listByAreas(areaStallKeys);
        Set<Long> stallIds = stalls.stream().map(Stall::getId).collect(Collectors.toSet());

        // 获取档位租赁信息
        List<SupplierStall> supplierStalls = this.supplierStallService.listByStalls(stallIds);
        Set<Long> supplierIds = supplierStalls.stream().map(SupplierStall::getSupplierId).collect(Collectors.toSet());

        // 获取批发商信息
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        SupplierQueryParam supplierQueryParam = new SupplierQueryParam();
        supplierQueryParam.setIds(supplierIds);
        supplierQueryParam.setPageSize(queryParam.getPageSize());
        supplierQueryParam.setPageNumber(queryParam.getPageNumber());
        supplierQueryParam.setTenant(tenant);
        Page<Supplier> page = this.supplierService.pageSupplier(supplierQueryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        // 获取认证信息
        supplierIds = page.getRecords().stream().map(Supplier::getId).collect(Collectors.toSet());
        List<SupplierAuthentication> supplierAuthentications = this.supplierAuthenticationService.listBySupplierIds(supplierIds, tenant);
        Map<Long, List<SupplierAuthentication>> supplierAuthenticationMap =
                supplierAuthentications.stream().collect(Collectors.groupingBy(SupplierAuthentication::getSupplierId));

        // 构建信息
        List<AreaSupplierStatVO> records = ObjectUtils.copying(page.getRecords(), AreaSupplierStatVO.class);
        records.forEach(record -> {
            if (supplierAuthenticationMap.containsKey(record.getId())) {
                SupplierAuthentication supplierAuthentication = supplierAuthenticationMap.get(record.getId()).get(0);
                record.setType(supplierAuthentication.getType());
            }
        });
        return PagingUtils.convert(page, records);
    }

    /**
     * 区域下的楼栋数分页
     *
     * @param queryParam 查询参数
     *
     * @return 楼栋数分页
     */
    @GetMapping("/area-bim-stat/area-building/page")
    public Pagination<AreaStatVO> pageAreaBuilding(AreaStatQueryParam queryParam) {
        // 获取区域组信息
        AreaGroup areaGroup = this.areaGroupService.getById(queryParam.getAreaGroupId());
        Assert.isNotFound(areaGroup, "不存在区域组信息!");

        // 获取区组组配置信息
        List<AreaGroupConfig> areaGroupConfigs = this.areaGroupConfigService.listAreaGroupsAreas(areaGroup.getId());
        if (CollectionUtils.isEmpty(areaGroupConfigs)) {
            return Pagination.empty();
        }

        // 获取区域信息
        Set<Long> areaIds = areaGroupConfigs.stream().map(AreaGroupConfig::getAreaId).collect(Collectors.toSet());
        AreaQueryParam areaQueryParam = new AreaQueryParam();
        areaQueryParam.setIds(areaIds);
        Page<Area> page = this.areaService.page(areaQueryParam);
        return PagingUtils.convert(page, AreaStatVO.class);
    }

    /**
     * 区域下档位分页
     *
     * @param queryParam 查询参数
     *
     * @return 分页信息
     */
    @GetMapping("/area-bim-stat/stall/page")
    public Pagination<AreaStallStatVO> pageAreaStall(AreaStatQueryParam queryParam) {
        // 获取区域组信息
        AreaGroup areaGroup = this.areaGroupService.getById(queryParam.getAreaGroupId());
        Assert.isNotFound(areaGroup, "不存在区域组信息!");

        // 获取区组组配置信息
        List<AreaGroupConfig> areaGroupConfigs = this.areaGroupConfigService.listAreaGroupsAreas(areaGroup.getId());
        if (CollectionUtils.isEmpty(areaGroupConfigs)) {
            return Pagination.empty();
        }

        // 获取档位信息
        Set<String> areaStallKeys = areaGroupConfigs.stream().map(x -> x.getArea().replace("-", ",")).collect(Collectors.toSet());
        AreaStatQueryParam areaStatQueryParam = new AreaStatQueryParam();
        areaStatQueryParam.setAreas(areaStallKeys);
        Page<Stall> page = this.stallService.pageByAreas(areaStatQueryParam);

        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        // 构建信息
        List<AreaStallStatVO> records = ObjectUtils.copying(page.getRecords(), AreaStallStatVO.class);
        return PagingUtils.convert(page, records);
    }
}
