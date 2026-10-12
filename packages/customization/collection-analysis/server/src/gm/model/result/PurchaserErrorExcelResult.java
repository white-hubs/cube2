package com.guhecloud.cube.biz.collection.analysis.gm.model.result;

import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.PurchaserExcelDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaserErrorExcelResult {
    private Integer total = 0;
    private Integer success = 0;
    private List<PurchaserExcelDTO> errorList = new ArrayList<>();
}
