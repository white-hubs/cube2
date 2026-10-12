package com.guhecloud.cube.iot.mgt.core.data.lang.env;

/**
 * 平台环境,此模块代码运行在两套平台上<br/>
 * <p>
 * 由于mgt-base、mgt-things模块代码在<b>IOT平台</b> 和 <b>运营平台</b> 代码逻辑相同.<br/>
 * - 运营平台没有租户概念;字典层级Code为"A";<br/>
 * - IOT平台有租户概念;字典层级Code为"C"; <br/>
 * </p>
 */
public enum PlatformEvn {
    IOT_PLATFORM("IOT平台", "iot_platform", "C"),
    STORAGE_PLATFORM("运营平台", "storage_platform", "A");

    public final String name;
    public final String code;
    public final String singleCode;

    PlatformEvn(String name, String code, String singleCode) {
        this.name = name;
        this.code = code;
        this.singleCode = singleCode;
    }
}