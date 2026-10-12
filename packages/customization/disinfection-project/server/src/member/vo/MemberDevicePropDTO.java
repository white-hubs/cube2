package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import lombok.Data;

@Data
public class MemberDevicePropDTO {
    /**
     * 属性code
     */
    private String code;
    /**
     * 属性名
     */
    private String name;
    /**
     * 属性值
     */
    private String expectedValue;
    /**
     * 属性值名
     */
    private String expectedName;
}
