package com.guhecloud.platform.database.interceptor.special;

import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.MetaObject;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.guhecloud.platform.database.interceptor.special.constant.SpecialCharacterConstants.ESCAPE_CHAR;
import static com.guhecloud.platform.database.interceptor.special.constant.SpecialCharacterConstants.MYBATIS_PLUS_LIKE_SQL;
import static com.guhecloud.platform.database.interceptor.special.constant.SpecialCharacterConstants.MYBATIS_PLUS_WRAPPER_KEY;
import static com.guhecloud.platform.database.interceptor.special.constant.SpecialCharacterConstants.MYBATIS_PLUS_WRAPPER_PREFIX;
import static com.guhecloud.platform.database.interceptor.special.constant.SpecialCharacterConstants.MYBATIS_PLUS_WRAPPER_SEPARATOR;
import static com.guhecloud.platform.database.interceptor.special.constant.SpecialCharacterConstants.MYBATIS_PLUS_WRAPPER_SEPARATOR_REGEX;
import static com.guhecloud.platform.database.interceptor.special.constant.SpecialCharacterConstants.REPLACED_LIKE_KEYWORD_MARK;
import static com.guhecloud.platform.database.interceptor.special.constant.SpecialCharacterConstants.SQL_LIKE;
import static com.guhecloud.platform.database.interceptor.special.constant.SpecialCharacterConstants.SQL_PLACEHOLDER;
import static com.guhecloud.platform.database.interceptor.special.constant.SpecialCharacterConstants.SQL_PLACEHOLDER_REGEX;

@Slf4j
public class DefaultSpecialCharacterHandler implements SpecialCharacterHandler {

    @Override
    public void transferLikeSql(String sql, MetaObject metaObject, Object parameterObject, BoundSql boundSql) {
        transferLikeSql(sql, parameterObject, boundSql);
    }

    /**
     * 修改特殊字符查询的sql
     *
     * @param sql
     * @param parameterObject
     * @param boundSql
     */
    private void transferLikeSql(String sql, Object parameterObject, BoundSql boundSql) {
        if (!isEscape(sql)) {
            return;
        }
        sql = sql.replaceAll(" {2}", "");
        Set<String> fields = this.getKeyFields(sql, boundSql);
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        Map parameterObjectMap = (MapperMethod.ParamMap) parameterObject;
        this.mapParamConvert(sql, fields, parameterObjectMap);

    }

    /**
     * 参数转换
     *
     * @param sql
     * @param fields
     * @param parameterObject
     */
    public void mapParamConvert(String sql, Set<String> fields, Map parameterObject) {
        for (String field : fields) {
            if (this.hasMybatisPlusLikeSql(sql)) {
                if (this.hasWrapper(field)) {
                    //第一种情况：对业务层中的条件构造生成的模糊查询关键字使用QueryWrapper、LambdaQueryWrapper
                    this.mapParamTransferWrapper(field, parameterObject);
                } else {
                    //第二种情况：不使用条件构造函数，但在服务层中手动拼接查询关键字和模糊查询符号“%”
                    this.transferSelf(field, parameterObject);
                }
            } else {
                //第三种情况：模糊查询拼接在Mapper类的注释SQL中
                this.transferSplice(field, parameterObject);
            }
        }
    }

    /**
     * map参数转换
     *
     * @param field
     * @param parameter
     */
    public void mapParamTransferWrapper(String field, Map parameter) {
        Object wrapper = parameter.get(MYBATIS_PLUS_WRAPPER_KEY);
        try {
            Method m = wrapper.getClass().getMethod("getParamNameValuePairs");
            parameter = (Map<String, Object>) m.invoke(wrapper);
        } catch (Exception e) {
            log.error("反射异常", e);
            return;
        }
        String[] keys = field.split(MYBATIS_PLUS_WRAPPER_SEPARATOR_REGEX);
        //ew.paramNameValuePairs.param1，截取字符串后，获取第三个，即参数名
        String paramName = keys[2];
        String mapKey = String.format("%s.%s", REPLACED_LIKE_KEYWORD_MARK, paramName);
        if (parameter.containsKey(mapKey) && Objects.equals(parameter.get(mapKey), true)) {
            return;
        }
        if (this.cascade(field)) {
            this.resolveCascadeObj(field, parameter);
        } else {
            Object param = parameter.get(paramName);
            if (this.hasEscapeChar(param)) {
                String paramStr = param.toString();
                parameter.put(keys[2],
                              String.format("%%%s%%", this.escapeChar(paramStr.substring(1, paramStr.length() - 1))));
            }
        }
        parameter.put(mapKey, true);
    }

    /**
     * 由自定义条件拼接的转义特殊字符
     * 未使用条件构造函数，但在服务层中手动拼接查询关键字和模糊查询符号“%”
     *
     * @param field     field name
     * @param parameter parameter object
     */
    public void transferSelf(String field, Map parameter) {
        if (this.cascade(field)) {
            this.resolveCascadeObj(field, parameter);
            return;
        }
        if (parameter.containsKey(field)) {
            Object param = parameter.get(field);
            if (this.hasEscapeChar(param)) {
                String paramStr = param.toString();
                parameter.put(field,
                              String.format("%%%s%%", this.escapeChar(paramStr.substring(1, paramStr.length() - 1))));
            }
        }
    }

