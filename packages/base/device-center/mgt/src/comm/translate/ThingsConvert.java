package com.guhecloud.cube.iot.mgt.comm.translate;

import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;

/**
 * 物模型相关的常量转换
 */
public class ThingsConvert {

    /**
     * 转中文
     *
     * @param nodeType nodeType
     *
     * @return String of chinese
     */
    public static String toNodeTypeName(Integer nodeType) {
        Assert.notNull(nodeType, "nodeType can not be null");

        switch (nodeType) {
            case 1:
                return "直连设备";
            case 2:
                return "网关设备";
            case 3:
                return "网关子设备";
            default:
                throw new IllegalArgumentException("nodeType is illegal");
        }
    }

    public static int nodeTypeNameToId(String nodeTypeName) {
        Assert.notNull(nodeTypeName, "nodeTypeName can not be null");
        switch (nodeTypeName) {
            case "直连设备":
                return 1;
            case "网关设备":
                return 2;
            case "网关子设备":
                return 3;
            default:
                throw new IllegalArgumentException("nodeTypeName is illegal");
        }
    }

    public static Long toNodeDictTypeId(Integer nodeType) {
        Assert.notNull(nodeType, "nodeType can not be null");

        switch (nodeType) {
            case 1:
                return InnerDicEnum.direct_node_acc.id;
            case 2:
                return InnerDicEnum.gateway_node_acc.id;
            case 3:
                return InnerDicEnum.gw_child_node_acc.id;
            default:
                throw new IllegalArgumentException("nodeType is illegal");
        }
    }

    public static String toStatusName(Integer status) {
        Assert.notNull(status, "status can not be null");

        switch (status) {
            case 0:
                return "禁用";
            case 1:
                return "启用";
            default:
                throw new IllegalArgumentException("status is illegal");
        }
    }

}
