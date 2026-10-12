package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DisinfectionEnableEnum {
    ENABLE(true),
    DISABLE(false);
    private final Boolean value;
}
