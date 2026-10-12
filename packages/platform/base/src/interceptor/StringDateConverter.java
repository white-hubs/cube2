package com.guhecloud.platform.base.interceptor;

import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 字符串日期转换器
 *
 * @author Woody
 * @date 2021/8/20
 */
@Component
public class StringDateConverter implements Converter<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        return DateUtils.parse(text);
    }
}
