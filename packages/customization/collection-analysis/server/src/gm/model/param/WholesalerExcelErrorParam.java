package com.guhecloud.cube.biz.collection.analysis.gm.model.param;

import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.WholesalerExcelDTO;
import lombok.Data;

import java.util.List;

@Data
public class WholesalerExcelErrorParam {
    private List<WholesalerExcelDTO> dataList;
}
