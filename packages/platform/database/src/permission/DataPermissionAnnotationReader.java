package com.guhecloud.platform.database.permission;

import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.permission.entity.DbDataFieldPermission;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.schema.Table;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class DataPermissionAnnotationReader {

    public boolean ignoreTable(String tableName) {
        return !DatabaseContextHolder.isDataPermissionTable(tableName);
    }

    public DataPermissionField getPermissionDefinedField(Table table, String permissionDefinedName) {
        DataPermission annotation = TableInfoHelper.getTableInfo(DatabaseContextHolder.cleanTableName(table.getName()))
                .getEntityType().getAnnotation(DataPermission.class);
        DataPermissionField[] dataPermissionFields = annotation.value();
        return Arrays.stream(dataPermissionFields).filter(p -> p.permission().equals(permissionDefinedName))
                .findFirst().orElse(null);
    }

    public Object getFieldFilterParams(DbDataFieldPermission dataPermission, DataPermissionField permissionDefined) {
        try {
            if ("CURRENT_USER".equals(permissionDefined.permission()) &&
                DataPermissionField.DefaultFieldFilter.class == permissionDefined.fieldFilter()) {
                return SecurityContextHolder.getPrincipal().getId();
            }
            if (Objects.isNull(dataPermission.getFilterParams()) &&
                DataPermissionField.DefaultFieldFilter.class != permissionDefined.fieldFilter()) {
                Class<? extends DataPermissionField.FieldFilter> aClass = permissionDefined.fieldFilter();
                DataPermissionField.FieldFilter fieldFilter = ApplicationContextHolder.getBean(aClass);
                return fieldFilter.getFieldFilterParams();
            }
            if (DataPermissionField.DefaultFieldParamConvert.class != permissionDefined.fieldParamConvert()) {
                Class<? extends DataPermissionField.FieldParamConvert> aClass = permissionDefined.fieldParamConvert();
                DataPermissionField.FieldParamConvert fieldParamConvert = ApplicationContextHolder.getBean(aClass);
                return fieldParamConvert.getFieldParamConvert(dataPermission.getFilterParams());
            }
        } catch (NoSuchBeanDefinitionException e) {
            log.error("get field_filter params error, no such bean definition field filter bean msg:", e);
        }
        return dataPermission.getFilterParams();
    }
}
