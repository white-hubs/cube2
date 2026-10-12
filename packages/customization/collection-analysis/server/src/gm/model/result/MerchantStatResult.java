package com.guhecloud.cube.biz.collection.analysis.gm.model.result;

import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.MerchantStatDTO;
import lombok.Data;

@Data
public class MerchantStatResult {
    private MerchantStatDTO wholesaler;
    private MerchantStatDTO purchaser;
}
