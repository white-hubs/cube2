package com.guhecloud.cube.base.device.center.metadata.lang;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Const {

    /**
     * 设备中心前端应用后缀
     */
    public static final String DEVICE_CENTER_FRONTEND_SUFFIX = "";//""/center-out";
    public static final String DEVICE_CENTER_MGT_FRONTEND_SUFFIX = "";//""/center-mgt";

    public static final String CLIENT_NAME = "iotDeviceCenter";

    /**
     * 物联中心设备变更事件推送TOPIC<br/>
     * 推送到所有APP应用
     */
    public static final String DEVICE_CHANGE_TOPIC = "DEVICE_CENTER_DEVICE_CHANGE_TOPIC";

    public static final String PRODUCT_CHANGE_TOPIC = "DEVICE_CENTER_PRODUCT_CHANGE_TOPIC";

    /**
     * IOT内部数据变更<br/>
     * eg: 三方id绑定事件、视频标签绑定
     */
    public static final String IOT_INNER_DATA_CHANGE = "IOT_INNER_DATA_CHANGE";

    /**
     * 视频设备标签类型
     */
    public static final String VIDEO_DEVICE_TAG_TYPE = "device-center.video";
    public static final String VIDEO_DEVICE_TAG_TYPE_NAME = "视频设备类别";

    /**
     * 设备标签类别编码
     */
    public static final String CODE_APP_INNER_TAG_TYPE = "cloud.app";
    public static final String CODE_APP_INNER_TAG_TYPE_NAME = "应用内置标签";

    /**
     * 视频设备标签编码
     */
    public static final String VIDEO_DEVICE_TAG_CODE = "builtin.usage::camera";
    public static final String VIDEO_DEVICE_TAG_CODE_NAME = "默认视频标签";

    /**
     * 设备类型标签
     */
    public static final String DEVICE_TYPE_REPLACE_TAG_TYPE = "cloud.old.type";
    public static final String DEVICE_TYPE_REPLACE_TAG_TYPE_NAME = "设备历史类型标签";

    /**
     * 物联中心自定义标签前缀
     */
    public static final String DEVICE_CENTER_CUSTOMER_TAG_TYPE = "custom.app::deviceCenter";
    public static final String DEVICE_CENTER_CUSTOMER_TAG_NAME = "物联中心设备标签";

    /**
     * 设备中心使用，其他应用可参考使用
     */
    public static final String TAG_TYPE_TAG_SPILT = "@@";
    /**
     * 预置标签前缀
     */
    public static final String BUILTIN_TAG_PREFIX = "builtin.";

    /**
     * 预置标签
     */
    public static final String BUILTIN_USAGE_TAG_PREFIX = "builtin.usage";

    /**
     * 内置标签类型
     */
    public static final Map<String, String> APP_INNER_TAG_TYPE_CODES;
    /**
     * 内置标签<br/>
     * key: tagTypeCode@@tagCode<br/>
     */
    public static final Map<String, String> APP_INNER_TAG_CODES;

    static {
        //Collections
        Map<String, String> map = new HashMap<>();
        map.put(CODE_APP_INNER_TAG_TYPE, CODE_APP_INNER_TAG_TYPE_NAME);
        map.put(VIDEO_DEVICE_TAG_TYPE, VIDEO_DEVICE_TAG_TYPE_NAME);

        APP_INNER_TAG_TYPE_CODES = Collections.unmodifiableMap(map);

        map = new HashMap<>();
        map.put(VIDEO_DEVICE_TAG_TYPE + TAG_TYPE_TAG_SPILT + VIDEO_DEVICE_TAG_CODE, VIDEO_DEVICE_TAG_CODE_NAME);
        APP_INNER_TAG_CODES = Collections.unmodifiableMap(map);
    }

    private Const() {
    }
}
