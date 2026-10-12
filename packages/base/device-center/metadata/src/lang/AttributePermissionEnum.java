package com.guhecloud.cube.base.device.center.metadata.lang;

import com.guhecloud.cube.base.device.center.metadata.annotation.VersionInfo;
import lombok.Getter;

@Getter
public enum AttributePermissionEnum {
    /**
     * 读写
     */
    @VersionInfo(availableSince = Version.V_2_0_1)
    READ_WRITE("读写") {
        @Override
        public String getAlias() {
            return "rw";
        }
    },
    /**
     * 只读
     */
    @VersionInfo(availableSince = Version.V_2_0_1)
    READ("只读") {
        @Override
        public String getAlias() {
            return "r";
        }
    },
    /**
     * 只写
     */
    @VersionInfo(deprecatedSince = Version.V_2_0_1)
    WRITE("只写") {
        @Override
        public String getAlias() {
            return "w";
        }
    };

    final String name;

    public static AttributePermissionEnum convertBySourceVal(int iotVal) {
        if (iotVal == 1) {
            return READ_WRITE;
        }

        if (iotVal == 0) {
            return READ;
        }

        throw new IllegalArgumentException("不支持的iotVal:" + iotVal);
    }

    public abstract String getAlias();

    AttributePermissionEnum(String name) {
        this.name = name;
    }
}
