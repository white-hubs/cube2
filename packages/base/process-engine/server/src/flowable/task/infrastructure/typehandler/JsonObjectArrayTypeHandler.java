package com.guhecloud.cube.base.flow.flowable.task.infrastructure.typehandler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.guhecloud.cube.base.flow.flowable.task.domain.TaskOperateInfo;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author pengjian
 * @date 2022/10/31 16:09
 * @description
 */
@Slf4j
@MappedTypes({ Object.class })
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JsonObjectArrayTypeHandler extends AbstractJsonTypeHandler<Object> {

    public JsonObjectArrayTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    public Object parse(String json) {

        return JsonUtils.deserializeToList(json, TaskOperateInfo.class);
    }

    @Override
    public String toJson(Object obj) {
        return JsonUtils.serialize(obj);
    }
}
