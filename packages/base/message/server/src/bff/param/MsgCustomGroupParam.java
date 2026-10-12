package com.guhecloud.cube.base.message2.server.bff.param;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

/**
 * 用户自定义消息分组参数
 *
 * @author lanchun
 * @date 2024/7/8 14:05
 */
@Data
@Builder
public class MsgCustomGroupParam {

    /**
     * 自定义类型编号
     */
    private String type;

    /**
     * 自定义类型名称
     */
    private String typeName;

    /**
     * 自定义分组信息
     */
    private Set<CustomGroups> customGroups;

    /**
     * 过滤掉的自定义分组信息
     */
    private Set<CustomGroups> filterCustomGroups;

    @Data
    @Builder
    public static class CustomGroups {

        /**
         * 应用编号
         */
        private String appCode;

        /**
         * 消息类型
         */
        private String msgType;

        /**
         * 场景编号
         */
        private String sceneCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MsgCustomGroupParam that = (MsgCustomGroupParam) o;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
