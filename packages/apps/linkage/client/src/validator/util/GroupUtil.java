package com.guhecloud.cube.biz.linkage.client.validator.util;

import com.guhecloud.cube.biz.linkage.client.validator.annotation.GroupId;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class GroupUtil {

    /**
     * 分组字段key
     */
    public static final String GROUP_ID_KEY = "_group_id_key";

    private static final ThreadLocal<Map<Object, Object>> GROUP_ID_MAP = new ThreadLocal<>();

    /**
     * 获取当前请求入参的分组值
     *
     * @param in
     *
     * @return
     */
    public static Object getRequestGroupValue(Object in) {
        try {
            Field[] fields = in.getClass().getSuperclass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(GroupId.class)) {
                    field.setAccessible(true);
                    Object groupId = field.get(in);
                    if (Objects.nonNull(groupId)) {
                        // 设置共享变量
                        GROUP_ID_MAP.set(Collections.singletonMap(GROUP_ID_KEY, groupId));
                    }
                    return groupId;
                }
            }
        } catch (Exception e) {
            log.error("发生了异常", e);
        }
        return null;
    }

    /**
     * 获取当前请求的分组值
     *
     * @return
     */
    public static Object getCurrentRequestValue() {
        return GROUP_ID_MAP.get().get(GROUP_ID_KEY);
    }
}
