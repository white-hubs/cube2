package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseStorageArchiveScanRecordPageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseStorageArchiveTrendParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseStorageArchiveService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStorageArchiveBasicInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStorageArchiveScanRecordVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStorageArchiveTrendVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 储量档案。
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiangcangWarehouseStorageArchiveController {

    private final WarehouseStorageArchiveService warehouseStorageArchiveService;

    /**
     * 基本信息。
     */
    @GetMapping("/liangcang/warehouse-management/storage-archive/basic-info")
    public WarehouseStorageArchiveBasicInfoVO basicInfo(Long warehouseId) {
        return warehouseStorageArchiveService.basicInfo(warehouseId);
    }

    /**
     * 储量变化曲线。
     */
    @GetMapping("/liangcang/warehouse-management/storage-archive/trend")
    public WarehouseStorageArchiveTrendVO trend(@ModelAttribute WarehouseStorageArchiveTrendParam param) {
        return warehouseStorageArchiveService.trend(param);
    }

    /**
     * 扫描记录分页。
     */
    @GetMapping("/liangcang/warehouse-management/storage-archive/scan-records/page")
    public Pagination<WarehouseStorageArchiveScanRecordVO> scanRecordPage(
            @ModelAttribute WarehouseStorageArchiveScanRecordPageParam param
                                                                         ) {
        return warehouseStorageArchiveService.scanRecordPage(param);
    }

    /**
     * 扫描记录导出。
     */
    @GetMapping("/liangcang/warehouse-management/storage-archive/scan-records/export")
    public void exportScanRecords(HttpServletResponse response,
                                  @ModelAttribute WarehouseStorageArchiveScanRecordPageParam param
                                 ) throws IOException {
        warehouseStorageArchiveService.exportScanRecords(response, param);
    }
}
