package com.guhecloud.cube.base.message.sdk.common;

import com.guhecloud.cube.base.message.sdk.api.request.BasicRequest;
import com.guhecloud.cube.base.message.sdk.common.exception.ModelValidateException;

public class Common {

    /**
     * Validate Request
     *
     * @return void
     */
    public static void validateModel(BasicRequest m) {
        if (null == m) {
            throw new ModelValidateException("parameter is not allowed as null");
        }
        try {
            m.validate();
        } catch (Exception e) {
            throw new ModelValidateException(e.getMessage(), e);
        }
    }
}
