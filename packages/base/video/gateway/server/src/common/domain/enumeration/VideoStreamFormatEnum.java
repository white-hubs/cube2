package com.guhecloud.cube.base.videogw.server.common.domain.enumeration;

import lombok.Getter;

/**
 * 输出码流转封装格式
 *
 * @description 输出码流转封装格式，“ps”:PS封装格式、“rtp”:RTP封装协议。当protocol=rtsp时生效，且不传值时默认为RTP封装协议。
 * @author lanchun
 * @date 2023/11/10 16:42
 */
@Getter
public enum VideoStreamFormatEnum {

    RTP,//RTP封装协议

    PS;//PS封装格式
}
