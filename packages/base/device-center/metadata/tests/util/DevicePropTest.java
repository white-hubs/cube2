package com.guhecloud.cube.base.device.center.metadata.util;

import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.cube.base.device.center.metadata.lang.AttributePermissionEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.guhecloud.cube.base.device.center.metadata.util.DevicePropUtils.convertReadWriteEnum;
import static com.guhecloud.cube.base.device.center.metadata.util.DevicePropUtils.iotPropVal2Chinese;

class DevicePropTest {

    @Test
    void propTranslateTest() {
        JSONArray dataConstraint = JSONArray.parseArray("[{\"value\":0,\"label\":\"yes\"},{\"value\":1,\"label\":\"no\"}]");
        Assertions.assertEquals("no", iotPropVal2Chinese("enum", "1", dataConstraint));

        Assertions.assertEquals("读写", AttributePermissionEnum.convertBySourceVal(1).getName());
        Assertions.assertEquals("只读", AttributePermissionEnum.convertBySourceVal(0).getName());

        Assertions.assertEquals(AttributePermissionEnum.READ_WRITE, convertReadWriteEnum(1));
        Assertions.assertEquals(AttributePermissionEnum.READ, convertReadWriteEnum(0));
    }
}
