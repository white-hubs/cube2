package com.guhecloud.cube.base.flow.form.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FormDefinitionQueryParam extends Paging {

    private String key;

    private Integer version;

}
