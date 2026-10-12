package com.guhecloud.cube.biz.mdata.client.param;

import com.guhecloud.cube.biz.mdata.client.dto.GoodsCategorySyncDTO;
import lombok.Data;

import java.util.List;

/**
 * 商品类型更新入参
 */
@Data
public class GoodsCategoryBatchRenewParam {
    private List<GoodsCategorySyncDTO> list;
}
