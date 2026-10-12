package com.guhecloud.cube.base.flow.context.submit.interfaces;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartAfterProcessInstanceCallBackData {
    private String processInstanceId;

    private Object preResponse;

    /**
     * 历史原因，后续去掉
     */
    private String businessType;

    private String applicationCode;

    private String propertyId;

    public String getPropertyId() {
        if (ObjectUtils.isNotEmpty(this.preResponse)) {
            return this.preResponse.toString();
        }
        return null;
    }
}
