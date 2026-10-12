package com.guhecloud.platform.base.interceptor;

import com.guhecloud.platform.base.model.Period;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * 字符串周期转换器
 *
 * @author Woody
 * @date 2021/8/20
 */
@Component
public class StringPeriodConverter implements Converter<String, Period> {
    @Override
    public Period convert(String text) {
        return Period.parse(text);
    }
}
