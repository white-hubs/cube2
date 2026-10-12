package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * @author pengjian
 * @date 2022/10/25 16:15
 * @description
 */
@Data
@NoArgsConstructor
public class PageOfWaitingDTO extends Paging {
    private Period period;
    @NotBlank(message = "业务类型不能为空")
    private String businessType;

    public PageOfWaitingDTO(Period period, String businessType, Integer pageNumber, Integer pageSize) {
        super(pageNumber, pageSize);
        this.period = period;
        this.businessType = businessType;
    }
}
