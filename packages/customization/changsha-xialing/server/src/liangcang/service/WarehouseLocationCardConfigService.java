package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardChangeLogQueryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardBoundWarehousePageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardConfigDetailParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardPositionProfileSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardQualityInspectionDeleteParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardQualityInspectionSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLocationCardConfigDetailVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLocationCardBoundWarehousePageVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLocationCardFieldChangeLogVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureReportPointVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/** 货位卡配置。 */
public interface WarehouseLocationCardConfigService {

    Boolean saveProfile(WarehouseLocationCardPositionProfileSaveParam param);

    Boolean createQualityInspection(WarehouseLocationCardQualityInspectionSaveParam param);

    Boolean updateQualityInspection(WarehouseLocationCardQualityInspectionSaveParam param);

    Boolean deleteQualityInspection(WarehouseLocationCardQualityInspectionDeleteParam param);

    WarehouseLocationCardConfigDetailVO detail(WarehouseLocationCardConfigDetailParam param);

    WarehouseLocationCardConfigDetailVO detail(WarehouseLocationCardConfigDetailParam param,
                                               List<List<WarehouseTemperatureReportPointVO>> reportList);

    Pagination<WarehouseLocationCardBoundWarehousePageVO> boundWarehousePage(WarehouseLocationCardBoundWarehousePageParam param);

    Pagination<WarehouseLocationCardFieldChangeLogVO> changeLogList(WarehouseLocationCardChangeLogQueryParam param);
}
