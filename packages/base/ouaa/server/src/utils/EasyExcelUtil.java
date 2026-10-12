package com.guhecloud.cube.biz.ouaa.server.utils;

import com.alibaba.excel.annotation.ExcelProperty;
import com.guhecloud.cube.base.dictionary.utils.BusinessConfigUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/28 9:57
 * @Description:
 */
public class EasyExcelUtil {

    /**
     * 字段别名配置
     */
    public static final String FIELD_CONFIG_ALIAS = "employee";

    public static List<List<String>> getCustomHeaders(Class<?> clazz) {
        List<List<String>> resultHeader = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, BusinessConfigUtils.FiledConfig> filedConfigMap = BusinessConfigUtils.getFieldNameConfig(FIELD_CONFIG_ALIAS);
        for (Field field : fields) {
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty == null) {
                continue;
            }
            //存在配置中且不展示则忽略
            BusinessConfigUtils.FiledConfig filedConfig = filedConfigMap.getOrDefault(field.getName(), new BusinessConfigUtils.FiledConfig());
            if (filedConfigMap.containsKey(field.getName()) && !Boolean.TRUE.equals(filedConfig.getShow())) {
                continue;
            }
            List<String> head = new ArrayList<>();
            head.add(StringUtils.isBlank(filedConfig.getAlias())
                     || StringUtils.equals(field.getName(), filedConfig.getAlias()) ? excelProperty.value()[0] : filedConfig.getAlias());
            resultHeader.add(head);
        }
        return resultHeader;
    }
}
