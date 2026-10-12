package com.guhecloud.cube.srv.gen.adapter.information.server.hik.qo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HikProgramInfoParam {
    /**
     * 指定uniqueField参数值所属字段。"idType"标识uniqueField为id字段的值，"aliasType"表示uniqueField为别名字段的值
     */
    private String type;
    /**
     * 素材或者节目的唯一字段的值，由type字段指定
     */
    private String uniqueField;
    /**
     * 播放方式 ： byTime-按时间
     */
    private String playMode;
    /**
     * 当playMode为byTime时，表示播放时长，单位为秒，范围10-604800.如果不填写或者不在更改范围内，默认为10
     */
    private Integer playDuration;
}
