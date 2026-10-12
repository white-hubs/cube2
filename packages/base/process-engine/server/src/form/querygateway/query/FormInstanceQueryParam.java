package com.guhecloud.cube.base.flow.form.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class FormInstanceQueryParam extends Paging {

    private String businessType;

    private String businessKey;

    private String dynamicDataJson;

}
