package com.guhecloud.cube.iot.mgt.core.data.lang.env;

public enum InnerDicEnum {
    direct_node_acc(1651067263916449792L, "直连设备接入方式", "direct_node_acc", true),
    gateway_node_acc(1651067263916449793L, "网关设备接入方式", "gateway_node_acc", true),
    gw_child_node_acc(1651067263916449794L, "网关子设备接入方式", "gw_child_node_acc", true),
    prod_category(1648234964497735681L, "产品品类", "prod_category", true),
    prod_brand(1648286778454712320L, "产品品牌", "prod_brand", true),
    prod_pic(1648286778454712324L, "品牌图片", "prod_pic", false),
    prop_unit(1650332053109219328L, "属性单位", "prop_unit", true),
    event_type(1650332053109219329L, "事件类型", "event_type", true),
    ;

    public final long id;
    public final String name;
    public final String value;
    public final boolean cacheSupport;

    InnerDicEnum(long id, String name, String value, boolean cacheSupport) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.cacheSupport = cacheSupport;
    }

    public static InnerDicEnum getEnumByValue(String value) {
        for (InnerDicEnum innerDicEnum : InnerDicEnum.values()) {
            if (innerDicEnum.value.equals(value)) {
                return innerDicEnum;
            }
        }
        return null;
    }
}
