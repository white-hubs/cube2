package com.guhecloud.cube.base.flow.client.dto;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author wql
 * @describe 流程看板实例列表查询参数
 * @date 2023/11/8 9:47
 */
@Data
public class PageOfDataBoardDTO extends Paging {
    @NotBlank
    private String type;

    @NotBlank
    private String applicationCode;

    private String status;

    private Period period;

    private String businessType;
}
