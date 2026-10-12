package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseCable;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCableAvailableDevicePageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCableCreateParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCableDeleteParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCablePointMappingCreateParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCablePointUnbindParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseCableAvailableDeviceVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseCableListItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseCablePointMappingVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/** 粮仓电缆与点位设备映射。 */
public interface WarehouseCableService extends IService<WarehouseCable> {

    /** 新增电缆并生成全量点位。 */
    Boolean createCable(WarehouseCableCreateParam param);

    /** 删除电缆及其点位映射。 */
    Boolean deleteCable(WarehouseCableDeleteParam param);

    /** 查询仓房下电缆列表。 */
    List<WarehouseCableListItemVO> listByWarehouseId(Long warehouseId);

    /** 查询电缆点位映射详情。 */
    List<WarehouseCablePointMappingVO> listPoints(Long cableId);

    /** 批量新增点位映射。 */
    Boolean createPointMappings(WarehouseCablePointMappingCreateParam param);

    /** 批量取消点位映射。 */
    Boolean unbindPoints(WarehouseCablePointUnbindParam param);

    /** 查询可选设备分页。 */
    Pagination<WarehouseCableAvailableDeviceVO> availableDevicePage(WarehouseCableAvailableDevicePageParam param);
}
