package com.guhecloud.cube.srv.gen.maintain.server.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * author: ht
 * create: 2025-06-04 18:30
 */
@AllArgsConstructor
@Getter
public enum FaultOrderStateEnum {
    HANDLING("HANDLING"),
    REJECT("REJECT"),
    UNDO("UNDO"),
    PASS("PASS");
    private final String value;

    public static FaultOrderStateEnum getByValue(String value) {
        for (FaultOrderStateEnum stateEnum : values()) {
            if (stateEnum.getValue().equals(value)) {
                return stateEnum;
            }
        }
        return HANDLING;
    }
}
