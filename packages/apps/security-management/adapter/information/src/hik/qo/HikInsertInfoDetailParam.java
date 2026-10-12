package com.guhecloud.cube.srv.gen.adapter.information.server.hik.qo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HikInsertInfoDetailParam {
    /**
     * 插播类型：material-素材；program-节目；character-文字
     */
    private String insertType;
    /**
     * 素材或者节目信息对象
     */
    private HikProgramInfoParam info;
}
