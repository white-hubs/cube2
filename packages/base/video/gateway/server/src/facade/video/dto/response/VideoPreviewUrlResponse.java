package com.guhecloud.cube.base.videogw.server.facade.video.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoPreviewUrlResponse {

    /**
     * 播放地址
     */
    private String url;
}
