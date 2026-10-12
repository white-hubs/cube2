package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.GasArchiveRecordPageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.GasArchiveTrendParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.GasArchiveService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasArchiveBasicInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasArchiveRecordVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasArchiveTrendVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/** 气体档案接口。 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiangcangGasArchiveController {

    private final GasArchiveService gasArchiveService;

    @GetMapping("/liangcang/warehouse-management/gas-archive/basic-info")
    public GasArchiveBasicInfoVO basicInfo(Long warehouseId) {
        return gasArchiveService.basicInfo(warehouseId);
    }

    @GetMapping("/liangcang/warehouse-management/gas-archive/trend")
    public GasArchiveTrendVO trend(@ModelAttribute GasArchiveTrendParam param) {
        return gasArchiveService.trend(param);
    }

    @GetMapping("/liangcang/warehouse-management/gas-archive/records/page")
    public Pagination<GasArchiveRecordVO> recordPage(@ModelAttribute GasArchiveRecordPageParam param) {
        return gasArchiveService.recordPage(param);
    }

    @GetMapping("/liangcang/warehouse-management/gas-archive/records/export")
    public void exportRecords(HttpServletResponse response,
                              @ModelAttribute GasArchiveRecordPageParam param) throws IOException {
        gasArchiveService.exportRecords(response, param);
    }
}
