package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionDataTypeEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class DataTypePageParam extends Paging {
    private String keyword;
    @NotNull(message = "数据类型不能为空")
    private DisinfectionDataTypeEnum dataType;
}
