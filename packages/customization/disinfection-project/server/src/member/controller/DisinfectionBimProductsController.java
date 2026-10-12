package com.guhecloud.cube.srv.gen.disinfection.server.member.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.member.param.CountsTrendParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.BoundDevicePageQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionBimProductsService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.BimPointVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.BimStatisticsOverviewVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.BimBoundDevicePageResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.DistrictDeviceCountVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ManualCommandUsageCountVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ProductTypeCountVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ProductUserCountVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionBimProductsController {
    private final DisinfectionBimProductsService disinfectionBimProductsService;

    /**
     * 绑定产品总数统计
     */
    @GetMapping("/bim/products/counts")
    public Long productsCounts() {
        return disinfectionBimProductsService.productsCounts();
    }

    /**
     * 绑定产品不同类型个数统计
     */
    @GetMapping("/bim/products/type-counts")
    public List<ProductTypeCountVO> productsTypeCounts() {
        return disinfectionBimProductsService.productsTypeCounts();
    }

    /**
     * 绑定产品用户数统计
     */
    @GetMapping("/bim/products/user-counts")
    public List<ProductUserCountVO> productsUserCounts() {
        return disinfectionBimProductsService.productsUsersCounts();
    }

    /**
     * 按不同时间纬度统计不同产品类型绑定数量
     */
    @GetMapping("/bim/products/month-product-counts")
    public List<BimPointVO> countsTrend(CountsTrendParam countsTrendParam) {
        return disinfectionBimProductsService.countsTrend(countsTrendParam);
    }

    /**
     * 按月份统计绑定的用户数量
     */
    @GetMapping("/bim/products/month-user-counts")
    public List<BimPointVO> productsMonthUserCounts(Period period) {
        return disinfectionBimProductsService.productsMonthUserCounts(period);
    }

    /**
     * 统计不同产类型新增的数量
     */
    @GetMapping("/bim/products/product-new-counts")
    public List<ProductTypeCountVO> productsNewCounts(Period period) {
        return disinfectionBimProductsService.productsNewCounts(period);
    }

    /**
     * BIM 设备与用户总览统计
     */
    @GetMapping("/bim/products/statistics-overview")
    public BimStatisticsOverviewVO statisticsOverview() {
        return disinfectionBimProductsService.statisticsOverview();
    }

    /**
     * 统计近半年每月新增设备数量
     *
     * @param productTypeCode 设备类型编码(产品分类编码)，为空则统计全部
     */
    @GetMapping("/bim/products/half-year-new-device-month-counts")
    public List<BimPointVO> halfYearNewDeviceMonthCounts(String productTypeCode) {
        return disinfectionBimProductsService.halfYearNewDeviceMonthCounts(productTypeCode);
    }

    /**
     * 获取绑定设备分页(设备名称/状态)
     */
    @GetMapping("/bim/products/bound-devices/page")
    public Pagination<BimBoundDevicePageResult> boundDevicesPage(BoundDevicePageQueryParam param) {
        return disinfectionBimProductsService.boundDevicesPage(param);
    }

    /**
     * 统计各区县绑定设备数量（基于数据库区县字段）
     */
    @GetMapping("/bim/products/district-device-counts")
    public List<DistrictDeviceCountVO> districtDeviceCounts() {
        return disinfectionBimProductsService.districtBoundDeviceCounts();
    }

    /**
     * 雾灌指令和驱蚊指令使用次数统计
     */
    @GetMapping("/bim/products/manual-command-usage-counts")
    public ManualCommandUsageCountVO manualCommandUsageCounts() {
        return disinfectionBimProductsService.manualCommandUsageCounts();
    }

}
