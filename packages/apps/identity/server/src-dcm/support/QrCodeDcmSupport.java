package com.guhecloud.cube.srv.gen.identity.dcm.support;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class QrCodeDcmSupport {
    public static final String PROVIDER_QR_CODE = "QRCODE";
    public static final String VERSION = "1.0";
    public static final String CONFIG_TYPE_STYLE = "Style";
    public static final String CONFIG_TYPE_APP = "App";

    public static final String CONFIG_TYPE_ENTITY_TYPE = "EntityType";
    public static final String CONFIG_TYPE_ENTITY_STYLE = "EntityStyle";

    public static final String CONFIG_TYPE_ENTITY_APP = "EntityApp";

    /**
     * 以下为集中定义的Rci kw args key, 以消除无意引入的差异.
     */
    public static final String KW_KEY_APP_CODE = "appCode";
    public static final String KW_KEY_ENTITY_TYPE = "entityType";

    // 依据依赖顺序确定的偏序集
    public static final List<String> CONFIG_TYPES_ORDERED = Arrays.asList(
            CONFIG_TYPE_STYLE,
            CONFIG_TYPE_APP,
            CONFIG_TYPE_ENTITY_TYPE,
            CONFIG_TYPE_ENTITY_STYLE,
            CONFIG_TYPE_ENTITY_APP
                                                                         );

    private QrCodeDcmSupport() {
        // disable instantiation
    }
}
