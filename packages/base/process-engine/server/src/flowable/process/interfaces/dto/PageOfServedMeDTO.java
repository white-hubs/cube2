package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * @author pengjian
 * @date 2022/10/25 16:18
 * @description
 */
@Data
@NoArgsConstructor
public class PageOfServedMeDTO extends Paging {
    private Period submitPeriod;
    private Period completePeriod;
    private String status;
    @NotBlank
    private String businessType;

    public PageOfServedMeDTO(Period submitPeriod, Period completePeriod, String status, String businessType, Integer pageNumber,
                             Integer pageSize
                            ) {
        super(pageNumber, pageSize);
        this.submitPeriod = submitPeriod;
        this.completePeriod = completePeriod;
        this.status = status;
        this.businessType = businessType;
    }
}
