package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 获取文本播放状态请求参数
 *
 * @author lanchun
 * @date 2023/3/31 19:26
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class PlayTextStateRequestAbstract extends AbstractBasicRequest {

    private String TTSUUID;

    @Override
    public String getUrl() {
        return getUrl("TextPlayState").toString();
    }
}
