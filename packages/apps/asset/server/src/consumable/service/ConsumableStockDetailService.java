package com.guhecloud.cube.srv.gen.property.management.server.consumable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStockDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableStockDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableStockDetailQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStockDetailExtendVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStockDetailVO;
import com.guhecloud.platform.base.model.Pagination;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 描述：出入库记录明细服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
public interface ConsumableStockDetailService extends IService<ConsumableStockDetail> {

    Long add(ConsumableStockDetailParam param);

    void update(Long id, ConsumableStockDetailParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    ConsumableStockDetailVO getById(Long id);

    Pagination<ConsumableStockDetailVO> getPage(ConsumableStockDetailQueryParam param);

    void extendInitStockInfo(Long storeId, ConsumableStockDetailParam param);

    /**
     * 出入库记录明细
     *
     * @param param
     *
     * @return
     */
    Pagination<ConsumableStockDetailExtendVO> getStockRecordDetail(ConsumableStockDetailQueryParam param);

    void exportStockRecordDetail(ConsumableStockDetailQueryParam param, HttpServletRequest request, HttpServletResponse response);
}