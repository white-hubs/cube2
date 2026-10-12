package com.guhecloud.cube.base.flow.flowable.resolver;

import lombok.AccessLevel;
import lombok.Getter;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * @author qiu wei jun
 * @date 2022/10/10 15:59:36
 */
public class FormDataResolver {
    @Getter(value = AccessLevel.PRIVATE)
    private static final String FORM_STRUCTURE = "form:";

    public String parse(String originalData) {
        if (StringUtils.isNotEmpty(originalData) && originalData.startsWith(FORM_STRUCTURE)) {
            return originalData.substring(originalData.indexOf(FORM_STRUCTURE) + FORM_STRUCTURE.length());
        }
        return originalData;
    }
}
