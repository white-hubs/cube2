package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ControTypeEnum {
    /**
     * case 1:
     * //开启
     * url = url+OPEN_DOOR;
     * break;
     * case 2:
     * //关闭
     * url = url+CLOSE_DOOR;
     * break;
     * case 0:
     * //常开
     * url = url+STAY_OPEN;
     * break;
     * case 3:
     * //常闭
     * url = url+STAY_CLOSE;
     * break;
     * case 5:
     * //正常模式
     * url = url+DOOR_NORMAL;
     * break;
     */
    OPEN(1, "开启"),
    CLOSE(2, "关闭"),
    CONSTANT_OPEN(0, "常开"),
    CANCEL_CONSTANT_OPEN(3, "常闭"),
    DOOR_NORMAL(5, "正常模式");

    private final Integer code;
    private final String desc;

    public static ControTypeEnum build(Integer controlType) {
        for (ControTypeEnum value : values()) {
            if (value.code.equals(controlType)) {
                return value;
            }
        }
        throw new ServiceException("不支持的控制枚举类型");
    }
}
