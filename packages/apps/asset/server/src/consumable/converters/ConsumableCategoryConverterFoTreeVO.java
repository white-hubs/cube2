package com.guhecloud.cube.srv.gen.property.management.server.consumable.converters;

import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCategory;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCategoryTreeVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class ConsumableCategoryConverterFoTreeVO {
    private ConsumableCategoryConverterFoTreeVO() {

    }

    public static ConsumableCategoryTreeVO toTreeVO(ConsumableCategory consumableCategory) {
        var consumableCategoryTreeVO = new ConsumableCategoryTreeVO();
        consumableCategoryTreeVO.setId(consumableCategory.getId());
        consumableCategoryTreeVO.setName(consumableCategory.getName());
        consumableCategoryTreeVO.setParentId(consumableCategory.getParentId());
        consumableCategoryTreeVO.setLevel(consumableCategory.getLevel());
        return consumableCategoryTreeVO;
    }

    public static List<ConsumableCategoryTreeVO> toTreeVOS(List<ConsumableCategory> consumableCategories) {
        if (CollectionUtils.isEmpty(consumableCategories)) {
            return CollectionUtils.emptyList();
        }
        return consumableCategories.stream().map(ConsumableCategoryConverterFoTreeVO::toTreeVO).toList();
    }
}
