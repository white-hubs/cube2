package com.guhecloud.cube.biz.adapter.broadcast.server.application.tianjin.facade.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TianJinPlayTextRequest extends AbstractTianJinBasicRequest {

    /**
     * 播放设备
     */
    private Set<String> deviceIds;

    /**
     * 播放文本内容
     */
    private String playTextContent;

    @Override
    public String getUrl() {
        return getUrl("/tian_jing_broadcast/tts_play/").append(this.deviceIds.stream().collect(Collectors.joining(","))).append("/")
                .append(this.playTextContent).toString();
    }
}
