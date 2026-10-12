package com.guhecloud.cube.srv.gen.property.management.server.consumable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCheck;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckDetailStatVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckWithStatVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockExtendVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 描述：耗材盘点服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
public interface ConsumableCheckService extends IService<ConsumableCheck> {

    Long add(ConsumableCheckParam param);

    void update(Long id, ConsumableCheckParam param);

    Boolean updateForSubmit(Long id, Boolean updateStock);

    void delete(Long id);

    //    void batchDelete(List<Long> ids);

    ConsumableCheckVO getById(Long id);

    Pagination<ConsumableCheckVO> getPage(Long checkerId, ConsumableCheckQueryParam param);

    Pagination<ConsumableCheckWithStatVO> getPageForApp(ConsumableCheckQueryParam param);

    ConsumableCheckDetailStatVO getCheckStatById(Long checkId);

    Pagination<ConsumableStoreStockExtendVO> getCheckDetailPage(ConsumableCheckDetailParam param);

    Boolean submitWin(Long taskId);

    Boolean addWin(Long taskId);
}