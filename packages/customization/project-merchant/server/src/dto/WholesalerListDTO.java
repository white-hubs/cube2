package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.Wholesaler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/18
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerListDTO extends ConvertionDTO<WholesalerListDTO, Wholesaler> {
    public WholesalerListDTO() {}

    public WholesalerListDTO(Wholesaler wholesaler) {
        super();
        map(wholesaler);
    }

    public WholesalerListDTO(List<Wholesaler> list) {
        super();
        map(list);
    }

    private String id;

    private String code;

    private String name;

    private String owner;

    private String mobile;

    private Integer stallCount;

    private LocalDateTime createTime;
}
