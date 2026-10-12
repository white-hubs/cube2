package com.guhecloud.cube.srv.gen.property.management.server.consumable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCategory;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCategoryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCategoryQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCategoryVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：耗材分类服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
public interface ConsumableCategoryService extends IService<ConsumableCategory> {

    Long add(ConsumableCategoryParam param);

    void update(Long id, ConsumableCategoryParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    ConsumableCategoryVO getById(Long id);

    Pagination<ConsumableCategoryVO> getPage(ConsumableCategoryQueryParam param);

}