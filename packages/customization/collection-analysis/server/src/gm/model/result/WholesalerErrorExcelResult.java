package com.guhecloud.cube.biz.collection.analysis.gm.model.result;

import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.WholesalerExcelDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WholesalerErrorExcelResult {
    private Integer total = 0;
    private Integer success = 0;
    private List<WholesalerExcelDTO> errorList = new ArrayList<>();

}
