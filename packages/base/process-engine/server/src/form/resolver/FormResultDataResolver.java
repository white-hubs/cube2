package com.guhecloud.cube.base.flow.form.resolver;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.form.dao.FormControlBusinessMapper;
import com.guhecloud.cube.base.flow.form.dao.FormDefinitionMapper;
import com.guhecloud.cube.base.flow.form.dao.FormInstanceMapper;
import com.guhecloud.cube.base.flow.form.po.FormControlBusinessPO;
import com.guhecloud.cube.base.flow.form.po.FormDefinitionPO;
import com.guhecloud.cube.base.flow.form.po.FormInstancePO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/10/20
 * @description 表单结果数据解析器
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormResultDataResolver {

    private final FormControlBusinessMapper formControlBusinessMapper;
    private final FormDefinitionMapper formDefinitionMapper;
    private final FormInstanceMapper formInstanceMapper;
    private static final String RECEPTACLE = "RECEPTACLE";

    /**
     * 如果表单数据是多个json的集合，那么合并成为一个json字符串
     *
     * @param dataJson 原始表单数据json
     *
     * @return 合并后的表单数据json
     */
    public String mergeDataListJson(String dataJson) {
        Map<String, Object> jsonObject = new HashMap<>();
        try {
            List<String> list = JsonUtils.deserializeToList(dataJson, String.class);
            list.forEach(json -> {
                jsonObject.putAll(JsonUtils.deserialize(json, new TypeReference<Map<String, Object>>() {}));
            });
        } catch (Exception e) {
            return dataJson;
        }
        return JsonUtils.serialize(jsonObject);
    }

    /**
     * 解析表单数据与业务字段数据对应
     *
     * @param formInstanceId 表单实例ID
     * @param dataJson       表单数据json
     *
     * @return 业务实体数据json
     */
    public String parse(Long formInstanceId, String dataJson) {
        // 根据表单实例ID查找表单定义ID
        FormInstancePO formInstancePO = formInstanceMapper.selectById(formInstanceId);
        if (formInstancePO == null) {
            return StringUtils.EMPTY_STRING;
        }

        //查找表单定义
        FormDefinitionPO formDefinitionPO = formDefinitionMapper.selectOne(Wrappers.<FormDefinitionPO>lambdaQuery()
                                                                                   .eq(FormDefinitionPO::getKey, formInstancePO.getFormKey())
                                                                                   .eq(FormDefinitionPO::getVersion, formInstancePO.getVersion()));
        if (formDefinitionPO == null) {
            return StringUtils.EMPTY_STRING;
        }

        return parseByFormId2(formDefinitionPO.getId(), dataJson);
    }

    /**
     * 通过表单定义的ID和json数据，进行表单映射解析
     *
     * @param formDefinitionId
     * @param dataJson
     *
     * @return
     */
    public String parseByFormId2(Long formDefinitionId, String dataJson) {
        //根据表单定义ID 查找表单的映射关系
        List<FormControlBusinessPO> formFieldMapList = formControlBusinessMapper.selectList(Wrappers.<FormControlBusinessPO>lambdaQuery()
                                                                                                    .eq(FormControlBusinessPO::getFormDefinitionId, formDefinitionId));

        if (CollectionUtils.isEmpty(formFieldMapList)) {
            return StringUtils.EMPTY_STRING;
        }
        //表单映射转成Map
        Map<String, String> formFieldMap = formFieldMapList.stream()
                .filter(r -> StringUtils.isNotBlank(r.getControlId()) && StringUtils.isNotBlank(r.getBusinessField()))
                .collect(Collectors.toMap(FormControlBusinessPO::getControlId, f -> f.getBusinessField()));
        //将表单数据 json -> map
        Map<String, Object> formMap = JsonUtils.deserialize(dataJson, new TypeReference<Map<String, Object>>() {});
        //字段转换 转换成业务Json
        Map<String, Object> formJsonForBiz = new HashMap<>();
        List<Map<String, Object>> jsonArray = new ArrayList<>();
        if (MapUtils.isNotEmpty(formMap)) {
            formMap.forEach((k, v) -> {
                if (v instanceof List<?>) {
                    List<Object> objectList = new ArrayList<>();
                    ((List) v).forEach(d -> {
                        if (d instanceof Map<?, ?>) {
                            Map<String, Object> subFormMap = (Map) d;
                            boolean field = subFormMap.keySet().stream().allMatch(ke -> String.valueOf(ke).startsWith("field"));
                            if (!field) {
                                Map<String, Object> jsonObject = this.parseBiz(String.valueOf(k), d, formFieldMap);
                                if (!jsonObject.isEmpty()) {
                                    objectList.add(jsonObject.get(formFieldMap.get(k)));
                                }
                            } else {
                                Map<String, Object> formJsonForBiz2 = new HashMap<>();
                                subFormMap.forEach((k2, v2) -> {
                                    Map<String, Object> jsonObject = this.parseBiz(String.valueOf(k2), v2, formFieldMap);
                                    formJsonForBiz2.putAll(jsonObject);
                                });
                                if (!formJsonForBiz2.isEmpty()) {
                                    jsonArray.add(formJsonForBiz2);
                                }
                            }
                        } else {
                            Map<String, Object> jsonObject = this.parseBiz(String.valueOf(k), v, formFieldMap);
                            if (!jsonObject.isEmpty()) {
                                formJsonForBiz.putAll(jsonObject);
                            }
                        }
                    });
                    if (CollectionUtils.isNotEmpty(objectList)) {
                        formJsonForBiz.put(formFieldMap.get(k), objectList);
                    }
                } else {
                    Map<String, Object> jsonObject = this.parseBiz(String.valueOf(k), v, formFieldMap);
                    if (!jsonObject.isEmpty()) {
                        formJsonForBiz.putAll(jsonObject);
                    }
                }

            });
        }
        String data = null;
        if (!formJsonForBiz.isEmpty()) {
            data = JsonUtils.serialize(formJsonForBiz);
        } else {
            return JsonUtils.serialize(jsonArray);
        }
        return this.analyzingContainers(data, dataJson, formFieldMap, formFieldMapList);
    }

    private Map<String, Object> parseBiz(String k, Object v, Map<String, String> formFieldMap) {
        Map<String, Object> formJsonForBiz = new HashMap<>();
        if (String.valueOf(k).startsWith("field") && StringUtils.isNotBlank(formFieldMap.get(k))) {
            String field = formFieldMap.get(k);
            if (v instanceof String || v instanceof Number) {
                formJsonForBiz.put(field, v);
            } else if (v instanceof Map<?, ?>) {
                Map<String, Object> jo = (Map<String, Object>) v;
                if (jo.get("flowConvertData") != null && !(Boolean) jo.get("flowConvertData")) {
                    formJsonForBiz.put(field, jo);
                } else {
                    Object realValue =
                            (jo.get("id") != null && StringUtils.isNotBlank(jo.get("id").toString())) ? jo.get("id") : jo.get("name");
                    formJsonForBiz.put(field, realValue);
                }
            } else if (v instanceof List<?>) {
                List<Object> jos = (List<Object>) v;
                List<Object> valList = jos.stream().map(d -> {
                    if (d instanceof Map<?, ?>) {
                        Map<String, Object> jo = (Map<String, Object>) d;
                        return (jo.get("id") != null && StringUtils.isNotBlank(jo.get("id").toString())) ? jo.get("id") : jo.get("name");
                    } else {
                        return d;
                    }
                }).collect(Collectors.toList());
                formJsonForBiz.put(field, valList);
            }
        }
        return formJsonForBiz;
    }

    /**
     * 组合容器解析
     */
    private String analyzingContainers(String data, String dataJson, Map<String, String> formFieldMap,
                                       List<FormControlBusinessPO> formFieldMapList
                                      ) {
        Map<String, Object> dataMap = JsonUtils.deserialize(data, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> formMap = JsonUtils.deserialize(dataJson, new TypeReference<Map<String, Object>>() {});
        Map<String, FormControlBusinessPO> formConfig = formFieldMapList.stream()
                .collect(Collectors.toMap(fcb -> fcb.getControlId(), FormControlBusinessPO -> FormControlBusinessPO));
        if (MapUtils.isNotEmpty(dataMap) && MapUtils.isNotEmpty(formMap)) {
            formMap.forEach((k, v) -> {
                FormControlBusinessPO formControlBusinessPO = formConfig.get(k);
                if (ObjectUtils.isNotEmpty(formControlBusinessPO) && RECEPTACLE.equals(formControlBusinessPO.getControlType()) &&
                    v instanceof List<?>) {
                    List<Map<String, Object>> list = new ArrayList<>();
                    ((List<?>) v).forEach(d -> {
                        Map<String, Object> map1 = new HashMap<>();
                        // 组合容器解析
                        if (d instanceof Map<?, ?>) {
                            Map<String, Object> subFormMap = (Map<String, Object>) d;
                            Set<String> setKey = subFormMap.keySet();
                            Map<String, String> map = new HashMap<>();
                            for (String ex : setKey) {
                                String key = formFieldMap.get(ex);
                                if (ObjectUtils.isNotEmpty(key)) {
                                    map.put(ex, key);
                                } else {
                                    map.put(ex, subFormMap.get(ex).toString());
                                }
                            }
                            map.forEach((s, o) -> {
                                Object KeyData = subFormMap.get(s);
                                if (ObjectUtils.isNotEmpty(KeyData)) {
                                    if (KeyData instanceof Map<?, ?>) {
                                        Map<String, Object> dataJo = (Map<String, Object>) KeyData;
                                        Object dataObject =
                                                (dataJo.get("id") != null && StringUtils.isNotBlank(dataJo.get("id").toString()))
                                                ? dataJo.get("id") : dataJo.get("name");
                                        KeyData = Objects.nonNull(dataObject) ? dataObject : KeyData;
                                    }
                                    if ("subDataId".equals(s)) {
                                        map1.put(s, KeyData);
                                    } else {
                                        map1.put(o, KeyData);
                                    }
                                }
                            });
                            if (map1.size() > 0) {
                                list.add(map1);
                            }
                        }
                    });
                    if (ObjectUtils.isNotEmpty(list)) {
                        String key = formFieldMap.get(k);
                        dataMap.put(ObjectUtils.isNotEmpty(key) ? key : k, list);
                    }
                }
            });
        }
        return JsonUtils.serialize(dataMap);
    }
}
