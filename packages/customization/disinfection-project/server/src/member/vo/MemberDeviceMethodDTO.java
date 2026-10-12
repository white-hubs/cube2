package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import lombok.Data;

import java.util.List;

@Data
public class MemberDeviceMethodDTO {
    /**
     * 方法code
     */
    private String code;
    /**
     * 方法名
     */
    private String name;
    /**
     * 方法参数
     */
    private List<MemberDeviceMethodParamDTO> params;

    @Data
    public static class MemberDeviceMethodParamDTO {
        /**
         * 方法参数code
         */
        private String code;
        /**
         * 方法参数名
         */
        private String name;
        /**
         * 方法参数值
         */
        private Long expectedValue;
    }

}
