package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.Wholesaler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/17
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerShortDTO extends ConvertionDTO<WholesalerShortDTO, Wholesaler> {
    public WholesalerShortDTO() {}

    public WholesalerShortDTO(Wholesaler wholesaler) {
        super();
        map(wholesaler);
    }

    public WholesalerShortDTO(List<Wholesaler> list) {
        super();
        map(list);
    }

    private String id;

    private String projectId;

    private String code;

    private String name;
}