    /**
     * 由自定义条件拼接的转义特殊字符
     * 模糊查询拼接在Mapper类的注释SQL中
     *
     * @param field     field name
     * @param parameter parameter object
     */
    public void transferSplice(String field, Map parameter) {
        if (this.cascade(field)) {
            this.resolveCascadeObj(field, parameter);
            return;
        }
        if (parameter.containsKey(field)) {
            Object param = parameter.get(field);
            if (this.hasEscapeChar(param)) {
                parameter.put(field, this.escapeChar(param.toString()));
            }
        }
    }

    /**
     * sql是否需要转义
     *
     * @param sql SQL statement
     *
     * @return true/false
     */
    private boolean isEscape(String sql) {
        return this.hasLike(sql) && this.hasPlaceholder(sql);
    }

    /**
     * 确定sql语句是否包含like关键字
     *
     * @param str SQL statement
     *
     * @return true/false
     */
    private boolean hasLike(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.toLowerCase().contains(SQL_LIKE);
    }

    /**
     * 确定sql语句是否包含SQL占位符
     *
     * @param str SQL statement
     *
     * @return true/false
     */
    private boolean hasPlaceholder(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.toLowerCase().contains(SQL_PLACEHOLDER);
    }

    /**
     * 获取需要替换的所有字段的集合
     *
     * @param sql      complete SQL statement
     * @param boundSql bound SQL object
     *
     * @return field collection list
     */
    private Set<String> getKeyFields(String sql, BoundSql boundSql) {
        String[] params = sql.split(SQL_PLACEHOLDER_REGEX);
        Set<String> fields = new HashSet<>();
        for (int i = 0; i < params.length; i++) {
            if (this.hasLike(params[i]) && i < boundSql.getParameterMappings().size()) {
                String field = boundSql.getParameterMappings().get(i).getProperty();
                fields.add(field);
            }
        }
        return fields;
    }

    /**
     * 是否包含mybatis的SQL语句格式以及包含类似内容
     *
     * @param sql complete SQL statement
     *
     * @return true/false
     */
    private boolean hasMybatisPlusLikeSql(String sql) {
        if (StringUtils.isBlank(sql)) {
            return false;
        }
        return sql.toLowerCase().contains(MYBATIS_PLUS_LIKE_SQL);
    }

    /**
     * 确定它是否为级联属性
     *
     * @param field field name
     *
     * @return true/false
     */
    private boolean cascade(String field) {
        if (StringUtils.isBlank(field)) {
            return false;
        }
        return field.contains(MYBATIS_PLUS_WRAPPER_SEPARATOR) && !this.hasWrapper(field);
    }

    /**
     * 转义通配符
     *
     * @param before the string to be escaped
     *
     * @return escaped string
     */
    private String escapeChar(String before) {
        if (StringUtils.isNotBlank(before)) {
            if (before.contains("%") && !before.contains("\\%")) {
                before = before.replaceAll("%", "\\\\%");
            }
            if (before.contains("\\\\") && !before.contains("\\\\\\")) {
                before = before.replaceAll("\\\\", "\\\\\\\\");
            }
            if (before.contains("_") && !before.contains("\\_")) {
                before = before.replaceAll("_", "\\\\_");
            }
        }
        return before;
    }

    /**
     * 是否包含需要转义的字符
     *
     * @param obj the object to be judged
     *
     * @return true/false
     */
    private boolean hasEscapeChar(Object obj) {
        if (!(obj instanceof String)) {
            return false;
        }
        return this.hasEscapeChar((String) obj);
    }

    /**
     * 处理类似对象的问题
     *
     * @param field     object field
     * @param parameter object
     */
    private void resolveObj(String field, Object parameter) {
        if (parameter == null || StringUtils.isBlank(field)) {
            return;
        }
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(field, parameter.getClass());
            Method readMethod = descriptor.getReadMethod();
            Object param = readMethod.invoke(parameter);
            if (this.hasEscapeChar(param)) {
                Method setMethod = descriptor.getWriteMethod();
                setMethod.invoke(parameter, this.escapeChar(param.toString()));
            } else if (this.cascade(field)) {
                int index = field.indexOf(MYBATIS_PLUS_WRAPPER_SEPARATOR) + 1;
                this.resolveObj(field.substring(index), param);
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            log.error("Reflected {} {} get/set method is abnormal", parameter, field, e);
        }
    }

    /**
     * 处理级联属性
     *
     * @param field     cascade field name
     * @param parameter parameter Map object
     */
    private void resolveCascadeObj(String field, Map parameter) {
        int index = field.indexOf(MYBATIS_PLUS_WRAPPER_SEPARATOR);
        Object param = parameter.get(field.substring(0, index));
        if (param == null) {
            return;
        }
        this.resolveObj(field.substring(index + 1), param);
    }

    /**
     * 确定是否使用mybatis加条件构造函数
     *
     * @param field
     *
     * @return
     */
    private boolean hasWrapper(String field) {
        if (StringUtils.isBlank(field)) {
            return false;
        }
        return field.contains(MYBATIS_PLUS_WRAPPER_PREFIX);
    }

    /**
     * 确定字符串是否包含需要转义的字符
     *
     * @param str String to be judged
     *
     * @return true/false
     */
    private boolean hasEscapeChar(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        for (String s : ESCAPE_CHAR) {
            if (str.contains(s)) {
                return true;
            }
        }
        return false;
    }
}
