package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Data
public class AreaUpsertParam {

    /**
     * 区域参数
     */

    private List<AreaParam> areaParamList;

    @Data
    public static class AreaParam {
        /**
         * id
         */
        private Long id;
        /**
         * 区域名称
         */
        @NotBlank(message = "区域名称不能为空")
        private String areaName;
        /**
         * 区域详情
         */
        private String areaDetail;
    }
}
