package com.guhecloud.cube.biz.collection.analysis.gm.model.dto;

import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.ParkSettleCategoryDayPO;
import com.guhecloud.platform.base.util.ConvertUtils;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-16  16:22
 * @Description:
 * @Version: 1.0
 */
@Data
public class ParkSettleCategoryDayDTO {
    /**
     * 货物总数量
     */
    private String goodsCount;
    /**
     * 标识 -1 下降；1 上升
     */
    private String dayToDayFlag;
    /**
     * 百分比
     */
    private String percent;
    /**
     * 昨日来货品类占比
     */
    private List<ParkSettleCategoryDayDetailsDTO> list;

    public ParkSettleCategoryDayPO getParkSettleCategoryDayPO(String deliveryDate) {
        ParkSettleCategoryDayPO po = ConvertUtils.convert(ParkSettleCategoryDayPO.class, this);
        po.setDeliveryDate(deliveryDate);
        return po;
    }
}
