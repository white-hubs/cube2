package com.guhecloud.cube.base.message2.client.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CodeVerifyResultEnum {
    SUCCESS,
    FAILED,
    VERIFIED,
    INVALID,
    EXPIRED;
}
