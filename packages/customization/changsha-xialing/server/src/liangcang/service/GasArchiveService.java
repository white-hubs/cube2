package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.GasArchiveRecordPageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.GasArchiveTrendParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasArchiveBasicInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasArchiveRecordVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasArchiveTrendVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** 气体档案服务。 */
public interface GasArchiveService {

    GasArchiveBasicInfoVO basicInfo(Long warehouseId);

    GasArchiveTrendVO trend(GasArchiveTrendParam param);

    Pagination<GasArchiveRecordVO> recordPage(GasArchiveRecordPageParam param);

    void exportRecords(HttpServletResponse response, GasArchiveRecordPageParam param) throws IOException;
}
