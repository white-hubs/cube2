package com.guhecloud.cube.biz.linkage.server.facade.iot.enumeration;

import lombok.Getter;

@Getter
public enum AttributePermissionEnum {
    /**
     * 读写
     */
    READ_WRITE("读写") {
        @Override
        public String getAlias() {
            return "rw";
        }
    },
    /**
     * 只读
     */
    READ("只读") {
        @Override
        public String getAlias() {
            return "r";
        }
    },
    /**
     * 只写
     */
    WRITE("只写") {
        @Override
        public String getAlias() {
            return "w";
        }
    };

    String name;

    public abstract String getAlias();

    AttributePermissionEnum(String name) {
        this.name = name;
    }
}
