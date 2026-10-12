package com.guhecloud.cube.srv.gen.property.management.server.consumable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCheckDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.AppConsumablePageParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckDetailAddParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckDetailQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.AppConsumablePageVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckDetailVO;
import com.guhecloud.platform.base.model.Pagination;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 描述：耗材盘点明细服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
public interface ConsumableCheckDetailService extends IService<ConsumableCheckDetail> {

    Long add(ConsumableCheckDetailAddParam param);

    void update(Long id, ConsumableCheckDetailParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    ConsumableCheckDetailVO getById(Long id);

    Pagination<ConsumableCheckDetailVO> getPage(ConsumableCheckDetailQueryParam param);

    /**
     * 耗材盘点资产信息
     */
    Pagination<AppConsumablePageVO> getAppPage(AppConsumablePageParam param);

    void getExcel(HttpServletResponse response, DepositoryDetailedPageExcelParam param);
}