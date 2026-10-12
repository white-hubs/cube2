package com.guhecloud.cube.base.dictionary.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.ReflectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/25 14:09
 * @Description:
 */

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictionaryRedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    public <T> T get(String itemCode, String code, Class<T> clazz) {
        Object value = this.redisTemplate.opsForValue().get(this.getRedisKey(itemCode, code));
        if (Objects.isNull(value)) {
            return null;
        }
        return JsonUtils.deserialize(value.toString(), clazz);
    }

    public <T> T get(String itemCode, String code, String tenant, Class<T> clazz) {
        Object value = this.redisTemplate.opsForValue().get(this.getRedisKey(itemCode, code, tenant));
        if (Objects.isNull(value)) {
            return null;
        }
        return JsonUtils.deserialize(value.toString(), clazz);
    }

    public <T> List<T> getListByCode(String key, Class<T> clazz, Object filterParam) {
        Set<String> keys = this.redisTemplate.keys("DICTIONARY:" + key + ":" + SessionContextHolder.getRequester().getTenant() + "*");
        return this.getList(keys, clazz, filterParam);
    }

    public <T> List<T> getList(Set<String> keys, Class<T> clazz, Object filterParam) {
        if (CollectionUtils.isEmpty(keys)) {
            return null;
        }
        List<Object> objectList = redisTemplate.opsForValue().multiGet(keys);
        if (CollectionUtils.isEmpty(objectList)) {
            return null;
        }

        List<T> deserialize = JsonUtils.deserialize(objectList.toString(),
                                                    new TypeReference<List<T>>() {

                                                    });
        Iterator<T> iterator = deserialize.iterator();
        List<T> filterList = new ArrayList<>();
        while (iterator.hasNext()) {
            T next = iterator.next();
            T deserialize1 = JsonUtils.deserialize(next, clazz);

            Field[] filterField = ReflectUtils.getFields(filterParam.getClass());
            boolean remove = false;
            for (Field field : filterField) {
                remove = this.filterByParam(filterParam, field, deserialize1, iterator);
                //如果移除，则跳出循环
                if (remove) {
                    break;
                }
            }
            if (!remove) {
                filterList.add(deserialize1);
            }
        }
        return filterList;
    }

    public <T> List<T> getListByUserId(String key, Class<T> clazz, Object filterParam) {
        Set<String> keys = this.redisTemplate.keys("DICTIONARY:" + key + ":" + SessionContextHolder.getRequester().getTenant() + ":" +
                                                   SecurityContextHolder.getPrincipal().getId() + "*");
        return this.getList(keys, clazz, filterParam);
    }

    private <T> boolean filterByParam(Object filterParam, Field field, T entity, Iterator<T> iterator) {
        Object fieldValue = ReflectUtils.getFieldValue(filterParam, field);
        //是否移除
        boolean remove = false;
        if (Objects.isNull(fieldValue) && !"status".equals(field.getName())) {
            return false;
        }
        switch (field.getName()) {
            case "status":
                Object status = ReflectUtils.getFieldValue(entity, "status");
                String string = String.valueOf(status);
                if (!getStatusNameList(fieldValue).contains(string)) {
                    iterator.remove();
                    remove = true;
                }
                break;
            case "codes":
                List<String> deserialize1 = JsonUtils.deserialize(fieldValue, new TypeReference<List<String>>() {
                });
                Object codeObj = ReflectUtils.getFieldValue(entity, "code");
                String code = String.valueOf(codeObj);
                if (!CollectionUtils.isEmpty(deserialize1) && !deserialize1.contains(code)) {
                    iterator.remove();
                    remove = true;
                }
                break;
            case "name":
                Object nameObj = ReflectUtils.getFieldValue(entity, "name");
                String name = String.valueOf(nameObj);
                if (!String.valueOf(fieldValue).contains(name)) {
                    iterator.remove();
                    remove = true;
                }
                break;
            case "code":
                Object codeObj1 = ReflectUtils.getFieldValue(entity, "code");
                String code1 = String.valueOf(codeObj1);
                if (!String.valueOf(fieldValue).contains(code1)) {
                    iterator.remove();
                    remove = true;
                }
        }
        return remove;
    }

    public List<String> getStatusNameList(Object disable) {
        List<String> statusNameList = new ArrayList<>(2);
        statusNameList.add("ENABLE");
        if (ObjectUtils.isNotEmpty(disable) && (Boolean) disable) {
            statusNameList.add("DISABLE");
        }
        return statusNameList;
    }

    public String getRedisKey(String itemCode, String code) {
        return "DICTIONARY:" + itemCode + ":" + SessionContextHolder.getRequester().getTenant() + ":" + code;
    }

    public String getRedisKey(String itemCode, String code, String tenant) {
        return "DICTIONARY:" + itemCode + ":" + tenant + ":" + code;
    }
}
