package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseStorageArchiveScanRecordPageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseStorageArchiveTrendParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStorageArchiveBasicInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStorageArchiveScanRecordVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStorageArchiveTrendVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface WarehouseStorageArchiveService {

    WarehouseStorageArchiveBasicInfoVO basicInfo(Long warehouseId);

    WarehouseStorageArchiveTrendVO trend(WarehouseStorageArchiveTrendParam param);

    Pagination<WarehouseStorageArchiveScanRecordVO> scanRecordPage(WarehouseStorageArchiveScanRecordPageParam param);

    void exportScanRecords(HttpServletResponse response, WarehouseStorageArchiveScanRecordPageParam param) throws IOException;
}
