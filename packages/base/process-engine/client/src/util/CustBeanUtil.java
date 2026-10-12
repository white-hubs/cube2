package util;

import annotation.FormField;
import vo.FormFieldVO;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CustBeanUtil {

    //获取特定字段
    public static List<FormFieldVO> getFormField(Class<?> clazz) {
        List<FormFieldVO> datas = new ArrayList<>();
        try {
            while (clazz != null) {
                Field[] declaredFields = clazz.getDeclaredFields();
                for (Field field : declaredFields) {
                    FormField formField = field.getAnnotation(FormField.class);
                    if (formField != null && !formField.ignore()) {
                        datas.add(FormFieldVO.builder().id(field.getName()).name(formField.label()).required(formField.required())
                                          .fromType(formField.fromType())
                                          .build());
                    }
                }
                clazz = clazz.getSuperclass();
            }
            return datas;
        } catch (Exception e) {
            return datas;
        }
    }
}
