package com.guhecloud.cube.biz.adapter.broadcast.server.application.liuzhou.facade.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiuZhouPlayTextRequest extends AbstractLiuZhouBasicRequest {

    /**
     * 播放设备
     */
    private Set<String> device_ids;

    /**
     * 播放文本内容
     */
    private String message;

    @Override
    public String getUrl() {
        return getUrl("/tts_play").toString();
    }
}
