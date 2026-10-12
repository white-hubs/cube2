package com.guhecloud.cube.srv.gen.attendance.management.server.overtime.util;

import com.guhecloud.cube.srv.gen.attendance.management.server.common.FormFieldVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.annotation.FormField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CustBeanUtil {

    //获取特定字段
    public static List<FormFieldVO> getFormField(Class<?> clazz) {
        List<FormFieldVO> datas = new ArrayList<>();
        try {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                FormField formField = field.getAnnotation(FormField.class);
                if (formField != null && !formField.ignore()) {
                    datas.add(FormFieldVO.builder().id(field.getName()).name(formField.label()).build());
                }
            }
            return datas;
        } catch (Exception e) {
            return datas;
        }
    }
}
