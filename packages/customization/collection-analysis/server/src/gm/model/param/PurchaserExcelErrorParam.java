package com.guhecloud.cube.biz.collection.analysis.gm.model.param;

import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.PurchaserExcelDTO;
import lombok.Data;

import java.util.List;

@Data
public class PurchaserExcelErrorParam {
    private List<PurchaserExcelDTO> dataList;
}
